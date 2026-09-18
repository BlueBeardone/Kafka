package za.co.bastion.maurabastion.impl.hireling;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.exception.BackendException;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.FischlBastion;
import za.co.bastion.fischl.FischlBastionException;
import za.co.bastion.fischl.FischlHireling;
import za.co.bastion.fischl.FischlHirelingException;
import za.co.bastion.fischl.bastion.FischlBastionConnector;
import za.co.bastion.fischl.hireling.FischlHirelingConnector;
import za.co.bastion.maurabastion.api.hireling.MauraBastionHirelingApi;
import za.co.bastion.maurabastion.impl.bastion.BastionCreationMessageBuilder;
import za.co.bastion.maurabastion.impl.bastion.BastionMapper;
import za.co.bastion.maurabastion.impl.discord.DiscordWebhookSender;

import java.util.List;

@ApplicationScoped
public class MauraBastionHirelingApiImpl implements MauraBastionHirelingApi {

    @Inject
    @RestClient
    FischlHirelingConnector fischlHirelingConnector;

    @Inject
    @RestClient
    FischlBastionConnector fischlBastionConnector;

    @Inject
    DiscordWebhookSender webhookSender;

    @Override
    public ApiResponse createHireling(String authHeader, Hireling hireling) {
        validateHireling(hireling);
        try {
            FischlHireling result = fischlHirelingConnector.create(toFischlHireling(hireling));
            tryRefreshForumPost(authHeader, hireling.getBastionId());
            return ApiResponse.success("Hireling created", toHireling(result));
        } catch (FischlHirelingException e) {
            throw new BackendException(e.getStatus(), "Hireling backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse getHireling(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        try {
            FischlHireling result = fischlHirelingConnector.read(id);
            return ApiResponse.success("Hireling found", toHireling(result));
        } catch (FischlHirelingException e) {
            throw new BackendException(e.getStatus(), "Hireling backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse getAllHirelings() {
        try {
            List<FischlHireling> results = fischlHirelingConnector.readAll();
            List<Hireling> hirelings = results.stream().map(this::toHireling).toList();
            return ApiResponse.success("Hirelings found", hirelings);
        } catch (FischlHirelingException e) {
            throw new BackendException(e.getStatus(), "Hireling backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse updateHireling(String authHeader, String id, Hireling hireling) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        validateHireling(hireling);
        try {
            FischlHireling result = fischlHirelingConnector.update(id, toFischlHireling(hireling));
            tryRefreshForumPost(authHeader, hireling.getBastionId());
            return ApiResponse.success("Hireling updated", toHireling(result));
        } catch (FischlHirelingException e) {
            throw new BackendException(e.getStatus(), "Hireling backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse deleteHireling(String authHeader, String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        String bastionId = null;
        try {
            FischlHireling existing = fischlHirelingConnector.read(id);
            bastionId = existing == null ? null : existing.getBastionId();
        } catch (FischlHirelingException ignored) {
            // best effort: without the hireling we cannot locate its bastion
        }
        try {
            fischlHirelingConnector.delete(id);
            tryRefreshForumPost(authHeader, bastionId);
            return ApiResponse.success("Hireling deleted", null);
        } catch (FischlHirelingException e) {
            throw new BackendException(e.getStatus(), "Hireling backend error: " + e.getMessage());
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
            // best effort: a missing/deleted thread must never fail the hireling change
        } catch (RuntimeException ignored) {
            // best effort: a missing/deleted thread must never fail the hireling change
        }
    }

    private void validateHireling(Hireling hireling) {
        if (hireling.getName() == null || hireling.getName().isBlank()) {
            throw new InvalidRequestException("name is required");
        }
        if (hireling.getBastionId() == null) {
            throw new InvalidRequestException("bastionId is required");
        }
    }

    private FischlHireling toFischlHireling(Hireling hireling) {
        return new FischlHireling()
                .setId(hireling.getId())
                .setName(hireling.getName())
                .setRole(hireling.getRole())
                .setDescription(hireling.getDescription())
                .setImgUrl(hireling.getImgUrl())
                .setBastionId(hireling.getBastionId())
                .setFacilityId(hireling.getFacilityId())
                .setAcquisitionStory(hireling.getAcquisitionStory());
    }

    private Hireling toHireling(FischlHireling fischl) {
        return new Hireling()
                .setId(fischl.getId())
                .setName(fischl.getName())
                .setRole(fischl.getRole())
                .setDescription(fischl.getDescription())
                .setImgUrl(fischl.getImgUrl())
                .setBastionId(fischl.getBastionId())
                .setFacilityId(fischl.getFacilityId())
                .setAcquisitionStory(fischl.getAcquisitionStory());
    }
}