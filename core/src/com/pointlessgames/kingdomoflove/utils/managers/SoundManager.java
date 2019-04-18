package com.pointlessgames.kingdomoflove.utils.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

	private static SoundManager instance = new SoundManager();

	public final static String SELECT = "sounds/select.wav";
	public final static String SELECT_ERROR = "sounds/selectError.wav";
	public final static String PICK_FIGURE = "sounds/pickFigure.wav";
	public final static String UPGRADE = "sounds/upgrade.wav";
	public final static String NEXT_DAY = "sounds/nextDay.wav";

	public final AssetManager am = new AssetManager();

	public static SoundManager getInstance() {
		return instance == null ? instance = new SoundManager() : instance;
	}

	public void loadSounds() {
		am.load(SELECT, Sound.class);
		am.load(SELECT_ERROR, Sound.class);
		am.load(PICK_FIGURE, Sound.class);
		am.load(UPGRADE, Sound.class);
		am.load(NEXT_DAY, Sound.class);
	}

	public Sound getSound(String name) {
		return am.get(name);
	}

	public void dispose() {
		instance = null;
		am.dispose();
	}
}
