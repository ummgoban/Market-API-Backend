package com.market.orders.dto.server;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * 주문 상품 정보 DTO 입니다.
 */
@Getter
@Builder
public class OrdersProductsDto {

    @Schema(description = "상품 ID")
    private Long id;

    @Schema(description = "상품 이미지")
    private String image;

    @Schema(description = "상품 이름")
    private String name;

    @Schema(description = "상품 원가")
    private Integer originPrice;

    @Schema(description = "상품 할인가")
    private Integer discountPrice;

    @Schema(description = "상품 할인율")
    private Integer discountRate;

    @Schema(description = "주문한 상품 갯수")
    private Integer count;
}
