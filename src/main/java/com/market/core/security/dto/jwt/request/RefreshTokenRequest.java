package com.market.core.security.dto.jwt.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * JWT 갱신 요청을 위한 Refresh Token을 담는 DTO 클래스입니다.
 */
@Getter
public class RefreshTokenRequest {

    @Schema(description = "JWT 갱신을 위한 Refresh Token", example = "eyJ...", required = true)
    private final String refreshToken;

    @JsonCreator
    public RefreshTokenRequest(@JsonProperty("refreshToken") String refreshToken) {
        this.refreshToken = refreshToken;
    }
}