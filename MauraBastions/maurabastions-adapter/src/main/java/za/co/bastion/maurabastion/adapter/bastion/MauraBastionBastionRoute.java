package za.co.bastion.maurabastion.adapter.bastion;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.maurabastion.api.bastion.MauraBastionBastionApi;

@Path("/maura/v1/bastions")
public class MauraBastionBastionRoute {

    @Inject
    private MauraBastionBastionApi bastionApi;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse createBastion(@HeaderParam("Authorization") String authHeader, MauraBastionBastionApi.Bastion bastion) {
        return bastionApi.createBastion(authHeader, bastion);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getBastion(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) {
        return bastionApi.getBastion(authHeader, id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getAllBastions(@HeaderParam("Authorization") String authHeader) {
        return bastionApi.getAllBastions(authHeader);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse updateBastion(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id, MauraBastionBastionApi.Bastion bastion) {
        return bastionApi.updateBastion(authHeader, id, bastion);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse deleteBastion(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) {
        return bastionApi.deleteBastion(authHeader, id);
    }
}
