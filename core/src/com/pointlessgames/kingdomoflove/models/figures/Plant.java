package com.pointlessgames.kingdomoflove.models.figures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.pointlessgames.kingdomoflove.renderers.CustomShapeRenderer;
import com.pointlessgames.kingdomoflove.utils.Colors;

import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public abstract class Plant extends Figure {

	private int life;

	Plant(Texture texture) {
		super(texture);
		life = getMaxLife();
	}

	@Override public void draw(SpriteBatch sP, CustomShapeRenderer sR, float tileX, float tileY) {
		draw(sP, sR, tileX, tileY, 1);
	}

	@Override public void draw(SpriteBatch sP, CustomShapeRenderer sR, float tileX, float tileY, float alpha) {
		drawTexture(sP, tileX, tileY, alpha);
		drawLifeBar(sR, tileX, tileY, alpha);
		if(hasLevels()) drawLevel(sP, sR, tileX, tileY, alpha);
		drawAbilityTip(sP, tileX, tileY, alpha);
	}

	private void drawLifeBar(CustomShapeRenderer sR, float tileX, float tileY, float alpha) {
//		if(alpha != 1) {
//			Gdx.gl.glEnable(GL20.GL_BLEND);
//			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//		}

		float size = tileSize * 0.2f;
		float halfSize = size * 0.5f;
		sR.begin(ShapeRenderer.ShapeType.Filled);
		sR.setColor(Colors.inactiveColor.cpy().mul(1, 1, 1, alpha));
		sR.roundedRect(tileX + size, tileY + halfSize, tileSize - (size + halfSize), size, halfSize * 0.3f);
		sR.setColor(Colors.loveColor.cpy().mul(1, 1, 1, alpha));
		sR.roundedRect(tileX + size, tileY + halfSize, MathUtils.lerp(0, tileSize - (size + halfSize), getLife()), size, halfSize * 0.3f);
		sR.end();

//		if(alpha != 1) Gdx.gl.glDisable(GL20.GL_BLEND);
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
