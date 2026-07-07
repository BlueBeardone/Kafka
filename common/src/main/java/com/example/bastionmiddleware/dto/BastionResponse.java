package com.example.bastionmiddleware.dto;

public final class BastionResponse {
	private final Long id;
	private final String ownerName;
	private final String rank;
	private final String facilityType;
	private final Integer constructionTurns;
	private final Integer hirelingsCount;

	public BastionResponse(Long id, String ownerName, String rank, String facilityType, Integer constructionTurns, Integer hirelingsCount) {
		this.id = id;
		this.ownerName = ownerName;
		this.rank = rank;
		this.facilityType = facilityType;
		this.constructionTurns = constructionTurns;
		this.hirelingsCount = hirelingsCount;
	}

	public Long id() { return id; }
	public String ownerName() { return ownerName; }
	public String rank() { return rank; }
	public String facilityType() { return facilityType; }
	public Integer constructionTurns() { return constructionTurns; }
	public Integer hirelingsCount() { return hirelingsCount; }
}
