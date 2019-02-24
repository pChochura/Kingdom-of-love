package com.pointlessgames.kingdomoflove;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.pointlessgames.kingdomoflove.screens.EndScreen;
import com.pointlessgames.kingdomoflove.screens.MenuScreen;
import com.pointlessgames.kingdomoflove.screens.StartScreen;
import com.pointlessgames.kingdomoflove.screens.StoryScreen;
import com.pointlessgames.kingdomoflove.utils.Settings;

public class KingdomOfLove extends Game {
	
	@Override
	public void create() {
		Settings.load();
		setScreen(new MenuScreen().setOnStartListener(() -> {
			if(Gdx.app.getPreferences("Stats").getBoolean("saved") || !Settings.historyOn) start();
			else setScreen(new StoryScreen().setOnEndListener(() -> {
				getScreen().dispose();
				start();
			}));
		}));
	}

	private void start() {
		setScreen(new StartScreen().setOnEndListener(() -> {
			getScreen().dispose();
			setScreen(new EndScreen().setOnEndListener(this::create));
			Gdx.app.getPreferences("Stats").putBoolean("saved", false).flush();
		}));
	}

	@Override public void dispose() {
		super.dispose();
		getScreen().dispose();
	}
}
