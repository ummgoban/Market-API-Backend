package com.market.orders.dto.request;

import com.market.orders.annotation.ValidOrdersStatus;
import com.market.orders.entity.OrdersStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketOrdersRequest {

    @ValidOrdersStatus
    private String ordersStatus;

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
