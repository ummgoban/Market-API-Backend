package com.market.core.exception;

import com.market.core.code.error.BaseErrorCode;
import com.market.core.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 애플리케이션 전역에서 발생하는 예외를 처리하는 핸들러 클래스입니다.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Member 관련 예외 Handler
     */
    @ExceptionHandler(MemberException.class)
    protected ResponseEntity<ErrorResponse> handleMemberException(MemberException exception) {
        BaseErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(errorCode.getErrorResponse());
    }

    /**
     * Jwt 관련 예외 Handler
     */
    @ExceptionHandler(JwtException.class)
    protected ResponseEntity<ErrorResponse> handleMemberException(JwtException exception) {
        BaseErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(errorCode.getErrorResponse());
    }

    /**
     * OAuth 관련 예외 Handler
     */
    @ExceptionHandler(OAuthException.class)
    protected ResponseEntity<ErrorResponse> handleMemberException(OAuthException exception) {
        BaseErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(errorCode.getErrorResponse());
    }
}