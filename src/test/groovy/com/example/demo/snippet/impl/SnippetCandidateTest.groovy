package com.example.demo.snippet.impl

import spock.lang.Specification

class SnippetCandidateTest extends Specification {

    def "markSearchTermFound updates ending position only if we are finding the last undetected search term"() {
        given:
        String term1 = "term1"
        String term2 = "term2"
        Set<String> searchTerms = Set.of(term1, term2)
        SnippetCandidate snippetCandidate = new SnippetCandidate(searchTerms)

        when:
        snippetCandidate.markSearchTermFound(term1, 1)
        snippetCandidate.markSearchTermFound(term2, 2)
        snippetCandidate.markSearchTermFound(term2, 3)

        then:
        snippetCandidate.getSnippetCandidateLength() == 2
    }

    def "doesSnippetCandidateContainAllSearchTerms works correctly"() {
        given:
        String term1 = "term1"
        String term2 = "term2"
        Set<String> searchTerms = Set.of(term1, term2)
        SnippetCandidate snippetCandidate = new SnippetCandidate(searchTerms)

        when:
        snippetCandidate.markSearchTermFound(term1, 1)

        then:
        !snippetCandidate.doesSnippetCandidateContainAllSearchTerms()

        when:
        snippetCandidate.markSearchTermFound(term2, 2)

        then:
        snippetCandidate.doesSnippetCandidateContainAllSearchTerms()
    }
}
