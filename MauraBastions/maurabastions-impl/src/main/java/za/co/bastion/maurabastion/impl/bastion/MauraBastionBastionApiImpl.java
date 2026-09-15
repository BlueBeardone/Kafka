package za.co.bastion.maurabastion.impl.bastion;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.dto.Rank;
import za.co.bastion.common.exception.BackendException;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.*;
import za.co.bastion.fischl.FischlBastionPage;
import za.co.bastion.fischl.bastion.FischlBastionConnector;
import za.co.bastion.maurabastion.api.bastion.MauraBastionBastionApi;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MauraBastionBastionApiImpl implements MauraBastionBastionApi {

    @Inject
    @RestClient
    FischlBastionConnector fischlBastionConnector;

    @Override
    public ApiResponse createBastion(String authHeader, Bastion bastion) {
        validateBastion(bastion);
        try {
            FischlBastion result = fischlBastionConnector.create(authHeader, toFischlBastion(bastion));
            return ApiResponse.success("Bastion created", toBastion(result));
        } catch (FischlBastionException e) {
            throw new BackendException(e.getStatus(), "Bastion backend error: " + e.getMessage());
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
            return ApiResponse.success("Bastion updated", toBastion(result));
        } catch (FischlBastionException e) {
            throw new BackendException(e.getStatus(), "Bastion backend error: " + e.getMessage());
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
        List<FischlFacility> facilities = new ArrayList<>();
        if (bastion.getFacilities() != null) {
            facilities = bastion.getFacilities().stream().map(f ->
                    new FischlFacility()
                            .setId(f.getId())
                            .setName(f.getName())
                            .setRank(f.getRank() != null ? f.getRank().name() : null)
                            .setMinimumRequiredHirelings(f.getMinimumRequiredHirelings())
                            .setConstructionTurns(f.getConstructionTurns())
                            .setCost(f.getCost())
                            .setDescription(f.getDescription())
                            .setImgUrl(f.getImgUrl())
                            .setTable(f.getTable())
                            .setConstructedTurns(f.getConstructedTurns())
                            .setBastionId(f.getBastionId())
            ).toList();
        }
        List<FischlDefender> defenders = new ArrayList<>();
        if (bastion.getDefenders() != null) {
            defenders = bastion.getDefenders().stream().map(this::toFischlDefender).toList();
        }
        List<FischlHireling> hirelings = new ArrayList<>();
        if (bastion.getHirelings() != null) {
            hirelings = bastion.getHirelings().stream().map(this::toFischlHireling).toList();
        }
        return new FischlBastion()
                .setId(bastion.getId())
                .setUserId(bastion.getUserId())
                .setName(bastion.getName())
                .setDescription(bastion.getDescription())
                .setImgUrl(bastion.getImgUrl())
                .setFacilities(facilities)
                .setDefenders(defenders)
                .setHirelings(hirelings);
    }

    private Bastion toBastion(FischlBastion fischl) {
        List<Facility> facilities = new ArrayList<>();
        if (fischl.getFacilities() != null) {
            facilities = fischl.getFacilities().stream().map(f ->
                    new Facility()
                            .setId(f.getId())
                            .setName(f.getName())
                            .setRank(f.getRank() != null ? Rank.valueOf(f.getRank()) : null)
                            .setMinimumRequiredHirelings(f.getMinimumRequiredHirelings())
                            .setConstructionTurns(f.getConstructionTurns())
                            .setCost(f.getCost())
                            .setDescription(f.getDescription())
                            .setImgUrl(f.getImgUrl())
                            .setTable(f.getTable())
                            .setConstructedTurns(f.getConstructedTurns())
                            .setBastionId(f.getBastionId())
            ).toList();
        }
        List<Defender> defenders = new ArrayList<>();
        if (fischl.getDefenders() != null) {
            defenders = fischl.getDefenders().stream().map(this::toDefender).toList();
        }
        List<Hireling> hirelings = new ArrayList<>();
        if (fischl.getHirelings() != null) {
            hirelings = fischl.getHirelings().stream().map(this::toHireling).toList();
        }
        return new Bastion()
                .setId(fischl.getId())
                .setUserId(fischl.getUserId())
                .setName(fischl.getName())
                .setDescription(fischl.getDescription())
                .setImgUrl(fischl.getImgUrl())
                .setFacilities(facilities)
                .setDefenders(defenders)
                .setHirelings(hirelings);
    }

    private FischlDefender toFischlDefender(Defender d) {
        return new FischlDefender()
                .setId(d.getId())
                .setName(d.getName())
                .setDescription(d.getDescription())
                .setImgUrl(d.getImgUrl())
                .setType(d.getType())
                .setBastionId(d.getBastionId())
                .setAcquisitionStory(d.getAcquisitionStory());
    }

    private Defender toDefender(FischlDefender d) {
        return new Defender()
                .setId(d.getId())
                .setName(d.getName())
                .setDescription(d.getDescription())
                .setImgUrl(d.getImgUrl())
                .setType(d.getType())
                .setBastionId(d.getBastionId())
                .setAcquisitionStory(d.getAcquisitionStory());
    }

    private FischlHireling toFischlHireling(Hireling h) {
        return new FischlHireling()
                .setId(h.getId())
                .setName(h.getName())
                .setRole(h.getRole())
                .setDescription(h.getDescription())
                .setImgUrl(h.getImgUrl())
                .setBastionId(h.getBastionId())
                .setFacilityId(h.getFacilityId())
                .setAcquisitionStory(h.getAcquisitionStory());
    }

    private Hireling toHireling(FischlHireling h) {
        return new Hireling()
                .setId(h.getId())
                .setName(h.getName())
                .setRole(h.getRole())
                .setDescription(h.getDescription())
                .setImgUrl(h.getImgUrl())
                .setBastionId(h.getBastionId())
                .setFacilityId(h.getFacilityId())
                .setAcquisitionStory(h.getAcquisitionStory());
    }
}
