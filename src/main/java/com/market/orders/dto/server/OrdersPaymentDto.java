package com.market.orders.dto.server;

import com.market.orders.entity.OrdersStatus;
import com.market.orders.entity.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrdersPaymentDto {

    @Schema(description = "주문 ID")
    private String id;

    @Schema(description = "주문 접수된 시각")
    private LocalDateTime createdAt;

    @Schema(description = "픽업 완료 또는 주문 취소된 시각")
    private LocalDateTime doneAt;

    @Schema(description = "픽업 희망 시각")
    private LocalDateTime pickupReservedAt;

    @Schema(description = "주문 가격")
    private Integer ordersPrice;

    @Schema(description = "주문자명")
    private String ordersMemberName;

    @Schema(description = "주문 이름")
    private String ordersName;

    @Schema(description = "주문 상태")
    private OrdersStatus ordersStatus;

    @Schema(description = "요청사항")
    private String customerRequest;

    @Schema(description = "결제번호")
    private String paymentKey;

    @Schema(description = "결제시각")
    private LocalDateTime approvedAt;

    @Schema(description = "결제금액")
    private Integer totalAmount;

    @Schema(description = "결제수단")
    private PaymentMethod method;
}
