package com.pointlessgames.kingdomoflove.screens;

import com.badlogic.gdx.Screen;
import com.pointlessgames.kingdomoflove.utils.Settings;

public class BaseScreen implements Screen {

	BaseScreen() {
		super();
	}

	@Override public void show() { }
	@Override public void render(float delta) { }
	@Override public void pause() { }
	@Override public void resume() { }
	@Override public void hide() { }
	@Override public void dispose() { }
	@Override public void resize(int width, int height) {
		Settings.refreshRatio();
	}
}