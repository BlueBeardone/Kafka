package za.co.bastion.maurabastion.adapter.defender;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.maurabastion.api.defender.MauraBastionDefenderApi;

@Path("/maura/v1/defenders")
public class MauraBastionDefenderRoute {

    @Inject
    private MauraBastionDefenderApi defenderApi;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse createDefender(@HeaderParam("Authorization") String authHeader,
                                      MauraBastionDefenderApi.Defender defender) {
        return defenderApi.createDefender(authHeader, defender);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getDefender(@PathParam("id") String id) {
        return defenderApi.getDefender(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getAllDefenders() {
        return defenderApi.getAllDefenders();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse updateDefender(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id,
                                      MauraBastionDefenderApi.Defender defender) {
        return defenderApi.updateDefender(authHeader, id, defender);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse deleteDefender(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) {
        return defenderApi.deleteDefender(authHeader, id);
    }
}
