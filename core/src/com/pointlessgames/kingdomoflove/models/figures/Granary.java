package com.pointlessgames.kingdomoflove.models.figures;

import com.badlogic.gdx.graphics.Texture;
import com.pointlessgames.kingdomoflove.models.Ability;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.TextureManager;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static com.pointlessgames.kingdomoflove.utils.Settings.HEIGHT;
import static com.pointlessgames.kingdomoflove.utils.Settings.WIDTH;
import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class Granary extends Structure {

	private int[] cost = {350, 650, 1050, 1350, 1650, 1950, 2400, 2750};
	private int[] capacity = {2, 3, 4, 4, 5, 5, 6, 6};
	private float[] loveProduction = {1, 1.5f, 2, 2.5f, 3, 3.5f, 4, 4.5f};

	public Granary() {
		super(new Texture("figures/granary.png"));
		height = tileSize * 1.2f;
		width = height * texture.getTexture().getWidth() / texture.getTexture().getHeight();
		setPos();
	}

	@Override public int getCapacity() {
		return capacity[getLevel() - 1];
	}

	@Override public void triggerAbility(Stats stats) {
		float love = getLoveProduction(stats);
		stats.love += love;

		if(love > 0)
			resetAbilityTip(String.format(Locale.getDefault(), "%+.1f", love), TextureManager.love);
	}

	private float getLoveProduction(Stats stats) {
		Set<Structure> structures = new HashSet<>();

		Structure[][] map = new Structure[WIDTH][HEIGHT];
		for(Figure f : stats.figures)
			if(f instanceof Structure)
				map[f.getMapX()][f.getMapY()] = (Structure) f;

		for(Figure f : stats.figures)
			if(f instanceof Sawmill || f instanceof Mill) {
				if(Math.pow(f.getMapX() - getMapX(), 2) + Math.pow(f.getMapY() - getMapY(), 2) <= 2)
					structures.add((Structure) f);
			} else if(f instanceof Road) {
				if(Math.pow(f.getMapX() - getMapX(), 2) + Math.pow(f.getMapY() - getMapY(), 2) < 2)
					structures.addAll(checkForConnection(map, f.getMapX(), f.getMapY(), 0));
			}

		return structures.size() * loveProduction[getLevel() - 1];
	}

	private Set<Structure> checkForConnection(Structure[][] map, int x, int y, int checkedDir) {
		Set<Structure> structures = new HashSet<>();
		if(checkedDir != 2 && x > 0 && map[x - 1][y] != null)
			if(map[x - 1][y] instanceof Mill || map[x - 1][y] instanceof Sawmill) structures.add(map[x - 1][y]);
			else if(map[x - 1][y] instanceof Road && !((Road) map[x - 1][y]).checked) {
				((Road) map[x - 1][y]).checked = true;
				structures.addAll(checkForConnection(map, x - 1, y, 1));
			}
		if(checkedDir != 1 && x < WIDTH - 1 && map[x + 1][y] != null)
			if(map[x + 1][y] instanceof Mill || map[x + 1][y] instanceof Sawmill) structures.add(map[x + 1][y]);
			else if(map[x + 1][y] instanceof Road && !((Road) map[x + 1][y]).checked ) {
				((Road) map[x + 1][y]).checked = true;
				structures.addAll(checkForConnection(map, x + 1, y, 2));
			}
		if(checkedDir != 4 && y > 0 && map[x][y - 1] != null)
			if(map[x][y - 1] instanceof Mill || map[x][y - 1] instanceof Sawmill) structures.add(map[x][y - 1]);
			else if(map[x][y - 1] instanceof Road && !((Road) map[x][y - 1]).checked) {
				((Road) map[x][y - 1]).checked = true;
				structures.addAll(checkForConnection(map, x, y - 1, 3));
			}
		if(checkedDir != 3 && y < HEIGHT - 1 && map[x][y + 1] != null) {
			if(map[x][y + 1] instanceof Mill || map[x][y + 1] instanceof Sawmill) structures.add(map[x][y + 1]);
			else if(map[x][y + 1] instanceof Road && !((Road) map[x][y + 1]).checked) {
				((Road) map[x][y + 1]).checked = true;
				structures.addAll(checkForConnection(map, x, y + 1, 4));
			}
		}
		return structures;
	}


	@Override public String getAbilityDescription() {
		return String.format(Locale.getDefault(), "Daily increases love by %.1f%% for every connected sawmill or mill.", loveProduction[getLevel() - 1]);
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
