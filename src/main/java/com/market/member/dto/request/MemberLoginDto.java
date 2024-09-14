package com.market.member.dto.request;

import com.market.member.entity.RolesType;
import lombok.Builder;
import lombok.Getter;

/**
 * // OAuth 로그인 요청 시 사용되는 회원 정보 DTO 클래스입니다.
 */
@Getter
@Builder
public class MemberLoginDto {

    private final String oauthId;
    private final String provider;
    private final String name;
    private final String profileImageUrl;
    private final RolesType role;
}