package com.pointlessgames.kingdomoflove.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
import com.pointlessgames.kingdomoflove.utils.ScrollableGestureDetector;
import com.pointlessgames.kingdomoflove.utils.Settings;
import com.pointlessgames.kingdomoflove.utils.SoundManager;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.TextureManager;

import static com.pointlessgames.kingdomoflove.utils.Settings.HEIGHT;
import static com.pointlessgames.kingdomoflove.utils.Settings.WIDTH;
import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;

public class StartScreen extends BaseScreen implements BackgroundStage.OnTileClickedListener,
		FiguresStage.OnFigureClickListener, StartUIStage.ButtonClickListener {

	public static BitmapFont font;
	private CustomShapeRenderer sR;
	private SpriteBatch sP;

	private Runnable endListener;
	private Stats stats;
	private float duration = 2f;
	private float time;

	public StartScreen() {
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
		stats = new Stats();
		stats.load();
		sR.setAutoShapeType(true);

		addStage(new BackgroundStage(sP, sR, stats).setOnTileClickedListener(this));
		addStage(new FiguresStage(sP, sR, stats).setOnFigureClickedListener(this));
		addStage(new StartUIStage(sP, sR, stats).setOnButtonClickListener(this));

		if(stats.figures.isEmpty()) {
			Monument m = new Monument();
			m.setMapPos(WIDTH / 2, HEIGHT / 2);
			stats.figures.add(m);
		}
	}

	@Override public void render(float delta) {
		super.render(delta);

		if(time < duration) {
			time += delta;
			float percent = time / duration;
			Color c = sP.getColor().cpy();
			sP.begin();
			sP.setColor(Colors.bgColor.r, Colors.bgColor.g, Colors.bgColor.b, MathUtils.lerp(1, 0, percent));
			sP.draw(TextureManager.getInstance().getTexture(TextureManager.BACKGROUND), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			sP.setColor(Color.WHITE);
			sP.end();
			sP.setColor(c);
		}
	}

	@Override public void dispose() {
		super.dispose();
		for(int i = stats.figures.size() - 1; i >= 0; i--)
			stats.figures.get(i).dispose();
	}

	@Override public void onEmptyTileClicked(int mapX, int mapY) {
		if(Settings.soundsOn)
			SoundManager.getInstance().getSound(SoundManager.SELECT).play(0.5f);
		PickFigureStage pickFigureStage = new PickFigureStage(sP, sR, stats);
		ScrollableGestureDetector gestureDetector = addStage(pickFigureStage);
		pickFigureStage.setClickListener(new PickFigureStage.ClickListener() {
			@Override public void onCancelClick() {
				pickFigureStage.hide(() -> removeStage(pickFigureStage, gestureDetector));
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
					stats.sortFigures();
					pickFigureStage.hide(() -> removeStage(pickFigureStage, gestureDetector));

					for(Figure figure : stats.figures)
						if(figure instanceof Road)
							figure.orientInSpace(stats);

					if(Settings.soundsOn)
						SoundManager.getInstance().getSound(SoundManager.PICK_FIGURE).play(0.5f);
				} else if(Settings.soundsOn) SoundManager.getInstance().getSound(SoundManager.SELECT_ERROR).play(0.5f);
			}
		});
	}

	@Override public void onFigureClick(Figure f) {
		stats.setCurrentFigure(f);
		if(Settings.soundsOn)
			SoundManager.getInstance().getSound(SoundManager.SELECT).play(0.5f);
		FigureInfoStage figureInfoStage = new FigureInfoStage(sP, sR, stats);
		figureInfoStage.setFigure(f);
		ScrollableGestureDetector gestureDetector = addStage(figureInfoStage);
		figureInfoStage.setClickListener(new FigureInfoStage.ClickListener() {
			@Override public void onCancelClick() {
				stats.setCurrentFigure(null);
				figureInfoStage.hide(() -> removeStage(figureInfoStage, gestureDetector));
			}

			@Override public void onUpgradeClick() {
				if(stats.money >= f.getUpdateCost()) {
					stats.money -= f.getUpdateCost();
					f.levelUp();
					if(Settings.soundsOn)
						SoundManager.getInstance().getSound(SoundManager.UPGRADE).play(0.5f);
				} else if(Settings.soundsOn) SoundManager.getInstance().getSound(SoundManager.SELECT_ERROR).play(0.5f);
			}
		});
	}

	@Override public void nextDayButtonClicked() {
		if(Settings.soundsOn)
			SoundManager.getInstance().getSound(SoundManager.NEXT_DAY).play(0.5f);
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