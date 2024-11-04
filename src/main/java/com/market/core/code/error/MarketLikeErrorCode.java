package com.market.core.code.error;


import com.market.core.response.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 가게 찜 에러 코드를 관리하는 enum 클래스입니다.
 */
@Getter
public enum MarketLikeErrorCode implements BaseErrorCode {

    // 404 NOT FOUND
    MARKET_LIKE_NOT_FOUND(404, "유저는 해당 가게를 찜하지 않았습니다.", HttpStatus.NOT_FOUND);

    private final int errorCode;
    private final String errorMessage;
    private final HttpStatus status;

    MarketLikeErrorCode(int errorCode, String errorMessage, HttpStatus status) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return new ErrorResponse(this.errorCode, this.errorMessage);
    }
}
