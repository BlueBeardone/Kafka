package za.co.bastion.fischl.identity;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;
import za.co.bastion.fischl.FischlIdentityException;

import java.io.IOException;

public class FischlIdentityExceptionMapper implements ResponseExceptionMapper<FischlIdentityException> {

    @Override
    public FischlIdentityException toThrowable(Response response) {
        FischlIdentityException exception = new FischlIdentityException();
        exception.setStatus(response.getStatus());

        String entity = response.readEntity(String.class);

        if (entity == null || entity.trim().isEmpty()) {
            exception.setMessage("HTTP " + response.getStatus() + " - Empty response from backend");
            exception.setCode("EMPTY_RESPONSE");
            return exception;
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            FischlIdentityException errorDetails = mapper.readValue(entity, FischlIdentityException.class);
            exception.setCode(errorDetails.getCode());
            exception.setTitle(errorDetails.getTitle());
            exception.setMessage(errorDetails.getMessage());
        } catch (IOException e) {
            exception.setMessage("Failed to parse error response: " + entity);
            exception.setDetails("Parse error: " + e.getMessage());
        }

        return exception;
    }

    @Override
    public boolean handles(int status, MultivaluedMap<String, Object> headers) {
        return status >= 400;
    }
}
