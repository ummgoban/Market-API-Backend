package com.market.orders.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * 결제 승인 요청을 위한 DTO 클래스입니다.
 */
@Getter
public class PaymentCreateRequsetDto {

    @NotBlank
    @Schema(description = "결제의 키 값", required = true)
    private String paymentKey;

    @NotBlank
    @Schema(description = "주문 ID", required = true)
    private String ordersId;

    @NotNull
    @Schema(description = "결제 금액", required = true)
    private Integer amount;

}
