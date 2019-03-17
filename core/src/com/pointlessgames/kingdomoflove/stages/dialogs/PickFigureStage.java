package com.pointlessgames.kingdomoflove.stages.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.pointlessgames.kingdomoflove.actors.Button;
import com.pointlessgames.kingdomoflove.models.figures.Church;
import com.pointlessgames.kingdomoflove.models.figures.Conifer;
import com.pointlessgames.kingdomoflove.models.figures.Figure;
import com.pointlessgames.kingdomoflove.models.figures.Granary;
import com.pointlessgames.kingdomoflove.models.figures.House;
import com.pointlessgames.kingdomoflove.models.figures.Library;
import com.pointlessgames.kingdomoflove.models.figures.Mill;
import com.pointlessgames.kingdomoflove.models.figures.Plant;
import com.pointlessgames.kingdomoflove.models.figures.Pond;
import com.pointlessgames.kingdomoflove.models.figures.Road;
import com.pointlessgames.kingdomoflove.models.figures.Sawmill;
import com.pointlessgames.kingdomoflove.models.figures.Structure;
import com.pointlessgames.kingdomoflove.models.figures.Tree;
import com.pointlessgames.kingdomoflove.models.figures.Well;
import com.pointlessgames.kingdomoflove.models.figures.Wheat;
import com.pointlessgames.kingdomoflove.stages.BaseStage;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.Settings;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.managers.TextureManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import static com.pointlessgames.kingdomoflove.screens.StartScreen.font;
import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;

public class PickFigureStage extends BaseStage {

	private final float bottomBarHeight = 650 * ratio;
	private final float tileSize = 400 * ratio;
	private final float tileSpace = 50 * ratio;
	private float offsetX = tileSpace;
	private float bottomBarY;
	private float time;
	private boolean hiding;

	private SpriteBatch sP;
	private Stats stats;

	private Runnable onHideListener;
	private ClickListener clickListener;
	private ArrayList<Figure> figures;

	private Class[] categories = new Class[] {Structure.class, Plant.class};
	private int itemsInCategory;

	private ArrayList<Button> allTiles;
	private ArrayList<Button> categoriesTabs;

	private Actor flingActor;

	public PickFigureStage(SpriteBatch sP, Stats stats) {
		this.sP = sP;
		this.stats = stats;

		touchInterruption = false;

		figures = new ArrayList<>();
		figures.add(new House());
		figures.add(new Sawmill());
		figures.add(new Tree());
		figures.add(new Well());
		figures.add(new Mill());
		figures.add(new Wheat());
		figures.add(new Pond());
		figures.add(new Road());
		figures.add(new Granary());
		figures.add(new Conifer());
		figures.add(new Library());
		figures.add(new Church());

		Collections.sort(figures, (f1, f2) -> f1.getCost() - f2.getCost());

		setAllTiles();
		setCategoriesTabs();

		setSelectedCategory(0);
	}

	private void drawBottomBar() {
		sP.setColor(Colors.barColor);
		TextureManager.getInstance().filledRect.draw(sP, 0, bottomBarY, Gdx.graphics.getWidth(), bottomBarHeight);
	}

	private void setCategoriesTabs() {
		categoriesTabs = new ArrayList<>();
		font.getData().setScale(0.4f);
		float x = tileSpace;
		float y = tileSize + 2 * tileSpace;
		float height = bottomBarHeight - y - tileSpace;
		float width = new GlyphLayout(font, "All").width + tileSpace * 2;

		Button b = new Button(Colors.buttonColor, Colors.tileColor);
		b.setName("All");
		b.setBounds(x, y, width, height);
		categoriesTabs.add(b);

		x += width + tileSpace;

		for(Class category : categories) {
			String name = category.getSimpleName();
			width = new GlyphLayout(font, name).width + tileSpace * 2;
			Button b2 = new Button(Colors.textColor, Colors.tileColor);
			b2.setName(name);
			b2.setBounds(x, y, width, height);
			categoriesTabs.add(b2);

			x += width + tileSpace;
		}
	}

	private void drawCategoriesTabs() {
		for(Button a : categoriesTabs) {
			sP.setColor(a.getColor());
			TextureManager.getInstance().rect.draw(sP, a.getX(), a.getY() + bottomBarY, a.getWidth(), a.getHeight());

			font.getData().setScale(0.4f * ratio);
			font.setColor(a.isSelected() ? Colors.textColor : Colors.tileColor);
			font.draw(sP, a.getName(), a.getX(), a.getY() + bottomBarY + 0.5f * (a.getHeight() + font.getCapHeight()), a.getWidth(), Align.center, false);
		}
	}

	private void setSelectedCategory(int category) {
		float x = 0;
		itemsInCategory = 0;
		for(Button a : allTiles) {
			if(category == 0 || categories[category - 1].isInstance(a.getUserObject())) {
				a.setVisible(true);
				a.setBounds(x, tileSpace, tileSize, tileSize);
				x += tileSize + tileSpace;
				itemsInCategory++;
			} else a.setVisible(false);
		}
		for(int i = categoriesTabs.size() - 1; i >= 0; i--) {
			categoriesTabs.get(i).setSelected(i == category);
			categoriesTabs.get(i).touchUp();
		}
	}

	private void setAllTiles() {
		allTiles = new ArrayList<>();
		for(Figure f : figures) {
			Button b = new Button(Colors.tileColor, Colors.tile2Color, Colors.tile2Color);
			b.setTouchable(stats.money >= f.getCost() ? Touchable.enabled : Touchable.disabled);
			b.setUserObject(f);
			allTiles.add(b);
		}
	}

	private void drawFigureCards() {
		for(Button b : allTiles) {
			if(b.isVisible() && b.getX() + offsetX > -b.getWidth() && b.getX() + offsetX < Gdx.graphics.getWidth()) {
				Figure figure = ((Figure) b.getUserObject());
				float x = b.getX() + offsetX;
				float y = b.getY() + bottomBarY;

				//Tile
				sP.setColor(b.getColor());
				TextureManager.getInstance().rect.draw(sP, x, y, tileSize, tileSize);

				sP.setColor(Color.WHITE);

				//Title
				font.getData().setScale(0.3f);
				font.setColor(Colors.textColor);
				font.draw(sP, figure.getName(), x, y + tileSize - tileSpace, tileSize, Align.center, false);

				//Texture
				sP.draw(figure.getTexture(), x + (tileSize - tileSize * 0.55f) / 2, y + (tileSize - tileSize * 0.55f) / 2, tileSize * 0.55f, tileSize * 0.55f);

				//Money
				sP.draw(TextureManager.getInstance().getTexture(TextureManager.MONEY), x + tileSpace / 2, y + tileSpace / 2, 75 * ratio, 75 * ratio);
				font.draw(sP, String.format(Locale.getDefault(), "%d", figure.getCost()), x + tileSpace / 2 + 75 * ratio, y + tileSpace / 2 + 50 * ratio, 100 * ratio, Align.left, false);

				//Love
				sP.draw(TextureManager.getInstance().getTexture(TextureManager.LOVE), x + tileSize - tileSpace / 2 - 75 * ratio, y + tileSpace / 2, 75 * ratio, 75 * ratio);
				font.draw(sP, String.format(Locale.getDefault(), "%+d%%", figure.getLove()), x + tileSize - tileSpace / 2 - 175 * ratio, y + tileSpace / 2 + 50 * ratio, 100 * ratio, Align.right, false);
			}
		}
	}

	@Override public void draw() {
		sP.begin();

		drawBottomBar();
		drawCategoriesTabs();
		drawFigureCards();

		sP.setColor(Color.WHITE);
		sP.end();
	}

	@Override public void act(float delta) {
		for(Button b : categoriesTabs)
			b.act(delta);
		for(Button b : allTiles)
			b.act(delta);

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
		for(Button b : categoriesTabs)
			b.touchUp();
		for(Button b : allTiles)
			b.touchUp();
		if(Gdx.graphics.getHeight() - y < bottomBarHeight) {
			for(Button b : allTiles)
				if(b.hit(x - offsetX - b.getX(), Gdx.graphics.getHeight() - y - b.getY(), true) != null) {
					clickListener.onFigureClick((Figure) b.getUserObject());
					return true;
				}
			for(int i = 0; i < categoriesTabs.size(); i++) {
				Button b = categoriesTabs.get(i);
				if(b.hit(x - b.getX(), Gdx.graphics.getHeight() - y - b.getY(), true) != null) {
					offsetX = tileSpace;
					setSelectedCategory(i);
					return true;
				}
			}
		} else clickListener.onCancelClick();
		return true;
	}

	@Override public boolean pan(float x, float y, float deltaX, float deltaY) {
		for(Button b : categoriesTabs)
			b.touchUp();
		for(Button b : allTiles)
			b.touchUp();

		offsetX = MathUtils.clamp(offsetX + deltaX, -((itemsInCategory - 2) * (tileSize + tileSpace) + tileSpace), tileSpace);
		return true;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		for(Button b : categoriesTabs)
			b.touchUp();
		for(Button b : allTiles)
			b.touchUp();
		return super.pinch(initialPointer1, initialPointer2, pointer1, pointer2);
	}

	@Override public boolean touchDown(float x, float y, int pointer, int button) {
		for(Button b : categoriesTabs)
			b.touchDown(x, Gdx.graphics.getHeight() - y);
		for(Button b : allTiles)
			b.touchDown(x - offsetX, Gdx.graphics.getHeight() - y);
		return super.touchDown(x, y, pointer, button);
	}

	@Override public boolean touchUp(float x, float y, int pointer, int button) {
		for(Button b : categoriesTabs)
			b.touchUp();
		for(Button b : allTiles)
			b.touchUp();
		return super.touchUp(x, y, pointer, button);
	}

	@Override public boolean fling(float velocityX, float velocityY, int button) {
		for(Button b : categoriesTabs)
			b.touchUp();
		for(Button b : allTiles)
			b.touchUp();

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
		if(keyCode == Input.Keys.BACK || keyCode == Input.Keys.ESCAPE) {
			clickListener.onCancelClick();
			return true;
		} else return false;
	}

	@Override public boolean longPress(float x, float y) {
		if(Gdx.graphics.getHeight() - y < bottomBarHeight) {
			for(Button b : allTiles)
				if(b.hit(x - offsetX - b.getX(), Gdx.graphics.getHeight() - y - b.getY(), false) != null) {
					clickListener.onInfoClick((Figure)b.getUserObject());
					b.touchUp();
					return true;
				}
		}
		return false;
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
		void onInfoClick(Figure f);
	}
}
