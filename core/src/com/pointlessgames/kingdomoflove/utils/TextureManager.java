package com.pointlessgames.kingdomoflove.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureManager {

	public static Texture background = new Texture("images/background.png");
	public static Texture money = new Texture("icons/money.png");
	public static Texture love = new Texture("icons/love.png");
	public static Texture capacity = new Texture("icons/capacity.png");
	public static Texture arrow = new Texture("icons/arrow.png");
	public static Texture soundsOn = new Texture("icons/sounds_on.png");
	public static Texture soundsOff = new Texture("icons/sounds_off.png");
	public static Texture historyOn = new Texture("icons/history_on.png");
	public static Texture historyOff = new Texture("icons/history_off.png");
	public static Texture logo = new Texture("images/logo.png");
	public static TextureRegion[][] road = TextureRegion.split(new Texture("figures/road.png"), 128, 128);

	public static void dispose() {
		background.dispose();
		money.dispose();
		love.dispose();
		capacity.dispose();
		arrow.dispose();
		soundsOn.dispose();
		soundsOff.dispose();
		historyOn.dispose();
		historyOff.dispose();
		logo.dispose();
		for(TextureRegion[] t1 : road) for(TextureRegion t2 : t1) t2.getTexture().dispose();
	}

	public static void loadTextures() {
		background = new Texture("images/background.png");
		money = new Texture("icons/money.png");
		love = new Texture("icons/love.png");
		capacity = new Texture("icons/capacity.png");
		arrow = new Texture("icons/arrow.png");
		soundsOn = new Texture("icons/sounds_on.png");
		soundsOff = new Texture("icons/sounds_off.png");
		historyOn = new Texture("icons/history_on.png");
		historyOff = new Texture("icons/history_off.png");
		logo = new Texture("images/logo.png");
		road = TextureRegion.split(new Texture("figures/road.png"), 128, 128);
	}
}
