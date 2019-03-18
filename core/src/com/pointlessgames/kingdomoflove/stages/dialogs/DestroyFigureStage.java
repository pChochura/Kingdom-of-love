package com.pointlessgames.kingdomoflove.stages.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.pointlessgames.kingdomoflove.actors.Button;
import com.pointlessgames.kingdomoflove.models.figures.Figure;
import com.pointlessgames.kingdomoflove.models.figures.Structure;
import com.pointlessgames.kingdomoflove.stages.BaseStage;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.Settings;
import com.pointlessgames.kingdomoflove.utils.managers.TextureManager;

import java.util.Locale;

import static com.pointlessgames.kingdomoflove.screens.StartScreen.font;
import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;

public class DestroyFigureStage extends BaseStage {

	private Runnable onHideListener;
	private ClickListener clickListener;
	private SpriteBatch sP;

	private Button buttonDestroy;
	private Actor dialog;

	private float time;
	private float alpha;
	private boolean hiding;

	private float offset;
	private float iconSize;

	private int money;
	private int capacity;
	private float love;
	private float warningTextHeight;

	public DestroyFigureStage(SpriteBatch sP) {
		this.sP = sP;

		touchInterruption = false;

		dialog = new Actor();
		dialog.setSize(900 * ratio, 1200 * ratio);
		dialog.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, Align.center);

		buttonDestroy = new Button(Colors.buttonColor, Colors.tile2Color, Colors.inactiveColor);
		buttonDestroy.setSize(500 * ratio, 100 * ratio);
		buttonDestroy.setPosition(Gdx.graphics.getWidth() / 2, dialog.getY(), Align.center);

		offset = 50 * ratio;
		iconSize = 75 * ratio;
		font.getData().setScale(0.5f);
		warningTextHeight = new GlyphLayout(font, "Are you sure?\nAfter destroying this figure you'll get:", Colors.textColor, dialog.getWidth() - 2 * offset, Align.center, true).height;
	}

	private void setDialogHeight() {
		float height = 4 * offset + iconSize + warningTextHeight;
		dialog.setHeight(height);
		dialog.setY(0.5f * Gdx.graphics.getHeight(), Align.center);
		buttonDestroy.setY(dialog.getY(), Align.center);
	}

	private void drawBackground() {
		sP.setColor(Color.BLACK.cpy().mul(1, 1, 1, 0.5f * alpha));
		TextureManager.getInstance().filledRect.draw(sP, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	private void drawDialog() {
		sP.setColor(Colors.barColor.cpy().mul(1, 1, 1, alpha));
		TextureManager.getInstance().rect.draw(sP, dialog.getX(), dialog.getY(), dialog.getWidth(), dialog.getHeight());
	}

	private void drawFigureInfo() {
		float y = dialog.getY() + dialog.getHeight() - offset;

		font.getData().setScale(0.5f);
		font.setColor(Colors.textColor.cpy().mul(1, 1, 1, alpha * alpha));
		font.draw(sP, "Are you sure?\nAfter destroying this figure you'll get:", dialog.getX() + offset, y, dialog.getWidth() - 2 * offset, Align.center, true);

		y -= warningTextHeight + offset;

		sP.setColor(Color.WHITE.cpy().mul(1, 1, 1, alpha * alpha));
		sP.draw(TextureManager.getInstance().getTexture(TextureManager.MONEY), dialog.getX() + offset, y - iconSize, iconSize, iconSize);
		sP.draw(TextureManager.getInstance().getTexture(TextureManager.CAPACITY), dialog.getX() + 0.5f * dialog.getWidth() - iconSize, y - iconSize, iconSize, iconSize);
		sP.draw(TextureManager.getInstance().getTexture(TextureManager.LOVE), dialog.getX() + dialog.getWidth() - 5 * offset, y - iconSize, iconSize, iconSize);

		font.getData().setScale(0.4f);
		font.setColor(Colors.textColor.cpy().mul(1, 1, 1, alpha * alpha));
		font.draw(sP, String.format(Locale.getDefault(), "%+d$", money),
				dialog.getX() + offset + iconSize, y - iconSize + 0.5f * (iconSize + font.getCapHeight()));
		font.draw(sP, String.format(Locale.getDefault(), "%d", capacity),
				dialog.getX() + 0.5f * dialog.getWidth(), y - iconSize + 0.5f * (iconSize + font.getCapHeight()));
		font.draw(sP, String.format(Locale.getDefault(), "%+.1f%%", love),
				dialog.getX() + dialog.getWidth() - 5 * offset + iconSize, y - iconSize + 0.5f * (iconSize + font.getCapHeight()));

	}

	private void drawDestroyButton() {
		sP.setColor(buttonDestroy.getColor().cpy().mul(1, 1, 1, alpha));
		TextureManager.getInstance().cutRect.draw(sP, buttonDestroy.getX(), buttonDestroy.getY(), buttonDestroy.getWidth(), buttonDestroy.getHeight());

		font.getData().setScale(0.45f);
		font.setColor(Colors.tileColor.cpy().mul(1, 1, 1, alpha * alpha));
		font.draw(sP, "Destroy", buttonDestroy.getX(), buttonDestroy.getY() + font.getCapHeight() / 2 + buttonDestroy.getHeight() / 2, buttonDestroy.getWidth(), Align.center, false);
	}

	@Override public void draw() {
		sP.begin();

		drawBackground();
		drawDialog();
		drawFigureInfo();
		drawDestroyButton();

		sP.setColor(Color.WHITE);
		sP.end();
	}

	@Override public void act(float delta) {
		buttonDestroy.act(delta);

		if(hiding && time > 0 || !hiding && time < Settings.duration) {
			time += hiding ? -delta : delta;
			alpha = hiding ? Interpolation.exp5In.apply(time / Settings.duration) : Interpolation.exp5Out.apply(time / Settings.duration);
		} else if(hiding && onHideListener != null) onHideListener.run();
	}

	@Override public boolean keyDown(int keyCode) {
		if(keyCode == Input.Keys.BACK || keyCode == Input.Keys.ESCAPE) {
			clickListener.onCancelClick();
			return true;
		} else return false;
	}

	@Override public boolean touchDown(float x, float y, int pointer, int button) {
		buttonDestroy.touchDown(x, Gdx.graphics.getHeight() - y);
		return super.touchDown(x, y, pointer, button);
	}

	@Override public boolean tap(float x, float y, int count, int button) {
		buttonDestroy.touchUp();
		Vector2 pos = new Vector2(x, Gdx.graphics.getHeight() - y);
		if(buttonDestroy.hit(pos.x - buttonDestroy.getX(), pos.y - buttonDestroy.getY(), false) != null) {
			clickListener.onDestroyClick(money, love);
			return true;
		}
		if(dialog.hit(pos.x - dialog.getX(), pos.y - dialog.getY(), false) == null)
			clickListener.onCancelClick();
		return true;
	}

	@Override public boolean pan(float x, float y, float deltaX, float deltaY) {
		buttonDestroy.touchUp();
		return super.pan(x, y, deltaX, deltaY);
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		buttonDestroy.touchUp();
		return super.pinch(initialPointer1, initialPointer2, pointer1, pointer2);
	}

	@Override public boolean touchUp(float x, float y, int pointer, int button) {
		buttonDestroy.touchUp();
		return super.touchUp(x, y, pointer, button);
	}

	public void hide(Runnable onHideListener) {
		hiding = true;
		this.onHideListener = onHideListener;
	}

	public void setFigure(Figure figure) {
		int level = figure.getLevel();

		capacity = -(figure instanceof Structure ? ((Structure)figure).getCapacity() : 0);
		money = 0;
		love = -figure.getLove();
		for(int i = 1; i <= level; i++) {
			figure.setLevel(i);
			money += figure.getCost();
		}
		money /= (2 * level);
		figure.setLevel(level);

		setDialogHeight();
	}

	public DestroyFigureStage setClickListener(ClickListener clickListener) {
		this.clickListener = clickListener;
		return this;
	}

	public interface ClickListener {
		void onCancelClick();

		void onDestroyClick(int money, float love);
	}
}