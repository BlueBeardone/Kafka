package za.co.bastion.fischl.newspapers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;
import za.co.bastion.fischl.FischlNewspaperException;

import java.io.IOException;

public class FischlNewspapersExceptionMapper implements ResponseExceptionMapper<FischlNewspaperException> {

    @Override
    public FischlNewspaperException toThrowable(Response response) {
        FischlNewspaperException exception = new FischlNewspaperException();
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
            FischlNewspaperException errorDetails = mapper.readValue(entity, FischlNewspaperException.class);
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
