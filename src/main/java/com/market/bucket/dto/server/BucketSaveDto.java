package com.market.bucket.dto.server;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 장바구니에 담고자 하는 상품의 정보 DTO 입니다.
 */
@Getter
@NoArgsConstructor
@Schema(description = "장바구니에 담고자 하는 상품의 정보")
public class BucketSaveDto {

    @Schema(description = "상품 id")
    private Long id;

    @Schema(description = "장바구니 속 상품 갯수")
    private Integer count;
}
