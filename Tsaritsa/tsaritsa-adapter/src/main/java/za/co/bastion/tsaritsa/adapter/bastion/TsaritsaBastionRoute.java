package za.co.bastion.tsaritsa.adapter.bastion;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.tsaritsa.api.bastion.TsaritsaBastionApi;

@Path("/tsaritsa/v1/bastions")
public class TsaritsaBastionRoute {

    @Inject
    private TsaritsaBastionApi bastionApi;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse createBastion(TsaritsaBastionApi.Bastion bastion) {
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
    public ApiResponse updateBastion(@PathParam("id") String id, TsaritsaBastionApi.Bastion bastion) {
        return bastionApi.updateBastion(id, bastion);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse deleteBastion(@PathParam("id") String id) {
        return bastionApi.deleteBastion(id);
    }
}