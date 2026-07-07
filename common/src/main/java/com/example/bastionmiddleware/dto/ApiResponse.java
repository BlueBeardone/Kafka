package com.example.bastionmiddleware.dto;

public class ApiResponse {
    private final boolean success;
    private final String message;
    private final Object data;
    private final Object errors;

    public ApiResponse(boolean success, String message, Object data, Object errors) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.errors = errors;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public Object getErrors() {
        return errors;
    }

    public static ApiResponse success(String message, Object data) {
        return new ApiResponse(true, message, data, null);
    }

    public static ApiResponse failure(String message, Object errors) {
        return new ApiResponse(false, message, null, errors);
    }
}
