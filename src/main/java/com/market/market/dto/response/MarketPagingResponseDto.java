package com.market.market.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 가게 페이지 네이션 응답에서 가게 정보입니다. (이미지 정보 제외)
 */
@Getter
@Builder
public class MarketPagingResponseDto {

    @Schema(description = "가게 정보")
    MarketPagingInfoDto marketPagingInfoDto;

    @Schema(description = "가게 이미지")
    List<String> images;
}
