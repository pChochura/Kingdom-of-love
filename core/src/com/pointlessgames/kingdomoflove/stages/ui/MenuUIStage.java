package com.pointlessgames.kingdomoflove.stages.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.pointlessgames.kingdomoflove.actors.Button;
import com.pointlessgames.kingdomoflove.stages.BaseStage;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.Settings;
import com.pointlessgames.kingdomoflove.utils.Strings;
import com.pointlessgames.kingdomoflove.utils.managers.SoundManager;
import com.pointlessgames.kingdomoflove.utils.managers.TextureManager;

import static com.pointlessgames.kingdomoflove.screens.MenuScreen.font;
import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;

public class MenuUIStage extends BaseStage {

	private SpriteBatch sP;

	private Runnable startListener;
	private Button buttonStart;
	private Button buttonSound;

	public MenuUIStage(SpriteBatch sP) {
		this.sP = sP;

		buttonStart = new Button(Colors.buttonColor, Colors.tile2Color);
		buttonStart.setWidth(500 * ratio);
		buttonStart.setHeight(125 * ratio);
		buttonStart.setX(Gdx.graphics.getWidth() / 2, Align.center);
		buttonStart.setY(400 * ratio);

		buttonSound = new Button(Colors.buttonColor, Colors.tile2Color);
		buttonSound.setWidth(128 * ratio);
		buttonSound.setHeight(128 * ratio);
		buttonSound.setX(Gdx.graphics.getWidth() - 50 * ratio, Align.right);
		buttonSound.setY(50 * ratio);
	}

	private void drawBackground() {
		sP.setColor(Color.WHITE);
		sP.draw(TextureManager.getInstance().getTexture(TextureManager.BACKGROUND), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	private void drawLogo() {
		font.getData().setScale(0.85f);
		font.setColor(Colors.textColor);
		font.draw(sP, Settings.APP_NAME, 0, Gdx.graphics.getHeight() - 750 * ratio, Gdx.graphics.getWidth(), Align.center, true);
		sP.setColor(Color.WHITE);
		sP.draw(TextureManager.getInstance().getTexture(TextureManager.LOGO), Gdx.graphics.getWidth() / 2 - 256 * ratio, Gdx.graphics.getHeight() - 750 * ratio, 512 * ratio, 512 * ratio);
	}

	private void drawButtons() {
		//Button start
		sP.setColor(buttonStart.getColor());
		TextureManager.getInstance().cutRect.draw(sP, buttonStart.getX(), buttonStart.getY(), buttonStart.getWidth(), buttonStart.getHeight());

		font.getData().setScale(0.55f);
		font.setColor(Colors.tileColor);
		font.draw(sP, Strings.START, buttonStart.getX(), buttonStart.getY() + font.getCapHeight() / 2 + buttonStart.getHeight() / 2, buttonStart.getWidth(), Align.center, false);

		//Button sound
		sP.setColor(buttonSound.getColor());
		TextureManager.getInstance().rect.draw(sP, buttonSound.getX(), buttonSound.getY(), buttonSound.getWidth(), buttonSound.getHeight());

		sP.setColor(Colors.tileColor);
		Texture texture = TextureManager.getInstance().getTexture(Settings.soundsOn ? TextureManager.SOUNDS_ON : TextureManager.SOUNDS_OFF);
		sP.draw(texture, buttonSound.getX(), buttonSound.getY(), buttonSound.getWidth() * 0.5f, buttonSound.getHeight() * 0.5f,
				buttonSound.getWidth(), buttonSound.getHeight(), 0.65f, 0.65f, 0, 0, 0, texture.getWidth(), texture.getHeight(), false, false);

	}

	@Override public void draw() {
		sP.begin();

		drawBackground();
		drawLogo();
		drawButtons();

		sP.setColor(Color.WHITE);
		sP.end();
	}

	@Override public void act(float delta) {
		buttonStart.act(delta);
		buttonSound.act(delta);
	}

	@Override public boolean tap(float x, float y, int count, int button) {
		buttonStart.touchUp();
		buttonSound.touchUp();
		if(buttonSound.hit(x - buttonSound.getX(), Gdx.graphics.getHeight() - y - buttonSound.getY(), true) != null) {
			Settings.soundsOn = !Settings.soundsOn;
			Settings.save();
			if(Settings.soundsOn)
				SoundManager.getInstance().getSound(SoundManager.SELECT).play(0.5f);
		} else if(buttonStart.hit(x - buttonStart.getX(), Gdx.graphics.getHeight() - y - buttonStart.getY(), true) != null) {
			if(Settings.soundsOn)
				SoundManager.getInstance().getSound(SoundManager.SELECT).play(0.5f);
			startListener.run();
		}
		return true;
	}

	@Override public boolean pan(float x, float y, float deltaX, float deltaY) {
		buttonStart.touchUp();
		buttonSound.touchUp();
		return true;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		buttonStart.touchUp();
		buttonSound.touchUp();
		return true;
	}

	@Override public boolean touchDown(float x, float y, int pointer, int button) {
		Vector2 pos = new Vector2(x, Gdx.graphics.getHeight() - y);
		buttonStart.touchDown(pos.x, pos.y);
		buttonSound.touchDown(pos.x, pos.y);
		return true;
	}

	@Override public boolean touchUp(float x, float y, int pointer, int button) {
		buttonStart.touchUp();
		buttonSound.touchUp();
		return true;
	}

	@Override public boolean keyDown(int keyCode) {
		if(keyCode == Input.Keys.BACK || keyCode == Input.Keys.ESCAPE) {
			Gdx.app.exit();
			return true;
		}
		return false;
	}

	public MenuUIStage setOnStartListener(Runnable startListener) {
		this.startListener = startListener;
		return this;
	}
}
