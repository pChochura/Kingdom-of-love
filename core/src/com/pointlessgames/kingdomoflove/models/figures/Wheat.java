package com.pointlessgames.kingdomoflove.models.figures;

import com.badlogic.gdx.graphics.Texture;
import com.pointlessgames.kingdomoflove.models.Ability;
import com.pointlessgames.kingdomoflove.utils.Stats;

import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class Wheat extends Plant {

	private int[] cost = {35, 75, 100, 125, 175, 225, 275, 350};

	public Wheat() {
		super(new Texture("figures/wheat.png"));
		height = tileSize * 1.2f;
		width = height * texture.getTexture().getWidth() / texture.getTexture().getHeight();
		setPos();
	}

	@Override public void triggerAbility(Stats stats) {
		int ponds = 0;
		for(Figure f : stats.figures)
			if(f instanceof Pond)
				if(Math.pow(f.getMapX() - getMapX(), 2) + Math.pow(f.getMapY() - getMapY(), 2) < 2)
					ponds += f.getLevel();
		this.level = ponds + 1;
	}

	@Override public String getAbilityDescription() {
		return "Increases own level if borders a pond.";
	}

	@Override public int getCost() {
		return cost[getLevel() - 1];
	}

	@Override public int getLove() {
		return 2;
	}

	@Override public Ability getAbility(Stats stats) {
		return new Ability(Ability.ProductionType.NOTHING, 0);
	}

	@Override public boolean isUpgradable() {
		return false;
	}

	@Override protected void setPos() {
		setX((tileSize - width * getScaleX()) / 2);
		setY((tileSize - height * getScaleY()) / 2);
	}

	@Override public int getMaxLife() {
		return 10;
	}
}
