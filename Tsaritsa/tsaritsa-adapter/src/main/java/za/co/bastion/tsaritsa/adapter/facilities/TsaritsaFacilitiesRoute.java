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
    public ApiResponse createFacility(TsaritsaFacilitiesApi.Facility facility) {
        return facilitiesApi.createFacility(facility);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getFacility(@PathParam("id") String id) {
        return facilitiesApi.getFacility(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getAllFacilities() {
        return facilitiesApi.getAllFacilities();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse updateFacility(@PathParam("id") String id, TsaritsaFacilitiesApi.Facility facility) {
        return facilitiesApi.updateFacility(id, facility);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse deleteFacility(@PathParam("id") String id) {
        return facilitiesApi.deleteFacility(id);
    }
}