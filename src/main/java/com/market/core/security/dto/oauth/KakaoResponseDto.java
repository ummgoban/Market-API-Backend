package com.market.core.security.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Kakao OAuth API 응답 DTO 클래스입니다.
 */
@Getter
@RequiredArgsConstructor
public class KakaoResponseDto {

    @JsonProperty("token_type")
    private final String tokenType;

    @JsonProperty("access_token")
    @NotNull
    private final String accessToken;

    @JsonProperty("id_token")
    private final String idToken;

    @JsonProperty("expires_int")
    private final Integer expiresIn;

    @JsonProperty("refresh_token")
    private final String refreshToken;

    @JsonProperty("refresh_token_expires_in")
    private final Integer refreshTokenExpiresIn;

    @JsonProperty("scope")
    private final String scope;
}