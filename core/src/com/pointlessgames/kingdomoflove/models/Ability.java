package com.pointlessgames.kingdomoflove.models;

public class Ability {

	public enum ProductionType {
		NOTHING(0),
		MONEY(1),
		LOVE(2),
		BOTH(MONEY.number | LOVE.number);

		public final int number;

		ProductionType(int number) {
			this.number = number;
		}
	}

	private final ProductionType productionType;
	private int moneyAmount;
	private float loveAmount;

	public Ability() {
		this.productionType = ProductionType.NOTHING;
		this.moneyAmount = 0;
		this.loveAmount = 0;
	}

	public Ability(ProductionType productionType, float amount) {
		this.productionType = productionType;
		if(productionType == ProductionType.MONEY) moneyAmount = (int) amount;
		else loveAmount = amount;
	}

	public float getAmount(ProductionType productionType) {
		return productionType == ProductionType.MONEY ? moneyAmount : loveAmount;
	}

	public ProductionType getProductionType() {
		return productionType;
	}
}
