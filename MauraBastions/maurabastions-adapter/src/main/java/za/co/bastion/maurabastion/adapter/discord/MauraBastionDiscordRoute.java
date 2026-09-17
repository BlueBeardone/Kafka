package za.co.bastion.maurabastion.adapter.discord;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.maurabastion.api.discord.MauraBastionDiscordApi;

@Path("/maura/v1/discord")
public class MauraBastionDiscordRoute {

    @Inject
    private MauraBastionDiscordApi discordApi;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse sendMessage(@HeaderParam("Authorization") String authHeader,
                                   MauraBastionDiscordApi.DiscordMessageRequest request) {
        return discordApi.sendMessage(authHeader, request.getMessage());
    }

    @POST
    @Path("/individual-bastion-turn")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse sendIndividualBastionTurn(@HeaderParam("Authorization") String authHeader,
                                                 MauraBastionDiscordApi.BastionTurnResult result) {
        return discordApi.sendIndividualBastionTurn(authHeader, result);
    }

    @POST
    @Path("/bastion-creation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse sendBastionCreated(@HeaderParam("Authorization") String authHeader,
                                          MauraBastionDiscordApi.DiscordMessageRequest request) {
        return discordApi.sendBastionCreated(authHeader, request.getMessage());
    }

    @POST
    @Path("/facility-built")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse sendFacilityBuilt(@HeaderParam("Authorization") String authHeader,
                                         MauraBastionDiscordApi.DiscordMessageRequest request) {
        return discordApi.sendFacilityBuilt(authHeader, request);
    }

    @POST
    @Path("/facility-rank-up")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse sendFacilityRankUp(@HeaderParam("Authorization") String authHeader,
                                          MauraBastionDiscordApi.DiscordMessageRequest request) {
        return discordApi.sendFacilityRankUp(authHeader, request);
    }

    @POST
    @Path("/branch-upgrade")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse sendBranchUpgradePurchased(@HeaderParam("Authorization") String authHeader,
                                                  MauraBastionDiscordApi.DiscordMessageRequest request) {
        return discordApi.sendBranchUpgradePurchased(authHeader, request);
    }

    @POST
    @Path("/facility-removed")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse sendFacilityRemoved(@HeaderParam("Authorization") String authHeader,
                                           MauraBastionDiscordApi.DiscordMessageRequest request) {
        return discordApi.sendFacilityRemoved(authHeader, request);
    }

    @POST
    @Path("/hireling-hired")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse sendHirelingHired(@HeaderParam("Authorization") String authHeader,
                                         MauraBastionDiscordApi.DiscordMessageRequest request) {
        return discordApi.sendHirelingHired(authHeader, request);
    }

    @POST
    @Path("/defender-acquired")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse sendDefenderAcquired(@HeaderParam("Authorization") String authHeader,
                                            MauraBastionDiscordApi.DiscordMessageRequest request) {
        return discordApi.sendDefenderAcquired(authHeader, request);
    }
}