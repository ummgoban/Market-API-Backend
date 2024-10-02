package com.market.core.code.error;

import com.market.core.response.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 장바구니 에러 코드를 관리하는 enum 클래스입니다.
 */
@Getter
public enum S3ErrorCode implements BaseErrorCode {

    FILE_UPLOAD_ERROR(500, "파일 업로드 중 오류가 발생했습니다. 다시 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR),
    IMAGE_NOT_FOUND_ERROR(404, "S3 Bucket에 사진 파일이 존재하지 않습니다. URL을 확인해주세요.", HttpStatus.NOT_FOUND);

    private final int errorCode;
    private final String errorMessage;
    private final HttpStatus status;

    S3ErrorCode(int errorCode, String errorMessage, HttpStatus status) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return new ErrorResponse(this.errorCode, this.errorMessage);
    }
}