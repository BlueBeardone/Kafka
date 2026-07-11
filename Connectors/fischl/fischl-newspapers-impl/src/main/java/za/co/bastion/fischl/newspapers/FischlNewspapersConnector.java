package za.co.bastion.fischl.newspapers;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import za.co.bastion.fischl.CustomHttpClientOptions;
import za.co.bastion.fischl.FischlNewspaper;
import za.co.bastion.fischl.FischlNewspaperException;

import java.util.List;

@RegisterRestClient(configKey = "fischl-newspapers-api")
@RegisterProvider(FischlNewspapersExceptionMapper.class)
@RegisterProvider(CustomHttpClientOptions.class)
public interface FischlNewspapersConnector {

    @POST
    @Path("/newspapers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlNewspaper create(FischlNewspaper newspaper) throws FischlNewspaperException;

    @GET
    @Path("/newspapers/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlNewspaper read(@PathParam("id") String id) throws FischlNewspaperException;

    @GET
    @Path("/newspapers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    List<FischlNewspaper> readAll() throws FischlNewspaperException;

    @PUT
    @Path("/newspapers/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlNewspaper update(@PathParam("id") String id, FischlNewspaper newspaper) throws FischlNewspaperException;

    @DELETE
    @Path("/newspapers/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void delete(@PathParam("id") String id) throws FischlNewspaperException;
}
