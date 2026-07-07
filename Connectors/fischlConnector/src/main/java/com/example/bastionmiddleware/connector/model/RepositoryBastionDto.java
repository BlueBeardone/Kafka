package com.example.bastionmiddleware.connector.model;

public final class RepositoryBastionDto {
	private final Long id;
	private final String ownerName;
	private final String rank;
	private final String facilityType;
	private final Integer constructionTurns;
	private final Integer hirelingsCount;

	public RepositoryBastionDto(Long id, String ownerName, String rank, String facilityType, Integer constructionTurns, Integer hirelingsCount) {
		this.id = id;
		this.ownerName = ownerName;
		this.rank = rank;
		this.facilityType = facilityType;
		this.constructionTurns = constructionTurns;
		this.hirelingsCount = hirelingsCount;
	}

	public Long getId() {
		return id;
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
