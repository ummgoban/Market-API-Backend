package com.market.bucket.dto.server;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * 회원의 장바구니에 담긴 다른 가게의 상품 조회 DTO 입니다.
 */
@Getter
@Builder
public class BucketDeletionDto {
    @Schema(description = "장바구니 id 값입니다.")
    private Long id;
}
