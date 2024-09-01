package com.market.core.error.exception.security;

import com.market.core.code.BaseErrorCode;
import lombok.Getter;

@Getter
public class MemberEmailNotFoundException extends RuntimeException {

    private final BaseErrorCode errorCode;

    public MemberEmailNotFoundException(BaseErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
