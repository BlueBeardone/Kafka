package za.co.bastion.maurabastion.impl.discord;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.exception.BackendException;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.FischlIdentityException;
import za.co.bastion.fischl.FischlUser;
import za.co.bastion.fischl.identity.FischlIdentityConnector;
import za.co.bastion.maurabastion.api.discord.MauraBastionDiscordApi;

@ApplicationScoped
public class MauraBastionDiscordApiImpl implements MauraBastionDiscordApi {

    private static final BastionTurnMessageFormatter FORMATTER = new BastionTurnMessageFormatter();

    @Inject
    DiscordWebhookSender webhookSender;

    @Inject
    @RestClient
    FischlIdentityConnector fischlIdentityConnector;

    @Override
    public ApiResponse sendMessage(String authHeader, String message) {
        authenticatedUser(authHeader);
        requireMessage(message);
        webhookSender.send(DiscordWebhookSender.Channel.GENERAL, message);
        return ApiResponse.success("Message sent", null);
    }

    @Override
    public ApiResponse sendIndividualBastionTurn(String authHeader, BastionTurnResult result) {
        FischlUser user = authenticatedUser(authHeader);
        if (result == null) {
            throw new InvalidRequestException("result is required");
        }
        webhookSender.send(DiscordWebhookSender.Channel.INDIVIDUAL_BASTION_TURN, FORMATTER.format(result, user));
        return ApiResponse.success("Message sent", null);
    }

    @Override
    public ApiResponse sendBastionCreated(String authHeader, String message) {
        authenticatedUser(authHeader);
        requireMessage(message);
        webhookSender.send(DiscordWebhookSender.Channel.BASTION_CREATION, message);
        return ApiResponse.success("Message sent", null);
    }

    @Override
    public ApiResponse sendFacilityBuilt(String authHeader, String message) {
        authenticatedUser(authHeader);
        requireMessage(message);
        webhookSender.send(DiscordWebhookSender.Channel.FACILITY_BUILT, message);
        return ApiResponse.success("Message sent", null);
    }

    @Override
    public ApiResponse sendFacilityRankUp(String authHeader, String message) {
        authenticatedUser(authHeader);
        requireMessage(message);
        webhookSender.send(DiscordWebhookSender.Channel.FACILITY_RANK_UP, message);
        return ApiResponse.success("Message sent", null);
    }

    @Override
    public ApiResponse sendBranchUpgradePurchased(String authHeader, String message) {
        authenticatedUser(authHeader);
        requireMessage(message);
        webhookSender.send(DiscordWebhookSender.Channel.BRANCH_UPGRADE, message);
        return ApiResponse.success("Message sent", null);
    }

    @Override
    public ApiResponse sendFacilityRemoved(String authHeader, String message) {
        authenticatedUser(authHeader);
        requireMessage(message);
        webhookSender.send(DiscordWebhookSender.Channel.FACILITY_REMOVED, message);
        return ApiResponse.success("Message sent", null);
    }

    @Override
    public ApiResponse sendHirelingHired(String authHeader, String message) {
        authenticatedUser(authHeader);
        requireMessage(message);
        webhookSender.send(DiscordWebhookSender.Channel.HIRELING_HIRED, message);
        return ApiResponse.success("Message sent", null);
    }

    @Override
    public ApiResponse sendDefenderAcquired(String authHeader, String message) {
        authenticatedUser(authHeader);
        requireMessage(message);
        webhookSender.send(DiscordWebhookSender.Channel.DEFENDER_ACQUIRED, message);
        return ApiResponse.success("Message sent", null);
    }

    private FischlUser authenticatedUser(String authHeader) {
        if (authHeader == null || authHeader.isBlank()) {
            throw new BackendException(401, "Authorization header is required");
        }
        try {
            return fischlIdentityConnector.getMe(authHeader);
        } catch (FischlIdentityException e) {
            throw new BackendException(e.getStatus(), "Discord auth error: " + e.getMessage());
        }
    }

    private void requireMessage(String message) {
        if (message == null || message.isBlank()) {
            throw new InvalidRequestException("message is required");
        }
    }
}
