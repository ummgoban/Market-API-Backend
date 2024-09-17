package com.market.member.dto.request;

import com.market.member.entity.ProviderType;
import com.market.member.entity.RolesType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * OAuth 로그인 요청에 필요한 정보를 담는 DTO 클래스입니다.
 */
@Getter
@RequiredArgsConstructor
public class OAuthLoginDto {

    private final ProviderType provider;
    private final RolesType roles;
    private final String accessToken;
}