package com.example.bastionmiddleware.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record BastionRequest(
        @NotBlank(message = "ownerName is required") String ownerName,
        @Pattern(regexp = "[DCBAS]", message = "rank must be one of D, C, B, A, S") String rank,
        @Pattern(regexp = "Barracks|TrainingGround|Workshop|Keep", message = "facilityType is invalid") String facilityType,
        @Min(value = 1, message = "constructionTurns must be greater than 0") Integer constructionTurns,
        @Min(value = 0, message = "hirelingsCount must be 0 or greater") Integer hirelingsCount) {
}
