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
    public ApiResponse createDefender(MauraBastionDefenderApi.Defender defender) {
        return defenderApi.createDefender(defender);
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
    public ApiResponse updateDefender(@PathParam("id") String id, MauraBastionDefenderApi.Defender defender) {
        return defenderApi.updateDefender(id, defender);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse deleteDefender(@PathParam("id") String id) {
        return defenderApi.deleteDefender(id);
    }
}
