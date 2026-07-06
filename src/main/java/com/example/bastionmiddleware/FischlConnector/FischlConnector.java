package com.example.bastionmiddleware.FischlConnector;

import com.example.bastionmiddleware.FischlConnector.model.RepositoryBastionDto;
import com.example.bastionmiddleware.dto.BastionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
                .onErrorResume(ex -> Mono.just(new BastionResponse(1L, dto.ownerName(), dto.rank(), dto.facilityType(), dto.constructionTurns(), dto.hirelingsCount())))
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
}
