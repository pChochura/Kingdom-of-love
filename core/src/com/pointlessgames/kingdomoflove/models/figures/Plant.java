package com.pointlessgames.kingdomoflove.models.figures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.managers.TextureManager;

import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public abstract class Plant extends Figure {

	private float life;

	Plant(Texture texture) {
		super(texture);
	}

	@Override public void draw(SpriteBatch sP, float tileX, float tileY, float alpha) {
		drawTexture(sP, tileX, tileY, alpha);
		drawLifeBar(sP, tileX, tileY, alpha);
		if(hasLevels()) drawLevel(sP, tileX, tileY, alpha);
		drawAbilityTip(sP, tileX, tileY, alpha);
	}

	private void drawLifeBar(SpriteBatch sP, float tileX, float tileY, float alpha) {
		float size = tileSize * 0.2f;
		float halfSize = size * 0.5f;

		sP.setColor(Colors.inactiveColor.cpy().mul(1, 1, 1, alpha));
		TextureManager.getInstance().filledRect.draw(sP, tileX + size, tileY + halfSize, tileSize - (size + halfSize), size);

		sP.setColor(Colors.loveColor.cpy().mul(1, 1, 1, alpha));
		TextureManager.getInstance().filledRect.draw(sP, tileX + size, tileY + halfSize, MathUtils.lerp(0, tileSize - (size + halfSize), getLife()), size);
	}

	@Override public void levelUp() {
		float life = getLife();
		super.levelUp();
		setLife(MathUtils.ceil(life * getMaxLife()));
	}

	@Override public boolean canUpgrade(Stats stats) {
		return true;
	}

	public abstract int getMaxLife();

	public float getLife() {
		return life / getMaxLife();
	}

	public void decreaseLife() {
		life = MathUtils.clamp(life - 1, 0, getMaxLife());
	}

	public void setLife(float life) {
		this.life = life;
	}
}
