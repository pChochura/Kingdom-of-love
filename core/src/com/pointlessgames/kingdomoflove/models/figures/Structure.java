package com.pointlessgames.kingdomoflove.models.figures;

import com.badlogic.gdx.graphics.Texture;
import com.pointlessgames.kingdomoflove.utils.Stats;

public abstract class Structure extends Figure {

	public boolean checked;

	Structure(Texture texture) {
		super(texture);
	}

	@Override public boolean canUpgrade(Stats stats) {
		return stats.money >= getUpgradeCost();
	}

	public abstract int getCapacity();

	public boolean hasRoad() {
		return true;
	}
}
