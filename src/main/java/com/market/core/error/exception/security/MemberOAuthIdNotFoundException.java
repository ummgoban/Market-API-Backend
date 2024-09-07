package com.market.core.error.exception.security;

import com.market.core.code.BaseErrorCode;
import lombok.Getter;

@Getter
public class MemberOAuthIdNotFoundException extends RuntimeException {

    private final BaseErrorCode errorCode;

    public MemberOAuthIdNotFoundException(BaseErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
