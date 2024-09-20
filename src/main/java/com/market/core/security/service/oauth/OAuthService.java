package com.market.core.security.service.oauth;

import com.market.member.dto.request.MemberLoginDto;
import com.market.member.dto.request.OAuthAuthorizationDto;
import com.market.member.dto.request.OAuthLoginDto;

/**
 * OAuth 인증 서비스 인터페이스입니다.
 */
public interface OAuthService {

    String getAccessToken(OAuthAuthorizationDto oAuthAuthorizationDto);

    MemberLoginDto getUserInfo(OAuthLoginDto oAuthLoginDto);
}