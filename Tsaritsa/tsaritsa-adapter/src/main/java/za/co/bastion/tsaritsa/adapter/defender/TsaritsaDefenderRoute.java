package za.co.bastion.tsaritsa.adapter.defender;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.tsaritsa.api.defender.TsaritsaDefenderApi;

@Path("/tsaritsa/v1/defenders")
public class TsaritsaDefenderRoute {

    @Inject
    private TsaritsaDefenderApi defenderApi;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse createDefender(TsaritsaDefenderApi.Defender defender) {
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
    public ApiResponse updateDefender(@PathParam("id") String id, TsaritsaDefenderApi.Defender defender) {
        return defenderApi.updateDefender(id, defender);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse deleteDefender(@PathParam("id") String id) {
        return defenderApi.deleteDefender(id);
    }
}