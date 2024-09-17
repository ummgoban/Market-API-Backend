package com.market.core.security.dto.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * JWT Access Token을 담는 DTO 클래스입니다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class AccessTokenDto {

    private final String accessToken;
}