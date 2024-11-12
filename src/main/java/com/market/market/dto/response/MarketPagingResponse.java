package com.market.market.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 가게 커서 기반 페이지네이션 응답 DTO입니다.
 */
@Getter
@Builder
@Schema(description = "상점 커서 기반 페이지네이션 응답 DTO입니다.")
public class MarketPagingResponse {

    @Schema(description = "가게 목록입니다.")
    List<MarketPagingInfoResponse> markets;

    @Schema(description = "다음 페이지 여부입니다.")
    boolean hasNext;
}
