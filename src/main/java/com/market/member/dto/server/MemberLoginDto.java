package com.market.member.dto.server;

import com.market.member.entity.ProviderType;
import com.market.member.entity.RolesType;
import lombok.Builder;
import lombok.Getter;

/**
 * OAuth 로그인 요청 시 사용되는 회원 정보 DTO 클래스입니다.
 */
@Getter
@Builder
public class MemberLoginDto {

    private final String oauthId;
    private final ProviderType provider;
    private final String name;
    private final RolesType roles;
}
