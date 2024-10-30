package com.market.market.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 사업자 등록 번호의 유효성 검증 결과의 사업자 정보를 담는 DTO 클래스입니다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class BusinessNumberValidateInfoResponse {

    @Schema(description = "사업자 등록 번호", example = "1234567890")
    private final String businessNumber;

    @Schema(description = "개업일자", example = "20240101")
    private final String startDate;

    @Schema(description = "이름", example = "홍길동")
    private final String memberName;

    @Schema(description = "가게 이름", example = "가게 이름")
    private final String marketName;
}