package com.market.product.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 상품 재고 수정 요청을 위한 DTO 클래스입니다.
 */
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductStockUpdateRequest {

    @Schema(description = "상품 ID", required = true)
    Long productId;

    @Schema(description = "변경할 추가 수량 [ +이면 1, -이면 -1]", required = true)
    Integer count;
}
