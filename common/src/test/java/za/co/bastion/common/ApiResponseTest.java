package za.co.bastion.common;

import org.junit.jupiter.api.Test;
import za.co.bastion.common.dto.ApiResponse;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    void renewedToken_isNullByDefault() {
        ApiResponse response = ApiResponse.success("ok", "data");
        assertNull(response.getRenewedToken());
    }

    @Test
    void withRenewedToken_returnsCopyWithToken_andLeavesOriginalUntouched() {
        ApiResponse original = ApiResponse.success("ok", "data");
        ApiResponse copy = original.withRenewedToken("token-123");

        assertEquals("token-123", copy.getRenewedToken());
        assertNull(original.getRenewedToken());
        assertEquals(original.isSuccess(), copy.isSuccess());
        assertEquals(original.getData(), copy.getData());
        assertEquals(original.getMessage(), copy.getMessage());
    }

    @Test
    void fiveArgConstructor_setsRenewedToken() {
        ApiResponse response = new ApiResponse(true, "ok", "data", null, "tok");
        assertEquals("tok", response.getRenewedToken());
    }
}