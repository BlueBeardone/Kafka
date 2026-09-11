package za.co.bastion.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class ApiResponse {
    private final boolean success;
    private final String message;
    private final Object data;
    private final Object errors;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String renewedToken;

    public ApiResponse(boolean success, String message, Object data, Object errors) {
        this(success, message, data, errors, null);
    }

    public ApiResponse(boolean success, String message, Object data, Object errors, String renewedToken) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.errors = errors;
        this.renewedToken = renewedToken;
    }

    public static ApiResponse success(String message, Object data) {
        return new ApiResponse(true, message, data, null);
    }

    public static ApiResponse failure(String message, Object errors) {
        return new ApiResponse(false, message, null, errors);
    }

    public ApiResponse withRenewedToken(String renewedToken) {
        return new ApiResponse(success, message, data, errors, renewedToken);
    }
}
