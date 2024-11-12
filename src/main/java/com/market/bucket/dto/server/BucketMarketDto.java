package com.market.bucket.dto.server;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 회원의 장바구니에 담긴 상품의 가게 DTO 입니다.
 */
@Getter
@Builder
public class BucketMarketDto {

    @Schema(description = "가게 id")
    private Long id;

    @Schema(description = "가게 이름")
    private String name;

    @Schema(description = "가게 이미지")
    private List<String> images;
}
