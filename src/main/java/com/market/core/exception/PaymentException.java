package com.market.core.exception;


import com.market.core.code.error.BaseErrorCode;
import lombok.Getter;

/**
 * 결제 관련 예외를 처리하는 클래스입니다.
 */
@Getter
public class PaymentException extends RuntimeException {

    private final BaseErrorCode errorCode;

    public PaymentException(BaseErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
