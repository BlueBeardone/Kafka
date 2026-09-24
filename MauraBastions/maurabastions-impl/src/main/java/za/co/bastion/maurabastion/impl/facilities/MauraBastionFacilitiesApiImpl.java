package za.co.bastion.maurabastion.impl.facilities;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.dto.Rank;
import za.co.bastion.common.exception.BackendException;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.*;
import za.co.bastion.fischl.bastion.FischlBastionConnector;
import za.co.bastion.fischl.facilities.FischlFacilitiesConnector;
import za.co.bastion.maurabastion.api.facilities.MauraBastionFacilitiesApi;
import za.co.bastion.maurabastion.impl.bastion.BastionCreationMessageBuilder;
import za.co.bastion.maurabastion.impl.bastion.BastionMapper;
import za.co.bastion.maurabastion.impl.discord.DiscordWebhookSender;

import java.util.List;

@ApplicationScoped
public class MauraBastionFacilitiesApiImpl implements MauraBastionFacilitiesApi {

    @Inject
    @RestClient
    FischlFacilitiesConnector fischlFacilitiesConnector;

    @Inject
    @RestClient
    FischlBastionConnector fischlBastionConnector;

    @Inject
    DiscordWebhookSender webhookSender;

    @Override
    public ApiResponse createFacility(String authHeader, Facility facility) {
        validateFacility(facility);
        try {
            FischlFacility result = fischlFacilitiesConnector.create(authHeader, toFischlFacility(facility));
            tryRefreshForumPost(authHeader, facility.getBastionId());
            return ApiResponse.success("Facility created", toFacility(result));
        } catch (FischlFacilityException e) {
            throw new BackendException(e.getStatus(), "Facility backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse getFacility(String authHeader, String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        try {
            FischlFacility result = fischlFacilitiesConnector.read(authHeader, id);
            return ApiResponse.success("Facility found", toFacility(result));
        } catch (FischlFacilityException e) {
            throw new BackendException(e.getStatus(), "Facility backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse getAllFacilities(String authHeader) {
        try {
            List<FischlFacility> results = fischlFacilitiesConnector.readAll(authHeader);
            List<Facility> facilities = results.stream().map(this::toFacility).toList();
            return ApiResponse.success("Facilities found", facilities);
        } catch (FischlFacilityException e) {
            throw new BackendException(e.getStatus(), "Facility backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse updateFacility(String authHeader, String id, Facility facility) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        validateFacility(facility);
        try {
            FischlFacility result = fischlFacilitiesConnector.update(authHeader, id, toFischlFacility(facility));
            tryRefreshForumPost(authHeader, facility.getBastionId());
            return ApiResponse.success("Facility updated", toFacility(result));
        } catch (FischlFacilityException e) {
            throw new BackendException(e.getStatus(), "Facility backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse deleteFacility(String authHeader, String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        String bastionId = null;
        try {
            FischlFacility existing = fischlFacilitiesConnector.read(authHeader, id);
            bastionId = existing == null ? null : existing.getBastionId();
        } catch (FischlFacilityException ignored) {
            // best effort: without the facility we cannot locate its bastion
        }
        try {
            fischlFacilitiesConnector.delete(authHeader, id);
            tryRefreshForumPost(authHeader, bastionId);
            return ApiResponse.success("Facility deleted", null);
        } catch (FischlFacilityException e) {
            throw new BackendException(e.getStatus(), "Facility backend error: " + e.getMessage());
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
            // best effort: a missing/deleted thread must never fail the facility change
        } catch (RuntimeException ignored) {
            // best effort: a missing/deleted thread must never fail the facility change
        }
    }

    private void validateFacility(Facility facility) {
        if (facility.getName() == null || facility.getName().isBlank()) {
            throw new InvalidRequestException("name is required");
        }
        if (facility.getDescription() == null || facility.getDescription().isBlank()) {
            throw new InvalidRequestException("description is required");
        }
        if (facility.getRank() == null) {
            throw new InvalidRequestException("rank is required");
        }
        if (facility.getMinimumRequiredHirelings() != null && facility.getMinimumRequiredHirelings() < 0) {
            throw new InvalidRequestException("minimumRequiredHirelings must be >= 0");
        }
    }

    private FischlFacility toFischlFacility(Facility facility) {
        return new FischlFacility()
                .setId(facility.getId())
                .setName(facility.getName())
                .setRank(facility.getRank() != null ? facility.getRank().name() : null)
                .setMinimumRequiredHirelings(facility.getMinimumRequiredHirelings())
                .setConstructionTurns(facility.getConstructionTurns())
                .setCost(facility.getCost())
                .setDescription(facility.getDescription())
                .setImgUrl(facility.getImgUrl())
                .setTable(facility.getTable())
                .setConstructedTurns(facility.getConstructedTurns())
                .setBastionId(facility.getBastionId());
    }

    private Facility toFacility(FischlFacility fischl) {
        return new Facility()
                .setId(fischl.getId())
                .setName(fischl.getName())
                .setRank(fischl.getRank() != null ? Rank.valueOf(fischl.getRank()) : null)
                .setMinimumRequiredHirelings(fischl.getMinimumRequiredHirelings())
                .setConstructionTurns(fischl.getConstructionTurns())
                .setCost(fischl.getCost())
                .setDescription(fischl.getDescription())
                .setImgUrl(fischl.getImgUrl())
                .setTable(fischl.getTable())
                .setConstructedTurns(fischl.getConstructedTurns())
                .setBastionId(fischl.getBastionId());
    }
}