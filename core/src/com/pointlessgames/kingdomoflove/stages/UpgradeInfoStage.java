package com.pointlessgames.kingdomoflove.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;
import com.pointlessgames.kingdomoflove.actors.Button;
import com.pointlessgames.kingdomoflove.models.figures.Figure;
import com.pointlessgames.kingdomoflove.models.figures.Structure;
import com.pointlessgames.kingdomoflove.renderers.CustomShapeRenderer;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.Settings;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.TextureManager;

import java.util.Locale;

import static com.pointlessgames.kingdomoflove.screens.StartScreen.font;
import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;

public class UpgradeInfoStage extends GestureStage {

	private Runnable onHideListener;
	private ClickListener clickListener;
	private CustomShapeRenderer sR;
	private SpriteBatch sP;
	private Stats stats;

	private Figure figure;
	private Button buttonUpgrade;
	private Actor dialog;

	private float time;
	private float alpha;
	private boolean hiding;

	public UpgradeInfoStage(SpriteBatch sP, CustomShapeRenderer sR, Stats stats) {
		this.sP = sP;
		this.sR = sR;
		this.stats = stats;

		touchInterruption = false;

		dialog = new Actor();
		dialog.setSize(900 * ratio, 1200 * ratio);
		dialog.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, Align.center);

		buttonUpgrade = new Button(Colors.tileColor, Colors.tile2Color, Colors.inactiveColor);
		buttonUpgrade.setSize(500 * ratio, 100 * ratio);
		buttonUpgrade.setPosition(Gdx.graphics.getWidth() / 2, dialog.getY(), Align.center);
	}

	private void drawBackground() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		sR.begin(ShapeRenderer.ShapeType.Filled);
		sR.setColor(Color.BLACK.cpy().mul(1, 1, 1, 0.5f * alpha));
		sR.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		sR.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	private void drawFigureInfo() {
		if(alpha != 1) {
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		}

		sR.begin(ShapeRenderer.ShapeType.Filled);
		sR.setColor(Colors.barColor.cpy().mul(1, 1, 1, alpha));
		sR.rect(dialog.getX(), dialog.getY(), dialog.getWidth(), dialog.getHeight(), 6 * ratio);
		sR.end();

		float textureSize = dialog.getWidth() / 2;
		float offset = 50 * ratio;
		float y;
		sP.begin();

		font.getData().setScale(0.65f);
		font.setColor(Colors.textColor.cpy().mul(1, 1, 1, alpha * alpha));
		font.draw(sP, String.format(Locale.getDefault(), "%s (lvl. %d)", figure.getName(), figure.getLevel()), dialog.getX(), y = (dialog.getY() + dialog.getHeight() - font.getCapHeight() / 2 - offset), dialog.getWidth(), Align.center, true);

		sP.setColor(Color.WHITE.cpy().mul(1, 1, 1, alpha * alpha));
		sP.draw(figure.getTexture(), dialog.getX() + (dialog.getWidth() - textureSize) / 2, y -= (offset + textureSize + font.getCapHeight()), textureSize, textureSize);

		font.getData().setScale(0.45f);
		font.setColor(Colors.textColor.cpy().mul(1, 1, 1, alpha * alpha));
		font.draw(sP, figure.getAbilityDescription(), dialog.getX() + 50 * ratio, y -= offset, dialog.getWidth() - 100 * ratio, Align.left, true);

		float height = new GlyphLayout(font, figure.getAbilityDescription(), Colors.text2Color, dialog.getWidth() - 100 * ratio, Align.left, true).height;

		if(figure instanceof Structure) {
			int capacity = ((Structure) figure).getCapacity();
			if(capacity != 0) {
				sP.draw(TextureManager.getInstance().getTexture(TextureManager.CAPACITY), dialog.getX() + 50 * ratio, y -= (font.getCapHeight() + offset + height + 150 * ratio), 150 * ratio, 150 * ratio);

				font.getData().setScale(0.6f);
				font.draw(sP, String.valueOf(capacity), dialog.getX() + 50 * ratio + 150 * ratio, y + font.getCapHeight() / 2 + 75 * ratio, 150 * ratio, Align.left, true);
			}
		}

		sP.end();

		if(alpha != 1) Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	private void drawUpgradeButton() {
		if(alpha != 1) {
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		}
		
		sR.begin(ShapeRenderer.ShapeType.Filled);
		sR.setColor(buttonUpgrade.getColor().cpy().mul(1, 1, 1, alpha));
		sR.cutRect(buttonUpgrade.getX(), buttonUpgrade.getY(), buttonUpgrade.getWidth(), buttonUpgrade.getHeight(), MathUtils.PI / 18, 4 * ratio);
		sR.end();

		font.getData().setScale(0.45f);
		font.setColor(Colors.buttonColor.cpy().mul(1, 1, 1, alpha * alpha));
		sP.begin();
		font.draw(sP, String.format(Locale.getDefault(), "Upgrade (%d$)", figure.getCost()),
				buttonUpgrade.getX(), buttonUpgrade.getY() + font.getCapHeight() / 2 + buttonUpgrade.getHeight() / 2, buttonUpgrade.getWidth(), Align.center, false);
		sP.end();

		if(alpha != 1) Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	@Override public void draw() {
		drawBackground();
		drawFigureInfo();
		drawUpgradeButton();
	}

	@Override public void act(float delta) {
		buttonUpgrade.act(delta);

		if(hiding && time > 0 || !hiding && time < Settings.duration) {
			time += hiding ? -delta : delta;
			alpha = hiding ? Interpolation.exp5In.apply(time / Settings.duration) : Interpolation.exp5Out.apply(time / Settings.duration);
		} else if(hiding && onHideListener != null) onHideListener.run();
	}

	@Override public boolean tap(float x, float y, int count, int button) {
		buttonUpgrade.touchUp(x, y);
		Vector2 pos = new Vector2(x, Gdx.graphics.getHeight() - y);
		if(buttonUpgrade.hit(pos.x - buttonUpgrade.getX(), pos.y - buttonUpgrade.getY(), true) != null) {
			clickListener.onUpgradeClick();
			buttonUpgrade.clearActions();
			buttonUpgrade.setTouchable(figure.canUpgrade(stats) ? Touchable.enabled : Touchable.disabled);
			return true;
		} if(dialog.hit(pos.x - dialog.getX(), pos.y - dialog.getY(), true) == null) clickListener.onCancelClick();
		return true;
	}

	@Override public boolean pan(float x, float y, float deltaX, float deltaY) {
		buttonUpgrade.touchUp(x, y);
		return super.pan(x, y, deltaX, deltaY);
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		buttonUpgrade.touchUp(0, 0);
		return super.pinch(initialPointer1, initialPointer2, pointer1, pointer2);
	}

	@Override public boolean touchDown(float x, float y, int pointer, int button) {
		buttonUpgrade.touchDown(x, Gdx.graphics.getHeight() - y);
		return super.touchDown(x, y, pointer, button);
	}

	@Override public boolean touchUp(float x, float y, int pointer, int button) {
		buttonUpgrade.touchUp(x, y);
		return super.touchUp(x, y, pointer, button);
	}

	@Override public boolean keyDown(int keyCode) {
		if(keyCode == Input.Keys.BACK || keyCode == Input.Keys.ESCAPE) {
			clickListener.onCancelClick();
			return true;
		} else return false;
	}

	public void hide(Runnable onHideListener) {
		hiding = true;
		this.onHideListener = onHideListener;
	}

	public void setFigure(Figure figure) {
		this.figure = figure;
		buttonUpgrade.setTouchable(figure.canUpgrade(stats) ? Touchable.enabled : Touchable.disabled);
		figure.setLevel(figure.getLevel() + 1);
	}

	public UpgradeInfoStage setClickListener(ClickListener clickListener) {
		this.clickListener = clickListener;
		return this;
	}

	public interface ClickListener {
		void onCancelClick();
		void onUpgradeClick();
	}
}