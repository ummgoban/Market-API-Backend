package com.market.market.dto.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GeocodingErrorResponse {

    @JsonProperty("error")
    private Error error;

    @Getter
    public static class Error {
        @JsonProperty("errorCode")
        private String errorCode;

        @JsonProperty("message")
        private String message;
    }
}
