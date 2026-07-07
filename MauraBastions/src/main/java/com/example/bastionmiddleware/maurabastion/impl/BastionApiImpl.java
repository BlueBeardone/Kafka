package com.example.bastionmiddleware.maurabastion.impl;

import com.example.bastionmiddleware.connector.FischlConnector;
import com.example.bastionmiddleware.maurabastion.api.BastionApi;
import com.example.bastionmiddleware.dto.BastionDto;
import com.example.bastionmiddleware.dto.BastionRequest;
import com.example.bastionmiddleware.dto.BastionResponse;
import com.example.bastionmiddleware.dto.FacilityDto;
import com.example.bastionmiddleware.dto.NewspaperDataDto;
import com.example.bastionmiddleware.dto.NewspaperArticleDto;
import com.example.bastionmiddleware.dto.UserDto;
import com.example.bastionmiddleware.exception.InvalidRequestException;
import com.example.bastionmiddleware.maurabastion.impl.mapper.BastionMapper;
import org.springframework.stereotype.Service;

import java.util.List;
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
        validateLegacyBastion(request);
        return fischlConnector.createBastion(mapper.toRepositoryDto(request));
    }

    @Override
    public BastionResponse getBastion(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidRequestException("id must be a positive number");
        }
        return fischlConnector.getBastion(id);
    }

    @Override
    public UserDto createUser(UserDto request) {
        validateUser(request);
        return fischlConnector.createUser(request);
    }

    @Override
    public UserDto getUser(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("user id is required");
        }
        return fischlConnector.getUser(id);
    }

    @Override
    public List<BastionDto> getAllBastions() {
        return fischlConnector.getAllBastions();
    }

    @Override
    public BastionDto getBastionById(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("bastion id is required");
        }
        return fischlConnector.getBastionById(id);
    }

    @Override
    public BastionDto createBastion(BastionDto request) {
        validateBastion(request);
        return fischlConnector.createBastion(request);
    }

    @Override
    public BastionDto updateBastion(String id, BastionDto request) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("bastion id is required");
        }
        validateBastion(request);
        request.setId(id);
        return fischlConnector.updateBastion(id, request);
    }

    @Override
    public void deleteBastion(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("bastion id is required");
        }
        fischlConnector.deleteBastion(id);
    }

    @Override
    public List<FacilityDto> getAllFacilities() {
        return fischlConnector.getAllFacilities();
    }

    @Override
    public FacilityDto getFacilityById(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("facility id is required");
        }
        return fischlConnector.getFacilityById(id);
    }

    @Override
    public NewspaperDataDto createNewspaper(NewspaperDataDto request) {
        validateNewspaper(request);
        return fischlConnector.createNewspaper(request);
    }

    @Override
    public List<NewspaperDataDto> getAllNewspapers() {
        return fischlConnector.getAllNewspapers();
    }

    @Override
    public NewspaperDataDto getNewspaperByName(String newspaperName) {
        if (newspaperName == null || newspaperName.isBlank()) {
            throw new InvalidRequestException("newspaperName is required");
        }
        return fischlConnector.getNewspaperByName(newspaperName);
    }

    private void validateLegacyBastion(BastionRequest request) {
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

    private void validateUser(UserDto request) {
        if (request == null) {
            throw new InvalidRequestException("request body is required");
        }
        if (request.getId() == null || request.getId().isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        if (request.getDisplayName() == null || request.getDisplayName().isBlank()) {
            throw new InvalidRequestException("displayName is required");
        }
    }

    private void validateBastion(BastionDto request) {
        if (request == null) {
            throw new InvalidRequestException("request body is required");
        }
        if (request.getId() == null || request.getId().isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        if (request.getName() == null || request.getName().isBlank()) {
            throw new InvalidRequestException("name is required");
        }
        if (request.getDescription() == null || request.getDescription().isBlank()) {
            throw new InvalidRequestException("description is required");
        }
        if (request.getFacilities() == null || request.getFacilities().isEmpty()) {
            throw new InvalidRequestException("facilities are required");
        }
        request.getFacilities().forEach(this::validateFacility);
    }

    private void validateFacility(FacilityDto facility) {
        if (facility == null) {
            throw new InvalidRequestException("facility is required");
        }
        if (facility.getId() == null || facility.getId().isBlank()) {
            throw new InvalidRequestException("facility id is required");
        }
        if (facility.getName() == null || facility.getName().isBlank()) {
            throw new InvalidRequestException("facility name is required");
        }
        if (facility.getRank() == null) {
            throw new InvalidRequestException("facility rank is required");
        }
        if (facility.getHirelingAmount() == null) {
            throw new InvalidRequestException("hirelingAmount is required");
        }
        if (facility.getDescription() == null || facility.getDescription().isBlank()) {
            throw new InvalidRequestException("facility description is required");
        }
    }

    private void validateNewspaper(NewspaperDataDto request) {
        if (request == null) {
            throw new InvalidRequestException("request body is required");
        }
        if (request.getNewspaperName() == null || request.getNewspaperName().isBlank()) {
            throw new InvalidRequestException("newspaperName is required");
        }
        if (request.getDate() == null || request.getDate().isBlank()) {
            throw new InvalidRequestException("date is required");
        }
        if (request.getEdition() == null || request.getEdition().isBlank()) {
            throw new InvalidRequestException("edition is required");
        }
        if (request.getLeadArticle() == null) {
            throw new InvalidRequestException("leadArticle is required");
        }
        if (request.getOtherArticles() == null || request.getOtherArticles().isEmpty()) {
            throw new InvalidRequestException("otherArticles are required");
        }
        validateArticle(request.getLeadArticle());
        request.getOtherArticles().forEach(this::validateArticle);
    }

    private void validateArticle(NewspaperArticleDto article) {
        if (article == null) {
            throw new InvalidRequestException("article is required");
        }
        if (article.getTitle() == null || article.getTitle().isBlank()) {
            throw new InvalidRequestException("article title is required");
        }
        if (article.getContent() == null || article.getContent().isBlank()) {
            throw new InvalidRequestException("article content is required");
        }
    }
}
