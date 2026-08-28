package za.co.bastion.fischl.bastion;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import za.co.bastion.fischl.CustomHttpClientOptions;
import za.co.bastion.fischl.FischlBastion;
import za.co.bastion.fischl.FischlBastionException;
import za.co.bastion.fischl.FischlEnvelopeBodyReader;

import java.util.List;

@RegisterRestClient(configKey = "fischl-bastion-api")
@RegisterProvider(FischlBastionExceptionMapper.class)
@RegisterProvider(CustomHttpClientOptions.class)
@RegisterProvider(FischlEnvelopeBodyReader.class)
public interface FischlBastionConnector {

    @POST
    @Path("/bastions")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlBastion create(@HeaderParam("Authorization") String authHeader, FischlBastion bastion) throws FischlBastionException;

    @GET
    @Path("/bastions/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlBastion read(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) throws FischlBastionException;

    @GET
    @Path("/bastions")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    List<FischlBastion> readAll(@HeaderParam("Authorization") String authHeader) throws FischlBastionException;

    @PUT
    @Path("/bastions/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlBastion update(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id, FischlBastion bastion) throws FischlBastionException;

    @DELETE
    @Path("/bastions/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void delete(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) throws FischlBastionException;
}
