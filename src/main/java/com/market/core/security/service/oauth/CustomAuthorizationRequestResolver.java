package com.market.core.security.service.oauth;

import com.market.core.code.error.MemberErrorCode;
import com.market.core.exception.MemberException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Component
public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private final OAuth2AuthorizationRequestResolver defaultAuthorizationRequestResolver;

    public CustomAuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        this.defaultAuthorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(
                clientRegistrationRepository, "/oauth2/authorization");
    }

    /**
     * 기본 Resolver를 통해 요청 처리
     */
    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest authorizationRequest = this.defaultAuthorizationRequestResolver.resolve(request);
        return customizeAuthorizationRequest(request, authorizationRequest);
    }

    /**
     * 기본 Resolver를 통해 요청 처리
     */
    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        OAuth2AuthorizationRequest authorizationRequest = this.defaultAuthorizationRequestResolver.resolve(request, clientRegistrationId);
        return customizeAuthorizationRequest(request, authorizationRequest);
    }

    /**
     * OAuth2 인증 요청에 state 파라미터를 추가하는 메서드
     */
    private OAuth2AuthorizationRequest customizeAuthorizationRequest(HttpServletRequest request, OAuth2AuthorizationRequest authorizationRequest) {
        if (authorizationRequest == null) {
            return null;
        }

        String state = request.getParameter("state");

        if (!StringUtils.hasText(state)) {
            throw new MemberException(MemberErrorCode.BAD_REQUEST_STATE_PARAMETER);
        }

        return OAuth2AuthorizationRequest.from(authorizationRequest)
                .state(state)
                .build();
    }
}

