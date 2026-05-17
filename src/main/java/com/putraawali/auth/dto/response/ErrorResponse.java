package com.putraawali.auth.dto.response;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.putraawali.auth.enums.ErrorCodeEnum;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final int statusCode;
    private String message;
    private ErrorCodeEnum errorCode;
    
    public ErrorResponse(String message, ErrorCodeEnum errorCode) {
        this.message = message;
        this.errorCode = errorCode;
        this.statusCode = Objects.requireNonNull(mapErrorCodeToStatus(errorCode)).value();
    }
    
    private HttpStatusCode mapErrorCodeToStatus(ErrorCodeEnum errorCode) {
        switch (errorCode) {
            case DATA_NOT_FOUND:
                return HttpStatus.NOT_FOUND;
            case INVALID_CREDENTIALS:
            case INVALID_TOKEN:
            case EXPIRED_TOKEN:
                return HttpStatus.UNAUTHORIZED;
            case DUPLICATE_EMAIL:
                return HttpStatus.CONFLICT;
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
