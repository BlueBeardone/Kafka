package za.co.bastion.fischl.identity;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import za.co.bastion.fischl.CustomHttpClientOptions;
import za.co.bastion.fischl.FischlIdentityException;
import za.co.bastion.fischl.FischlUser;

import java.util.List;

@RegisterRestClient(configKey = "fischl-identity-api")
@RegisterProvider(FischlIdentityExceptionMapper.class)
@RegisterProvider(CustomHttpClientOptions.class)
public interface FischlIdentityConnector {

    @POST
    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlUser create(FischlUser user) throws FischlIdentityException;

    @GET
    @Path("/users/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlUser read(@PathParam("id") String id) throws FischlIdentityException;

    @GET
    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    List<FischlUser> readAll() throws FischlIdentityException;

    @PUT
    @Path("/users/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlUser update(@PathParam("id") String id, FischlUser user) throws FischlIdentityException;

    @DELETE
    @Path("/users/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void delete(@PathParam("id") String id) throws FischlIdentityException;
}
