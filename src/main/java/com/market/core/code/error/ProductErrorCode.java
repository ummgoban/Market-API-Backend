package com.market.core.code.error;


import com.market.core.response.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 상품 에러 코드를 관리하는 enum 클래스입니다.
 */
@Getter
public enum ProductErrorCode implements BaseErrorCode {

    NOT_FOUND_PRODUCT_ID(404, "존재하지 않는 상품입니다.", HttpStatus.NOT_FOUND);

    private final int errorCode;
    private final String errorMessage;
    private final HttpStatus status;

    ProductErrorCode(int errorCode, String errorMessage, HttpStatus status) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return new ErrorResponse(this.errorCode, this.errorMessage);
    }
}
