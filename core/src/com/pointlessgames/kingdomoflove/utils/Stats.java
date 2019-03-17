package com.pointlessgames.kingdomoflove.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.pointlessgames.kingdomoflove.models.Ability;
import com.pointlessgames.kingdomoflove.models.figures.Figure;
import com.pointlessgames.kingdomoflove.models.figures.Plant;
import com.pointlessgames.kingdomoflove.models.figures.Road;
import com.pointlessgames.kingdomoflove.models.figures.Structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.pointlessgames.kingdomoflove.utils.Settings.HEIGHT;
import static com.pointlessgames.kingdomoflove.utils.Settings.WIDTH;

public class Stats {

	public int day = 1;
	public int money = 50;
	public float love = 50;
	public ArrayList<Figure> figures;
	public Set<Figure> selectedFigures;
	public Figure selectedFigure;

	public Vector2 mapOffset;

	public Stats() {
		this.figures = new ArrayList<>();
		this.mapOffset = new Vector2();
	}

	public void sortFigures() {
		Collections.sort(figures, (f2, f1) -> {
			if(f1.getMapY() > f2.getMapY()) return -1;
			else if(f1.getMapY() == f2.getMapY())
				return Integer.compare(f1.getMapX(), f2.getMapX());
			return 1;
		});
	}

	public void setCurrentFigure(Figure figure) {
		if(figure == null) {
			selectedFigures = null;
			selectedFigure = null;
			return;
		}
		Structure[][] map = new Structure[WIDTH][HEIGHT];
		for(Figure f : figures)
			if(f instanceof Structure && ((Structure) f).hasRoad()) {
				map[f.getMapX()][f.getMapY()] = (Structure) f;
				((Structure) f).checked = false;
			}
		selectedFigures = new HashSet<>();
		selectedFigures.add(selectedFigure = figure);
		if(figures.contains(figure) && figure instanceof Structure && ((Structure) figure).hasRoad()) {
			((Structure) figure).checked = true;
			selectedFigures.addAll(getConnectedFigures(map, figure.getMapX(), figure.getMapY(), !(figure instanceof Road)));
		}
	}

	public Set<Structure> getConnectedFigures(Structure[][] map, int x, int y, boolean onlyRoad) {
		Set<Structure> structures = new HashSet<>();
		structures.add(map[x][y]);
		if(x > 0 && map[x - 1][y] != null && !map[x - 1][y].checked) {
			boolean isRoad = map[x - 1][y] instanceof Road;
			if(!onlyRoad || isRoad) {
				map[x - 1][y].checked = true;
				structures.add(map[x - 1][y]);
				structures.addAll(getConnectedFigures(map, x - 1, y, !isRoad));
			}
		} if(x < WIDTH - 1 && map[x + 1][y] != null && !map[x + 1][y].checked) {
			boolean isRoad = map[x + 1][y] instanceof Road;
			if(!onlyRoad || isRoad) {
				map[x + 1][y].checked = true;
				structures.add(map[x + 1][y]);
				structures.addAll(getConnectedFigures(map, x + 1, y, !isRoad));
			}
		} if(y > 0 && map[x][y - 1] != null && !map[x][y - 1].checked) {
			boolean isRoad = map[x][y - 1] instanceof Road;
			if(!onlyRoad || isRoad) {
				map[x][y - 1].checked = true;
				structures.add(map[x][y - 1]);
				structures.addAll(getConnectedFigures(map, x, y - 1, !isRoad));
			}
		} if(y < HEIGHT - 1 && map[x][y + 1] != null && !map[x][y + 1].checked) {
			boolean isRoad = map[x][y + 1] instanceof Road;
			if(!onlyRoad || isRoad) {
				map[x][y + 1].checked = true;
				structures.add(map[x][y + 1]);
				structures.addAll(getConnectedFigures(map, x, y + 1, !isRoad));
			}
		}
		return structures;
	}

	public int getPopulation() {
		int population = 0;
		for(Figure f : figures)
			if(f instanceof Structure)
				population += ((Structure) f).getCapacity();
		return population;
	}

	public void nextDay() {
		love -= getPopulation();
		love = MathUtils.clamp(love, 0, 100);

		day++;
		for(int i = figures.size() - 1; i >= 0; i--)
			figures.get(i).triggerAbility(this);
	}

	public int getMoneyProduction() {
		int money = 0;
		for(Figure f : figures) {
			Ability ability = f.getAbility(this);
			if((ability.getProductionType().number & Ability.ProductionType.MONEY.number) != 0)
				money += ability.getAmount(Ability.ProductionType.MONEY);
		}
		return money;
	}

	public float getLoveProduction() {
		float love = 0;
		for(Figure f : figures) {
			if(!(f instanceof Road) || !((Road) f).checked) {
				Ability ability = f.getAbility(this);
				if((ability.getProductionType().number & Ability.ProductionType.LOVE.number) != 0)
					love += ability.getAmount(Ability.ProductionType.LOVE);
			}
		}
		love -= getPopulation();
		return love;
	}

	public void save() {
		Preferences prefs = Gdx.app.getPreferences("Stats");
		prefs.putBoolean("saved", true);
		prefs.putInteger("day", day);
		prefs.putInteger("money", money);
		prefs.putFloat("love", love);
		prefs.putInteger("f_size", figures.size());
		for(int i = 0; i < figures.size(); i++) {
			prefs.putString("f_name_" + i, figures.get(i).getClass().getName());
			prefs.putInteger("f_level_" + i, figures.get(i).getLevel());
			prefs.putInteger("f_map_x_" + i, figures.get(i).getMapX());
			prefs.putInteger("f_map_y_" + i, figures.get(i).getMapY());
			if(figures.get(i) instanceof Plant)
				prefs.putInteger("f_life_" + i, (int) (((Plant) figures.get(i)).getLife() * ((Plant) figures.get(i)).getMaxLife()));
		}
		prefs.flush();
	}

	public void load() {
		Preferences prefs = Gdx.app.getPreferences("Stats");
		if(prefs.getBoolean("saved")) {
			day = prefs.getInteger("day", day);
			money = prefs.getInteger("money", money);
			love = prefs.getFloat("love", love);

			int size = prefs.getInteger("f_size", 0);
			for(int i = 0; i < size; i++) {
				try {
					Figure f = (Figure) Class.forName(prefs.getString("f_name_" + i, "com.pointlessgames.kingdomoflove.Monument")).newInstance();
					f.setMapPos(prefs.getInteger("f_map_x_" + i, 0), prefs.getInteger("f_map_y_" + i, 0));
					f.setLevel(prefs.getInteger("f_level_" + i, 1));
					if(f instanceof Plant)
						((Plant) f).setLife(prefs.getInteger("f_life_" + i, ((Plant) f).getMaxLife()));
					figures.add(f);
				} catch(ClassCastException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}

			for(Figure f : figures) if(f instanceof Road) f.orientInSpace(this);
		}
	}

	public boolean isTileAvailable(int x, int y) {
		for(Figure f : figures)
			if(Math.pow(f.getMapX() - x, 2) + Math.pow(f.getMapY() - y, 2) <= 2)
				return true;
		return false;
	}
}