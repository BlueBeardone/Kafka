package za.co.bastion.fischl.defender;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import za.co.bastion.fischl.CustomHttpClientOptions;
import za.co.bastion.fischl.FischlDefender;
import za.co.bastion.fischl.FischlDefenderException;

import java.util.List;

@RegisterRestClient(configKey = "fischl-defender-api")
@RegisterProvider(FischlDefenderExceptionMapper.class)
@RegisterProvider(CustomHttpClientOptions.class)
public interface FischlDefenderConnector {

    @POST
    @Path("/defenders")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlDefender create(FischlDefender defender) throws FischlDefenderException;

    @GET
    @Path("/defenders/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlDefender read(@PathParam("id") String id) throws FischlDefenderException;

    @GET
    @Path("/defenders")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    List<FischlDefender> readAll() throws FischlDefenderException;

    @PUT
    @Path("/defenders/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlDefender update(@PathParam("id") String id, FischlDefender defender) throws FischlDefenderException;

    @DELETE
    @Path("/defenders/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void delete(@PathParam("id") String id) throws FischlDefenderException;
}
