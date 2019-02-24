package com.pointlessgames.kingdomoflove.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;

public class Settings {
	public static final float duration = 0.3f;
	public static final float screenWidth = 1080f;
	public static final float screenHeight = 1920f;
	public static final String APP_NAME = "Kingdom Of Love";
	public static float ratio = Gdx.graphics.getHeight() / screenHeight;

	public static final int WIDTH = 5;
	public static final int HEIGHT = 7;

	public static final float scale = 0.85f;
	public static float tileSize = (Math.min((float) Gdx.graphics.getHeight() / (HEIGHT + 1), (float) Gdx.graphics.getWidth() / (WIDTH + 1))) * Settings.ratio;

	public static boolean soundsOn = true;
	public static boolean historyOn = true;

	public static void refreshRatio() {
		ratio = Gdx.graphics.getHeight() / screenHeight;
	}

	public static Vector2 getWorldSize() {
		float aspectRatio = (float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
		return new Vector2(screenHeight / aspectRatio, screenHeight);
	}

	public static void load() {
		Preferences prefs = Gdx.app.getPreferences("Settings");
		soundsOn = prefs.getBoolean("soundsOn", soundsOn);
		historyOn = prefs.getBoolean("historyOn", historyOn);
	}

	public static void save() {
		Preferences prefs = Gdx.app.getPreferences("Settings");
		prefs.putBoolean("soundsOn", soundsOn);
		prefs.putBoolean("historyOn", historyOn);
		prefs.flush();
	}
}