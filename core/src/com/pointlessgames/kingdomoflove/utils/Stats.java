package com.pointlessgames.kingdomoflove.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.pointlessgames.kingdomoflove.models.Ability;
import com.pointlessgames.kingdomoflove.models.figures.Figure;
import com.pointlessgames.kingdomoflove.models.figures.Mill;
import com.pointlessgames.kingdomoflove.models.figures.Plant;
import com.pointlessgames.kingdomoflove.models.figures.Road;
import com.pointlessgames.kingdomoflove.models.figures.Sawmill;
import com.pointlessgames.kingdomoflove.models.figures.Structure;

import java.util.ArrayList;

public class Stats {

	public int day = 1;
	public int money = 150;
	public float love = 50;
	public ArrayList<Figure> figures;

	public Vector2 mapOffset;

	public Stats() {
		this.figures = new ArrayList<>();
		this.mapOffset = new Vector2();
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
	}

	public int getMoneyProduction() {
		int money = 0;
		for(Figure f : figures) {
			Ability ability = f.getAbility(this);
			if(ability.getProductionType() == Ability.ProductionType.MONEY)
				money += ability.getAmount();
		}
		return money;
	}

	public float getLoveProduction() {
		float love = 0;
		for(Figure f : figures) {
			if(!(f instanceof Road) || !((Road) f).checked) {
				Ability ability = f.getAbility(this);
				if(ability.getProductionType() == Ability.ProductionType.LOVE)
					love += ability.getAmount();
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