package com.market.core.security.dto.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * JWT 액세스 토큰과 리프레시 토큰을 담는 DTO 클래스입니다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class JwtTokenDto {

    private final String accessToken;
    private final String refreshToken;
}