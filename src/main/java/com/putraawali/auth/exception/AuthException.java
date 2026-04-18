package com.putraawali.auth.exception;

import java.util.Objects;

import org.springframework.http.HttpStatusCode;
import org.springframework.lang.NonNull;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {
    @NonNull
    private final HttpStatusCode statusCode;

    public AuthException(String message, HttpStatusCode statusCode) {
        super(message);
        this.statusCode = Objects.requireNonNull(statusCode);
    }
}
