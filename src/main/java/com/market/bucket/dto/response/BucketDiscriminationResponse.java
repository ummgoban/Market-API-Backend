package com.market.bucket.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 장바구니에 다른 가게 상품이 있는지 판별 응답 DTO 입니다.
 * 동일한 가게의 상품이 존재 or 현재 장바구니가 비어있으면 true, 다른 가게의 상품이 존재하면 false
 */
@Getter
@AllArgsConstructor
@Schema(description = "장바구니 조회 응답")
public class BucketDiscriminationResponse {

    @Schema(description = "동일한 가게의 상품만 존재 or 현재 장바구니가 비어있으면 true, 다른 가게의 상품이 존재하면 false")
    private boolean sameMarketProduct;
}
