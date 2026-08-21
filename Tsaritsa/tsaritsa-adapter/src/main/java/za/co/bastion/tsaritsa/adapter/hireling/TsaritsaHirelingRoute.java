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
    public ApiResponse createHireling(TsaritsaHirelingApi.Hireling hireling) {
        return hirelingApi.createHireling(hireling);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getHireling(@PathParam("id") String id) {
        return hirelingApi.getHireling(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getAllHirelings() {
        return hirelingApi.getAllHirelings();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse updateHireling(@PathParam("id") String id, TsaritsaHirelingApi.Hireling hireling) {
        return hirelingApi.updateHireling(id, hireling);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse deleteHireling(@PathParam("id") String id) {
        return hirelingApi.deleteHireling(id);
    }
}