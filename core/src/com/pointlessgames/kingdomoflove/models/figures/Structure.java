package com.pointlessgames.kingdomoflove.models.figures;

import com.badlogic.gdx.graphics.Texture;

public abstract class Structure extends Figure {

	Structure(Texture texture) {
		super(texture);
	}

	public abstract int getCapacity();

	public boolean hasRoad() {
		return true;
	}
}
