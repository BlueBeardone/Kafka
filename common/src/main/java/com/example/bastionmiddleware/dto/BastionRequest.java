package com.example.bastionmiddleware.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class BastionRequest {

    @NotBlank(message = "ownerName is required")
    private String ownerName;

    @Pattern(regexp = "[DCBAS]", message = "rank must be one of D, C, B, A, S")
    private String rank;

    @Pattern(regexp = "Barracks|TrainingGround|Workshop|Keep", message = "facilityType is invalid")
    private String facilityType;

    @Min(value = 1, message = "constructionTurns must be greater than 0")
    private Integer constructionTurns;

    @Min(value = 0, message = "hirelingsCount must be 0 or greater")
    private Integer hirelingsCount;

    public BastionRequest() {
    }

    public BastionRequest(String ownerName,
                          String rank,
                          String facilityType,
                          Integer constructionTurns,
                          Integer hirelingsCount) {
        this.ownerName = ownerName;
        this.rank = rank;
        this.facilityType = facilityType;
        this.constructionTurns = constructionTurns;
        this.hirelingsCount = hirelingsCount;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getRank() {
        return rank;
    }

    public String getFacilityType() {
        return facilityType;
    }

    public Integer getConstructionTurns() {
        return constructionTurns;
    }

    public Integer getHirelingsCount() {
        return hirelingsCount;
    }
}
