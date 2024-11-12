package com.market.core.security.dto.jwt.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class KakaoErrorResponseDto {

    @JsonProperty("code")
    private String errorCode;

    @JsonProperty("msg")
    private String errorMessage;
}
