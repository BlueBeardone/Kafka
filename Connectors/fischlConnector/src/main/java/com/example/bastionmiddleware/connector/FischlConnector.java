package com.example.bastionmiddleware.connector;

import com.example.bastionmiddleware.connector.model.RepositoryBastionDto;
import com.example.bastionmiddleware.dto.BastionDto;
import com.example.bastionmiddleware.dto.BastionResponse;
import com.example.bastionmiddleware.dto.FacilityDto;
import com.example.bastionmiddleware.dto.NewspaperDataDto;
import com.example.bastionmiddleware.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class FischlConnector {
    private final WebClient webClient;

    public FischlConnector(@Value("${app.data-access.base-url}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public BastionResponse createBastion(RepositoryBastionDto dto) {
        return webClient.post()
                .uri("/api/v1/repository/bastions")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(BastionResponse.class)
                .onErrorResume(ex -> Mono.just(new BastionResponse(1L, dto.getOwnerName(), dto.getRank(), dto.getFacilityType(), dto.getConstructionTurns(), dto.getHirelingsCount())))
                .block();
    }

    public BastionResponse getBastion(Long id) {
        return webClient.get()
                .uri("/api/v1/repository/bastions/{id}", id)
                .retrieve()
                .bodyToMono(BastionResponse.class)
                .onErrorResume(ex -> Mono.just(new BastionResponse(id, "Unavailable", "D", "Barracks", 1, 0)))
                .block();
    }

    public UserDto createUser(UserDto dto) {
        return webClient.post()
                .uri("/api/v1/repository/users")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(UserDto.class)
                .onErrorResume(ex -> Mono.just(dto))
                .block();
    }

    public UserDto getUser(String id) {
        return webClient.get()
                .uri("/api/v1/repository/users/{id}", id)
                .retrieve()
                .bodyToMono(UserDto.class)
                .onErrorResume(ex -> Mono.just(new UserDto(id, "Unknown User", null)))
                .block();
    }

    public List<BastionDto> getAllBastions() {
        return webClient.get()
                .uri("/api/v1/repository/bastions")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<BastionDto>>() {})
                .onErrorResume(ex -> Mono.just(List.of()))
                .block();
    }

    public BastionDto getBastionById(String id) {
        return webClient.get()
                .uri("/api/v1/repository/bastions/{id}", id)
                .retrieve()
                .bodyToMono(BastionDto.class)
                .onErrorResume(ex -> Mono.just(new BastionDto(id, "Unavailable", "No description available", null, List.of())))
                .block();
    }

    public BastionDto createBastion(BastionDto dto) {
        return webClient.post()
                .uri("/api/v1/repository/bastions")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(BastionDto.class)
                .onErrorResume(ex -> Mono.just(dto))
                .block();
    }

    public BastionDto updateBastion(String id, BastionDto dto) {
        return webClient.put()
                .uri("/api/v1/repository/bastions/{id}", id)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(BastionDto.class)
                .onErrorResume(ex -> Mono.just(dto))
                .block();
    }

    public void deleteBastion(String id) {
        webClient.delete()
                .uri("/api/v1/repository/bastions/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(ex -> Mono.empty())
                .block();
    }

    public List<FacilityDto> getAllFacilities() {
        return webClient.get()
                .uri("/api/v1/repository/facilities")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<FacilityDto>>() {})
                .onErrorResume(ex -> Mono.just(List.of()))
                .block();
    }

    public FacilityDto getFacilityById(String id) {
        return webClient.get()
                .uri("/api/v1/repository/facilities/{id}", id)
                .retrieve()
                .bodyToMono(FacilityDto.class)
                .onErrorResume(ex -> Mono.just(new FacilityDto(id, "Unknown", null, 0, "Unavailable", null)))
                .block();
    }

    public List<NewspaperDataDto> getAllNewspapers() {
        return webClient.get()
                .uri("/api/v1/repository/newspapers")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<NewspaperDataDto>>() {})
                .onErrorResume(ex -> Mono.just(List.of()))
                .block();
    }

    public NewspaperDataDto getNewspaperByName(String newspaperName) {
        return webClient.get()
                .uri("/api/v1/repository/newspapers/{name}", newspaperName)
                .retrieve()
                .bodyToMono(NewspaperDataDto.class)
                .onErrorResume(ex -> Mono.just(new NewspaperDataDto(newspaperName, "1970-01-01", "Unknown", null, List.of())))
                .block();
    }

    public NewspaperDataDto createNewspaper(NewspaperDataDto dto) {
        return webClient.post()
                .uri("/api/v1/repository/newspapers")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(NewspaperDataDto.class)
                .onErrorResume(ex -> Mono.just(dto))
                .block();
    }
}
