package com.market.orders.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * 결제 승인 요청을 위한 DTO 클래스입니다.
 */
@Getter
public class PaymentCreateRequsetDto {

    @Schema(description = "결제의 키 값", required = true)
    private String paymentKey;

    @Schema(description = "주문 ID", required = true)
    private String ordersId;

    @Schema(description = "결제 금액", required = true)
    private Integer amount;

}
