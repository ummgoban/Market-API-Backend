package com.market.orders.dto.request;

import com.market.orders.annotation.ValidOrdersStatus;
import com.market.orders.entity.OrdersStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "가게의 주문 관리 조회 요청 DTO 입니다.")
public class MarketOrdersRequest {

    @ValidOrdersStatus
    @Schema(description = "주문 상태 값. [접수 대기 : ORDERED, 주문 수락(픽업 대기) : ACCEPTED, 픽업완료/취소된 주문 : PICKUP_OR_CANCEL]")
    private String ordersStatus;

    @Schema(description = "가게 ID")
    private Long marketId;

    public static List<OrdersStatus> from(MarketOrdersRequest request) {

        if (request.ordersStatus.equals(OrdersStatus.PICKUP_OR_CANCEL.name()))
            return new ArrayList<>(List.of(OrdersStatus.CANCEL, OrdersStatus.PICKUP));

        OrdersStatus orderStatus = Arrays.stream(OrdersStatus.values()).
                filter(ordersStatus -> ordersStatus.name().equals(request.getOrdersStatus()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid order status: " + request.getOrdersStatus()));

        return new ArrayList<>(List.of(orderStatus));
    }
}
