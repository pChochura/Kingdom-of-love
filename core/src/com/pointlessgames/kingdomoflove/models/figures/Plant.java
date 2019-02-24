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
		sR.roundedRect(tileX + 30 * ratio, tileY + 15 * ratio, tileSize - 45 * ratio, 30 * ratio, 5 * ratio);
		sR.setColor(Colors.loveColor);
		sR.roundedRect(tileX + 30 * ratio, tileY + 15 * ratio, MathUtils.lerp(0, tileSize - 45 * ratio, getLife()), 30 * ratio, 5 * ratio);
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
