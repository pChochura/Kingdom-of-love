package com.pointlessgames.kingdomoflove.stages.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;
import com.pointlessgames.kingdomoflove.actors.Button;
import com.pointlessgames.kingdomoflove.models.figures.Figure;
import com.pointlessgames.kingdomoflove.models.figures.Structure;
import com.pointlessgames.kingdomoflove.stages.BaseStage;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.Settings;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.managers.TextureManager;

import java.util.Locale;

import static com.pointlessgames.kingdomoflove.screens.StartScreen.font;
import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;

public class UpgradeFigureStage extends BaseStage {

	private Runnable onHideListener;
	private ClickListener clickListener;
	private SpriteBatch sP;
	private Stats stats;

	private Figure figure;
	private Button buttonUpgrade;
	private Actor dialog;

	private float time;
	private float alpha;
	private boolean hiding;

	public UpgradeFigureStage(SpriteBatch sP, Stats stats) {
		this.sP = sP;
		this.stats = stats;

		touchInterruption = false;

		dialog = new Actor();
		dialog.setSize(900 * ratio, 1200 * ratio);
		dialog.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, Align.center);

		buttonUpgrade = new Button(Colors.buttonColor, Colors.tile2Color, Colors.inactiveColor);
		buttonUpgrade.setSize(500 * ratio, 100 * ratio);
		buttonUpgrade.setPosition(Gdx.graphics.getWidth() / 2, dialog.getY(), Align.center);

		//TODO remake whole stage
	}

	private void drawBackground() {
		sP.setColor(Color.BLACK.cpy().mul(1, 1, 1, 0.5f * alpha));
		TextureManager.getInstance().filledRect.draw(sP, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	private void drawFigureInfo() {
		sP.setColor(Colors.barColor.cpy().mul(1, 1, 1, alpha));
		TextureManager.getInstance().rect.draw(sP, dialog.getX(), dialog.getY(), dialog.getWidth(), dialog.getHeight());

		float textureSize = dialog.getWidth() / 2;
		float offset = 50 * ratio;
		float y;

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
				sP.draw(TextureManager.getInstance().getTexture(TextureManager.CAPACITY), dialog.getX() + 50 * ratio, y -= (font.getCapHeight() + offset + height + 100 * ratio), 100 * ratio, 100 * ratio);

				font.getData().setScale(0.6f);
				font.draw(sP, String.valueOf(capacity), dialog.getX() + 50 * ratio + 100 * ratio, y + font.getCapHeight() / 2 + 50 * ratio, 100 * ratio, Align.left, true);
			}
		}
	}

	private void drawUpgradeButton() {
		sP.setColor(buttonUpgrade.getColor().cpy().mul(1, 1, 1, alpha));
		TextureManager.getInstance().cutRect.draw(sP, buttonUpgrade.getX(), buttonUpgrade.getY(), buttonUpgrade.getWidth(), buttonUpgrade.getHeight());

		font.getData().setScale(0.45f);
		font.setColor(Colors.tile2Color.cpy().mul(1, 1, 1, alpha * alpha));
		font.draw(sP, String.format(Locale.getDefault(), "Upgrade (%d$)", figure.getCost()),
				buttonUpgrade.getX(), buttonUpgrade.getY() + font.getCapHeight() / 2 + buttonUpgrade.getHeight() / 2, buttonUpgrade.getWidth(), Align.center, false);
	}

	@Override public void draw() {
		sP.begin();

		drawBackground();
		drawFigureInfo();
		drawUpgradeButton();

		sP.setColor(Color.WHITE);
		sP.end();
	}

	@Override public void act(float delta) {
		buttonUpgrade.act(delta);

		if(hiding && time > 0 || !hiding && time < Settings.duration) {
			time += hiding ? -delta : delta;
			alpha = hiding ? Interpolation.exp5In.apply(time / Settings.duration) : Interpolation.exp5Out.apply(time / Settings.duration);
		} else if(hiding && onHideListener != null) onHideListener.run();
	}

	@Override public boolean tap(float x, float y, int count, int button) {
		buttonUpgrade.touchUp();
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
		buttonUpgrade.touchUp();
		return super.pan(x, y, deltaX, deltaY);
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		buttonUpgrade.touchUp();
		return super.pinch(initialPointer1, initialPointer2, pointer1, pointer2);
	}

	@Override public boolean touchDown(float x, float y, int pointer, int button) {
		buttonUpgrade.touchDown(x, Gdx.graphics.getHeight() - y);
		return super.touchDown(x, y, pointer, button);
	}

	@Override public boolean touchUp(float x, float y, int pointer, int button) {
		buttonUpgrade.touchUp();
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
	}

	public UpgradeFigureStage setClickListener(ClickListener clickListener) {
		this.clickListener = clickListener;
		return this;
	}

	public interface ClickListener {
		void onCancelClick();
		void onUpgradeClick();
	}
}