package com.pointlessgames.kingdomoflove.models.figures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.pointlessgames.kingdomoflove.renderers.CustomShapeRenderer;
import com.pointlessgames.kingdomoflove.utils.Colors;

import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;
import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public abstract class Plant extends Figure {

	private int life;

	Plant(Texture texture) {
		super(texture);
		life = getMaxLife();
	}

	@Override public void draw(SpriteBatch sP, CustomShapeRenderer sR, float tileX, float tileY) {
		drawTexture(sP, tileX, tileY);
		drawLifeBar(sR, tileX, tileY);
		if(hasLevels()) drawLevel(sP, sR, tileX, tileY);
		drawAbilityTip(sP, tileX, tileY);
	}

	private void drawLifeBar(CustomShapeRenderer sR, float tileX, float tileY) {
		sR.begin(ShapeRenderer.ShapeType.Filled);
		sR.setColor(Colors.inactiveColor);
		float size = tileSize * 0.15f * ratio;
		float halfSize = size * 0.5f;
		sR.roundedRect(tileX + size, tileY + halfSize, tileSize - (size + halfSize), size, halfSize * 0.3f);
		sR.setColor(Colors.loveColor);
		sR.roundedRect(tileX + size, tileY + halfSize, MathUtils.lerp(0, tileSize - (size + halfSize), getLife()), size, halfSize * 0.3f);
		sR.end();
	}

	public abstract int getMaxLife();

	public float getLife() {
		return (float) life / getMaxLife();
	}

	public void decreaseLife() {
		life--;
	}

	public void setLife(int life) {
		this.life = life;
	}
}
