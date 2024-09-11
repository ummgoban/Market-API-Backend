package com.market.core.exception;

import com.market.core.code.error.BaseErrorCode;
import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {

    private final BaseErrorCode errorCode;

    public MemberException(BaseErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}