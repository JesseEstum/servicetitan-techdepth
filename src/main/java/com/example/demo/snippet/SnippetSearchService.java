package com.example.demo.snippet;

import java.util.List;
import java.util.Set;

public interface SnippetSearchService {

    /**
     * Find the shortest snippet of ordered elements from <code>document</code> which contains all of the
     * provided <code>searchTerms</code>
     * @param document
     * @param searchTerms
     * @return shortest snippet
     */
    List<String> findShortestSnippet(List<String> document, Set<String> searchTerms);
}
