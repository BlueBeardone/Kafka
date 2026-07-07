package com.example.bastionmiddleware.maurabastion.impl;

import com.example.bastionmiddleware.connector.FischlConnector;
import com.example.bastionmiddleware.maurabastion.api.BastionApi;
import com.example.bastionmiddleware.dto.BastionRequest;
import com.example.bastionmiddleware.dto.BastionResponse;
import com.example.bastionmiddleware.exception.InvalidRequestException;
import com.example.bastionmiddleware.maurabastion.impl.mapper.BastionMapper;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class BastionApiImpl implements BastionApi {
    private static final Set<String> VALID_RANKS = Set.of("D", "C", "B", "A", "S");
    private static final Set<String> VALID_FACILITIES = Set.of("Barracks", "TrainingGround", "Workshop", "Keep");

    private final BastionMapper mapper;
    private final FischlConnector fischlConnector;

    public BastionApiImpl(BastionMapper mapper, FischlConnector fischlConnector) {
        this.mapper = mapper;
        this.fischlConnector = fischlConnector;
    }

    @Override
    public BastionResponse createBastion(BastionRequest request) {
        validate(request);
        return fischlConnector.createBastion(mapper.toRepositoryDto(request));
    }

    @Override
    public BastionResponse getBastion(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidRequestException("id must be a positive number");
        }
        return fischlConnector.getBastion(id);
    }

    private void validate(BastionRequest request) {
        if (request == null) {
            throw new InvalidRequestException("request body is required");
        }
        if (request.getOwnerName() == null || request.getOwnerName().isBlank()) {
            throw new InvalidRequestException("ownerName is required");
        }
        if (!VALID_RANKS.contains(request.getRank())) {
            throw new InvalidRequestException("rank must be one of D, C, B, A, S");
        }
        if (!VALID_FACILITIES.contains(request.getFacilityType())) {
            throw new InvalidRequestException("facilityType is invalid");
        }
        if (request.getConstructionTurns() == null || request.getConstructionTurns() <= 0) {
            throw new InvalidRequestException("constructionTurns must be greater than 0");
        }
        if (request.getHirelingsCount() == null || request.getHirelingsCount() < 0) {
            throw new InvalidRequestException("hirelingsCount must be 0 or greater");
        }
    }
}
