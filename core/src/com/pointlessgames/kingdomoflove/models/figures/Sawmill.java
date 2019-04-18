package com.pointlessgames.kingdomoflove.models.figures;

import com.pointlessgames.kingdomoflove.models.Ability;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.Strings;
import com.pointlessgames.kingdomoflove.utils.managers.TextureManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class Sawmill extends Structure {

	private final int[] cost = {75, 150, 350, 650, 950, 1450, 1850, 2100};
	private final int[] capacity = {2, 4, 5, 6, 7, 8, 9, 10};
	private final int[] moneyProduction = {3, 5, 7, 10, 13, 15, 17, 20};

	public Sawmill() {
		super(TextureManager.getInstance().getTexture(TextureManager.SAWMILL));
		refreshSize();
		setPos();
	}

	@Override public void refreshSize() {
		height = tileSize * 1.2f;
		width = height * texture.getTexture().getWidth() / texture.getTexture().getHeight();
	}

	private int getMoneyProduction(Stats stats) {
		return getMoneyProduction(stats, null);
	}

	private int getMoneyProduction(Stats stats, ArrayList<Plant> trees) {
		if(trees == null) trees = new ArrayList<>();
		int treeLevels = 0;
		for(Figure f : stats.figures)
			if(f instanceof Tree || f instanceof Conifer)
				if(Math.pow(f.getMapX() - getMapX(), 2) + Math.pow(f.getMapY() - getMapY(), 2) <= 2 && ((Plant) f).getLife() > 0) {
					trees.add((Plant) f);
					treeLevels += f.getLevel();
				}
		return treeLevels * moneyProduction[getLevel() - 1];
	}

	@Override public void triggerAbility(Stats stats) {
		ArrayList<Plant> trees = new ArrayList<>();
		int money = getMoneyProduction(stats, trees);
//		stats.money += money;

		if(money > 0)
			resetAbilityTip(String.format(Locale.getDefault(), "%+d", money), TextureManager.getInstance().getTexture(TextureManager.MONEY));

		if(trees.size() > 0) {
			Collections.sort(trees, (t1, t2) -> (int) Math.signum(t1.getLife() - t2.getLife()));
			trees.get(0).decreaseLife();
			if(trees.get(0).getLife() == 0)
				trees.get(0).destroy(stats);
		}
	}

	@Override public String getAbilityDescription() {
		if(getLevel() > 1)
			return String.format(Locale.getDefault(), Strings.SAWMILL_ABILITY_DESCRIPTION_EXTENDED, moneyProduction[getLevel() - 1], getLevel());
		else return String.format(Locale.getDefault(), Strings.SAWMILL_ABILITY_DESCRIPTION, moneyProduction[getLevel() - 1]);
	}

	@Override public int getCapacity() {
		return capacity[getLevel() - 1];
	}

	@Override public int getCost() {
		return cost[getLevel() - 1];
	}

	@Override public int getLove() {
		return -15;
	}

	@Override public Ability getAbility(Stats stats) {
		return new Ability(Ability.ProductionType.MONEY, getMoneyProduction(stats));
	}

	@Override public boolean isUpgradable() {
		return true;
	}

	@Override protected void setPos() {
		setX((tileSize - width * getScaleX()) / 2);
		setY((tileSize - height * getScaleY()) / 2);
	}
}
