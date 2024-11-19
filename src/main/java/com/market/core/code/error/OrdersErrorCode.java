package com.market.core.code.error;


import com.market.core.response.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 주문 에러 코드를 관리하는 enum 클래스입니다.
 */
@Getter
public enum OrdersErrorCode implements BaseErrorCode {

    // 404 NOT_FOUND
    NOT_FOUND_ORDERS_ID(404, "존재하지 않는 주문번호입니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_ORDERS_STATUS(404, "존재하지 않는 주문 상태입니다.", HttpStatus.NOT_FOUND);

    private final int errorCode;
    private final String errorMessage;
    private final HttpStatus status;

    OrdersErrorCode(int errorCode, String errorMessage, HttpStatus status) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return new ErrorResponse(this.errorCode, this.errorMessage);
    }
}
