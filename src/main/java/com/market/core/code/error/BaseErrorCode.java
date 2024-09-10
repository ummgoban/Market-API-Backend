package com.market.core.code.error;

import com.market.core.response.ErrorResponse;
import org.springframework.http.HttpStatus;

public interface BaseErrorCode {
    int getErrorCode();

    String getErrorMessage();

    HttpStatus getStatus();

    ErrorResponse getErrorResponse();
}