package com.pointlessgames.kingdomoflove.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.pointlessgames.kingdomoflove.models.figures.Figure;
import com.pointlessgames.kingdomoflove.models.figures.Monument;
import com.pointlessgames.kingdomoflove.models.figures.Road;
import com.pointlessgames.kingdomoflove.renderers.CustomShapeRenderer;
import com.pointlessgames.kingdomoflove.stages.BackgroundStage;
import com.pointlessgames.kingdomoflove.stages.FigureInfoStage;
import com.pointlessgames.kingdomoflove.stages.FiguresStage;
import com.pointlessgames.kingdomoflove.stages.PickFigureStage;
import com.pointlessgames.kingdomoflove.stages.ui.StartUIStage;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.stages.GestureStage;
import com.pointlessgames.kingdomoflove.utils.Settings;
import com.pointlessgames.kingdomoflove.utils.SoundManager;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.TextureManager;

import java.util.ArrayList;

import static com.pointlessgames.kingdomoflove.utils.Settings.HEIGHT;
import static com.pointlessgames.kingdomoflove.utils.Settings.WIDTH;
import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;

public class StartScreen extends BaseScreen implements
		BackgroundStage.OnTileClickedListener, FiguresStage.OnFigureClickListener, StartUIStage.ButtonClickListener {

	public static BitmapFont font;
	private StretchViewport viewport;
	private CustomShapeRenderer sR;
	private SpriteBatch sP;

	private Runnable endListener;
	private InputMultiplexer input;
	private ArrayList<GestureStage> stages;
	private Stats stats;
	private float duration = 2f;
	private float time;

	@Override public void show() {
		Vector2 worldSize = Settings.getWorldSize();
		viewport = new StretchViewport(worldSize.x, worldSize.y, new OrthographicCamera(worldSize.x, worldSize.y));

		TextureManager.loadTextures();
		SoundManager.loadSounds();

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arcon.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = (int) (100 * ratio);
		parameter.incremental = true;
		parameter.borderWidth = 0;
		font = generator.generateFont(parameter);
		generator.dispose();

		sR = new CustomShapeRenderer();
		sP = new SpriteBatch();
		stats = new Stats();
		stats.load();

		sP.enableBlending();
		sP.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		sR.setAutoShapeType(true);
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Gdx.input.setCatchBackKey(true);

		configureStages();

		input = new InputMultiplexer();
		for(int i = stages.size() - 1; i >= 0; i--) input.addProcessor(new GestureDetector(stages.get(i)));
		Gdx.input.setInputProcessor(input);

		if(stats.figures.isEmpty()) {
			Monument m = new Monument();
			m.setMapPos(WIDTH / 2, HEIGHT / 2);
			stats.figures.add(m);
		}
	}

	private void configureStages() {
		stages = new ArrayList<>();
		stages.add(new BackgroundStage(sP, sR, stats).setOnTileClickedListener(this));
		stages.add(new FiguresStage(sP, sR, stats).setOnFigureClickedListener(this));
		stages.add(new StartUIStage(sP, sR, stats).setOnButtonClickListener(this));
	}

	@Override public void render(float delta) {
		Gdx.gl.glClearColor(Colors.bgColor.r, Colors.bgColor.g, Colors.bgColor.b, Colors.bgColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		viewport.getCamera().update();

		//Updating
		for(int i = 0; i < stages.size(); i++) stages.get(i).act(delta);

		//Drawing
		for(int i = 0; i < stages.size(); i++) stages.get(i).draw();

		if(time < duration) {
			time += delta;
			float percent = time / duration;
			Color c = sP.getColor().cpy();
			sP.begin();
			sP.setColor(Colors.bgColor.r, Colors.bgColor.g, Colors.bgColor.b, MathUtils.lerp(1, 0, percent));
			sP.draw(TextureManager.background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			sP.end();
			sP.setColor(c);
		}
	}

	@Override public void dispose() {
		for(Stage s : stages)
			s.dispose();

		for(int i = stats.figures.size() - 1; i >= 0; i--)
			stats.figures.get(i).dispose();

		TextureManager.dispose();
		SoundManager.dispose();
	}

	@Override public void onEmptyTileClicked(int mapX, int mapY) {
		if(Settings.soundsOn)
			SoundManager.select.play(0.5f);
		PickFigureStage pickFigureStage = new PickFigureStage(sP, sR, stats);
		GestureDetector processor = new GestureDetector(pickFigureStage);
		pickFigureStage.setClickListener(new PickFigureStage.ClickListener() {
			@Override public void onCancelClick() {
				pickFigureStage.hide(() -> {
					input.removeProcessor(processor);
					stages.remove(pickFigureStage);
				});
			}
			@Override public void onFigureClick(Figure f) {
				if(stats.money >= f.getCost()) {
					stats.money -= f.getCost();
					stats.love += f.getLove();

					f.setMapPos(mapX, mapY);
					f.orientInSpace(stats);
					f.setScale(0);
					f.addAction(Actions.scaleTo(1, 1, Settings.duration, new Interpolation.SwingOut(3f)));
					stats.figures.add(f);
					pickFigureStage.hide(() -> {
						input.removeProcessor(processor);
						stages.remove(pickFigureStage);
					});

					for(Figure figure : stats.figures)
						if(figure instanceof Road)
							figure.orientInSpace(stats);

					if(Settings.soundsOn)
						SoundManager.pickFigure.play(0.5f);
				} else if(Settings.soundsOn) SoundManager.selectError.play(0.5f);
			}
		});

		input.addProcessor(0, processor);
		stages.add(pickFigureStage);
	}

	@Override public void onFigureClick(Figure f) {
		if(Settings.soundsOn)
			SoundManager.select.play(0.5f);
		FigureInfoStage figureInfoStage = new FigureInfoStage(sP, sR, stats);
		figureInfoStage.setFigure(f);
		GestureDetector processor = new GestureDetector(figureInfoStage);
		figureInfoStage.setClickListener(new FigureInfoStage.ClickListener() {
			@Override public void onCancelClick() {
				figureInfoStage.hide(() -> {
					input.removeProcessor(processor);
					stages.remove(figureInfoStage);
				});
			}

			@Override public void onUpgradeClick() {
				if(stats.money >= f.getUpdateCost()) {
					stats.money -= f.getUpdateCost();
					f.levelUp();
					if(Settings.soundsOn)
						SoundManager.upgrade.play(0.5f);
				} else if(Settings.soundsOn) SoundManager.selectError.play(0.5f);
			}
		});
		input.addProcessor(0, processor);
		stages.add(figureInfoStage);
	}

	@Override public void nextDayButtonClicked() {
		if(Settings.soundsOn)
			SoundManager.nextDay.play(0.5f);
		stats.day++;
		for(int i = stats.figures.size() - 1; i >= 0; i--)
			stats.figures.get(i).triggerAbility(stats);

		for(Figure f : stats.figures)
			if(f instanceof Road) ((Road) f).checked = false;

		stats.nextDay();

		if(stats.love == 0) endListener.run();
	}

	public StartScreen setOnEndListener(Runnable endListener) {
		this.endListener = endListener;
		return this;
	}
}