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
                request.ownerName().trim(),
                request.rank(),
                request.facilityType(),
                request.constructionTurns(),
                request.hirelingsCount());
    }

    public BastionResponse toResponse(RepositoryBastionDto dto) {
        return new BastionResponse(dto.id(), dto.ownerName(), dto.rank(), dto.facilityType(), dto.constructionTurns(), dto.hirelingsCount());
    }
}
