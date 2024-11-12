package com.market.orders.entity;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum OrdersStatus {
    ORDERED,// 주문 접수
    ACCEPTED, // 주문 수락
    PICKUPED, // 픽업 완료
    CANCELED, // 주문 취소
    PICKUPED_OR_CANCELED; // 픽업 완료 or 주문 취소 (조회 시, 사용되는 데이터)

    public static List<OrdersStatus> getInProgressOrderStatus() {
        return Arrays.asList(OrdersStatus.ORDERED, OrdersStatus.ACCEPTED);
    }

}
