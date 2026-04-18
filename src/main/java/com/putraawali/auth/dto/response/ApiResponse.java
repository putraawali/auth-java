package com.putraawali.auth.dto.response;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private T data;
    private String message;
    private Object error;
    // private int statusCode;

    public ApiResponse(String message, T data, Object error /* , int statusCode*/) {
        this.message = message;
        this.data = data;
        this.error = error;
        // this.statusCode = statusCode;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<T>("success", data, null);
    }

    public static <T> ApiResponse<T> error(Object error) {
        return new ApiResponse<T>("error", null, error);
    }
}
