package za.co.bastion.fischl.facilities;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import za.co.bastion.fischl.CustomHttpClientOptions;
import za.co.bastion.fischl.FischlFacility;
import za.co.bastion.fischl.FischlFacilityException;
import za.co.bastion.fischl.FischlEnvelopeBodyReader;
import za.co.bastion.fischl.FischlRenewedTokenFilter;

import java.util.List;

@RegisterRestClient(configKey = "fischl-facilities-api")
@RegisterProvider(FischlFacilitiesExceptionMapper.class)
@RegisterProvider(CustomHttpClientOptions.class)
@RegisterProvider(FischlEnvelopeBodyReader.class)
@RegisterProvider(FischlRenewedTokenFilter.class)
public interface FischlFacilitiesConnector {

    @POST
    @Path("/facilities")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlFacility create(@HeaderParam("Authorization") String authHeader, FischlFacility facility) throws FischlFacilityException;

    @GET
    @Path("/facilities/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlFacility read(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) throws FischlFacilityException;

    @GET
    @Path("/facilities")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    List<FischlFacility> readAll(@HeaderParam("Authorization") String authHeader) throws FischlFacilityException;

    @PUT
    @Path("/facilities/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlFacility update(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id, FischlFacility facility) throws FischlFacilityException;

    @DELETE
    @Path("/facilities/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void delete(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) throws FischlFacilityException;
}
