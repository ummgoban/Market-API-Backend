package com.market.market.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BusinessStatus {
    ACTIVE("계속 사업자"),
    SUSPENDED("휴업자"),
    CLOSED("폐업자");

    private final String status;

    BusinessStatus(String status) {
        this.status = status;
    }

    @JsonValue
    public String getStatus() {
        return status;
    }

    @JsonCreator
    public static BusinessStatus fromStatus(String status) {
        for (BusinessStatus businessStatus : BusinessStatus.values()) {
            if (businessStatus.status.equals(status)) {
                return businessStatus;
            }
        }
        return null;
    }
}