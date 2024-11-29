package com.market.orders.entity;

import com.market.core.code.error.PaymentErrorCode;
import com.market.core.exception.PaymentException;
import lombok.Getter;

import static com.market.core.code.error.PaymentErrorCode.Payment_Method_NOT_FOUND;

@Getter
public enum PaymentMethod {

    CARD("카드"), // 카드
    VIRTUAL_ACCOUNT("가상계좌"), // 가상 계좌
    MOBILE_PHONE("휴대폰"), // 모바일 휴대폰
    CULTURE_GIFT_CERTIFICATE("문화상품권"), // 문화 상품권
    BOOK_CULTURE_GIFT_CERTIFICATE("도서문화상품권"), // 도서 문화 상품권
    GAME_CULTE_GIFT_CERTIFICATE("게임문화상품권"), // 게임 문화 상품권
    TRANSFER("계좌이체"), // 계좌 이체
    EASYPAY("간편결제"); // 간편결제

    private final String method;

    PaymentMethod(String method) {
        this.method = method;
    }

    public static PaymentMethod getFromString(String name) {
        for (PaymentMethod paymentMethod : PaymentMethod.values()) {
            if (paymentMethod.getMethod().equals(name)) {
                return paymentMethod;
            }
        }

        throw new PaymentException(Payment_Method_NOT_FOUND);
    }
}
