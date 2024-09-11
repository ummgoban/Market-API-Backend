package com.market.core.security.service.oauth;

import com.market.core.security.dto.oauth.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // OAuth2 로그인 시 키(PK)가 되는 값
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // naver, kakao, apple 구분
        OAuth2UserInfo oAuth2UserInfo = getOAuth2UserInfo(registrationId, attributes);

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                userNameAttributeName,
                oAuth2UserInfo.getId(),
                oAuth2UserInfo.getProvider(),
                oAuth2UserInfo.getName(),
                oAuth2UserInfo.getImageUrl()
        );
    }

    private OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) {
            case "naver" -> new NaverUserInfo(attributes);
            case "kakao" -> new KakaoUserInfo(attributes);
//            case "apple" -> new AppleUser/**/Info(attributes);
            default -> throw new IllegalArgumentException("Unknown registrationId: " + registrationId);
        };
    }
}