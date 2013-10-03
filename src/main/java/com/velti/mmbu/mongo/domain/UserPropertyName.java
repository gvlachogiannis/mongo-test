package com.velti.mmbu.mongo.domain;

/** @author gvlachogiannis */
public enum UserPropertyName {
    FIST_NAME("first_name"),
    LAST_NAME("last_name"),
    ADDRESS("address"),
    AGE("age");

    private final String name;

    private UserPropertyName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}