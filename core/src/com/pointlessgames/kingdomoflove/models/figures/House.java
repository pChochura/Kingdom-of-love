package com.pointlessgames.kingdomoflove.models.figures;

import com.pointlessgames.kingdomoflove.models.Ability;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.Strings;
import com.pointlessgames.kingdomoflove.utils.managers.TextureManager;

import java.util.Locale;

import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class House extends Structure {

	private int[] cost = {35, 80, 130, 230, 400, 750, 1200};
	private int[] capacity = {1, 2, 3, 4, 5, 6, 7, 8};
	private int[] moneyProduction = {3, 7, 11, 15, 19, 24, 29, 35};

	public House() {
		super(TextureManager.getInstance().getTexture(TextureManager.HOUSE));
		refreshSize();
		setPos();
	}

	@Override public void refreshSize() {
		height = tileSize * 1.2f;
		width = height * texture.getTexture().getWidth() / texture.getTexture().getHeight();
	}

	private int getMoneyProduction() {
		return moneyProduction[getLevel() - 1] * capacity[getLevel() - 1];
	}

	@Override public void triggerAbility(Stats stats) {
		int money = getMoneyProduction();
//		stats.money += money;

		if(money > 0)
			resetAbilityTip(String.format(Locale.getDefault(), "%+d", money), TextureManager.getInstance().getTexture(TextureManager.MONEY));
	}

	@Override public String getAbilityDescription() {
		return String.format(Locale.getDefault(), Strings.HOUSE_ABILITY_DESCRIPTION, moneyProduction[getLevel() - 1]);
	}

	@Override public int getCapacity() {
		return capacity[getLevel() - 1];
	}

	@Override public int getCost() {
		return cost[getLevel() - 1];
	}

	@Override public int getLove() {
		return 10;
	}

	@Override public Ability getAbility(Stats stats) {
		return new Ability(Ability.ProductionType.MONEY, getMoneyProduction());
	}

	@Override public boolean isUpgradable() {
		return true;
	}

	@Override protected void setPos() {
		setX((tileSize - width * getScaleX()) / 2);
		setY((tileSize - height * getScaleY()) / 2);
	}
}
