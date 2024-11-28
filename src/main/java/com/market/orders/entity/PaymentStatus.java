package com.market.orders.entity;

import lombok.Getter;

@Getter
public enum PaymentStatus {

    IN_PROGRESS, // 결제 인증을 마친 상태 (결제 승인 전)
    DONE, // 결제가 승인된 상태
    CANCELED, // 승인된 결제가 취소된 상태
    ABORTED // 결제 승인이 실패한 상태

}
