package com.pointlessgames.kingdomoflove.models.figures;

import com.badlogic.gdx.math.Interpolation;
import com.pointlessgames.kingdomoflove.models.Ability;
import com.pointlessgames.kingdomoflove.utils.Settings;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.managers.TextureManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class Mill extends Structure {

	private int[] cost = {175, 450, 950, 1250, 1750, 2250, 2850, 3100};
	private int[] capacity = {2, 3, 4, 5, 6, 7, 8, 9};
	private int[] moneyProduction = {5, 7, 10, 13, 17, 21, 26, 35};

	public Mill() {
		super(TextureManager.getInstance().getTexture(TextureManager.MILL));
		refreshSize();
		setPos();
	}

	@Override public void refreshSize() {
		height = tileSize;
		width = height * texture.getTexture().getWidth() / texture.getTexture().getHeight();
	}

	private int getMoneyProduction(Stats stats) {
		return getMoneyProduction(stats, null);
	}

	private int getMoneyProduction(Stats stats, ArrayList<Plant> wheat) {
		if(wheat == null) wheat = new ArrayList<>();
		int wheatLevel = 0, ponds = 0, wells = 0;
		for(Figure f : stats.figures) {
			if(Math.pow(f.getMapX() - getMapX(), 2) + Math.pow(f.getMapY() - getMapY(), 2) <= 2) {
				if(f instanceof Wheat && ((Plant) f).getLife() > 0) {
					wheatLevel += f.getLevel();
					wheat.add((Plant) f);
				} if(f instanceof Pond)
					ponds += f.getLevel();
				if(f instanceof Well)
					wells += f.getLevel();
			}
		}
		return moneyProduction[getLevel() - 1] * wheatLevel * (Math.max(ponds, wells));
	}

	@Override public void triggerAbility(Stats stats) {
		ArrayList<Plant> wheat = new ArrayList<>();
		int money = getMoneyProduction(stats, wheat);
		stats.money += money;

		if(money > 0)
			resetAbilityTip(String.format(Locale.getDefault(), "%+d", money), TextureManager.getInstance().getTexture(TextureManager.MONEY));

		if(wheat.size() > 0) {
			Collections.sort(wheat, (w1, w2) -> (int) Math.signum(w1.getLife() - w2.getLife()));
			wheat.get(0).decreaseLife();
			if(wheat.get(0).getLife() == 0)
				wheat.get(0).addAction(sequence(scaleTo(0, 0, Settings.duration, Interpolation.swingIn), run(() -> stats.figures.remove(wheat.get(0)))));
		}
	}

	@Override public String getAbilityDescription() {
		return String.format(Locale.getDefault(), "Daily produces %d$ money for every surrounding wheat and pond or well." +
				"\nDecreases life of the weakest surrounding wheat every day.", moneyProduction[getLevel() - 1]);
	}

	@Override public int getCapacity() {
		return capacity[getLevel() - 1];
	}

	@Override public int getCost() {
		return cost[getLevel() - 1];
	}

	@Override public int getLove() {
		return -7;
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
