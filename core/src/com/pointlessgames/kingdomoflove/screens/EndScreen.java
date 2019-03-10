package com.pointlessgames.kingdomoflove.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.pointlessgames.kingdomoflove.renderers.CustomShapeRenderer;
import com.pointlessgames.kingdomoflove.stages.story.EndStoryStage;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.Settings;

import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;

public class EndScreen extends BaseScreen {

	public static BitmapFont font;
	private CustomShapeRenderer sR;
	private SpriteBatch sP;

	private Runnable endListener;

	public EndScreen() {
		super(Settings.screenHeight, Colors.bgColor);
	}

	@Override public void show() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arcon.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = (int) (100 * ratio);
		parameter.incremental = true;
		parameter.borderWidth = 0;
		font = generator.generateFont(parameter);
		generator.dispose();

		sR = new CustomShapeRenderer();
		sP = new SpriteBatch();

		addStage(new EndStoryStage(sP, sR).setOnEndListener(endListener));
	}

	public EndScreen setOnEndListener(Runnable endListener) {
		this.endListener = endListener;
		return this;
	}
}
