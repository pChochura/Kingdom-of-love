package com.pointlessgames.kingdomoflove.models.figures;

import com.pointlessgames.kingdomoflove.models.Ability;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.TextureManager;

import java.util.Locale;

import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class Library extends Structure {

	private float love = 0.1f;

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
		return love * stats.getPopulation();
	}

	@Override public int getCapacity() {
		return 0;
	}

	@Override public void triggerAbility(Stats stats) {
		float love = getLoveProduction(stats);
		stats.love += love;

		if(love > 0)
			resetAbilityTip(String.format(Locale.getDefault(), "%+.1f", love), TextureManager.getInstance().getTexture(TextureManager.LOVE));
	}

	@Override public String getAbilityDescription() {
		return String.format(Locale.getDefault(), "Daily increases love for every Kingdom inhabitant by %.1f%%.", love);
	}

	@Override public int getCost() {
		return 300;
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
}
