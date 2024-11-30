package com.market.product.dto.request;

import com.market.product.entity.ProductStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 상품 수정 요청을 위한 DTO 클래스입니다.
 */
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductUpdateRequest {

    @Schema(description = "상품 ID", required = true)
    Long productId;

    @Schema(description = "상품 이미지", example = "https://.../ab123...456.png", required = true)
    private String productImage;

    @Schema(description = "상품명", example = "상품명", required = true)
    private String name;

    @Pattern(regexp = "IN_STOCK|OUT_OF_STOCK|HIDDEN", message = "상품 상태는 IN_STOCK, OUT_OF_STOCK, HIDDEN 중 하나여야 합니다.")
    @Schema(description = "상품 상태", example = "IN_STOCK/OUT_OF_STOCK/HIDDEN", required = true, allowableValues = {"IN_STOCK", "OUT_OF_STOCK", "HIDDEN"})
    private ProductStatus productStatus;

    @Schema(description = "판매 정가", example = "10000", required = true)
    private int originPrice;

    @Schema(description = "할인가", example = "8000", required = true)
    private int discountPrice;

    @Schema(description = "할인율", example = "20", required = true)
    private int discountRate;

    @Schema(description = "재고", example = "2", required = true)
    private int stock;

    @Schema(description = "상품 태그 목록", required = true)
    private List<String> productTags = new ArrayList<>();
}
