package za.co.bastion.tsaritsa.adapter.hireling;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.tsaritsa.api.hireling.TsaritsaHirelingApi;

@Path("/tsaritsa/v1/hirelings")
public class TsaritsaHirelingRoute {

    @Inject
    private TsaritsaHirelingApi hirelingApi;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse createHireling(@HeaderParam("Authorization") String authHeader,
                                      TsaritsaHirelingApi.Hireling hireling) {
        return hirelingApi.createHireling(authHeader, hireling);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getHireling(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) {
        return hirelingApi.getHireling(authHeader, id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getAllHirelings(@HeaderParam("Authorization") String authHeader) {
        return hirelingApi.getAllHirelings(authHeader);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse updateHireling(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id,
                                      TsaritsaHirelingApi.Hireling hireling) {
        return hirelingApi.updateHireling(authHeader, id, hireling);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse deleteHireling(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) {
        return hirelingApi.deleteHireling(authHeader, id);
    }
}