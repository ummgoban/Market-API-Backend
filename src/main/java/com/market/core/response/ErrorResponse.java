package com.market.core.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

/**
 * 프로젝트 전역적으로 사용되는 REST API 에러 응답 클래스입니다.
 */
@Getter
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {

    private final int errorCode;
    private final String errorMessage;


    public ErrorResponse(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
