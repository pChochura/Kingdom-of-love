package com.pointlessgames.kingdomoflove.models.figures;

import com.pointlessgames.kingdomoflove.models.Ability;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.managers.TextureManager;

import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class Well extends Structure {

	public Well() {
		super(TextureManager.getInstance().getTexture(TextureManager.WELL));
		refreshSize();
		setPos();
	}

	@Override public void refreshSize() {
		height = tileSize * 1.2f;
		width = height * texture.getTexture().getWidth() / texture.getTexture().getHeight();
	}

	@Override public void triggerAbility(Stats stats) { }

	@Override public String getAbilityDescription() {
		return "Just a well.";
	}

	@Override public int getCapacity() {
		return 0;
	}

	@Override public int getCost() {
		return 50;
	}

	@Override public int getLove() {
		return 3;
	}

	@Override public Ability getAbility(Stats stats) {
		return new Ability();
	}

	@Override public boolean isUpgradable() {
		return true;
	}

	@Override protected void setPos() {
		setX((tileSize - width * getScaleX()) / 2);
		setY((tileSize - height * getScaleY()) / 2);
	}
}
