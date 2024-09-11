package com.market.core.exception;

import com.market.core.code.error.BaseErrorCode;
import lombok.Getter;

@Getter
public class JwtException extends RuntimeException {

    private final BaseErrorCode errorCode;

    public JwtException(BaseErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}