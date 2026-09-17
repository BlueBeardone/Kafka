package za.co.bastion.maurabastion.impl.discord;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.exception.BackendException;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.FischlBastion;
import za.co.bastion.fischl.FischlBastionException;
import za.co.bastion.fischl.FischlIdentityException;
import za.co.bastion.fischl.FischlUser;
import za.co.bastion.fischl.bastion.FischlBastionConnector;
import za.co.bastion.fischl.identity.FischlIdentityConnector;
import za.co.bastion.maurabastion.api.discord.MauraBastionDiscordApi;
import za.co.bastion.maurabastion.impl.bastion.BastionCreationMessageBuilder;
import za.co.bastion.maurabastion.impl.bastion.BastionMapper;

@ApplicationScoped
public class MauraBastionDiscordApiImpl implements MauraBastionDiscordApi {

    private static final BastionTurnMessageFormatter FORMATTER = new BastionTurnMessageFormatter();

    @Inject
    DiscordWebhookSender webhookSender;

    @Inject
    @RestClient
    FischlIdentityConnector fischlIdentityConnector;

    @Inject
    @RestClient
    FischlBastionConnector fischlBastionConnector;

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
        String message = FORMATTER.format(result, user);
        webhookSender.send(DiscordWebhookSender.Channel.INDIVIDUAL_BASTION_TURN, message);
        tryThreadPost(authHeader, result.getBastionId(), message);
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
    public ApiResponse sendFacilityBuilt(String authHeader, DiscordMessageRequest request) {
        authenticatedUser(authHeader);
        String message = requireMessage(request);
        webhookSender.send(DiscordWebhookSender.Channel.FACILITY_BUILT, message);
        tryThreadPost(authHeader, request == null ? null : request.getBastionId(), message, true);
        return ApiResponse.success("Message sent", null);
    }

    @Override
    public ApiResponse sendFacilityRankUp(String authHeader, DiscordMessageRequest request) {
        authenticatedUser(authHeader);
        String message = requireMessage(request);
        webhookSender.send(DiscordWebhookSender.Channel.FACILITY_RANK_UP, message);
        tryThreadPost(authHeader, request == null ? null : request.getBastionId(), message, true);
        return ApiResponse.success("Message sent", null);
    }

    @Override
    public ApiResponse sendBranchUpgradePurchased(String authHeader, DiscordMessageRequest request) {
        authenticatedUser(authHeader);
        String message = requireMessage(request);
        webhookSender.send(DiscordWebhookSender.Channel.BRANCH_UPGRADE, message);
        tryThreadPost(authHeader, request == null ? null : request.getBastionId(), message);
        return ApiResponse.success("Message sent", null);
    }

    @Override
    public ApiResponse sendFacilityRemoved(String authHeader, DiscordMessageRequest request) {
        authenticatedUser(authHeader);
        String message = requireMessage(request);
        webhookSender.send(DiscordWebhookSender.Channel.FACILITY_REMOVED, message);
        tryThreadPost(authHeader, request == null ? null : request.getBastionId(), message, true);
        return ApiResponse.success("Message sent", null);
    }

    @Override
    public ApiResponse sendHirelingHired(String authHeader, DiscordMessageRequest request) {
        authenticatedUser(authHeader);
        String message = requireMessage(request);
        webhookSender.send(DiscordWebhookSender.Channel.HIRELING_HIRED, message);
        tryThreadPost(authHeader, request == null ? null : request.getBastionId(), message, true);
        return ApiResponse.success("Message sent", null);
    }

    @Override
    public ApiResponse sendDefenderAcquired(String authHeader, DiscordMessageRequest request) {
        authenticatedUser(authHeader);
        String message = requireMessage(request);
        webhookSender.send(DiscordWebhookSender.Channel.DEFENDER_ACQUIRED, message);
        tryThreadPost(authHeader, request == null ? null : request.getBastionId(), message, true);
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

    private String requireMessage(DiscordMessageRequest request) {
        String message = request == null ? null : request.getMessage();
        if (message == null || message.isBlank()) {
            throw new InvalidRequestException("message is required");
        }
        return message;
    }

    private void tryThreadPost(String authHeader, String bastionId, String message) {
        tryThreadPost(authHeader, bastionId, message, false);
    }

    private void tryThreadPost(String authHeader, String bastionId, String message, boolean updateForm) {
        if (bastionId == null || bastionId.isBlank()) {
            return;
        }
        try {
            FischlBastion bastion = fischlBastionConnector.read(authHeader, bastionId);
            String threadId = bastion == null ? null : bastion.getDiscordThreadId();
            if (threadId == null || threadId.isBlank()) {
                return;
            }
            webhookSender.sendToThread(DiscordWebhookSender.Channel.BASTION_CREATION, threadId, message);
            if (updateForm) {
                String form = BastionCreationMessageBuilder.build(BastionMapper.toBastion(bastion));
                webhookSender.editPost(DiscordWebhookSender.Channel.BASTION_CREATION, threadId, form);
            }
        } catch (RuntimeException ignored) {
            // best effort: a missing/deleted thread must never fail the event
        } catch (FischlBastionException ignored) {
            // best effort: a missing/deleted thread must never fail the event
        }
    }
}
