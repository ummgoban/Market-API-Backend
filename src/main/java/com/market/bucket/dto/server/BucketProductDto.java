package com.market.bucket.dto.server;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * 회원의 장바구니에 담긴 상품 정보 DTO 입니다.
 */
@Getter
@Builder
public class BucketProductDto {

    @Schema(description = "상품 id")
    private Long id;

    @Schema(description = "상품 이름")
    private String name;

    @Schema(description = "상품 이미지")
    private String image;

    @Schema(description = "상품 원가")
    private Integer originPrice;

    @Schema(description = "상품 할인가(실 판매가)")
    private Integer discountPrice;

    @Schema(description = "상품 재고")
    private Integer stock;

    @Schema(description = "장바구니 속 상품 갯수")
    private Integer count;
}
