package com.market.core.security.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Naver OAuth API 응답 DTO 클래스입니다.
 */
@Getter
@RequiredArgsConstructor
public class NaverResponseDto {

    @JsonProperty("access_token")
    @NotNull
    private final String accessToken;

    @JsonProperty("refresh_token")
    private final String refreshToken;

    @JsonProperty("token_type")
    private final String tokenType;

    @JsonProperty("expires_in")
    private final Integer expiresIn;

    @JsonProperty("error")
    private final String error;

    @JsonProperty("error_description")
    private final String errorDescription;
}