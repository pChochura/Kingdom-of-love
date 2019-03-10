package com.pointlessgames.kingdomoflove.stages.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.pointlessgames.kingdomoflove.renderers.CustomShapeRenderer;
import com.pointlessgames.kingdomoflove.stages.GestureStage;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.Settings;
import com.pointlessgames.kingdomoflove.utils.SoundManager;
import com.pointlessgames.kingdomoflove.utils.TextureManager;

import static com.pointlessgames.kingdomoflove.screens.MenuScreen.font;
import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;

public class MenuUIStage extends GestureStage {

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
		sP.setColor(Color.WHITE);
		sP.draw(TextureManager.getInstance().background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		sP.end();
	}

	private void drawLogo() {
		sP.begin();
		font.getData().setScale(0.85f);
		font.setColor(Colors.textColor);
		font.draw(sP, Settings.APP_NAME, 0, Gdx.graphics.getHeight() - 750 * ratio, Gdx.graphics.getWidth(), Align.center, true);
		sP.setColor(Color.WHITE);
		sP.draw(TextureManager.getInstance().logo, Gdx.graphics.getWidth() / 2 - 256 * ratio, Gdx.graphics.getHeight() - 750 * ratio, 512 * ratio, 512 * ratio);
		sP.end();
	}

	private void drawButtons() {
		sP.begin();
		sP.setColor(Colors.buttonColor);
		sP.draw(TextureManager.getInstance().arrow, startActor.getX(), startActor.getY(), startActor.getWidth(), startActor.getHeight());
		sP.draw(Settings.soundsOn ? TextureManager.getInstance().soundsOn : TextureManager.getInstance().soundsOff, soundsActor.getX(), soundsActor.getY(), soundsActor.getWidth(), soundsActor.getHeight());
		sP.draw(Settings.historyOn ? TextureManager.getInstance().historyOn : TextureManager.getInstance().historyOff, historyActor.getX(), historyActor.getY(), historyActor.getWidth(), historyActor.getHeight());
		sP.setColor(Color.WHITE);
		sP.end();
	}

	@Override public void draw() {
		drawBackground();
		drawLogo();
		drawButtons();
	}

	@Override public boolean tap(float x, float y, int count, int button) {
		if(soundsActor.hit(x - soundsActor.getX(), Gdx.graphics.getHeight() - y - soundsActor.getY(), true) != null) {
			Settings.soundsOn = !Settings.soundsOn;
			Settings.save();
			if(Settings.soundsOn)
				SoundManager.getInstance().select.play(0.5f);
		} else if(historyActor.hit(x - historyActor.getX(), Gdx.graphics.getHeight() -  y - historyActor.getY(), true) != null) {
			Settings.historyOn = !Settings.historyOn;
			Settings.save();
			if(Settings.soundsOn)
				SoundManager.getInstance().select.play(0.5f);
		} else if(startActor.hit(x - startActor.getX(), Gdx.graphics.getHeight() - y - startActor.getY(), true) != null) {
			if(Settings.soundsOn)
				SoundManager.getInstance().select.play(0.5f);
			startListener.run();
		}
		return true;
	}

	public MenuUIStage setOnStartListener(Runnable startListener) {
		this.startListener = startListener;
		return this;
	}
}
