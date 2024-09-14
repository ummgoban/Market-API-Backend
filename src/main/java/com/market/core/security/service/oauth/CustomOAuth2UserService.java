package com.market.core.security.service.oauth;

import com.market.core.code.error.MemberErrorCode;
import com.market.core.exception.MemberException;
import com.market.core.security.dto.oauth.*;
import com.market.member.entity.RolesType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * OAuth2 사용자 정보 로딩 및 권한 설정을 위한 커스텀 서비스 클래스입니다.
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 권한 처리
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String state = request.getParameter("state");
        Set<SimpleGrantedAuthority> authorities = determineAuthorities(state);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // OAuth2 로그인 시 키(PK)가 되는 값
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // naver, kakao, apple 구분
        OAuth2UserInfo oAuth2UserInfo = getOAuth2UserInfo(registrationId, attributes);

        return new CustomOAuth2User(
                authorities,
                attributes,
                userNameAttributeName,
                oAuth2UserInfo.getId(),
                oAuth2UserInfo.getProvider(),
                oAuth2UserInfo.getName(),
                oAuth2UserInfo.getImageUrl()
        );
    }

    /**
     * state 값에 따라 권한을 결정
     */
    private Set<SimpleGrantedAuthority> determineAuthorities(String state) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        if ("USER".equalsIgnoreCase(state)) {
            authorities.add(new SimpleGrantedAuthority(RolesType.ROLE_USER.name()));
        } else if ("STORE_OWNER".equalsIgnoreCase(state)) {
            authorities.add(new SimpleGrantedAuthority(RolesType.ROLE_STORE_OWNER.name()));
        } else {
            throw new MemberException(MemberErrorCode.BAD_REQUEST_STATE_PARAMETER);
        }
        return authorities;
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