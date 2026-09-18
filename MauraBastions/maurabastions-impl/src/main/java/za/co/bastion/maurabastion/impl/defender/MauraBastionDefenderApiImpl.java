package za.co.bastion.maurabastion.impl.defender;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.exception.BackendException;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.FischlBastion;
import za.co.bastion.fischl.FischlBastionException;
import za.co.bastion.fischl.FischlDefender;
import za.co.bastion.fischl.FischlDefenderException;
import za.co.bastion.fischl.bastion.FischlBastionConnector;
import za.co.bastion.fischl.defender.FischlDefenderConnector;
import za.co.bastion.maurabastion.api.defender.MauraBastionDefenderApi;
import za.co.bastion.maurabastion.impl.bastion.BastionCreationMessageBuilder;
import za.co.bastion.maurabastion.impl.bastion.BastionMapper;
import za.co.bastion.maurabastion.impl.discord.DiscordWebhookSender;

import java.util.List;

@ApplicationScoped
public class MauraBastionDefenderApiImpl implements MauraBastionDefenderApi {

    @Inject
    @RestClient
    FischlDefenderConnector fischlDefenderConnector;

    @Inject
    @RestClient
    FischlBastionConnector fischlBastionConnector;

    @Inject
    DiscordWebhookSender webhookSender;

    @Override
    public ApiResponse createDefender(String authHeader, Defender defender) {
        validateDefender(defender);
        try {
            FischlDefender result = fischlDefenderConnector.create(toFischlDefender(defender));
            tryRefreshForumPost(authHeader, defender.getBastionId());
            return ApiResponse.success("Defender created", toDefender(result));
        } catch (FischlDefenderException e) {
            throw new BackendException(e.getStatus(), "Defender backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse getDefender(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        try {
            FischlDefender result = fischlDefenderConnector.read(id);
            return ApiResponse.success("Defender found", toDefender(result));
        } catch (FischlDefenderException e) {
            throw new BackendException(e.getStatus(), "Defender backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse getAllDefenders() {
        try {
            List<FischlDefender> results = fischlDefenderConnector.readAll();
            List<Defender> defenders = results.stream().map(this::toDefender).toList();
            return ApiResponse.success("Defenders found", defenders);
        } catch (FischlDefenderException e) {
            throw new BackendException(e.getStatus(), "Defender backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse updateDefender(String authHeader, String id, Defender defender) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        validateDefender(defender);
        try {
            FischlDefender result = fischlDefenderConnector.update(id, toFischlDefender(defender));
            tryRefreshForumPost(authHeader, defender.getBastionId());
            return ApiResponse.success("Defender updated", toDefender(result));
        } catch (FischlDefenderException e) {
            throw new BackendException(e.getStatus(), "Defender backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse deleteDefender(String authHeader, String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        String bastionId = null;
        try {
            FischlDefender existing = fischlDefenderConnector.read(id);
            bastionId = existing == null ? null : existing.getBastionId();
        } catch (FischlDefenderException ignored) {
            // best effort: without the defender we cannot locate its bastion
        }
        try {
            fischlDefenderConnector.delete(id);
            tryRefreshForumPost(authHeader, bastionId);
            return ApiResponse.success("Defender deleted", null);
        } catch (FischlDefenderException e) {
            throw new BackendException(e.getStatus(), "Defender backend error: " + e.getMessage());
        }
    }

    private void tryRefreshForumPost(String authHeader, String bastionId) {
        if (bastionId == null || bastionId.isBlank()) {
            return;
        }
        try {
            FischlBastion bastion = fischlBastionConnector.read(authHeader, bastionId);
            String threadId = bastion == null ? null : bastion.getDiscordThreadId();
            if (threadId == null || threadId.isBlank()) {
                return;
            }
            String form = BastionCreationMessageBuilder.build(BastionMapper.toBastion(bastion));
            webhookSender.editPost(DiscordWebhookSender.Channel.BASTION_CREATION, threadId, form);
        } catch (FischlBastionException ignored) {
            // best effort: a missing/deleted thread must never fail the defender change
        } catch (RuntimeException ignored) {
            // best effort: a missing/deleted thread must never fail the defender change
        }
    }

    private void validateDefender(Defender defender) {
        if (defender.getType() == null || defender.getType().isBlank()) {
            throw new InvalidRequestException("type is required");
        }
        if (defender.getBastionId() == null || defender.getBastionId().isBlank()) {
            throw new InvalidRequestException("bastionId is required");
        }
    }

    private FischlDefender toFischlDefender(Defender defender) {
        return new FischlDefender()
                .setId(defender.getId())
                .setName(defender.getName())
                .setDescription(defender.getDescription())
                .setImgUrl(defender.getImgUrl())
                .setType(defender.getType())
                .setBastionId(defender.getBastionId())
                .setAcquisitionStory(defender.getAcquisitionStory());
    }

    private Defender toDefender(FischlDefender fischl) {
        return new Defender()
                .setId(fischl.getId())
                .setName(fischl.getName())
                .setDescription(fischl.getDescription())
                .setImgUrl(fischl.getImgUrl())
                .setType(fischl.getType())
                .setBastionId(fischl.getBastionId())
                .setAcquisitionStory(fischl.getAcquisitionStory());
    }
}
