package za.co.bastion.fischl.hireling;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import za.co.bastion.fischl.CustomHttpClientOptions;
import za.co.bastion.fischl.FischlEnvelopeBodyReader;
import za.co.bastion.fischl.FischlHireling;
import za.co.bastion.fischl.FischlHirelingException;

import java.util.List;

@RegisterRestClient(configKey = "fischl-hireling-api")
@RegisterProvider(FischlHirelingExceptionMapper.class)
@RegisterProvider(CustomHttpClientOptions.class)
@RegisterProvider(FischlEnvelopeBodyReader.class)
public interface FischlHirelingConnector {

    @POST
    @Path("/hirelings")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlHireling create(@HeaderParam("Authorization") String authHeader, FischlHireling hireling) throws FischlHirelingException;

    @GET
    @Path("/hirelings/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlHireling read(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) throws FischlHirelingException;

    @GET
    @Path("/hirelings")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    List<FischlHireling> readAll(@HeaderParam("Authorization") String authHeader) throws FischlHirelingException;

    @PUT
    @Path("/hirelings/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlHireling update(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id, FischlHireling hireling) throws FischlHirelingException;

    @DELETE
    @Path("/hirelings/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void delete(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) throws FischlHirelingException;
}