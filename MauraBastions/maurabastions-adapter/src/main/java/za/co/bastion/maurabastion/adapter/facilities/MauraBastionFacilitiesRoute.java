package za.co.bastion.maurabastion.adapter.facilities;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.maurabastion.api.facilities.MauraBastionFacilitiesApi;

@Path("/maura/v1/facilities")
public class MauraBastionFacilitiesRoute {

    @Inject
    private MauraBastionFacilitiesApi facilitiesApi;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse createFacility(@HeaderParam("Authorization") String authHeader,
                                      MauraBastionFacilitiesApi.Facility facility) {
        return facilitiesApi.createFacility(authHeader, facility);
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
    public ApiResponse updateFacility(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id,
                                      MauraBastionFacilitiesApi.Facility facility) {
        return facilitiesApi.updateFacility(authHeader, id, facility);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse deleteFacility(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) {
        return facilitiesApi.deleteFacility(authHeader, id);
    }
}
