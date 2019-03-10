package com.pointlessgames.kingdomoflove.models.figures;

import com.pointlessgames.kingdomoflove.models.Ability;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.TextureManager;

import java.util.Locale;

import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class Pond extends Structure {

	private int[] cost = {225, 325, 425, 550, 725, 850, 975, 1150};
	private float love = -1.5f;

	public Pond() {
		super(TextureManager.getInstance().pond);
		refreshSize();
		setPos();
	}

	@Override public void refreshSize() {
		height = tileSize;
		width = height * texture.getTexture().getWidth() / texture.getTexture().getHeight();
	}

	private float getLoveProduction(Stats stats) {
		int roads = 0;
		for(Figure f : stats.figures)
			if(f instanceof Road)
				if(Math.pow(f.getMapX() - getMapX(), 2) + Math.pow(f.getMapY() - getMapY(), 2) <= 2)
					roads += f.getLevel();
		return love * roads;
	}

	@Override public void triggerAbility(Stats stats) {
		float love = getLoveProduction(stats);
		stats.love += love;

		if(love > 0)
			resetAbilityTip(String.format(Locale.getDefault(), "%+.1f", Math.abs(love)), TextureManager.getInstance().love);
	}

	@Override public String getAbilityDescription() {
		return String.format(Locale.getDefault(), "Decreases %.1f%% love for every surrounding road.", love);
	}

	@Override public int getCapacity() {
		return 0;
	}

	@Override public int getCost() {
		return cost[getLevel() - 1];
	}

	@Override public int getLove() {
		return 12;
	}

	@Override public Ability getAbility(Stats stats) {
		return new Ability(Ability.ProductionType.LOVE, getLoveProduction(stats));
	}

	@Override public boolean isUpgradable() {
		return true;
	}

	@Override public boolean hasRoad() {
		return false;
	}

	@Override protected void setPos() {
		setX((tileSize - width * getScaleX()) / 2);
		setY((tileSize - height * getScaleY()) / 2);
	}
}
