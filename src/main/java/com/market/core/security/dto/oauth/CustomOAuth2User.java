package com.market.core.security.dto.oauth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private final String oauthId; // 소셜 회원 고유 ID
    private final String provider;
    private final String name; // 사용자 이름
    private final String profileImageUrl; // 프로필 이미지

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes,
                            String nameAttributeKey,
                            String oauthId,
                            String provider,
                            String name,
                            String profileImageUrl) {
        super(authorities, attributes, nameAttributeKey);
        this.oauthId = oauthId;
        this.provider = provider;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }
}