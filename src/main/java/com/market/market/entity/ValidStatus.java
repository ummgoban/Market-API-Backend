package com.market.market.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ValidStatus {
    VALID("01"),
    INVALID("02");

    private final String code;

    ValidStatus(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static ValidStatus fromCode(String code) {
        for (ValidStatus status : ValidStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}