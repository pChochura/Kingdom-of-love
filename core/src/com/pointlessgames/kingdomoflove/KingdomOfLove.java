package com.pointlessgames.kingdomoflove;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.pointlessgames.kingdomoflove.renderers.CustomShapeRenderer;
import com.pointlessgames.kingdomoflove.screens.EndScreen;
import com.pointlessgames.kingdomoflove.screens.MenuScreen;
import com.pointlessgames.kingdomoflove.screens.StartScreen;
import com.pointlessgames.kingdomoflove.screens.StoryScreen;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.Settings;
import com.pointlessgames.kingdomoflove.utils.SoundManager;
import com.pointlessgames.kingdomoflove.utils.TextureManager;
import com.pointlessgames.kingdomoflove.utils.Utils;

import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;

public class KingdomOfLove extends Game {

	private boolean loaded;
	private CustomShapeRenderer sR;
	
	@Override
	public void create() {
		TextureManager.getInstance().loadTextures();
		SoundManager.getInstance().loadSounds();
		Settings.load();
		sR = new CustomShapeRenderer();
	}

	@Override public void render() {
		if(!loaded) {
			if(TextureManager.getInstance().am.update() && SoundManager.getInstance().am.update()) {
				loaded = true;
				TextureManager.getInstance().getTextures();
				restart();
			}

			Gdx.gl.glClearColor(Colors.bgColor.r, Colors.bgColor.g, Colors.bgColor.b, Colors.bgColor.a);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			float progress = TextureManager.getInstance().am.getProgress() * SoundManager.getInstance().am.getProgress();
			float round = Utils.map(progress, 0, 1f, 0, 25 * ratio);

			sR.begin(ShapeRenderer.ShapeType.Filled);
			sR.setColor(Colors.inactiveColor);
			sR.roundedRect(150 * ratio, Gdx.graphics.getHeight() / 2, Gdx.graphics.getWidth() - 300 * ratio, 50 * ratio, round);
			sR.setColor(Colors.loveColor);
			sR.roundedRect(150 * ratio, Gdx.graphics.getHeight() / 2, Utils.map(progress, 0, 1f, 0, Gdx.graphics.getWidth() - 300 * ratio), 50 * ratio, round);
			sR.end();
		}
		super.render();
	}

	private void restart() {
		setScreen(new MenuScreen().setOnStartListener(() -> {
			if(Gdx.app.getPreferences("Stats").getBoolean("saved") || !Settings.historyOn)
				start();
			else setScreen(new StoryScreen().setOnEndListener(this::start));
		}));
	}

	private void start() {
		getScreen().dispose();
		setScreen(new StartScreen().setOnEndListener(() -> {
			getScreen().dispose();
			if(Settings.historyOn)
				setScreen(new EndScreen().setOnEndListener(this::create));
			else restart();
			Gdx.app.getPreferences("Stats").putBoolean("saved", false).flush();
		}));
	}

	@Override public void dispose() {
		super.dispose();
		getScreen().dispose();
		TextureManager.getInstance().dispose();
		SoundManager.getInstance().dispose();
	}
}
