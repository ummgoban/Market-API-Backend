package com.market.core.security.dto.oauth;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {

    private final Map<String, Object> response;

    public NaverUserInfo(Map<String, Object> attributes) {
        this.response = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getId() {
        return response != null ? (String) response.get("id") : null;
    }

    @Override
    public String getProvider() {
        return "NAVER";
    }

    @Override
    public String getName() {
        return response != null ? (String) response.get("name") : null;
    }

    @Override
    public String getEmail() {
        return response != null ? (String) response.get("email") : null;
    }

    @Override
    public String getImageUrl() {
        return response != null ? (String) response.get("profile_image") : null;
    }
}
