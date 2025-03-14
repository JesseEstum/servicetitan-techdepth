package com.example.demo.snippet.impl;

import com.example.demo.snippet.SnippetSearchService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class SinglePassSnippetSearchServiceImpl implements SnippetSearchService {
    @Override
    public List<String> findShortestSnippet(List<String> document, Set<String> searchTerms) {
        // validation
        if (document == null || document.isEmpty()) {
            return Collections.emptyList();
        }
        if (searchTerms == null || searchTerms.isEmpty()) {
            return Collections.emptyList();
        }

        // skip the work in case we have an unsolvable problem on our hands
        if (searchTerms.size() > document.size()) {
            return Collections.emptyList();
        }

        List<SnippetCandidate> snippetCandidates = new ArrayList<>();

        // actual single-pass loop through the document
        for (int documentPosition = 0; documentPosition < document.size(); documentPosition++) {
            String documentToken = document.get(documentPosition);

            if (searchTerms.contains(documentToken)) {
                // update all tracked snippet candidates
                for (SnippetCandidate snippetCandidate : snippetCandidates) {
                    snippetCandidate.markSearchTermFound(documentToken, documentPosition);
                }

                // start a new snippet candidate that begins with our current document token
                SnippetCandidate snippetCandidate = new SnippetCandidate(searchTerms);
                snippetCandidate.markSearchTermFound(documentToken, documentPosition);
                snippetCandidates.add(snippetCandidate);
            } else {
                // for purposes of this loop we only care about document tokens that are search terms
                continue;
            }
        };

        // get the shortest snippet candidate that contains all search terms
        Optional<SnippetCandidate> shortestSnippetCandidate = snippetCandidates
                .stream()
                .filter(SnippetCandidate::doesSnippetCandidateContainAllSearchTerms)
                .sorted((snippetCandidate1, snippetCandidate2) ->
                        snippetCandidate1.getSnippetCandidateLength().compareTo(snippetCandidate2.getSnippetCandidateLength()))
                .findFirst();

        // convert it to an actual snippet of the document using its starting position and ending position
        return shortestSnippetCandidate
                .map(shortest -> document
                        .subList(shortest.getStartingPosition(), shortest.getEndingPosition()+1)
                ).orElseGet(ArrayList::new);
    }
}
