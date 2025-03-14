package com.example.demo.snippet

import com.example.demo.snippet.impl.SinglePassSnippetSearchServiceImpl
import spock.lang.Specification

class SnippetSearchServiceTest extends Specification {

    private final SnippetSearchService snippetSearchService = new SinglePassSnippetSearchServiceImpl()
    private final List<String> interviewExampleDocument = ["apple", "banana", "apple", "apple", "dog", "cat", "apple", "dog", "banana", "apple", "cat", "dog"]

    def "empty document"() {
        given:
        List<String> document = []
        Set<String> searchTerms = ["term1"]

        when:
        List<String> result = snippetSearchService.findShortestSnippet(document, searchTerms)

        then:
        result.isEmpty()
    }

    def "empty search terms"() {
        given:
        List<String> document = interviewExampleDocument
        Set<String> searchTerms = []

        when:
        List<String> result = snippetSearchService.findShortestSnippet(document, searchTerms)

        then:
        result.isEmpty()
    }

    def "interview example - happy path / search for shortest (banana, cat) snippet"() {
        given:
        List<String> document = interviewExampleDocument
        Set<String> searchTerms = ["banana", "cat"]

        when:
        List<String> result = snippetSearchService.findShortestSnippet(document, searchTerms)

        then:
        result.equals(["banana", "apple", "cat"])
    }

    def "interview example - search terms contains something not in the document"() {
        given:
        List<String> document = interviewExampleDocument
        Set<String> searchTerms = ["banana", "cat", "notInDocument"]

        when:
        List<String> result = snippetSearchService.findShortestSnippet(document, searchTerms)

        then:
        result.isEmpty()
    }

    def "modified interview example - nested matches in the document returns the shortest snippet"() {
        given:
        List<String> document = ["banana", "banana", "cat", "cat"]
        Set<String> searchTerms = ["banana", "cat"]

        when:
        List<String> result = snippetSearchService.findShortestSnippet(document, searchTerms)

        then:
        result.equals(["banana", "cat"])
        result.size().equals(2)
    }
}
