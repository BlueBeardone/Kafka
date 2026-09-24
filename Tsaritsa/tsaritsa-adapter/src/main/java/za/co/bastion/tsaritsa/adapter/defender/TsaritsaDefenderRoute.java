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
    public ApiResponse createDefender(@HeaderParam("Authorization") String authHeader,
                                      TsaritsaDefenderApi.Defender defender) {
        return defenderApi.createDefender(authHeader, defender);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getDefender(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) {
        return defenderApi.getDefender(authHeader, id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getAllDefenders(@HeaderParam("Authorization") String authHeader) {
        return defenderApi.getAllDefenders(authHeader);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse updateDefender(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id,
                                      TsaritsaDefenderApi.Defender defender) {
        return defenderApi.updateDefender(authHeader, id, defender);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse deleteDefender(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) {
        return defenderApi.deleteDefender(authHeader, id);
    }
}