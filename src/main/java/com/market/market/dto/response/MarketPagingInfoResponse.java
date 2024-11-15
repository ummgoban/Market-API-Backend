package com.market.market.dto.response;

import com.market.product.dto.response.ProductResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 가게 페이지 네이션 응답에서 가게 정보입니다. (이미지 정보 제외)
 */
@Getter
@Builder
public class MarketPagingInfoResponse {

    @Schema(description = "가게 id 값입니다.")
    private Long id;

    @Schema(description = "가게 이름입니다.")
    private String name;

    @Schema(description = "가게 주소입니다.")
    private String address;

    @Schema(description = "가게 상세 주소입니다.")
    private String specificAddress;

    @Schema(description = "가게 오픈 시간입니다.")
    private String openAt;

    @Schema(description = "가게 마감 시간입니다.")
    private String closeAt;

    @Schema(description = "가게 픽업 시작 시간입니다.")
    private String pickupStartAt;

    @Schema(description = "가게 픽업 마감 시간입니다.")
    private String pickupEndAt;

    @Schema(description = "가게의 상품 정보")
    List<ProductResponse> products;
}

