package com.pointlessgames.kingdomoflove.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import com.pointlessgames.kingdomoflove.models.figures.Figure;

public class Settings {
	public static final float duration = 0.3f;
	public static final float screenWidth = 1080f;
	public static final float screenHeight = 1920f;
	public static float ratio = Gdx.graphics.getHeight() / screenHeight;

	public static final int WIDTH = 15;
	public static final int HEIGHT = 15;
	public static final int WINDOW_WIDTH = 5;
	public static final int WINDOW_HEIGHT = 7;

	public static float scale = 1;
	public static float tileSize = 240 * Settings.ratio;

	public static boolean soundsOn = true;

	public static void refreshTileSize(Stats stats) {
		tileSize = 240 * Settings.ratio * scale;

		for(Figure f : stats.figures)
			f.refreshSize();
	}

	public static void refreshRatio() {
		ratio = Gdx.graphics.getHeight() / screenHeight;
	}

	public static Vector2 getWorldSize() {
		float aspectRatio = (float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
		return new Vector2(screenHeight / aspectRatio, screenHeight);
	}

	public static void load() {
		Preferences prefs = Gdx.app.getPreferences(Strings.PREFERENCES_SETTINGS);
		soundsOn = prefs.getBoolean(Strings.PREFERENCES_SOUNDS_ON, soundsOn);
	}

	public static void save() {
		Preferences prefs = Gdx.app.getPreferences(Strings.PREFERENCES_SETTINGS);
		prefs.putBoolean(Strings.PREFERENCES_SOUNDS_ON, soundsOn);
		prefs.flush();
	}
}