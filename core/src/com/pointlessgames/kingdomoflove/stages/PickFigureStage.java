package com.pointlessgames.kingdomoflove.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.pointlessgames.kingdomoflove.models.figures.Conifer;
import com.pointlessgames.kingdomoflove.models.figures.Figure;
import com.pointlessgames.kingdomoflove.models.figures.Granary;
import com.pointlessgames.kingdomoflove.models.figures.House;
import com.pointlessgames.kingdomoflove.models.figures.Library;
import com.pointlessgames.kingdomoflove.models.figures.Mill;
import com.pointlessgames.kingdomoflove.models.figures.Monument;
import com.pointlessgames.kingdomoflove.models.figures.Plant;
import com.pointlessgames.kingdomoflove.models.figures.Pond;
import com.pointlessgames.kingdomoflove.models.figures.Road;
import com.pointlessgames.kingdomoflove.models.figures.Sawmill;
import com.pointlessgames.kingdomoflove.models.figures.Structure;
import com.pointlessgames.kingdomoflove.models.figures.Tree;
import com.pointlessgames.kingdomoflove.models.figures.Well;
import com.pointlessgames.kingdomoflove.models.figures.Wheat;
import com.pointlessgames.kingdomoflove.renderers.CustomShapeRenderer;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.Settings;
import com.pointlessgames.kingdomoflove.utils.SoundManager;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.TextureManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import static com.pointlessgames.kingdomoflove.screens.StartScreen.font;
import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;

public class PickFigureStage extends GestureStage {

	private final float bottomBarHeight = 650 * ratio;
	private final float tileSize = 400 * ratio;
	private final float tileSpace = 50 * ratio;
	private float offsetX = tileSpace;
	private boolean hiding;
	private float bottomBarY;
	private float time;

	private CustomShapeRenderer sR;
	private SpriteBatch sP;
	private Stats stats;

	private Runnable onHideListener;
	private ClickListener clickListener;
	private ArrayList<Figure> figures;

	private Class[] categories = new Class[] {Structure.class, Plant.class};
	private int selectedCategory;
	private int itemsInCategory;

	private ArrayList<Actor> allTiles;
	private ArrayList<Actor> categoriesTabs;

	private Actor flingActor;

	public PickFigureStage(SpriteBatch sP, CustomShapeRenderer sR, Stats stats) {
		this.sP = sP;
		this.sR = sR;
		this.stats = stats;

		figures = new ArrayList<>();
		figures.add(new House());
		figures.add(new Sawmill());
		figures.add(new Tree());
		figures.add(new Well());
		figures.add(new Mill());
		figures.add(new Wheat());
		figures.add(new Monument());
		figures.add(new Pond());
		figures.add(new Road());
		figures.add(new Granary());
		figures.add(new Conifer());
		figures.add(new Library());

		Collections.sort(figures, (f1, f2) -> f1.getCost() - f2.getCost());

		setAllTiles();
		setSelectedCategory(selectedCategory = 0);

		setCategoriesTabs();
	}

	private void drawBottomBar() {
		sR.begin(ShapeRenderer.ShapeType.Filled);
		sR.setColor(Colors.barColor);
		sR.rect(0, bottomBarY, Gdx.graphics.getWidth(), bottomBarHeight);
		sR.end();
	}

	private void setCategoriesTabs() {
		categoriesTabs = new ArrayList<>();
		font.getData().setScale(0.4f);
		float x = tileSpace;
		float y = tileSize + 2 * tileSpace;
		float height = bottomBarHeight - y - tileSpace;
		float width = new GlyphLayout(font, "All").width + tileSpace * 2;

		Actor a = new Actor();
		a.setName("All");
		a.setWidth(width);
		a.setHeight(height);
		a.setX(x);
		a.setY(y);
		categoriesTabs.add(a);

		x += width + tileSpace;

		for(Class category : categories) {
			String name = category.getSimpleName();
			width = new GlyphLayout(font, name).width + tileSpace * 2;
			Actor a2 = new Actor();
			a2.setName(name);
			a2.setWidth(width);
			a2.setHeight(height);
			a2.setX(x);
			a2.setY(y);
			categoriesTabs.add(a2);

			x += width + tileSpace;
		}
	}

	private void drawCategoriesTabs() {
		for(int i = 0; i < categoriesTabs.size(); i++) {
			Actor a = categoriesTabs.get(i);
			font.getData().setScale(0.4f);
			font.setColor(selectedCategory == i ? Colors.tileColor : Colors.textColor);

			sR.begin(ShapeRenderer.ShapeType.Filled);
			sR.setColor(selectedCategory == i ? Colors.textColor : Colors.tileColor);
			sR.roundedRect(a.getX(), a.getY() + bottomBarY, a.getWidth(), a.getHeight(), 25 * ratio);
			sR.end();

			sP.begin();
			font.draw(sP, a.getName(), a.getX(), a.getY() + bottomBarY + a.getHeight() / 1.5f, a.getWidth(), Align.center, false);
			sP.end();
		}
	}

	private void setSelectedCategory(int category) {
		float x = 0;
		itemsInCategory = 0;
		for(Actor a : allTiles) {
			if(category == 0 || categories[category - 1].isInstance(a.getUserObject())) {
				a.setVisible(true);
				a.setX(x);
				a.setY(tileSpace);
				a.setWidth(tileSize);
				a.setHeight(tileSize);
				x += tileSize + tileSpace;
				itemsInCategory++;
			} else a.setVisible(false);
		}
	}

	private void setAllTiles() {
		allTiles = new ArrayList<>();
		for(Figure f : figures) {
			Actor a = new Actor();
			a.setUserObject(f);
			allTiles.add(a);
		}
	}

	private void drawFigureCards() {
		for(Actor a : allTiles) {
			if(a.isVisible() && a.getX() + offsetX > -a.getWidth() && a.getX() + offsetX < Gdx.graphics.getWidth()) {
				Figure figure = ((Figure) a.getUserObject());
				float x = a.getX() + offsetX;
				float y = a.getY() + bottomBarY;
				sR.begin(ShapeRenderer.ShapeType.Filled);
				sR.setColor(stats.money >= figure.getCost() ? Colors.tileColor : Colors.tile2Color);
				sR.roundedRect(x, y, tileSize, tileSize, 25 * ratio);
				sR.end();

				sP.begin();

				//Title
				font.getData().setScale(0.3f);
				font.setColor(Colors.textColor);
				font.draw(sP, figure.getName(), x, y + tileSize - tileSpace, tileSize, Align.center, false);

				//Texture
				sP.draw(figure.getTexture(), x + (tileSize - tileSize * 0.55f) / 2, y + (tileSize - tileSize * 0.55f) / 2, tileSize * 0.55f, tileSize * 0.55f);

				//Money
				sP.draw(TextureManager.getInstance().money, x + tileSpace / 2, y + tileSpace / 2, 75 * ratio, 75 * ratio);
				font.draw(sP, String.format(Locale.getDefault(), "%d", figure.getCost()), x + tileSpace / 2 + 75 * ratio, y + tileSpace / 2 + 50 * ratio, 100 * ratio, Align.left, false);

				//Love
				sP.draw(TextureManager.getInstance().love, x + tileSize - tileSpace / 2 - 75 * ratio, y + tileSpace / 2, 75 * ratio, 75 * ratio);
				font.draw(sP, String.format(Locale.getDefault(), "%+d%%", figure.getLove()), x + tileSize - tileSpace / 2 - 175 * ratio, y + tileSpace / 2 + 50 * ratio, 100 * ratio, Align.right, false);

				sP.end();
			}
		}
	}

	@Override public void draw() {
		drawBottomBar();
		drawCategoriesTabs();
		drawFigureCards();
	}

	@Override public void act(float delta) {
		if(hiding && time > 0 || !hiding && time < Settings.duration) {
			time += hiding ? -delta : delta;
			float percent = hiding ? Interpolation.exp5In.apply(time / Settings.duration) : Interpolation.exp5Out.apply(time / Settings.duration);
			bottomBarY = MathUtils.lerp(-bottomBarHeight, 0, percent);
		} else {
			bottomBarY = 0;
			if(hiding && onHideListener != null) onHideListener.run();
		}

		if(flingActor != null) {
			offsetX = MathUtils.clamp(flingActor.getX(), -((itemsInCategory - 2) * (tileSize + tileSpace) + tileSpace), tileSpace);
			flingActor.act(delta);
		}
	}

	@Override public boolean tap(float x, float y, int count, int button) {
		if(Gdx.graphics.getHeight() - y < bottomBarHeight) {
			for(Actor a : allTiles)
				if(a.hit(x - offsetX - a.getX(), Gdx.graphics.getHeight() - y - a.getY(), true) != null) {
					clickListener.onFigureClick((Figure) a.getUserObject());
					return true;
				}
			for(int i = 0; i < categoriesTabs.size(); i++) {
				Actor a = categoriesTabs.get(i);
				if(a.hit(x - a.getX(), Gdx.graphics.getHeight() - y - a.getY(), true) != null) {
					offsetX = tileSpace;
					setSelectedCategory(selectedCategory = i);
					if(Settings.soundsOn)
						SoundManager.getInstance().select.play(0.5f);
					return true;
				}
			}
		} else clickListener.onCancelClick();
		return true;
	}

	@Override public boolean pan(float x, float y, float deltaX, float deltaY) {
		offsetX = MathUtils.clamp(offsetX + deltaX, -((itemsInCategory - 2) * (tileSize + tileSpace) + tileSpace), tileSpace);
		return true;
	}

	@Override public boolean fling(float velocityX, float velocityY, int button) {
		float deltaX = Settings.duration * velocityX;
		if(flingActor == null)
			flingActor = new Actor();
		else flingActor.clearActions();
		flingActor.setPosition(offsetX, 0);
		flingActor.addAction(Actions.sequence(
				Actions.moveBy(deltaX, 0, Settings.duration, Interpolation.fastSlow),
				Actions.run(() -> flingActor = null)));
		return true;
	}

	@Override public boolean keyDown(int keyCode) {
		if(keyCode == Input.Keys.BACK) {
			clickListener.onCancelClick();
			return true;
		} else return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return true;
	}

	public void hide(Runnable onHideListener) {
		this.onHideListener = onHideListener;
		hiding = true;
	}

	public PickFigureStage setClickListener(ClickListener clickListener) {
		this.clickListener = clickListener;
		return this;
	}

	public interface ClickListener {
		void onCancelClick();
		void onFigureClick(Figure f);
	}
}
