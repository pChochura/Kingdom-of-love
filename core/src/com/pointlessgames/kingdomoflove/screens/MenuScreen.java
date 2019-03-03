package com.pointlessgames.kingdomoflove.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.pointlessgames.kingdomoflove.renderers.CustomShapeRenderer;
import com.pointlessgames.kingdomoflove.stages.ui.MenuUIStage;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.Settings;
import com.pointlessgames.kingdomoflove.utils.SoundManager;
import com.pointlessgames.kingdomoflove.utils.TextureManager;

import java.util.ArrayList;

import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;

public class MenuScreen extends BaseScreen {

	public static BitmapFont font;
	private StretchViewport viewport;
	private CustomShapeRenderer sR;
	private SpriteBatch sP;

	private InputMultiplexer input;
	private ArrayList<Stage> stages;
	private Runnable startListener;

	@Override public void show() {
		Vector2 worldSize = Settings.getWorldSize();
		viewport = new StretchViewport(worldSize.x, worldSize.y, new OrthographicCamera(worldSize.x, worldSize.y));

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arcon.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = (int) (100 * ratio);
		parameter.incremental = true;
		parameter.borderWidth = 0;
		font = generator.generateFont(parameter);
		generator.dispose();

		sR = new CustomShapeRenderer();
		sP = new SpriteBatch();

		TextureManager.loadTextures();
		SoundManager.loadSounds();

		sP.enableBlending();
		sP.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		sR.setAutoShapeType(true);
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Gdx.input.setCatchBackKey(true);

		configureStages();

		input = new InputMultiplexer();
		for(int i = stages.size() - 1; i >= 0; i--) input.addProcessor(stages.get(i));
		Gdx.input.setInputProcessor(input);
	}

	private void configureStages() {
		stages = new ArrayList<>();
		stages.add(new MenuUIStage(sP, sR).setOnStartListener(startListener));
	}

	@Override public void render(float delta) {
		Gdx.gl.glClearColor(Colors.bgColor.r, Colors.bgColor.g, Colors.bgColor.b, Colors.bgColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		viewport.getCamera().update();

		//Updating
		for(Stage s : stages)
			s.act(delta);

		//Drawing
		for(Stage s : stages)
			s.draw();
	}

	@Override public void dispose() {
		for(Stage s : stages)
			s.dispose();

		TextureManager.dispose();
		SoundManager.dispose();
	}

	public MenuScreen setOnStartListener(Runnable startListener) {
		this.startListener = startListener;
		return this;
	}
}
