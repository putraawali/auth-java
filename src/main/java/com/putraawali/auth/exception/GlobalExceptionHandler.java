package com.putraawali.auth.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.putraawali.auth.dto.response.ApiResponse;
import com.putraawali.auth.dto.response.ErrorResponse;
import com.putraawali.auth.enums.ErrorCodeEnum;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final String internalServerErrorMessage = "An unexpected error occurred";

    private ResponseEntity<ApiResponse<Object>> internalServerError() {
        ErrorResponse errorResponse = new ErrorResponse(internalServerErrorMessage, ErrorCodeEnum.INTERNAL_SERVER_ERROR);
        ApiResponse<Object> response = ApiResponse.error(errorResponse);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put("message", error.getDefaultMessage()));

        ErrorResponse errorResponse = new ErrorResponse("Validation failed", ErrorCodeEnum.VALIDATION_FAILED);
        ApiResponse<Object> response = ApiResponse.error(errorResponse);

        return ResponseEntity.badRequest().body(response);
    }
    
    @ExceptionHandler(AuthServiceException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthError(AuthServiceException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getErrorCode());

        ApiResponse<Object> response = ApiResponse.error(errorResponse);

        int statusCode = errorResponse.getStatusCode();

        return ResponseEntity.status(statusCode).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneralError(Exception ex) {
        return internalServerError();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeError(RuntimeException ex) {
        return internalServerError();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Invalid argument provided", ErrorCodeEnum.INVALID_ARGUMENT);
        ApiResponse<Object> response = ApiResponse.error(errorResponse);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<Object>> handleNullPointer(NullPointerException ex) {
        return internalServerError();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalState(IllegalStateException ex) {
        return internalServerError();
    }
    
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataAccess(DataAccessException ex) {
        return internalServerError();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Object>> NoHandlerFoundException(NoHandlerFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Endpoint not found", ErrorCodeEnum.ROUTE_NOT_FOUND);
        ApiResponse<Object> response = ApiResponse.error(errorResponse);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
