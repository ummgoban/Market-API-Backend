package com.market.orders.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * 주문 생성 응답 DTO 입니다.
 */
@Getter
@Builder
@Schema(description = "주문 생성 응답 DTO 입니다.")
public class OrdersCreateResponseDto {

    @Schema(description = "주문 ID")
    private String ordersId;

    @Schema(description = "주문 이름", example = "생수 외 1건")
    private String ordersName;

    @Schema(description = "주문 금액")
    private Integer amount;
}
