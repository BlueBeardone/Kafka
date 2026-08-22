package za.co.bastion.fischl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class FischlEnvelopeBodyReader implements MessageBodyReader<Object> {

    private final ObjectMapper mapper = new ObjectMapper();

    private ObjectMapper mapper() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type != String.class && mediaType != null && mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE);
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations,
                           MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
                           InputStream entityStream) throws IOException {
        JsonNode root = mapper().readTree(entityStream);

        boolean enveloped = root != null && root.isObject()
                && root.has("success") && root.has("data");
        JsonNode payload = enveloped ? root.get("data") : root;

        if (payload == null || payload.isNull()) {
            return null;
        }

        Type target = genericType != null ? genericType : type;
        return mapper().readValue(payload.traverse(), mapper().constructType(target));
    }
}