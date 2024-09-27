package com.market.market.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 가게 등록 요청에 대한 응답 데이터를 담는 DTO 클래스입니다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class RegisterMarketResponse {

    @Schema(description = "등록된 Market의 ID", example = "1")
    private final Long marketId;
}