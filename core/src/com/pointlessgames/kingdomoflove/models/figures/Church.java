package com.pointlessgames.kingdomoflove.models.figures;

import com.pointlessgames.kingdomoflove.models.Ability;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.managers.TextureManager;

import java.util.Locale;

import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class Church extends Structure {

	private int[] cost = {800, 1500, 2200, 2800, 3500, 4100, 4800, 5300};
	private int[] capacity = {5, 8, 11, 14, 17, 20, 23, 26};
	private float[] loveProduction = {2, 3, 4, 5, 6, 7, 8, 9};

	public Church() {
		super(TextureManager.getInstance().getTexture(TextureManager.CHURCH));
		refreshSize();
		setPos();
	}

	@Override public void refreshSize() {
		height = tileSize * 1.2f;
		width = height * texture.getTexture().getWidth() / texture.getTexture().getHeight();
	}

	private float getLoveProduction(Stats stats) {
		float love = 0;
		for(Figure f : stats.figures) {
			if(f instanceof Structure && ((Structure) f).hasRoad() && !(f instanceof Road))
				love += loveProduction[getLevel() - 1] / Math.sqrt(Math.pow(f.getMapX() - getMapX(), 2) + Math.pow(f.getMapY() - getMapX(), 2));
		}

		return love;
	}

	@Override public void triggerAbility(Stats stats) {
		float love = getLoveProduction(stats);
		stats.love += love;

		if(love > 0)
			resetAbilityTip(String.format(Locale.getDefault(), "%+.1f", love), TextureManager.getInstance().getTexture(TextureManager.LOVE));
	}

	@Override public String getAbilityDescription() {
		return String.format(Locale.getDefault(), "Daily increases love by at most %.1f%% for every structure. The bigger the distance is, the lower the love increase is.", loveProduction[getLevel() - 1]);
	}

	@Override public int getCapacity() {
		return capacity[getLevel() - 1];
	}

	@Override public int getCost() {
		return cost[getLevel() - 1];
	}

	@Override public int getLove() {
		return 10;
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
