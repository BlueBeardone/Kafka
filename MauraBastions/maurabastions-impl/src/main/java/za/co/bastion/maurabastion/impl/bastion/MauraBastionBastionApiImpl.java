package za.co.bastion.maurabastion.impl.bastion;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.dto.Rank;
import za.co.bastion.common.exception.BackendException;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.*;
import za.co.bastion.fischl.bastion.FischlBastionConnector;
import za.co.bastion.maurabastion.api.bastion.MauraBastionBastionApi;
import za.co.bastion.maurabastion.impl.discord.DiscordWebhookSender;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MauraBastionBastionApiImpl implements MauraBastionBastionApi {

    @Inject
    @RestClient
    FischlBastionConnector fischlBastionConnector;

    @Inject
    DiscordWebhookSender webhookSender;

    @Override
    public ApiResponse createBastion(String authHeader, Bastion bastion) {
        validateBastion(bastion);
        try {
            FischlBastion result = fischlBastionConnector.create(authHeader, toFischlBastion(bastion));
            MauraBastionBastionApi.Bastion created = BastionMapper.toBastion(result);
            try {
                String content = BastionCreationMessageBuilder.build(created);
                String threadId = webhookSender.createPost(
                        DiscordWebhookSender.Channel.BASTION_CREATION, result.getName(), content);
                fischlBastionConnector.update(authHeader, result.getId(), result.setDiscordThreadId(threadId));
                if (created.getFacilities() != null) {
                    for (MauraBastionBastionApi.Facility facility : created.getFacilities()) {
                        webhookSender.sendToThread(DiscordWebhookSender.Channel.BASTION_CREATION,
                                threadId, BastionConstructionMessageBuilder.build(created, facility));
                    }
                }
            } catch (BackendException e) {
                compensateDelete(authHeader, result.getId());
                throw e;
            } catch (FischlBastionException e) {
                compensateDelete(authHeader, result.getId());
                throw new BackendException(e.getStatus(), "Bastion backend error: " + e.getMessage());
            }
            return ApiResponse.success("Bastion created", created);
        } catch (FischlBastionException e) {
            throw new BackendException(e.getStatus(), "Bastion backend error: " + e.getMessage());
        }
    }

    private void compensateDelete(String authHeader, String id) {
        try {
            fischlBastionConnector.delete(authHeader, id);
        } catch (RuntimeException ignored) {
            // best effort compensation; rethrow the original failure regardless
        } catch (FischlBastionException ignored) {
            // best effort compensation; rethrow the original failure regardless
        }
    }

    @Override
    public ApiResponse getBastion(String authHeader, String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        try {
            FischlBastion result = fischlBastionConnector.read(authHeader, id);
            return ApiResponse.success("Bastion found", toBastion(result));
        } catch (FischlBastionException e) {
            throw new BackendException(e.getStatus(), "Bastion backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse getAllBastions(String authHeader, Boolean all) {
        try {
            List<FischlBastion> results = fischlBastionConnector.readAll(authHeader, all);
            List<Bastion> bastions = results.stream().map(this::toBastion).toList();
            return ApiResponse.success("Bastions found", bastions);
        } catch (FischlBastionException e) {
            throw new BackendException(e.getStatus(), "Bastion backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse browseBastions(String authHeader, Integer page, Integer limit) {
        if (page == null || page < 1) {
            throw new InvalidRequestException("page must be >= 1");
        }
        if (limit == null || limit < 1 || limit > 50) {
            throw new InvalidRequestException("limit must be between 1 and 50");
        }
        try {
            FischlBastionPage result = fischlBastionConnector.readPage(authHeader, page, limit);
            List<Bastion> bastions = result.getBastions() == null
                    ? List.<Bastion>of()
                    : result.getBastions().stream().map(this::toBastion).toList();
            BastionPage bastionPage = new BastionPage()
                    .setBastions(bastions)
                    .setPage(result.getPage())
                    .setLimit(result.getLimit())
                    .setTotal(result.getTotal())
                    .setHasMore(result.isHasMore());
            return ApiResponse.success("Bastions found", bastionPage);
        } catch (FischlBastionException e) {
            throw new BackendException(e.getStatus(), "Bastion backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse updateBastion(String authHeader, String id, Bastion bastion) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        validateBastion(bastion);
        try {
            FischlBastion result = fischlBastionConnector.update(authHeader, id, toFischlBastion(bastion));
            tryRefreshForumPost(result);
            return ApiResponse.success("Bastion updated", toBastion(result));
        } catch (FischlBastionException e) {
            throw new BackendException(e.getStatus(), "Bastion backend error: " + e.getMessage());
        }
    }

    private void tryRefreshForumPost(FischlBastion result) {
        String threadId = result == null ? null : result.getDiscordThreadId();
        if (threadId == null || threadId.isBlank()) {
            return;
        }
        try {
            String form = BastionCreationMessageBuilder.build(toBastion(result));
            webhookSender.editPost(DiscordWebhookSender.Channel.BASTION_CREATION, threadId, form);
        } catch (RuntimeException ignored) {
            // best effort: a missing/deleted thread must never fail the update
        }
    }

    @Override
    public ApiResponse deleteBastion(String authHeader, String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        try {
            fischlBastionConnector.delete(authHeader, id);
            return ApiResponse.success("Bastion deleted", null);
        } catch (FischlBastionException e) {
            throw new BackendException(e.getStatus(), "Bastion backend error: " + e.getMessage());
        }
    }

    private void validateBastion(Bastion bastion) {
        if (bastion.getName() == null || bastion.getName().isBlank()) {
            throw new InvalidRequestException("name is required");
        }
        if (bastion.getDescription() == null || bastion.getDescription().isBlank()) {
            throw new InvalidRequestException("description is required");
        }
    }

    private FischlBastion toFischlBastion(Bastion bastion) {
        return BastionMapper.toFischlBastion(bastion);
    }

    private Bastion toBastion(FischlBastion fischl) {
        return BastionMapper.toBastion(fischl);
    }
}
