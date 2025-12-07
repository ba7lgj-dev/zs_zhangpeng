package com.addressbook.service;

import java.util.ArrayList;
import java.util.List;

public class GlobalSearchResult {
    private final List<SearchMatch> classMatches = new ArrayList<>();
    private final List<SearchMatch> studentMatches = new ArrayList<>();

    public List<SearchMatch> getClassMatches() {
        return classMatches;
    }

    public List<SearchMatch> getStudentMatches() {
        return studentMatches;
    }

    public boolean isEmpty() {
        return classMatches.isEmpty() && studentMatches.isEmpty();
    }
}
