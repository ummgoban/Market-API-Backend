package com.market.core.exception;

import com.market.core.code.error.BaseErrorCode;
import com.market.core.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 애플리케이션 전역에서 발생하는 예외를 처리하는 핸들러 클래스입니다.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @Valid 관련 예외 Handler
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity handleValidationException(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException 발생");
        return ErrorResponse.toResponseEntityWithErrors(ex.getBindingResult());
    }

    /**
     * ConstraintViolationException 관련 예외 Handler
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("ConstraintViolationException 발생");
        return ErrorResponse.toResponseEntityWithConstraints(ex.getConstraintViolations());
    }

    /**
     * Security Access Denied 관련 예외 Handler
     */
    @ExceptionHandler(CustomSecurityException.class)
    protected ResponseEntity<ErrorResponse> handleCustomSecurityException(CustomSecurityException exception) {
        log.error("CustomSecurityException 발생");
        BaseErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(errorCode.getErrorResponse());
    }

    /**
     * Jwt 관련 예외 Handler
     */
    @ExceptionHandler(JwtException.class)
    protected ResponseEntity<ErrorResponse> handleMemberException(JwtException ex) {
        log.error("JwtException 발생");
        BaseErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(errorCode.getErrorResponse());
    }

    /**
     * OAuth 관련 예외 Handler
     */
    @ExceptionHandler(OAuthException.class)
    protected ResponseEntity<ErrorResponse> handleMemberException(OAuthException ex) {
        log.error("JwtException 발생");
        BaseErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(errorCode.getErrorResponse());
    }

    /**
     * S3 관련 예외 Handler
     */
    @ExceptionHandler(S3Exception.class)
    protected ResponseEntity<ErrorResponse> handleS3Exception(S3Exception ex) {
        log.error("S3Exception 발생");
        BaseErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(errorCode.getErrorResponse());
    }

    /**
     * Member 관련 예외 Handler
     */
    @ExceptionHandler(MemberException.class)
    protected ResponseEntity<ErrorResponse> handleMemberException(MemberException ex) {
        log.error("MemberException 발생");
        BaseErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(errorCode.getErrorResponse());
    }

    /**
     * Market 관련 예외 Handler
     */
    @ExceptionHandler(MarketException.class)
    protected ResponseEntity<ErrorResponse> handleMarketException(MarketException ex) {
        log.error("MarketException 발생");
        BaseErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(errorCode.getErrorResponse());
    }

    /**
     * MarketLike 관련 예외 Handler
     */
    @ExceptionHandler(MarketLikeException.class)
    protected ResponseEntity<ErrorResponse> handleMarketLikeException(MarketLikeException ex) {
        log.error("MarketLikeException 발생");
        BaseErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(errorCode.getErrorResponse());
    }
}