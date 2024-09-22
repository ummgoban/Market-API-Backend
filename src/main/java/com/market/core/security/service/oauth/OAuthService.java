package com.market.core.security.service.oauth;

import com.market.member.dto.server.MemberLoginDto;
import com.market.member.dto.request.OAuthAuthorizationRequest;
import com.market.member.dto.request.OAuthLoginRequest;

/**
 * OAuth 인증 서비스 인터페이스입니다.
 */
public interface OAuthService {

    String getAccessToken(OAuthAuthorizationRequest oAuthAuthorizationRequest);

    MemberLoginDto getUserInfo(OAuthLoginRequest oAuthLoginRequest);
}