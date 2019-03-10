package com.pointlessgames.kingdomoflove.models.figures;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pointlessgames.kingdomoflove.models.Ability;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.TextureManager;

import java.util.Locale;

import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class Tree extends Plant {

	private float[] love = new float[]{0.0f, 0.1f, 0.3f, 0.5f, 0.8f, 1.1f, 1.4f, 1.7f};
	private int[] cost = {10, 25, 45, 75, 100, 135, 175, 225};

	public Tree() {
		super(TextureManager.getInstance().tree);
		refreshSize();
		setPos();
	}

	@Override public void refreshSize() {
		height = tileSize * 1.75f;
		width = height * texture.getTexture().getWidth() / texture.getTexture().getHeight();
	}

	@Override public void triggerAbility(Stats stats) {
		float love = this.love[getLevel() - 1];
		stats.love += love;

		if(love > 0)
			resetAbilityTip(String.format(Locale.getDefault(), "%+.1f", love), TextureManager.getInstance().love);
	}

	@Override public String getAbilityDescription() {
		return String.format(Locale.getDefault(), "Daily increases love by %.1f%%.", love[getLevel() - 1]);
	}

	@Override public int getCost() {
		return cost[getLevel() - 1];
	}

	@Override public int getLove() {
		return 3;
	}

	@Override public Ability getAbility(Stats stats) {
		return new Ability(Ability.ProductionType.LOVE, love[getLevel() - 1]);
	}

	@Override public boolean isUpgradable() {
		return true;
	}

	@Override protected void setPos() {
		setX((tileSize - width * getScaleX()) / 2);
		setY((tileSize - height * getScaleY()) / 2 + tileSize / 4);
	}

	@Override public int getMaxLife() {
		return 5;
	}

	@Override protected void drawTexture(SpriteBatch sP, float tileX, float tileY, float alpha) {
		sP.begin();
		Color color = sP.getColor();
		sP.setColor(color.cpy().mul(1, 1, 1, alpha));
		switch(getLevel()) {
			case 1:
				sP.draw(texture, tileX + getX(), tileY + getY(), getScaleX() * width, getScaleY() * height);
				break;
			case 2:
				sP.draw(texture, tileX + getX() - width / 6f, tileY + getY(), getScaleX() * width, getScaleY() * height);
				sP.draw(texture, tileX + getX() + width / 6f, tileY + getY(), getScaleX() * width, getScaleY() * height);
				break;
			case 3:
				sP.draw(texture, tileX + getX() - width / 6f, tileY + getY() + height / 6f, getScaleX() * width, getScaleY() * height);
				sP.draw(texture, tileX + getX() + width / 6f, tileY + getY() + height / 6f, getScaleX() * width, getScaleY() * height);
				sP.draw(texture, tileX + getX(), tileY + getY(), getScaleX() * width, getScaleY() * height);
				break;
			case 4:
				sP.draw(texture, tileX + getX() - width / 6f, tileY + getY() + height / 6f, getScaleX() * width, getScaleY() * height);
				sP.draw(texture, tileX + getX() + width / 6f, tileY + getY() + height / 6f, getScaleX() * width, getScaleY() * height);
				sP.draw(texture, tileX + getX() - width / 8f, tileY + getY(), getScaleX() * width, getScaleY() * height);
				sP.draw(texture, tileX + getX() + width / 8f, tileY + getY(), getScaleX() * width, getScaleY() * height);
				break;
			case 5:
				sP.draw(texture, tileX + getX() - width / 6f, tileY + getY() + height / 6f, getScaleX() * width, getScaleY() * height);
				sP.draw(texture, tileX + getX() + width / 6f, tileY + getY() + height / 6f, getScaleX() * width, getScaleY() * height);
				sP.draw(texture, tileX + getX(), tileY + getY(), getScaleX() * width, getScaleY() * height);
				sP.draw(texture, tileX + getX() - width / 6f, tileY + getY() - height / 6f, getScaleX() * width, getScaleY() * height);
				sP.draw(texture, tileX + getX() + width / 6f, tileY + getY() - height / 6f, getScaleX() * width, getScaleY() * height);
				break;
			case 6:
				sP.draw(texture, tileX + getX() - width / 6f, tileY + getY() + height / 6f, getScaleX() * width, getScaleY() * height);
				sP.draw(texture, tileX + getX() + width / 6f, tileY + getY() + height / 6f, getScaleX() * width, getScaleY() * height);
				sP.draw(texture, tileX + getX() - width / 8f, tileY + getY(), getScaleX() * width, getScaleY() * height);
				sP.draw(texture, tileX + getX() + width / 8f, tileY + getY(), getScaleX() * width, getScaleY() * height);
				sP.draw(texture, tileX + getX() - width / 6f, tileY + getY() - height / 6f, getScaleX() * width, getScaleY() * height);
				sP.draw(texture, tileX + getX() + width / 6f, tileY + getY() - height / 6f, getScaleX() * width, getScaleY() * height);
				break;
			case 7:
				sP.draw(texture, tileX + getX() - width / 6f, tileY + getY() + height / 6f, getScaleX() * width, getScaleY() * height);
				sP.draw(texture, tileX + getX() + width / 6f, tileY + getY() + height / 6f, getScaleX() * width, getScaleY() * height);
				sP.draw(texture, tileX + getX() - width / 8f, tileY + getY(), getScaleX() * width, getScaleY() * height);
				sP.draw(texture, tileX + getX(), tileY + getY(), getScaleX() * width, getScaleY() * height);
				sP.draw(texture, tileX + getX() + width / 8f, tileY + getY(), getScaleX() * width, getScaleY() * height);
				sP.draw(texture, tileX + getX() - width / 6f, tileY + getY() - height / 6f, getScaleX() * width, getScaleY() * height);
				sP.draw(texture, tileX + getX() + width / 6f, tileY + getY() - height / 6f, getScaleX() * width, getScaleY() * height);
				break;
		}
		sP.setColor(color);
		sP.end();
	}
}
