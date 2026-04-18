package com.putraawali.auth.exception;

import org.springframework.http.HttpStatusCode;

import io.micrometer.common.lang.NonNull;
import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {
    @NonNull
    private final HttpStatusCode statusCode;

    public AuthException(String message, HttpStatusCode statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
