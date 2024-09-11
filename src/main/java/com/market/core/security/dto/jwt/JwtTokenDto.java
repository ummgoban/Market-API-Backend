package com.market.core.security.dto.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class JwtTokenDto {

    private final String accessToken;
    private final String refreshToken;
}