package com.putraawali.auth.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.putraawali.auth.dto.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final String internalServerErrorMessage = "An unexpected error occurred";

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

        HttpStatusCode statusCode = ex.getStatusCode() != null ? ex.getStatusCode() : HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity.status(Objects.requireNonNull(statusCode)).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneralError(Exception ex) {
        Map<String, String> errors = new HashMap<>();

        errors.put("message", internalServerErrorMessage);
        errors.put("details", ex.getMessage());

        ApiResponse<Object> response = ApiResponse.error(errors);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeError(RuntimeException ex) {
        Map<String, String> errors = new HashMap<>();

        errors.put("message", internalServerErrorMessage);
        errors.put("details", ex.getMessage());

        ApiResponse<Object> response = ApiResponse.error(errors);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> errors = new HashMap<>();

        errors.put("message", "Invalid argument provided");
        errors.put("details", ex.getMessage());

        ApiResponse<Object> response = ApiResponse.error(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<Object>> handleNullPointer(NullPointerException ex) {
        Map<String, String> errors = new HashMap<>();

        errors.put("message", internalServerErrorMessage);
        errors.put("details", ex.getMessage());

        ApiResponse<Object> response = ApiResponse.error(errors);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalState(IllegalStateException ex) {
        Map<String, String> errors = new HashMap<>();

        errors.put("message", internalServerErrorMessage);
        errors.put("details", ex.getMessage());

        ApiResponse<Object> response = ApiResponse.error(errors);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataAccess(DataAccessException ex) {
        Map<String, String> errors = new HashMap<>();

        errors.put("message", internalServerErrorMessage);
        errors.put("details", ex.getMessage());

        ApiResponse<Object> response = ApiResponse.error(errors);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
