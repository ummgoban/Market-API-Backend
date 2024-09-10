package com.market.core.security.dto.oauth;

public interface OAuth2UserInfo {
    String getId();

    String getProvider();

    String getName();

    String getEmail();

    String getImageUrl();
}