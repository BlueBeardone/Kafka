package za.co.bastion.tsaritsa.impl.facilities;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.dto.Rank;
import za.co.bastion.common.exception.BackendException;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.*;
import za.co.bastion.fischl.facilities.FischlFacilitiesConnector;
import za.co.bastion.tsaritsa.api.facilities.TsaritsaFacilitiesApi;

import java.util.List;

@ApplicationScoped
public class TsaritsaFacilitiesApiImpl implements TsaritsaFacilitiesApi {

    @Inject
    @RestClient
    FischlFacilitiesConnector fischlFacilitiesConnector;

    @Override
    public ApiResponse createFacility(String authHeader, Facility facility) {
        validateFacility(facility);
        try {
            FischlFacility result = fischlFacilitiesConnector.create(authHeader, toFischlFacility(facility));
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
        try {
            fischlFacilitiesConnector.delete(authHeader, id);
            return ApiResponse.success("Facility deleted", null);
        } catch (FischlFacilityException e) {
            throw new BackendException(e.getStatus(), "Facility backend error: " + e.getMessage());
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