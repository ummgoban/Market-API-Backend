package com.market.orders.dto.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TossPaymentErrorResponse {

    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;
}
