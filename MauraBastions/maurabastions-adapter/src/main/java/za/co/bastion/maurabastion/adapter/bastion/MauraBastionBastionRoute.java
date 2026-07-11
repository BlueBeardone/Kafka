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
    public ApiResponse createBastion(MauraBastionBastionApi.Bastion bastion) {
        return bastionApi.createBastion(bastion);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getBastion(@PathParam("id") String id) {
        return bastionApi.getBastion(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getAllBastions() {
        return bastionApi.getAllBastions();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse updateBastion(@PathParam("id") String id, MauraBastionBastionApi.Bastion bastion) {
        return bastionApi.updateBastion(id, bastion);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse deleteBastion(@PathParam("id") String id) {
        return bastionApi.deleteBastion(id);
    }
}
