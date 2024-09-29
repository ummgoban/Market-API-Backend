package com.market.core.code.error;

import com.market.core.response.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 가게 관련 에러 코드를 관리하는 enum 클래스입니다.
 */
@Getter
public enum MarketErrorCode implements BaseErrorCode {
    // 400 BAD_REQUEST
    BAD_REQUEST_BUSINESS_STATUS(400, "잘못된 요청입니다. 사업자 등록 번호를 확인하세요.", HttpStatus.BAD_REQUEST),
    NOT_FOUND_MARKET_ID(404, "존재하지 않는 가게입니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_MARKET_NAME(400, "이미 존재하는 마켓 이름입니다.", HttpStatus.BAD_REQUEST),
    INVALID_BUSINESS_NUMBER(400, "유효하지 않은 사업자 등록번호입니다.", HttpStatus.BAD_REQUEST),
    INVALID_BUSINESS_HOURS(400, "유효하지 않은 영업 시간입니다.", HttpStatus.BAD_REQUEST),
    INVALID_PICKUP_HOURS(400, "유효하지 않은 픽업 시간입니다.", HttpStatus.BAD_REQUEST),

    // 500 INTERNAL_SERVER_ERROR
    BUSINESS_STATUS_SERVER_ERROR(500, "사업자 등록 번호 조회 서버에서 문제가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int errorCode;
    private final String errorMessage;
    private final HttpStatus status;

    MarketErrorCode(int errorCode, String errorMessage, HttpStatus status) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return new ErrorResponse(this.errorCode, this.errorMessage);
    }
}