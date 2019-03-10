package com.pointlessgames.kingdomoflove.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureManager {

	private static TextureManager instance = new TextureManager();

	public AssetManager am = new AssetManager();
	public Texture background, money, love, capacity, arrow, soundsOn, soundsOff, historyOn, historyOff, logo;
	public Texture conifer, granary, house, library, mill, monument, pond, sawmill, tree, well, wheat;
	public TextureRegion[][] road;

	public static TextureManager getInstance() {
		return instance;
	}

	public void loadTextures() {
		am.load("images/background.png", Texture.class);
		am.load("images/logo.png", Texture.class);
		am.load("icons/money.png", Texture.class);
		am.load("icons/love.png", Texture.class);
		am.load("icons/capacity.png", Texture.class);
		am.load("icons/arrow.png", Texture.class);
		am.load("icons/sounds_on.png", Texture.class);
		am.load("icons/sounds_off.png", Texture.class);
		am.load("icons/history_on.png", Texture.class);
		am.load("icons/history_off.png", Texture.class);
		am.load("figures/road.png", Texture.class);
		am.load("figures/conifer.png", Texture.class);
		am.load("figures/granary.png", Texture.class);
		am.load("figures/house.png", Texture.class);
		am.load("figures/library.png", Texture.class);
		am.load("figures/mill.png", Texture.class);
		am.load("figures/monument.png", Texture.class);
		am.load("figures/pond.png", Texture.class);
		am.load("figures/sawmill.png", Texture.class);
		am.load("figures/tree.png", Texture.class);
		am.load("figures/well.png", Texture.class);
		am.load("figures/wheat.png", Texture.class);
	}

	public void getTextures() {
		background = am.get("images/background.png");
		money = am.get("icons/money.png");
		love = am.get("icons/love.png");
		capacity = am.get("icons/capacity.png");
		arrow = am.get("icons/arrow.png");
		soundsOn = am.get("icons/sounds_on.png");
		soundsOff = am.get("icons/sounds_off.png");
		historyOn = am.get("icons/history_on.png");
		historyOff = am.get("icons/history_off.png");
		logo = am.get("images/logo.png");
		road = TextureRegion.split(am.get("figures/road.png"), 128, 128);
		conifer = am.get("figures/conifer.png");
		granary = am.get("figures/granary.png");
		house = am.get("figures/house.png");
		library = am.get("figures/library.png");
		mill = am.get("figures/mill.png");
		monument = am.get("figures/monument.png");
		pond = am.get("figures/pond.png");
		sawmill = am.get("figures/sawmill.png");
		tree = am.get("figures/tree.png");
		well = am.get("figures/well.png");
		wheat = am.get("figures/wheat.png");
	}

	public void dispose() {
		am.dispose();
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
		conifer.dispose();
		granary.dispose();
		house.dispose();
		library.dispose();
		mill.dispose();
		monument.dispose();
		pond.dispose();
		sawmill.dispose();
		tree.dispose();
		well.dispose();
		wheat.dispose();
		for(TextureRegion[] t1 : road) for(TextureRegion t2 : t1) t2.getTexture().dispose();
	}
}
