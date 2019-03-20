package com.pointlessgames.kingdomoflove.models.figures;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.pointlessgames.kingdomoflove.models.Ability;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.managers.TextureManager;

import java.util.Locale;

import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class Tree extends Plant {

	private float[] love = {0.1f, 0.2f, 0.3f, 0.4f, 0.6f, 0.8f, 1.0f, 1.2f};
	private int[] cost = {10, 25, 55, 110, 220, 350, 600, 850};
	private int maxLife = 5;

	public Tree() {
		super(null);
		texture = TextureManager.getInstance().tree[0][0];

		refreshSize();
		setPos();
		setLife(getMaxLife());
	}

	@Override public void refreshSize() {
		height = tileSize * 1.2f;
		width = height;
	}

	@Override public void triggerAbility(Stats stats) {
		float love = this.love[getLevel() - 1];
		stats.love += love;

		setLife(MathUtils.clamp(getLife() * getMaxLife() - 0.2f, 0, getMaxLife()));
		if(getLife() == 0)
			destroy(stats);

		if(love > 0)
			resetAbilityTip(String.format(Locale.getDefault(), "%+.1f", love), TextureManager.getInstance().getTexture(TextureManager.LOVE));
	}

	@Override public String getAbilityDescription() {
		return String.format(Locale.getDefault(), "Daily increases love by %.1f%%.\nDecreases its own life every 5 days.", love[getLevel() - 1]);
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

	@Override public boolean canUpgrade(Stats stats) {
		int maxLevel = 1;
		for(Figure figure : stats.figures)
			if(figure instanceof Sawmill && Math.pow(figure.getMapX() - getMapX(), 2) + Math.pow(figure.getMapY() - getMapY(), 2) <= 2)
				maxLevel = Math.max(figure.getLevel(), maxLevel);

		return stats.money >= getUpgradeCost() && getLevel() < maxLevel;
	}

	@Override public int getMaxLife() {
		return maxLife + getLevel() - 1;
	}

	@Override protected void drawTexture(SpriteBatch sP, float tileX, float tileY, float alpha) {
		sP.setColor(Color.WHITE.cpy().mul(1, 1, 1, alpha));
		sP.draw(TextureManager.getInstance().tree[(getLevel() - 1) / 3][(getLevel() - 1) % 3], tileX + getX(), tileY + getY(), width * getScaleX(), height * getScaleY());
	}
}