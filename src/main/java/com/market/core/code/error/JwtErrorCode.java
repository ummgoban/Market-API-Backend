package com.market.core.code.error;

import com.market.core.response.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum JwtErrorCode implements BaseErrorCode {
    VALIDATION_TOKEN_FAILED(400, "유효하지 않은 토큰입니다.", HttpStatus.BAD_REQUEST),
    VALIDATION_TOKEN_EXPIRED(401, "토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    VALIDATION_TOKEN_NOT_AUTHORIZATION(401, "접근 권한이 없습니다.", HttpStatus.UNAUTHORIZED),

    NOT_FOUND_REFRESH_TOKEN(404, "refresh token이 존재하지 않습니다.", HttpStatus.NOT_FOUND);

    private final int errorCode;
    private final String errorMessage;
    private final HttpStatus status;

    JwtErrorCode(int errorCode, String errorMessage, HttpStatus status) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return new ErrorResponse(this.errorCode, this.errorMessage);
    }
}