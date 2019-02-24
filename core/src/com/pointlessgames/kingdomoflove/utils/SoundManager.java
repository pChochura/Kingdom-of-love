package com.pointlessgames.kingdomoflove.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
	public static Sound select = Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav"));
	public static Sound selectError = Gdx.audio.newSound(Gdx.files.internal("sounds/selectError.wav"));
	public static Sound pickFigure = Gdx.audio.newSound(Gdx.files.internal("sounds/pickFigure.wav"));
	public static Sound upgrade = Gdx.audio.newSound(Gdx.files.internal("sounds/upgrade.wav"));
	public static Sound nextDay = Gdx.audio.newSound(Gdx.files.internal("sounds/nextDay.wav"));

	public static void dispose() {
		select.dispose();
		selectError.dispose();
		pickFigure.dispose();
		upgrade.dispose();
		nextDay.dispose();
	}

	public static void loadSounds() {
		select = Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav"));
		selectError = Gdx.audio.newSound(Gdx.files.internal("sounds/selectError.wav"));
		pickFigure = Gdx.audio.newSound(Gdx.files.internal("sounds/pickFigure.wav"));
		upgrade = Gdx.audio.newSound(Gdx.files.internal("sounds/upgrade.wav"));
		nextDay = Gdx.audio.newSound(Gdx.files.internal("sounds/nextDay.wav"));
	}
}
