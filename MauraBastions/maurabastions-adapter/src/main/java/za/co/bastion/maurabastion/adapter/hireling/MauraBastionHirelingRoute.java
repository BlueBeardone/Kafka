package za.co.bastion.maurabastion.adapter.hireling;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.maurabastion.api.hireling.MauraBastionHirelingApi;

@Path("/maura/v1/hirelings")
public class MauraBastionHirelingRoute {

    @Inject
    private MauraBastionHirelingApi hirelingApi;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse createHireling(@HeaderParam("Authorization") String authHeader,
                                      MauraBastionHirelingApi.Hireling hireling) {
        return hirelingApi.createHireling(authHeader, hireling);
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
    public ApiResponse updateHireling(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id,
                                      MauraBastionHirelingApi.Hireling hireling) {
        return hirelingApi.updateHireling(authHeader, id, hireling);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse deleteHireling(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) {
        return hirelingApi.deleteHireling(authHeader, id);
    }
}