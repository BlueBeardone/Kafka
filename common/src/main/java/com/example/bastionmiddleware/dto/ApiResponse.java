package com.example.bastionmiddleware.dto;

public record ApiResponse(boolean success, String message, Object data, Object errors) {
    public static ApiResponse success(String message, Object data) {
        return new ApiResponse(true, message, data, null);
    }

    public static ApiResponse failure(String message, Object errors) {
        return new ApiResponse(false, message, null, errors);
    }
}
