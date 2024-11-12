package com.market.core.exception;

import com.market.core.code.error.BaseErrorCode;
import lombok.Getter;

/**
 * OAuth 관련 예외를 처리하는 클래스입니다.
 */
@Getter
public class OAuthException extends RuntimeException {

    private final BaseErrorCode errorCode;

    public OAuthException(BaseErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}