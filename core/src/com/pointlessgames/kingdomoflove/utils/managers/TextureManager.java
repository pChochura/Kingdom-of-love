package com.pointlessgames.kingdomoflove.utils.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureManager {

	private static TextureManager instance = new TextureManager();

	public final static String BACKGROUND = "images/background.png";
	public final static String LOGO = "images/logo.png";
	public final static String MONEY = "icons/money.png";
	public final static String LOVE = "icons/love.png";
	public final static String CAPACITY = "icons/capacity.png";
	public final static String SOUNDS_ON = "icons/sounds_on.png";
	public final static String SOUNDS_OFF = "icons/sounds_off.png";
	public final static String UPGRADE = "icons/upgrade.png";
	public final static String INFO = "icons/info.png";
	public final static String DESTROY = "icons/cancel.png";
	public final static String CANCEL = "icons/cancel.png";
	public final static String CONFIRM = "icons/confirm.png";
	public final static String GRANARY = "figures/granary.png";
	public final static String HOUSE = "figures/house.png";
	public final static String LIBRARY = "figures/library.png";
	public final static String MILL = "figures/mill.png";
	public final static String MONUMENT = "figures/monument.png";
	public final static String POND = "figures/pond.png";
	public final static String SAWMILL = "figures/sawmill.png";
	public final static String WELL = "figures/well.png";
	public final static String WHEAT = "figures/wheat.png";
	public static final String CHURCH = "figures/church.png";

	private final static String CONIFER = "figures/conifer.png";
	private final static String TREE = "figures/tree.png";
	private final static String ROAD = "figures/road.png";
	private final static String CUT_RECT = "images/cut_rect.png";
	private final static String RECT = "images/rect.png";
	private final static String FILLED_RECT = "images/filled_rect.png";
	private final static String OUTLINE_RECT = "images/outline_rect.png";

	public final AssetManager am = new AssetManager();
	public TextureRegion[][] road;
	public TextureRegion[][] tree;
	public TextureRegion[][] conifer;
	public NinePatch cutRect;
	public NinePatch rect;
	public NinePatch filledRect;
	public NinePatch outlineRect;

	public static TextureManager getInstance() {
		return instance == null ? instance = new TextureManager() : instance;
	}

	public void loadTextures() {
		am.load(BACKGROUND, Texture.class);
		am.load(LOGO, Texture.class);
		am.load(CUT_RECT, Texture.class);
		am.load(RECT, Texture.class);
		am.load(FILLED_RECT, Texture.class);
		am.load(OUTLINE_RECT, Texture.class);
		am.load(MONEY, Texture.class);
		am.load(LOVE, Texture.class);
		am.load(CAPACITY, Texture.class);
		am.load(SOUNDS_ON, Texture.class);
		am.load(SOUNDS_OFF, Texture.class);
		am.load(UPGRADE, Texture.class);
		am.load(INFO, Texture.class);
		am.load(DESTROY, Texture.class);
		am.load(CANCEL, Texture.class);
		am.load(CONFIRM, Texture.class);
		am.load(CONIFER, Texture.class);
		am.load(GRANARY, Texture.class);
		am.load(HOUSE, Texture.class);
		am.load(LIBRARY, Texture.class);
		am.load(MILL, Texture.class);
		am.load(MONUMENT, Texture.class);
		am.load(POND, Texture.class);
		am.load(SAWMILL, Texture.class);
		am.load(TREE, Texture.class);
		am.load(WELL, Texture.class);
		am.load(WHEAT, Texture.class);
		am.load(CHURCH, Texture.class);
		am.load(ROAD, Texture.class);
	}

	public void getTextures() {
		road = TextureRegion.split(am.get(ROAD), 128, 128);
		tree = TextureRegion.split(am.get(TREE), 128, 128);
		conifer = TextureRegion.split(am.get(CONIFER), 128, 128);
		cutRect = new NinePatch(am.get(CUT_RECT, Texture.class), 15, 15, 15, 15);
		rect = new NinePatch(am.get(RECT, Texture.class), 7, 7, 7, 7);
		filledRect = new NinePatch(am.get(FILLED_RECT, Texture.class), 0, 0, 0, 0);
		outlineRect = new NinePatch(am.get(OUTLINE_RECT, Texture.class), 3, 3, 3, 3);
	}

	public Texture getTexture(String name) {
		return am.get(name);
	}

	public void dispose() {
		instance = null;
		am.dispose();
		cutRect.getTexture().dispose();
		rect.getTexture().dispose();
		filledRect.getTexture().dispose();
		outlineRect.getTexture().dispose();
		for(TextureRegion[] t1 : road) for(TextureRegion t2 : t1) t2.getTexture().dispose();
		for(TextureRegion[] t1 : tree) for(TextureRegion t2 : t1) t2.getTexture().dispose();
		for(TextureRegion[] t1 : conifer) for(TextureRegion t2 : t1) t2.getTexture().dispose();
	}
}
