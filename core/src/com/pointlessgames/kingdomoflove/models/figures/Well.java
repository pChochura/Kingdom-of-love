package com.pointlessgames.kingdomoflove.models.figures;

import com.badlogic.gdx.graphics.Texture;
import com.pointlessgames.kingdomoflove.models.Ability;
import com.pointlessgames.kingdomoflove.utils.Stats;

import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class Well extends Structure {

	public Well() {
		super(new Texture("figures/well.png"));
		height = tileSize * 1.2f;
		width = height * texture.getTexture().getWidth() / texture.getTexture().getHeight();
		setPos();
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
		return new Ability(Ability.ProductionType.NOTHING, 0);
	}

	@Override public boolean isUpgradable() {
		return true;
	}

	@Override protected void setPos() {
		setX((tileSize - width * getScaleX()) / 2);
		setY((tileSize - height * getScaleY()) / 2);
	}
}
