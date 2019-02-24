package com.pointlessgames.kingdomoflove.models.figures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pointlessgames.kingdomoflove.models.Ability;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.TextureManager;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static com.pointlessgames.kingdomoflove.utils.Settings.HEIGHT;
import static com.pointlessgames.kingdomoflove.utils.Settings.WIDTH;
import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class Road extends Structure {

	public boolean checked;
	private float love = 0.2f;
	private int roadY, roadX;

	public Road() {
		super(new Texture("figures/road.png"));
		height = tileSize;
		width = height;
		setPos();

		texture = TextureManager.road[roadX = 0][roadY = 0];
	}

	@Override public void orientInSpace(Stats stats) {
		int direction = 0;
		int LEFT = 1;
		int TOP = 1 << 1;
		int RIGHT = 1 << 2;
		int BOTTOM = 1 << 3;

		for(Figure f : stats.figures) {
			if(f instanceof Structure && ((Structure) f).hasRoad() || f instanceof Road) {
				if(f.getMapX() == getMapX() - 1 && f.getMapY() == getMapY()) direction |= LEFT;
				if(f.getMapX() == getMapX() + 1 && f.getMapY() == getMapY()) direction |= RIGHT;
				if(f.getMapX() == getMapX() && f.getMapY() == getMapY() - 1) direction |= TOP;
				if(f.getMapX() == getMapX() && f.getMapY() == getMapY() + 1) direction |= BOTTOM;
			}
		}

		if((direction & (LEFT | RIGHT | TOP | BOTTOM)) == (LEFT | RIGHT | TOP | BOTTOM)) {
			roadY = 0;
			roadX = 0;
		} else if((direction & (LEFT | RIGHT | TOP)) == (LEFT | RIGHT | TOP)) {
			roadY = 1;
			roadX = 0;
		} else if((direction & (LEFT | TOP | BOTTOM)) == (LEFT | TOP | BOTTOM)) {
			roadY = 1;
			roadX = 1;
		} else if((direction & (RIGHT | TOP | BOTTOM)) == (RIGHT | TOP | BOTTOM)) {
			roadY = 0;
			roadX = 1;
		} else if((direction & (LEFT | BOTTOM | RIGHT)) == (LEFT | BOTTOM | RIGHT)) {
			roadY = 3;
			roadX = 0;
		} else if((direction & (LEFT | TOP)) == (LEFT | TOP)) {
			roadY = 3;
			roadX = 1;
		} else if((direction & (RIGHT | TOP)) == (RIGHT | TOP)) {
			roadY = 2;
			roadX = 1;
		} else if((direction & (RIGHT | BOTTOM)) == (RIGHT | BOTTOM)) {
			roadY = 0;
			roadX = 2;
		} else if((direction & (LEFT | BOTTOM)) == (LEFT | BOTTOM)) {
			roadY = 1;
			roadX = 2;
		} else if((direction & (LEFT | RIGHT)) == (LEFT | RIGHT)) {
			roadY = 2;
			roadX = 0;
		} else if((direction & (TOP | BOTTOM)) == (TOP | BOTTOM)) {
			roadY = 3;
			roadX = 2;
		} else if(direction == LEFT) {
			roadY = 1;
			roadX = 3;
		} else if(direction == RIGHT) {
			roadY = 0;
			roadX = 3;
		} else if(direction == TOP) {
			roadY = 2;
			roadX = 3;
		} else if(direction == BOTTOM) {
			roadY = 2;
			roadX = 2;
		} else roadY = roadX = 3;

		texture = TextureManager.road[roadX][roadY];
	}

	@Override protected void drawTexture(SpriteBatch sP, float tileX, float tileY) {
		sP.begin();
		sP.draw(TextureManager.road[roadX][roadY], tileX + getX(), tileY + getY(), width * getScaleX(), height * getScaleY());
		sP.end();
	}

	@Override public int getCapacity() {
		return 0;
	}

	private float getLoveProduction(Stats stats) {
		Structure[][] map = new Structure[WIDTH][HEIGHT];
		for(Figure f : stats.figures)
			if(f instanceof Structure) {
				if(f instanceof Road) ((Road) f).checked = false;
				map[f.getMapX()][f.getMapY()] = (Structure) f;
			}
		Set<Structure> structures = checkForConnection(map, getMapX(), getMapY(), 0);
		if(structures.size() <= 1) return 0;
		return this.love * structures.size();
	}

	@Override public void triggerAbility(Stats stats) {
		if(!checked) {
			float love = getLoveProduction(stats);
			stats.love += love;

			if(love > 0)
				resetAbilityTip(String.format(Locale.getDefault(), "%+.1f", love), TextureManager.love);
		}
	}

	private Set<Structure> checkForConnection(Structure[][] map, int x, int y, int checkedDir) {
		Set<Structure> structures = new HashSet<>();
		if(checkedDir != 2 && x > 0 && map[x - 1][y] != null)
			if(map[x - 1][y].hasRoad()) structures.add(map[x - 1][y]);
			else if(map[x - 1][y] instanceof Road && !((Road) map[x - 1][y]).checked) {
				((Road) map[x - 1][y]).checked = true;
				structures.addAll(checkForConnection(map, x - 1, y, 1));
			}
		if(checkedDir != 1 && x < WIDTH - 1 && map[x + 1][y] != null)
			if(map[x + 1][y].hasRoad()) structures.add(map[x + 1][y]);
			else if(map[x + 1][y] instanceof Road && !((Road) map[x + 1][y]).checked) {
				((Road) map[x + 1][y]).checked = true;
				structures.addAll(checkForConnection(map, x + 1, y, 2));
			}
		if(checkedDir != 4 && y > 0 && map[x][y - 1] != null)
			if(map[x][y - 1].hasRoad()) structures.add(map[x][y - 1]);
			else if(map[x][y - 1] instanceof Road && !((Road) map[x][y - 1]).checked) {
				((Road) map[x][y - 1]).checked = true;
				structures.addAll(checkForConnection(map, x, y - 1, 3));
			}
		if(checkedDir != 3 && y < HEIGHT - 1 && map[x][y + 1] != null) {
			if(map[x][y + 1].hasRoad()) structures.add(map[x][y + 1]);
			else if(map[x][y + 1] instanceof Road && !((Road) map[x][y + 1]).checked) {
				((Road) map[x][y + 1]).checked = true;
				structures.addAll(checkForConnection(map, x, y + 1, 4));
			}
		}
		return structures;
	}

	@Override public String getAbilityDescription() {
		return String.format(Locale.getDefault(), "Daily increases love by %.1f%% for every connected structure.", love);
	}

	@Override public int getCost() {
		return 30;
	}

	@Override public int getLove() {
		return 5;
	}

	@Override public Ability getAbility(Stats stats) {
		return new Ability(Ability.ProductionType.LOVE, getLoveProduction(stats));
	}

	@Override public boolean isUpgradable() {
		return false;
	}

	@Override public boolean hasLevels() {
		return false;
	}

	@Override public boolean hasRoad() {
		return false;
	}

	@Override protected void setPos() {
		setX((tileSize - width * getScaleX()) / 2);
		setY((tileSize - height * getScaleY()) / 2);
	}
}
