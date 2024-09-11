package com.market.core.code.error;

import com.market.core.response.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MemberErrorCode implements BaseErrorCode {
    // 404 NOT_FOUND
    NOT_FOUND_MEMBER_ID(404, "존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_MEMBER_OAUTH_ID(404, "존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_MEMBER_ROLES(404, "회원의 권한이 존재하지 않습니다.", HttpStatus.NOT_FOUND);

    private final int errorCode;
    private final String errorMessage;
    private final HttpStatus status;

    MemberErrorCode(int errorCode, String errorMessage, HttpStatus status) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return new ErrorResponse(this.errorCode, this.errorMessage);
    }
}