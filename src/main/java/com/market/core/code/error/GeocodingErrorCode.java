package com.market.core.code.error;


import com.market.core.response.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Geocoding 에러 코드를 관리하는 enum 클래스입니다.
 */
@Getter
public enum GeocodingErrorCode implements BaseErrorCode {

    INVALID_ADDRESS(400, "유효한 주소가 아닙니다.", HttpStatus.BAD_REQUEST),
    BAD_REQUEST_EXCEPTION(400, "요청 구문 오류", HttpStatus.BAD_REQUEST),
    AUTHENTICATION_FAILED(401, "인증 실패", HttpStatus.UNAUTHORIZED),
    PERMISSION_DENIED(401, "접근 권한 없음", HttpStatus.UNAUTHORIZED),
    NOT_FOUND_EXCEPTION(404, "서버에서 찾을 수 없음", HttpStatus.NOT_FOUND),
    REQUEST_ENTITY_TOO_LARGE(413, "요청 크기(10 MB) 초과", HttpStatus.PAYLOAD_TOO_LARGE),
    QUOTA_EXCEEDED(429, "요청 할당량 초과", HttpStatus.TOO_MANY_REQUESTS),
    THROTTLE_LIMITED(429, "너무 빠르거나 잦은 요청", HttpStatus.TOO_MANY_REQUESTS),
    RATE_LIMITED(429, "특정 시간 동안 너무 많은 요청", HttpStatus.TOO_MANY_REQUESTS),

    UNEXPECTED_CLIENT_ERROR(500, "알 수 없는 클라이언트 오류", HttpStatus.INTERNAL_SERVER_ERROR),
    UNEXPECTED_SERVER_ERROR(500, "알 수 없는 서버 오류", HttpStatus.INTERNAL_SERVER_ERROR),
    ENDPOINT_ERROR(503, "엔트포인트 오류", HttpStatus.SERVICE_UNAVAILABLE),
    ENDPOINT_TIMEOUT(504, "엔트포인트 타임아웃", HttpStatus.GATEWAY_TIMEOUT);

    private final int errorCode;
    private final String errorMessage;
    private final HttpStatus status;

    GeocodingErrorCode(int errorCode, String errorMessage, HttpStatus status) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return new ErrorResponse(this.errorCode, this.errorMessage);
    }
}
