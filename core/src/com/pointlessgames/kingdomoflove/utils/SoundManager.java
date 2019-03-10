package com.pointlessgames.kingdomoflove.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

	private static SoundManager instance = new SoundManager();
	public AssetManager am = new AssetManager();
	public Sound select, selectError, pickFigure, upgrade, nextDay;

	public static SoundManager getInstance() {
		return instance;
	}

	public void loadSounds() {
		am.load("sounds/select.wav", Sound.class);
		am.load("sounds/selectError.wav", Sound.class);
		am.load("sounds/pickFigure.wav", Sound.class);
		am.load("sounds/upgrade.wav", Sound.class);
		am.load("sounds/nextDay.wav", Sound.class);
	}

	public void getSounds() {
		select = am.get("sounds/select.wav");
		selectError = am.get("sounds/selectError.wav");
		pickFigure = am.get("sounds/pickFigure.wav");
		upgrade = am.get("sounds/upgrade.wav");
		nextDay = am.get("sounds/nextDay.wav");
	}

	public void dispose() {
		select.dispose();
		selectError.dispose();
		pickFigure.dispose();
		upgrade.dispose();
		nextDay.dispose();
		am.dispose();
	}
}
