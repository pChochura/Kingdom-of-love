package com.pointlessgames.kingdomoflove.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.pointlessgames.kingdomoflove.renderers.CustomShapeRenderer;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.Settings;
import com.pointlessgames.kingdomoflove.utils.SoundManager;
import com.pointlessgames.kingdomoflove.utils.TextureManager;

import static com.pointlessgames.kingdomoflove.screens.MenuScreen.font;
import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;

public class MenuUIStage extends Stage {

	private CustomShapeRenderer sR;
	private SpriteBatch sP;

	private Runnable startListener;
	private Actor startActor;
	private Actor soundsActor;
	private Actor historyActor;

	public MenuUIStage(SpriteBatch sP, CustomShapeRenderer sR) {
		this.sP = sP;
		this.sR = sR;

		startActor = new Actor();
		startActor.setX(Gdx.graphics.getWidth() / 2 - 128 * ratio);
		startActor.setY(400 * ratio);
		startActor.setWidth(256 * ratio);
		startActor.setHeight(256 * ratio);

		soundsActor = new Actor();
		soundsActor.setX(Gdx.graphics.getWidth() - 178 * ratio);
		soundsActor.setY(50 * ratio);
		soundsActor.setWidth(128 * ratio);
		soundsActor.setHeight(128 * ratio);

		historyActor = new Actor();
		historyActor.setX(50 * ratio);
		historyActor.setY(50 * ratio);
		historyActor.setWidth(128 * ratio);
		historyActor.setHeight(128 * ratio);
	}

	private void drawBackground() {
		sP.begin();
		sP.draw(TextureManager.background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		sP.end();
	}

	private void drawLogo() {
		sP.begin();
		font.getData().setScale(0.85f);
		font.setColor(Colors.textColor);
		font.draw(sP, Settings.APP_NAME, 0, Gdx.graphics.getHeight() - 750 * ratio, Gdx.graphics.getWidth(), Align.center, true);
		sP.draw(TextureManager.logo, Gdx.graphics.getWidth() / 2 - 256 * ratio, Gdx.graphics.getHeight() - 750 * ratio, 512, 512);
		sP.end();
	}

	private void drawButtons() {
		Color c = sP.getColor().cpy();
		sP.begin();
		sP.setColor(Colors.buttonColor);
		sP.draw(TextureManager.arrow, startActor.getX(), startActor.getY(), startActor.getWidth(), startActor.getHeight());
		sP.draw(Settings.soundsOn ? TextureManager.soundsOn : TextureManager.soundsOff, soundsActor.getX(), soundsActor.getY(), soundsActor.getWidth(), soundsActor.getHeight());
		sP.draw(Settings.historyOn ? TextureManager.historyOn : TextureManager.historyOff, historyActor.getX(), historyActor.getY(), historyActor.getWidth(), historyActor.getHeight());
		sP.end();
		sP.setColor(c);
	}

	@Override public void draw() {
		drawBackground();
		drawLogo();
		drawButtons();
	}

	@Override public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(soundsActor.hit(screenX - soundsActor.getX(), Gdx.graphics.getHeight() - screenY - soundsActor.getY(), true) != null) {
			Settings.soundsOn = !Settings.soundsOn;
			Settings.save();
			if(Settings.soundsOn)
				SoundManager.select.play(0.5f);
		} else if(historyActor.hit(screenX - historyActor.getX(), Gdx.graphics.getHeight() - screenY - historyActor.getY(), true) != null) {
			Settings.historyOn = !Settings.historyOn;
			Settings.save();
			if(Settings.soundsOn)
				SoundManager.select.play(0.5f);
		} else if(startActor.hit(screenX - startActor.getX(), Gdx.graphics.getHeight() - screenY - startActor.getY(), true) != null) {
			if(Settings.soundsOn)
				SoundManager.select.play(0.5f);
			startListener.run();
		}
		return true;
	}

	public MenuUIStage setOnStartListener(Runnable startListener) {
		this.startListener = startListener;
		return this;
	}
}
