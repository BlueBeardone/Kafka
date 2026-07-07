package com.example.bastionmiddleware.maurabastion.impl.mapper;

import com.example.bastionmiddleware.connector.model.RepositoryBastionDto;
import com.example.bastionmiddleware.dto.BastionRequest;
import com.example.bastionmiddleware.dto.BastionResponse;
import org.springframework.stereotype.Component;

@Component
public class BastionMapper {

    public RepositoryBastionDto toRepositoryDto(BastionRequest request) {
        return new RepositoryBastionDto(
                null,
                request.getOwnerName().trim(),
                request.getRank(),
                request.getFacilityType(),
                request.getConstructionTurns(),
                request.getHirelingsCount());
    }

    public BastionResponse toResponse(RepositoryBastionDto dto) {
        return new BastionResponse(dto.getId(), dto.getOwnerName(), dto.getRank(), dto.getFacilityType(), dto.getConstructionTurns(), dto.getHirelingsCount());
    }
}
