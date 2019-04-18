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
import com.pointlessgames.kingdomoflove.stages.BaseStage;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.Settings;
import com.pointlessgames.kingdomoflove.utils.Strings;
import com.pointlessgames.kingdomoflove.utils.managers.TextureManager;

import static com.pointlessgames.kingdomoflove.screens.StartScreen.font;
import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;

public class ExitStage extends BaseStage {

	private final float logoSize = 500 * ratio;
	private Runnable onHideListener;
	private ClickListener clickListener;
	private final SpriteBatch sP;

	private final Button buttonExit;
	private final Actor dialog;

	private float time;
	private float alpha;
	private boolean hiding;

	private final float offset;

	private final float warningTextHeight;

	public ExitStage(SpriteBatch sP) {
		this.sP = sP;

		touchInterruption = false;

		dialog = new Actor();
		dialog.setSize(900 * ratio, 1200 * ratio);
		dialog.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Align.center);

		buttonExit = new Button(Colors.buttonColor, Colors.tile2Color, Colors.inactiveColor);
		buttonExit.setSize(500 * ratio, 100 * ratio);
		buttonExit.setPosition(Gdx.graphics.getWidth() / 2f, dialog.getY(), Align.center);

		offset = 50 * ratio;
		font.getData().setScale(0.6f);
		warningTextHeight = new GlyphLayout(font, Strings.DIALOG_EXIT_DESCRIPTION, Colors.textColor, dialog.getWidth() - 2 * offset, Align.center, true).height;

		setDialogHeight();
	}

	private void setDialogHeight() {
		float height = offset + 0.4f * logoSize + warningTextHeight;
		dialog.setHeight(height);
		dialog.setY(0.5f * Gdx.graphics.getHeight(), Align.center);
		buttonExit.setY(dialog.getY(), Align.center);
	}

	private void drawBackground() {
		sP.setColor(Color.BLACK.cpy().mul(1, 1, 1, 0.5f * alpha));
		TextureManager.getInstance().filledRect.draw(sP, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	private void drawDialog() {
		sP.setColor(Colors.barColor.cpy().mul(1, 1, 1, alpha));
		TextureManager.getInstance().rect.draw(sP, dialog.getX(), dialog.getY(), dialog.getWidth(), dialog.getHeight());
	}

	private void drawDialogDescription() {
		float x = dialog.getX() + 0.5f * (dialog.getWidth() - logoSize);
		float y = dialog.getY() + dialog.getHeight() - 0.4f * logoSize;

		sP.setColor(Color.WHITE.cpy().mul(1, 1, 1, alpha * alpha));
		sP.draw(TextureManager.getInstance().getTexture(TextureManager.LOGO), x, y, logoSize, logoSize);

		y += offset;

		font.getData().setScale(0.6f);
		font.setColor(Colors.textColor.cpy().mul(1, 1, 1, alpha * alpha));
		font.draw(sP, Strings.DIALOG_EXIT_DESCRIPTION, dialog.getX() + offset, y, dialog.getWidth() - 2 * offset, Align.center, true);
	}

	private void drawExitButton() {
		sP.setColor(buttonExit.getColor().cpy().mul(1, 1, 1, alpha));
		TextureManager.getInstance().cutRect.draw(sP, buttonExit.getX(), buttonExit.getY(), buttonExit.getWidth(), buttonExit.getHeight());

		font.getData().setScale(0.45f);
		font.setColor(Colors.tileColor.cpy().mul(1, 1, 1, alpha * alpha));
		font.draw(sP, Strings.EXIT, buttonExit.getX(), buttonExit.getY() + font.getCapHeight() / 2 + buttonExit.getHeight() / 2, buttonExit.getWidth(), Align.center, false);
	}

	@Override public void draw() {
		sP.begin();

		drawBackground();
		drawDialog();
		drawDialogDescription();
		drawExitButton();

		sP.setColor(Color.WHITE);
		sP.end();
	}

	@Override public void act(float delta) {
		buttonExit.act(delta);

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
		buttonExit.touchDown(x, Gdx.graphics.getHeight() - y);
		return super.touchDown(x, y, pointer, button);
	}

	@Override public boolean tap(float x, float y, int count, int button) {
		buttonExit.touchUp();
		Vector2 pos = new Vector2(x, Gdx.graphics.getHeight() - y);
		if(buttonExit.hit(pos.x - buttonExit.getX(), pos.y - buttonExit.getY(), false) != null) {
			clickListener.onExitClick();
			return true;
		}
		if(dialog.hit(pos.x - dialog.getX(), pos.y - dialog.getY(), false) == null)
			clickListener.onCancelClick();
		return true;
	}

	@Override public boolean pan(float x, float y, float deltaX, float deltaY) {
		buttonExit.touchUp();
		return super.pan(x, y, deltaX, deltaY);
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		buttonExit.touchUp();
		return super.pinch(initialPointer1, initialPointer2, pointer1, pointer2);
	}

	@Override public boolean touchUp(float x, float y, int pointer, int button) {
		buttonExit.touchUp();
		return super.touchUp(x, y, pointer, button);
	}

	public void hide(Runnable onHideListener) {
		hiding = true;
		this.onHideListener = onHideListener;
	}

	public ExitStage setClickListener(ClickListener clickListener) {
		this.clickListener = clickListener;
		return this;
	}

	public interface ClickListener {
		void onCancelClick();
		void onExitClick();
	}
}
