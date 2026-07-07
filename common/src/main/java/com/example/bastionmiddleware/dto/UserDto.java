package com.example.bastionmiddleware.dto;

import jakarta.validation.constraints.NotBlank;

public class UserDto {
    @NotBlank(message = "id is required")
    private String id;

    @NotBlank(message = "displayName is required")
    private String displayName;

    private String bastionId;

    public UserDto() {
    }

    public UserDto(String id, String displayName, String bastionId) {
        this.id = id;
        this.displayName = displayName;
        this.bastionId = bastionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBastionId() {
        return bastionId;
    }

    public void setBastionId(String bastionId) {
        this.bastionId = bastionId;
    }
}
