package com.pointlessgames.kingdomoflove.models.figures;

import com.pointlessgames.kingdomoflove.models.Ability;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.Strings;
import com.pointlessgames.kingdomoflove.utils.managers.TextureManager;

import java.util.Locale;

import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class Library extends Structure {

	private final int[] cost = {1500, 2500, 3750, 5500, 7500, 9500, 12000, 15000};
	private final float[] loveProduction = {0.1f, 0.2f, 0.35f, 0.5f, 0.65f, 0.8f, 1.0f, 1.25f};

	public Library() {
		super(TextureManager.getInstance().getTexture(TextureManager.LIBRARY));
		refreshSize();
		setPos();
	}

	@Override public void refreshSize() {
		height = tileSize * 1.2f;
		width = height * texture.getTexture().getWidth() / texture.getTexture().getHeight();
	}

	private float getLoveProduction(Stats stats) {
		return loveProduction[getLevel() - 1] * stats.getPopulation();
	}

	@Override public int getCapacity() {
		return 0;
	}

	@Override public void triggerAbility(Stats stats) {
		float love = getLoveProduction(stats);
//		stats.love += love;

		if(love > 0)
			resetAbilityTip(String.format(Locale.getDefault(), "%+.1f", love), TextureManager.getInstance().getTexture(TextureManager.LOVE));
	}

	@Override public String getAbilityDescription() {
		return String.format(Locale.getDefault(), Strings.LIBRARY_ABILITY_DESCRIPTION, loveProduction[getLevel() - 1]);
	}

	@Override public int getCost() {
		return cost[getLevel() - 1];
	}

	@Override public int getLove() {
		return 25;
	}

	@Override public Ability getAbility(Stats stats) {
		return new Ability(Ability.ProductionType.LOVE, getLoveProduction(stats));
	}

	@Override public boolean isUpgradable() {
		return true;
	}

	@Override protected void setPos() {
		setX((tileSize - width * getScaleX()) / 2);
		setY((tileSize - height * getScaleY()) / 2);
	}

	@Override public boolean canUpgrade(Stats stats) {
		return false;
	}
}
