package com.market.market.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 사업자 등록 번호의 유효성 검증 결과를 담는 DTO 클래스입니다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class BusinessNumberValidateResponse {

    @Schema(description = "사업자 등록 번호의 유효성 여부", example = "true")
    private final boolean validBusinessNumber;

    @Schema(description = "사업자 등록 번호 검증 결과의 사업자 정보")
    private final BusinessNumberValidateInfoResponse businessNumberValidateInfoResponse;
}