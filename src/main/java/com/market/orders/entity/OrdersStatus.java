package com.market.orders.entity;

import lombok.Getter;

@Getter
public enum OrdersStatus {
    ORDERED,// 주문 접수
    ACCEPTED, // 주문 수락
    PICKUP, // 픽업 완료
    CANCEL, // 주문 취소
    PICKUP_OR_CANCEL // 픽업 완료 or 주문 취소 (조회 시, 사용되는 데이터)
}
