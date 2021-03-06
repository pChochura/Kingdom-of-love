package com.pointlessgames.kingdomoflove.models.figures;

import com.badlogic.gdx.math.MathUtils;
import com.pointlessgames.kingdomoflove.models.Ability;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.Strings;
import com.pointlessgames.kingdomoflove.utils.managers.TextureManager;

import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class Wheat extends Plant {

	private final int[] cost = {35, 75, 100, 125, 175, 225, 275, 350};
	private final int maxLife = 5;

	public Wheat() {
		super(TextureManager.getInstance().getTexture(TextureManager.WHEAT));
		refreshSize();
		setPos();
		setLife(getMaxLife());
	}

	@Override public void refreshSize() {
		height = tileSize * 1.2f;
		width = height * texture.getTexture().getWidth() / texture.getTexture().getHeight();
	}

	@Override public void triggerAbility(Stats stats) {
		int ponds = 0, maxLevel = 1;
		for(Figure f : stats.figures) {
			if(f instanceof Pond) {
				if(Math.pow(f.getMapX() - getMapX(), 2) + Math.pow(f.getMapY() - getMapY(), 2) < 2)
					ponds += f.getLevel();
			} else if(f instanceof Mill)
				maxLevel = Math.max(maxLevel, f.getLevel());
		}
		this.level = Math.min(maxLevel, ponds + 1);

		setLife(MathUtils.clamp(getLife() * getMaxLife() - 0.2f, 0, getMaxLife()));
		if(getLife() == 0)
			destroy(stats);
	}

	@Override public String getAbilityDescription() {
		return Strings.WHEAT_ABILITY_DESCRIPTION;
	}

	@Override public int getCost() {
		return cost[getLevel() - 1];
	}

	@Override public int getLove() {
		return 2;
	}

	@Override public Ability getAbility(Stats stats) {
		return new Ability();
	}

	@Override public boolean isUpgradable() {
		return false;
	}

	@Override protected void setPos() {
		setX((tileSize - width * getScaleX()) / 2);
		setY((tileSize - height * getScaleY()) / 2);
	}

	@Override public int getMaxLife() {
		return maxLife + getLevel() - 1;
	}
}
