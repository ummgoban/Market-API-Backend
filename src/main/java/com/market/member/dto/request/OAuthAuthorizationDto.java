package com.market.member.dto.request;

import com.market.member.entity.ProviderType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * OAuth 인증 요청에 필요한 정보를 담는 DTO 클래스입니다.
 */
@Getter
@RequiredArgsConstructor
public class OAuthAuthorizationDto {

    private final ProviderType provider;
    private final String code;
    private final String state;
}