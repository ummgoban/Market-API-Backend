package com.market.member.dto.request;

import com.market.member.entity.ProviderType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * OAuth 인증 요청에 필요한 정보를 담는 DTO 클래스입니다.
 */
@Getter
@RequiredArgsConstructor
public class OAuthAuthorizationRequest {

    @Schema(description = "OAuth 제공자 타입 (예: NAVER, KAKAO, APPLE)", example = "NAVER", required = true)
    private final ProviderType provider;

    @Schema(description = "OAuth 제공자로부터 발급받은 인증 코드", example = "JjG...", required = true)
    private final String code;

    @Schema(description = "OAuth 요청 시 전달된 state 값 (NAVER에서만 필요)", example = "abcde")
    @Nullable
    private final String state;
}