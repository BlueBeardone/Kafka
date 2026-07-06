package com.example.bastionmiddleware.model;

public class Bastion {
    private Long id;
    private String ownerName;
    private String rank;
    private String facilityType;
    private Integer constructionTurns;
    private Integer hirelingsCount;

    public Bastion() {
    }

    public Bastion(Long id, String ownerName, String rank, String facilityType, Integer constructionTurns, Integer hirelingsCount) {
        this.id = id;
        this.ownerName = ownerName;
        this.rank = rank;
        this.facilityType = facilityType;
        this.constructionTurns = constructionTurns;
        this.hirelingsCount = hirelingsCount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }
    public String getFacilityType() { return facilityType; }
    public void setFacilityType(String facilityType) { this.facilityType = facilityType; }
    public Integer getConstructionTurns() { return constructionTurns; }
    public void setConstructionTurns(Integer constructionTurns) { this.constructionTurns = constructionTurns; }
    public Integer getHirelingsCount() { return hirelingsCount; }
    public void setHirelingsCount(Integer hirelingsCount) { this.hirelingsCount = hirelingsCount; }
}
