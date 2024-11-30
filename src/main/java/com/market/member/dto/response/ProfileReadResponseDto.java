package com.market.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * 프로필 조회 응답 DTO 입니다.
 */
@Getter
@Builder
@Schema(description = "프로필 조회 응답 DTO 입니다.")
public class ProfileReadResponseDto {

    @Schema(description = "회원 ID")
    private Long id;

    @Schema(description = "회원 닉네임")
    private String name;

    @Schema(description = "소셜 정보. [ NAVER, KAKAO, APPLE]")
    private String provider;
}
