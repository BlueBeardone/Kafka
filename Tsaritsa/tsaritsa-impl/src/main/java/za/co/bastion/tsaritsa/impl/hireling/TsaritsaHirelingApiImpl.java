package za.co.bastion.tsaritsa.impl.hireling;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.exception.BackendException;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.FischlHireling;
import za.co.bastion.fischl.FischlHirelingException;
import za.co.bastion.fischl.hireling.FischlHirelingConnector;
import za.co.bastion.tsaritsa.api.hireling.TsaritsaHirelingApi;

import java.util.List;

@ApplicationScoped
public class TsaritsaHirelingApiImpl implements TsaritsaHirelingApi {

    @Inject
    @RestClient
    FischlHirelingConnector fischlHirelingConnector;

    @Override
    public ApiResponse createHireling(String authHeader, Hireling hireling) {
        validateHireling(hireling);
        try {
            FischlHireling result = fischlHirelingConnector.create(authHeader, toFischlHireling(hireling));
            return ApiResponse.success("Hireling created", toHireling(result));
        } catch (FischlHirelingException e) {
            throw new BackendException(e.getStatus(), "Hireling backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse getHireling(String authHeader, String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        try {
            FischlHireling result = fischlHirelingConnector.read(authHeader, id);
            return ApiResponse.success("Hireling found", toHireling(result));
        } catch (FischlHirelingException e) {
            throw new BackendException(e.getStatus(), "Hireling backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse getAllHirelings(String authHeader) {
        try {
            List<FischlHireling> results = fischlHirelingConnector.readAll(authHeader);
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
            FischlHireling result = fischlHirelingConnector.update(authHeader, id, toFischlHireling(hireling));
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
        try {
            fischlHirelingConnector.delete(authHeader, id);
            return ApiResponse.success("Hireling deleted", null);
        } catch (FischlHirelingException e) {
            throw new BackendException(e.getStatus(), "Hireling backend error: " + e.getMessage());
        }
    }

    private void validateHireling(Hireling hireling) {
        if (hireling.getName() == null || hireling.getName().isBlank()) {
            throw new InvalidRequestException("name is required");
        }
        if (hireling.getBastionId() == null || hireling.getBastionId().isBlank()) {
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