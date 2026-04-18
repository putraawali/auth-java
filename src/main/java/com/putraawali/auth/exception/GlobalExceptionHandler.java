package com.putraawali.auth.exception;

import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.putraawali.auth.dto.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put("message", error.getDefaultMessage()));

        ApiResponse<Object> response = ApiResponse.error(errors);

        return ResponseEntity.badRequest().body(response);
    }
    
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthError(AuthException ex) {
        Map<String, String> errors = new HashMap<>();

        errors.put("message", ex.getMessage());

        ApiResponse<Object> response = ApiResponse.error(errors);

        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }
}
