package za.co.bastion.maurabastion.impl.bastion;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.dto.Rank;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.*;
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
    public ApiResponse createBastion(Bastion bastion) {
        validateBastion(bastion);
        try {
            FischlBastion result = fischlBastionConnector.create(toFischlBastion(bastion));
            return ApiResponse.success("Bastion created", toBastion(result));
        } catch (FischlBastionException e) {
            throw new RuntimeException("Bastion backend error: " + e.getMessage(), e);
        }
    }

    @Override
    public ApiResponse getBastion(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        try {
            FischlBastion result = fischlBastionConnector.read(id);
            return ApiResponse.success("Bastion found", toBastion(result));
        } catch (FischlBastionException e) {
            throw new RuntimeException("Bastion backend error: " + e.getMessage(), e);
        }
    }

    @Override
    public ApiResponse getAllBastions() {
        try {
            List<FischlBastion> results = fischlBastionConnector.readAll();
            List<Bastion> bastions = results.stream().map(this::toBastion).toList();
            return ApiResponse.success("Bastions found", bastions);
        } catch (FischlBastionException e) {
            throw new RuntimeException("Bastion backend error: " + e.getMessage(), e);
        }
    }

    @Override
    public ApiResponse updateBastion(String id, Bastion bastion) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        validateBastion(bastion);
        try {
            FischlBastion result = fischlBastionConnector.update(id, toFischlBastion(bastion));
            return ApiResponse.success("Bastion updated", toBastion(result));
        } catch (FischlBastionException e) {
            throw new RuntimeException("Bastion backend error: " + e.getMessage(), e);
        }
    }

    @Override
    public ApiResponse deleteBastion(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        try {
            fischlBastionConnector.delete(id);
            return ApiResponse.success("Bastion deleted", null);
        } catch (FischlBastionException e) {
            throw new RuntimeException("Bastion backend error: " + e.getMessage(), e);
        }
    }

    private void validateBastion(Bastion bastion) {
        if (bastion.getId() == null || bastion.getId().isBlank()) {
            throw new InvalidRequestException("id is required");
        }
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
        return new FischlBastion()
                .setId(bastion.getId())
                .setName(bastion.getName())
                .setDescription(bastion.getDescription())
                .setImgUrl(bastion.getImgUrl())
                .setFacilities(facilities);
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
        return new Bastion()
                .setId(fischl.getId())
                .setName(fischl.getName())
                .setDescription(fischl.getDescription())
                .setImgUrl(fischl.getImgUrl())
                .setFacilities(facilities);
    }
}
