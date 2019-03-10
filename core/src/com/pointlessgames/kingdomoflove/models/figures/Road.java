package com.pointlessgames.kingdomoflove.models.figures;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pointlessgames.kingdomoflove.models.Ability;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.TextureManager;

import java.util.Locale;
import java.util.Set;

import static com.pointlessgames.kingdomoflove.utils.Settings.HEIGHT;
import static com.pointlessgames.kingdomoflove.utils.Settings.WIDTH;
import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class Road extends Structure {

	private float love = 0.5f;
	private int roadY, roadX;

	public Road() {
		super(null);
		refreshSize();
		setPos();

		texture = TextureManager.getInstance().road[roadX = 0][roadY = 0];
	}

	@Override public void refreshSize() {
		height = tileSize;
		width = height;
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

		texture = TextureManager.getInstance().road[roadX][roadY];
	}

	@Override protected void drawTexture(SpriteBatch sP, float tileX, float tileY, float alpha) {
		sP.begin();
		Color color = sP.getColor();
		sP.setColor(color.cpy().mul(1, 1, 1, alpha));
		sP.draw(TextureManager.getInstance().road[roadX][roadY], tileX + getX(), tileY + getY(), width * getScaleX(), height * getScaleY());
		sP.setColor(color);
		sP.end();
	}

	@Override public int getCapacity() {
		return 0;
	}

	private float getLoveProduction(Stats stats) {
		Structure[][] map = new Structure[WIDTH][HEIGHT];
		for(Figure f : stats.figures)
			if(f instanceof Structure && ((Structure) f).hasRoad()) {
				map[f.getMapX()][f.getMapY()] = (Structure) f;
				((Structure) f).checked = false;
			}
		checked = true;
		Set<Structure> structures = stats.getConnectedFigures(map, getMapX(), getMapY(), false);
		checked = false;
		int amount = 0;
		for(Structure s : structures)
			if(!(s instanceof Road)) amount++;
		if(amount <= 1) return 0;
		return this.love * amount;
	}

	@Override public void triggerAbility(Stats stats) {
		if(!checked) {
			float love = getLoveProduction(stats);
			stats.love += love;

			if(love > 0)
				resetAbilityTip(String.format(Locale.getDefault(), "%+.1f", love), TextureManager.getInstance().love);
		}
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

	@Override protected void setPos() {
		setX((tileSize - width * getScaleX()) / 2);
		setY((tileSize - height * getScaleY()) / 2);
	}
}
