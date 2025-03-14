package com.example.demo.snippet.impl;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class SnippetCandidate {

    // map of search term to boolean indicating if it has been detected or not; true means it has been detected
    private Map<String, Boolean> searchTermDetections = new HashMap<>();

    @Getter
    private Integer startingPosition = null;

    @Getter
    private Integer endingPosition = null;

    public SnippetCandidate(Set<String> searchTerms) {
        searchTerms.forEach(searchTerm -> {
            searchTermDetections.put(searchTerm, false);
        });
    }

    /**
     * Mark a search term as being found
     * @param searchTerm the search term that has been found
     * @param documentPosition the position in our document where the <code>searchTerm</code> was found
     */
    public void markSearchTermFound(String searchTerm, Integer documentPosition) {
        // record the starting position first time through
        if (startingPosition == null) {
            startingPosition = documentPosition;
        }

        if (endingPosition != null) {
            // we already found everything and marked an endingPosition, so do no updates
            return;
        } else {
            // mark that we have found it
            searchTermDetections.put(searchTerm, true);

            // check if we are now complete
            if (doesSnippetCandidateContainAllSearchTerms()) {
                // this made us complete, so mark the ending position
                endingPosition = documentPosition;
            }
        }
    }

    /**
     *
     * @return true if and only if all searchTerms have been marked found for this snippet candidate
     */
    public boolean doesSnippetCandidateContainAllSearchTerms() {
        Optional<Map.Entry<String, Boolean>> firstNotFoundSearchDetection = searchTermDetections
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() == false)
                .findFirst();

        return !firstNotFoundSearchDetection.isPresent();
    }

    /**
     *
     * @return length of this snippet candidate
     */
    public Integer getSnippetCandidateLength() {
        if (startingPosition == null || endingPosition == null) {
            return null;
        }

        return endingPosition - startingPosition + 1;
    }
}
