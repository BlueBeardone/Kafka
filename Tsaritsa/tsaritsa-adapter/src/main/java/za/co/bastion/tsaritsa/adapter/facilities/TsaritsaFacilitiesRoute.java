package za.co.bastion.tsaritsa.adapter.facilities;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.tsaritsa.api.facilities.TsaritsaFacilitiesApi;

@Path("/tsaritsa/v1/facilities")
public class TsaritsaFacilitiesRoute {

    @Inject
    private TsaritsaFacilitiesApi facilitiesApi;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse createFacility(@HeaderParam("Authorization") String authHeader,
                                      TsaritsaFacilitiesApi.Facility facility) {
        return facilitiesApi.createFacility(authHeader, facility);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getFacility(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) {
        return facilitiesApi.getFacility(authHeader, id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getAllFacilities(@HeaderParam("Authorization") String authHeader) {
        return facilitiesApi.getAllFacilities(authHeader);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse updateFacility(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id,
                                      TsaritsaFacilitiesApi.Facility facility) {
        return facilitiesApi.updateFacility(authHeader, id, facility);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse deleteFacility(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) {
        return facilitiesApi.deleteFacility(authHeader, id);
    }
}