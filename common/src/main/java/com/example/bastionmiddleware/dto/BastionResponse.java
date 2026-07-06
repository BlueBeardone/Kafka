package com.example.bastionmiddleware.dto;

public record BastionResponse(Long id, String ownerName, String rank, String facilityType, Integer constructionTurns, Integer hirelingsCount) {
}
