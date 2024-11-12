package com.market.market.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * 가게 등록 요청을 위한 DTO 클래스입니다.
 */
@Getter
public class MarketRegisterRequest {

    @Schema(description = "가게 이름", example = "가게 이름", required = true)
    private String name;

    @Schema(description = "사업자 등록 번호", example = "0000000000", required = true)
    private String businessNumber;

    @Schema(description = "가게 주소", example = "가게 주소", required = true)
    private String address;

    @Schema(description = "가게 상세 주소", example = "가게 상세 주소")
    private String specificAddress;

    @Schema(description = "가게 연락처", example = "01012345678", required = true)
    private String contactNumber;
}