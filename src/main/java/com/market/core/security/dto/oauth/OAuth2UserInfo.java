package com.market.core.security.dto.oauth;

/**
 * 다양한 OAuth2 공급자Kakao, Naver 등)에서 받아온 OAuth2 사용자 정보를 표준화하기 위한 인터페이스입니다.
 */
public interface OAuth2UserInfo {
    String getId();

    String getProvider();

    String getName();

    String getEmail();

    String getImageUrl();
}