package za.co.bastion.maurabastion.adapter;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.exception.BackendException;
import za.co.bastion.common.exception.InvalidRequestException;

import static org.junit.jupiter.api.Assertions.*;

class MauraBastionExceptionMapperTest {

    private final MauraBastionExceptionMapper mapper = new MauraBastionExceptionMapper();

    private ApiResponse entity(Response response) {
        return (ApiResponse) response.getEntity();
    }

    @Test
    void invalidRequestReturnsBadRequestWithMessage() {
        Response response = mapper.toResponse(new InvalidRequestException("bad"));

        assertEquals(400, response.getStatus());
        assertEquals("bad", entity(response).getMessage());
    }

    @Test
    void backendClientErrorReturnsMessage() {
        Response response = mapper.toResponse(new BackendException(403, "nope"));

        assertEquals(403, response.getStatus());
        assertEquals("nope", entity(response).getMessage());
    }

    @Test
    void backendServerErrorIsSanitized() {
        Response response = mapper.toResponse(new BackendException(500, "secret detail"));

        assertEquals(500, response.getStatus());
        assertEquals("Internal server error", entity(response).getMessage());
        assertFalse(entity(response).getMessage().contains("secret detail"));
    }

    @Test
    void runtimeExceptionIsSanitized() {
        Response response = mapper.toResponse(new RuntimeException("boom"));

        assertEquals(500, response.getStatus());
        assertEquals("Internal server error", entity(response).getMessage());
        assertFalse(entity(response).getMessage().contains("boom"));
    }

    @Test
    void backendBadGatewayIsSanitized() {
        Response response = mapper.toResponse(new BackendException(502, "boom"));

        assertEquals(502, response.getStatus());
        assertEquals("Internal server error", entity(response).getMessage());
        assertFalse(entity(response).getMessage().contains("boom"));
    }
}