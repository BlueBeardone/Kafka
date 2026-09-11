package za.co.bastion.fischl;

import jakarta.inject.Inject;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
public class FischlRenewedTokenFilter implements ClientResponseFilter {

    public static final String HEADER = "X-Renewed-Token";

    @Inject
    FischlRenewedTokenHolder holder;

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext)
            throws IOException {
        String token = responseContext.getHeaderString(HEADER);
        if (token != null && !token.isBlank()) {
            holder.set(token);
        }
    }
}
