package za.co.bastion.fischl.facilities;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import za.co.bastion.fischl.CustomHttpClientOptions;
import za.co.bastion.fischl.FischlFacility;
import za.co.bastion.fischl.FischlFacilityException;

import java.util.List;

@RegisterRestClient(configKey = "fischl-facilities-api")
@RegisterProvider(FischlFacilitiesExceptionMapper.class)
@RegisterProvider(CustomHttpClientOptions.class)
public interface FischlFacilitiesConnector {

    @POST
    @Path("/facilities")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlFacility create(FischlFacility facility) throws FischlFacilityException;

    @GET
    @Path("/facilities/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlFacility read(@PathParam("id") String id) throws FischlFacilityException;

    @GET
    @Path("/facilities")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    List<FischlFacility> readAll() throws FischlFacilityException;

    @PUT
    @Path("/facilities/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlFacility update(@PathParam("id") String id, FischlFacility facility) throws FischlFacilityException;

    @DELETE
    @Path("/facilities/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void delete(@PathParam("id") String id) throws FischlFacilityException;
}
