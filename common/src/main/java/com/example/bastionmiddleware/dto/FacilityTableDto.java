package com.example.bastionmiddleware.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public class FacilityTableDto {
    @NotNull(message = "table is required")
    private List<List<String>> table;

    public FacilityTableDto() {
    }

    public FacilityTableDto(List<List<String>> table) {
        this.table = table;
    }

    public List<List<String>> getTable() {
        return table;
    }

    public void setTable(List<List<String>> table) {
        this.table = table;
    }
}
