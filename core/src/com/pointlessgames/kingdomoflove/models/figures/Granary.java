package com.pointlessgames.kingdomoflove.models.figures;

import com.pointlessgames.kingdomoflove.models.Ability;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.Strings;
import com.pointlessgames.kingdomoflove.utils.managers.TextureManager;

import java.util.Locale;
import java.util.Set;

import static com.pointlessgames.kingdomoflove.utils.Settings.HEIGHT;
import static com.pointlessgames.kingdomoflove.utils.Settings.WIDTH;
import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class Granary extends Structure {

	private int[] cost = {350, 650, 1050, 1350, 1650, 1950, 2400, 2750};
	private int[] capacity = {2, 3, 4, 4, 5, 5, 6, 6};
	private float[] loveProduction = {1, 1.2f, 1.5f, 1.8f, 2.1f, 2.5f, 2.9f, 3.5f};

	public Granary() {
		super(TextureManager.getInstance().getTexture(TextureManager.GRANARY));
		refreshSize();
		setPos();
	}

	@Override public void refreshSize() {
		height = tileSize * 1.2f;
		width = height * texture.getTexture().getWidth() / texture.getTexture().getHeight();
	}

	@Override public int getCapacity() {
		return capacity[getLevel() - 1];
	}

	@Override public void triggerAbility(Stats stats) {
		float love = getLoveProduction(stats);
		stats.love += love;

		if(love > 0)
			resetAbilityTip(String.format(Locale.getDefault(), "%+.1f", love), TextureManager.getInstance().getTexture(TextureManager.LOVE));
	}

	private float getLoveProduction(Stats stats) {
		Structure[][] map = new Structure[WIDTH][HEIGHT];
		for(Figure f : stats.figures)
			if(f instanceof Structure) {
				map[f.getMapX()][f.getMapY()] = (Structure) f;
				((Structure) f).checked = false;
			}

		Set<Structure> connectedStructures = stats.getConnectedFigures(map, getMapX(), getMapY(), true);
		int amount = 0;
		for(Structure s : connectedStructures)
			if(s instanceof Sawmill || s instanceof Mill) amount++;

		return amount * loveProduction[getLevel() - 1];
	}

	@Override public String getAbilityDescription() {
		return String.format(Locale.getDefault(), Strings.GRANARY_ABILITY_DESCRIPTION, loveProduction[getLevel() - 1]);
	}

	@Override public int getCost() {
		return cost[getLevel() - 1];
	}

	@Override public int getLove() {
		return 7;
	}

	@Override public Ability getAbility(Stats stats) {
		return new Ability(Ability.ProductionType.LOVE, getLoveProduction(stats));
	}

	@Override public boolean isUpgradable() {
		return true;
	}

	@Override protected void setPos() {
		setX((tileSize - width * getScaleX()) / 2);
		setY((tileSize - height * getScaleY()) / 2);
	}
}
