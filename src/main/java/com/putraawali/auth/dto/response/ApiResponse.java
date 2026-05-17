package com.putraawali.auth.dto.response;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private T data;
    private String message;
    private ErrorResponse error;

    public ApiResponse(String message, T data, ErrorResponse error) {
        this.message = message;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<T>("success", data, null);
    }

    public static <T> ApiResponse<T> error(ErrorResponse error) {
        return new ApiResponse<T>("error", null, error);
    }
    
    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException("Error converting to JSON", e);
        }
    }
}
