package com.putraawali.auth.exception;

import com.putraawali.auth.enums.ErrorCodeEnum;

import lombok.Getter;

@Getter
public class AuthServiceException extends RuntimeException {
    private final ErrorCodeEnum errorCode;

    public AuthServiceException(String message, ErrorCodeEnum errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
