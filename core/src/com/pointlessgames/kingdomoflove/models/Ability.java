package com.pointlessgames.kingdomoflove.models;

public class Ability {

	public enum ProductionType {
		MONEY, LOVE, NOTHING
	}

	private ProductionType productionType;
	private float amount;

	public Ability(ProductionType productionType, float amount) {
		this.productionType = productionType;
		this.amount = amount;
	}

	public float getAmount() {
		return amount;
	}

	public ProductionType getProductionType() {
		return productionType;
	}
}
