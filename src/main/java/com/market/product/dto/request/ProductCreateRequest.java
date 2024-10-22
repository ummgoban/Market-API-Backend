package com.market.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 상품 등록 요청을 위한 DTO 클래스입니다.
 */
@Getter
@RequiredArgsConstructor
public class ProductCreateRequest {

    @Schema(description = "상품 이미지", example = "https://.../ab123...456.png", required = true)
    private final String productImage;

    @Schema(description = "상품명", example = "상품명", required = true)
    private final String name;

    @Schema(description = "판매 정가", example = "10000", required = true)
    private final int originPrice;

    @Schema(description = "할인가", example = "8000", required = true)
    private final int discountPrice;

    @Schema(description = "할인율", example = "20", required = true)
    private final int discountRate;

    @Schema(description = "재고", example = "2", required = true)
    private final int stock;
}
