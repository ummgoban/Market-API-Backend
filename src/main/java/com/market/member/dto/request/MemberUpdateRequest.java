package com.market.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * 회원 정보를 업데이트하기 위한 정보를 담는 DTO 클래스입니다.
 */
@Getter
public class MemberUpdateRequest {

    @Schema(description = "회원의 이름", example = "이소은", required = true)
    private String name;
}
