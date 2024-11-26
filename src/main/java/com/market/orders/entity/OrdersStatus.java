package com.market.orders.entity;

import com.market.core.exception.OrdersException;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

import static com.market.core.code.error.OrdersErrorCode.NOT_FOUND_ORDERS_STATUS;

@Getter
public enum OrdersStatus {
    IN_PROGRESS, // 결제 승인 이전의 주문 상태
    ORDERED, // 주문 접수
    ACCEPTED, // 주문 수락
    PICKEDUP, // 픽업 완료
    CANCELED, // 주문 취소
    PICKEDUP_OR_CANCELED; // 픽업 완료 or 주문 취소 (조회 시, 사용되는 데이터)

    public static List<OrdersStatus> getInProgressOrderStatus() {
        return Arrays.asList(OrdersStatus.ORDERED, OrdersStatus.ACCEPTED);
    }

    public static OrdersStatus from(String orderStatus) {

        for(OrdersStatus ordersStatus : OrdersStatus.values()) {
            if(ordersStatus.name().equals(orderStatus)) return ordersStatus;
        }
        throw new OrdersException(NOT_FOUND_ORDERS_STATUS);
    }
}
