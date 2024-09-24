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

    // 500 INTERNAL_SERVER_ERROR
    OAUTH_PROVIDER_SERVER_ERROR(500, "OAuth 서버에서 문제가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // 네이버 OAuth 개발자 문서에 따른 에러 정의
    AUTHENTICATION_FAILED(401, "인증에 실패했습니다.", HttpStatus.UNAUTHORIZED),
    OAUTH_HEADER_NOT_EXISTS(401, "OAuth 인증 헤더가 누락되었습니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(403, "권한이 없습니다. 클라이언트 ID 값을 확인하고 다시 시도해 주세요.", HttpStatus.FORBIDDEN),
    NOT_FOUND(404, "OAuth 서버에서 사용자 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // 카카오 OAuth 개발자 문서에 따른 에러 정의
    INVALID_CLIENT(400, "앱 키가 존재하지 않거나 잘못된 앱 키입니다.", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(401, "잘못된 토큰입니다. 새로운 토큰으로 다시 시도해 주세요.", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(401, "유효하지 않은 ID 토큰 발급자입니다.", HttpStatus.FORBIDDEN);


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