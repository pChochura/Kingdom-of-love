package com.pointlessgames.kingdomoflove.models.figures;

import com.badlogic.gdx.graphics.Texture;
import com.pointlessgames.kingdomoflove.models.Ability;
import com.pointlessgames.kingdomoflove.utils.Stats;

import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class Monument extends Structure {

	public Monument() {
		super(new Texture("figures/monument.png"));
		height = tileSize * 1.5f;
		width = height * texture.getTexture().getWidth() / texture.getTexture().getHeight();
		setPos();
	}

	@Override public int getCapacity() {
		return 1;
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

	@Override public boolean hasRoad() {
		return false;
	}

	@Override protected void setPos() {
		setX((tileSize - width * getScaleX()) / 2);
		setY((tileSize - height * getScaleY()) / 2 + tileSize / 4f);
	}
}
