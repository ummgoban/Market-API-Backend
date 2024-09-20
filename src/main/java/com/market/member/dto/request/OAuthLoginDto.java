package com.market.member.dto.request;

import com.market.member.entity.ProviderType;
import com.market.member.entity.RolesType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * OAuth 로그인 요청에 필요한 정보를 담는 DTO 클래스입니다.
 */
@Getter
@RequiredArgsConstructor
public class OAuthLoginDto {

    @Schema(description = "OAuth 제공자 타입 (예: NAVER, KAKAO, APPLE)", example = "NAVER", required = true)
    private final ProviderType provider;

    @Schema(description = "사용자의 역할 (예: ROLE_USER, ROLE_STORE_OWNER)", example = "ROLE_USER", required = true)
    private final RolesType roles;

    @Schema(description = "OAuth 제공자에서 발급된 액세스 토큰", example = "AAA...", required = true)
    private final String accessToken;
}