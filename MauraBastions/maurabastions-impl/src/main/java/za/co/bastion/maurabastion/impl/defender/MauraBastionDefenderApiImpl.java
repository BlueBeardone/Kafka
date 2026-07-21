package za.co.bastion.maurabastion.impl.defender;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.FischlDefender;
import za.co.bastion.fischl.FischlDefenderException;
import za.co.bastion.fischl.defender.FischlDefenderConnector;
import za.co.bastion.maurabastion.api.defender.MauraBastionDefenderApi;

import java.util.List;

@ApplicationScoped
public class MauraBastionDefenderApiImpl implements MauraBastionDefenderApi {

    @Inject
    @RestClient
    FischlDefenderConnector fischlDefenderConnector;

    @Override
    public ApiResponse createDefender(Defender defender) {
        validateDefender(defender);
        try {
            FischlDefender result = fischlDefenderConnector.create(toFischlDefender(defender));
            return ApiResponse.success("Defender created", toDefender(result));
        } catch (FischlDefenderException e) {
            throw new RuntimeException("Defender backend error: " + e.getMessage(), e);
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
            throw new RuntimeException("Defender backend error: " + e.getMessage(), e);
        }
    }

    @Override
    public ApiResponse getAllDefenders() {
        try {
            List<FischlDefender> results = fischlDefenderConnector.readAll();
            List<Defender> defenders = results.stream().map(this::toDefender).toList();
            return ApiResponse.success("Defenders found", defenders);
        } catch (FischlDefenderException e) {
            throw new RuntimeException("Defender backend error: " + e.getMessage(), e);
        }
    }

    @Override
    public ApiResponse updateDefender(String id, Defender defender) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        validateDefender(defender);
        try {
            FischlDefender result = fischlDefenderConnector.update(id, toFischlDefender(defender));
            return ApiResponse.success("Defender updated", toDefender(result));
        } catch (FischlDefenderException e) {
            throw new RuntimeException("Defender backend error: " + e.getMessage(), e);
        }
    }

    @Override
    public ApiResponse deleteDefender(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        try {
            fischlDefenderConnector.delete(id);
            return ApiResponse.success("Defender deleted", null);
        } catch (FischlDefenderException e) {
            throw new RuntimeException("Defender backend error: " + e.getMessage(), e);
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
