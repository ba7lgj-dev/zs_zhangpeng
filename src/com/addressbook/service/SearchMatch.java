package com.addressbook.service;

public class SearchMatch {
    private final String type;
    private final String className;
    private final String description;

    public SearchMatch(String type, String className, String description) {
        this.type = type;
        this.className = className;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getClassName() {
        return className;
    }

    public String getDescription() {
        return description;
    }
}
