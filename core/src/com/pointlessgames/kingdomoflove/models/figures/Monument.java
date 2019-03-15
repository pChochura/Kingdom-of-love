package com.pointlessgames.kingdomoflove.models.figures;

import com.pointlessgames.kingdomoflove.models.Ability;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.managers.TextureManager;

import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class Monument extends Structure {

	public Monument() {
		super(TextureManager.getInstance().getTexture(TextureManager.MONUMENT));
		refreshSize();
		setPos();
	}

	@Override public void refreshSize() {
		height = tileSize * 1.5f;
		width = height * texture.getTexture().getWidth() / texture.getTexture().getHeight();
	}

	@Override public int getCapacity() {
		return 0;
	}

	@Override public void triggerAbility(Stats stats) {

	}

	@Override public String getAbilityDescription() {
		return "Just a monument.";
	}

	@Override public int getCost() {
		return 500;
	}

	@Override public int getLove() {
		return 25;
	}

	@Override public Ability getAbility(Stats stats) {
		return new Ability(Ability.ProductionType.NOTHING, 0);
	}

	@Override public boolean isUpgradable() {
		return false;
	}

	@Override public boolean hasLevels() {
		return false;
	}

	@Override public boolean hasRoad() {
		return false;
	}

	@Override protected void setPos() {
		setX((tileSize - width * getScaleX()) / 2);
		setY((tileSize - height * getScaleY()) / 2 + tileSize / 4f);
	}
}
