package com.pointlessgames.kingdomoflove.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.pointlessgames.kingdomoflove.stages.ui.MenuUIStage;
import com.pointlessgames.kingdomoflove.utils.Colors;

import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;

public class MenuScreen extends BaseScreen {

	public static BitmapFont font;

	private Runnable startListener;

	public MenuScreen() {
		super(Colors.bgColor);
	}

	@Override public void show() {
	FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arcon.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = (int) (100 * ratio);
		parameter.incremental = true;
		parameter.borderWidth = 0;
		font = generator.generateFont(parameter);
		generator.dispose();

		addStage(new MenuUIStage(new SpriteBatch()).setOnStartListener(startListener));
	}

	public MenuScreen setOnStartListener(Runnable startListener) {
		this.startListener = startListener;
		return this;
	}
}
