package com.market.core.exception;

import com.market.core.code.error.BaseErrorCode;
import lombok.Getter;

/**
 * 인증 권한이 없는 엔드포인트 접근 관련 예외를 처리하는 클래스입니다.
 */
@Getter
public class CustomSecurityException extends RuntimeException {

    private final BaseErrorCode errorCode;

    public CustomSecurityException(BaseErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
