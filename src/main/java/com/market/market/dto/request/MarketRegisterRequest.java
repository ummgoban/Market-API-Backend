package com.market.market.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * 매장 등록 요청을 위한 DTO 클래스입니다.
 */
@Getter
public class MarketRegisterRequest {

    @Schema(description = "매장의 이름", example = "매장 이름", required = true)
    private String marketName;

    @Schema(description = "사업자 등록 번호", example = "0000000000", required = true)
    private String businessNumber;

    @Schema(description = "사업장 주소", example = "사업장 주소", required = true)
    private String address;

    @Schema(description = "사업자 상세 주소", example = "사업자 상세 주소")
    private String specificAddress;

    @Schema(description = "사업장 연락처", example = "01012345678", required = true)
    private String contactNumber;
}