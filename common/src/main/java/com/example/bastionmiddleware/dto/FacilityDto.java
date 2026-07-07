package com.example.bastionmiddleware.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FacilityDto {
    @NotBlank(message = "id is required")
    private String id;

    @NotBlank(message = "name is required")
    private String name;

    @NotNull(message = "rank is required")
    private Rank rank;

    @NotNull(message = "hirelingAmount is required")
    private Integer hirelingAmount;

    @NotBlank(message = "description is required")
    private String description;

    @Valid
    private FacilityTableDto table;

    public FacilityDto() {
    }

    public FacilityDto(String id, String name, Rank rank, Integer hirelingAmount, String description, FacilityTableDto table) {
        this.id = id;
        this.name = name;
        this.rank = rank;
        this.hirelingAmount = hirelingAmount;
        this.description = description;
        this.table = table;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public Integer getHirelingAmount() {
        return hirelingAmount;
    }

    public void setHirelingAmount(Integer hirelingAmount) {
        this.hirelingAmount = hirelingAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FacilityTableDto getTable() {
        return table;
    }

    public void setTable(FacilityTableDto table) {
        this.table = table;
    }
}
