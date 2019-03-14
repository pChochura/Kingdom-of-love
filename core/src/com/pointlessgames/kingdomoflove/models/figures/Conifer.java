package com.pointlessgames.kingdomoflove.models.figures;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pointlessgames.kingdomoflove.models.Ability;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.TextureManager;

import java.util.Locale;

import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class Conifer extends Plant {

	private float[] love = {0.2f, 0.3f, 0.4f, 0.6f, 0.8f, 1.1f, 1.3f, 1.5f};
	private int[] cost = {65, 130, 180, 275, 375, 650, 850, 1100};
	private int maxLife = 10;

	public Conifer() {
		super(TextureManager.getInstance().getTexture(TextureManager.CONIFER));
		refreshSize();
		setPos();
		setLife(getMaxLife());
	}

	@Override public void refreshSize() {
		height = tileSize * 1.2f;
		width = height * texture.getTexture().getWidth() / texture.getTexture().getHeight();
	}

	@Override public void triggerAbility(Stats stats) {
		float love = this.love[getLevel() - 1];
		stats.love += love;

		if(love > 0)
			resetAbilityTip(String.format(Locale.getDefault(), "%+.1f", love), TextureManager.getInstance().getTexture(TextureManager.LOVE));
	}

	@Override public String getAbilityDescription() {
		return String.format(Locale.getDefault(), "Daily increases love by %.1f%%.", love[getLevel() - 1]);
	}

	@Override public int getCost() {
		return cost[getLevel() - 1];
	}

	@Override public int getLove() {
		return 4;
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

	@Override public boolean canUpgrade(Stats stats) {
		int maxLevel = 1;
		for(Figure figure : stats.figures)
			if(figure instanceof Sawmill && Math.pow(figure.getMapX() - getMapX(), 2) + Math.pow(figure.getMapY() - getMapY(), 2) <= 2)
				maxLevel = Math.max(figure.getLevel(), maxLevel);

		return stats.money >= getUpgradeCost() && getLevel() < maxLevel;
	}

	@Override public int getMaxLife() {
		return maxLife;
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
