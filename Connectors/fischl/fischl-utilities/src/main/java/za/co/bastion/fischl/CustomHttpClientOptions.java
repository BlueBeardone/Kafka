package za.co.bastion.fischl;

import io.vertx.core.http.HttpClientOptions;
import jakarta.annotation.Priority;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(1)
public class CustomHttpClientOptions implements ContextResolver<HttpClientOptions> {
    @Override
    public HttpClientOptions getContext(Class<?> type) {
        HttpClientOptions options = new HttpClientOptions();
        options.setMaxHeaderSize(32768);
        return options;
    }
}
