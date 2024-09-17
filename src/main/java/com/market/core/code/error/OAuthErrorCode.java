package com.market.core.code.error;

import com.market.core.response.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * OAuth 관련 에러 코드를 관리하는 enum 클래스입니다.
 */
@Getter
public enum OAuthErrorCode implements BaseErrorCode {
    // 400 BAD_REQUEST
    BAD_REQUEST_OAUTH_USER_INFO(400, "OAuth 사용자 정보를 가져오는 과정에서 오류가 발생했습니다.", HttpStatus.BAD_REQUEST),
    UNSUPPORTED_OAUTH_PROVIDER(400, "지원하지 않는 OAuth Provider 입니다.", HttpStatus.BAD_REQUEST),
    BAD_REQUEST_OAUTH_TOKEN(400, "OAuth 토큰을 요청하는 과정에서 오류가 발생했습니다.", HttpStatus.BAD_REQUEST),

    // 500 INTERNAL_SERVER_ERROR
    OAUTH_PROVIDER_SERVER_ERROR(500, "외부 OAuth 서버에서 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int errorCode;
    private final String errorMessage;
    private final HttpStatus status;

    OAuthErrorCode(int errorCode, String errorMessage, HttpStatus status) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return new ErrorResponse(this.errorCode, this.errorMessage);
    }
}