package com.jpmorgan.support;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Flag {

    BUY("B"), SELL("S");

    private String value;

    Flag(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}