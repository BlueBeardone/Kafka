package za.co.bastion.maurabastion.adapter;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.fischl.FischlRenewedTokenHolder;

import java.io.IOException;

@Provider
public class RenewedTokenEnvelopeFilter implements ContainerResponseFilter {

    @Inject
    FischlRenewedTokenHolder holder;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        String token = holder.get();
        if (token == null || token.isBlank()) {
            return;
        }
        holder.clear();
        if (responseContext.getEntity() instanceof ApiResponse apiResponse
                && apiResponse.getRenewedToken() == null) {
            responseContext.setEntity(apiResponse.withRenewedToken(token));
        }
    }
}