package com.market.core.security.dto.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * OAuth 제공자로부터 발급된 Access Token을 담는 DTO 클래스입니다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class AccessTokenResponse {

    @Schema(description = "발급된 OAuth Provider의 Access Token", example = "AAA...")
    private final String accessToken;
}