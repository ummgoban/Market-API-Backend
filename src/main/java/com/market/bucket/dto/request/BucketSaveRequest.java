package com.market.bucket.dto.request;


import com.market.bucket.dto.server.BucketSaveDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 장바구니 추가 요청 DTO 입니다.
 */
@Getter
@NoArgsConstructor
@Schema(description = "장바구니 추가 요청 응답")
public class BucketSaveRequest {

    @Schema(description = "장바구니에 담고자하는 상품의 목록")
    List<BucketSaveDto> products;
}
