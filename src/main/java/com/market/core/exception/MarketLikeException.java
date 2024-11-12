package com.market.core.exception;


import com.market.core.code.error.BaseErrorCode;
import lombok.Getter;

/**
 * 가게 찜 관련 에러를 처리하는 클래스입니다.
 */
@Getter
public class MarketLikeException extends RuntimeException {

    private final BaseErrorCode errorCode;

    public MarketLikeException(BaseErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
