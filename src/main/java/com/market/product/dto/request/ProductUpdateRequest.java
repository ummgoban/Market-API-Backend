package com.market.product.dto.request;

import com.market.product.entity.ProductStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 상품 수정 요청을 위한 DTO 클래스입니다.
 */
@Getter
@RequiredArgsConstructor
public class ProductUpdateRequest {

    @Schema(description = "상품 이미지", example = "https://.../ab123...456.png", required = true)
    private final String productImage;

    @Schema(description = "상품명", example = "상품명", required = true)
    private final String name;

    @Schema(description = "상품 상태", example = "OUT_OF_STOCK", required = true, allowableValues = {"IN_STOCK", "OUT_OF_STOCK", "HIDDEN"})
    private ProductStatus productStatus;

    @Schema(description = "판매 정가", example = "10000", required = true)
    private final int originPrice;

    @Schema(description = "할인가", example = "8000", required = true)
    private final int discountPrice;

    @Schema(description = "할인율", example = "20", required = true)
    private final int discountRate;

    @Schema(description = "판매 정가", example = "판매 정가", required = true)
    private final int stock;
}
