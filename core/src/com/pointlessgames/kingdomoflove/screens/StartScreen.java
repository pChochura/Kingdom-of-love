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
import com.pointlessgames.kingdomoflove.stages.BackgroundStage;
import com.pointlessgames.kingdomoflove.stages.FigureMenuStage;
import com.pointlessgames.kingdomoflove.stages.FiguresStage;
import com.pointlessgames.kingdomoflove.stages.PickFigureMenuStage;
import com.pointlessgames.kingdomoflove.stages.dialogs.DestroyFigureStage;
import com.pointlessgames.kingdomoflove.stages.dialogs.FigureInfoStage;
import com.pointlessgames.kingdomoflove.stages.dialogs.PickFigureStage;
import com.pointlessgames.kingdomoflove.stages.dialogs.UpgradeFigureStage;
import com.pointlessgames.kingdomoflove.stages.ui.StartUIStage;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.Settings;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.managers.TextureManager;
import com.pointlessgames.kingdomoflove.utils.overridden.ScrollableGestureDetector;

import static com.pointlessgames.kingdomoflove.utils.Settings.HEIGHT;
import static com.pointlessgames.kingdomoflove.utils.Settings.WIDTH;
import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;

public class StartScreen extends BaseScreen implements BackgroundStage.OnTileClickedListener,
		FiguresStage.OnFigureClickListener, StartUIStage.ButtonClickListener {

	public static BitmapFont font;
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

		sP = new SpriteBatch();
		stats = new Stats();
		stats.load();

		addStage(new BackgroundStage(sP, stats).setOnTileClickedListener(this));
		addStage(new FiguresStage(sP, stats).setOnFigureClickedListener(this));
		addStage(new StartUIStage(sP, stats).setOnButtonClickListener(this));

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
		showPickFigureStage(mapX, mapY);
	}

	@Override public void onFigureClick(Figure f) {
		stats.setCurrentFigure(f);
		showFigureMenuStage(f);
	}

	private void showPickFigureStage(int mapX, int mapY) {
		PickFigureStage pickFigureStage = new PickFigureStage(sP, stats);
		ScrollableGestureDetector gestureDetector = addStage(pickFigureStage);
		pickFigureStage.setClickListener(new PickFigureStage.ClickListener() {
			@Override public void onCancelClick() {
				removeStage(gestureDetector, pickFigureStage.touchInterruption);
				pickFigureStage.hide(() -> removeStage(pickFigureStage));
			}
			@Override public void onFigureClick(Figure f) {
				if(stats.money >= f.getCost()) {
					removeStage(gestureDetector, pickFigureStage.touchInterruption);
					pickFigureStage.hide(() -> removeStage(pickFigureStage));

					f.setMapPos(mapX, mapY);
					showPickFigureMenuStage(f);
				}
			}
			@Override public void onInfoClick(Figure f) {
				showFigureInfoStage(f);
			}
		});
	}

	private void showPickFigureMenuStage(Figure f) {
		stats.setCurrentFigure(f);
		f.setScale(0);
		f.addAction(Actions.scaleTo(1, 1, Settings.duration, new Interpolation.SwingOut(3f)));
		PickFigureMenuStage pickFigureMenuStage = new PickFigureMenuStage(sP, stats);
		pickFigureMenuStage.setFigure(f);
		ScrollableGestureDetector gestureDetector = addStage(getStagesCount() - 2, pickFigureMenuStage);
		pickFigureMenuStage.setClickListener(new PickFigureMenuStage.ClickListener() {
			@Override public void onCancelClick() {
				stats.setCurrentFigure(null);
				removeStage(gestureDetector, pickFigureMenuStage.touchInterruption);
				pickFigureMenuStage.hide(() -> removeStage(pickFigureMenuStage));
			}

			@Override public void onConfirmClick() {
				stats.money -= f.getCost();
				stats.love += f.getLove();
				stats.setCurrentFigure(null);
				removeStage(gestureDetector, pickFigureMenuStage.touchInterruption);
				pickFigureMenuStage.hide(() -> removeStage(pickFigureMenuStage));

				f.orientInSpace(stats);
				stats.figures.add(f);
				stats.sortFigures();

				for(Figure figure : stats.figures)
					if(figure instanceof Road)
						figure.orientInSpace(stats);
			}
		});
	}

	private void showFigureMenuStage(Figure f) {
		FigureMenuStage figureMenuStage = new FigureMenuStage(sP, stats);
		figureMenuStage.setFigure(f);
		ScrollableGestureDetector gestureDetector = addStage(getStagesCount() - 1, figureMenuStage);
		figureMenuStage.setClickListener(new FigureMenuStage.ClickListener() {
			@Override public void onCancelClick() {
				stats.setCurrentFigure(null);
				removeStage(gestureDetector, figureMenuStage.touchInterruption);
				figureMenuStage.hide(() -> removeStage(figureMenuStage));
			}

			@Override public void onUpgradeClick() {
				showUpgradeFigureStage(f);
			}

			@Override public void onInfoClick() {
				showFigureInfoStage(f);
			}

			@Override public void onDestroyClick() {
				showDestroyFigureStage(f, () -> {
					stats.setCurrentFigure(null);
					removeStage(gestureDetector, figureMenuStage.touchInterruption);
					figureMenuStage.hide(() -> removeStage(figureMenuStage));
				});
			}
		});
	}

	private void showUpgradeFigureStage(Figure f) {
		UpgradeFigureStage upgradeFigureStage = new UpgradeFigureStage(sP, stats);
		upgradeFigureStage.setFigure(f);
		ScrollableGestureDetector gestureDetector = addStage(upgradeFigureStage);
		upgradeFigureStage.setClickListener(new UpgradeFigureStage.ClickListener() {
			@Override public void onCancelClick() {
				removeStage(gestureDetector, upgradeFigureStage.touchInterruption);
				upgradeFigureStage.hide(() -> removeStage(upgradeFigureStage));
			}

			@Override public void onUpgradeClick() {
				if(f.canUpgrade(stats)) {
					removeStage(gestureDetector, upgradeFigureStage.touchInterruption);
					upgradeFigureStage.hide(() -> removeStage(upgradeFigureStage));
					stats.money -= f.getUpgradeCost();
					stats.love += f.getLove();
					f.levelUp();
				}
			}
		});
	}

	private void showFigureInfoStage(Figure f) {
		FigureInfoStage figureInfoStage = new FigureInfoStage(sP, stats);
		figureInfoStage.setFigure(f);
		ScrollableGestureDetector gestureDetector = addStage(figureInfoStage);
		figureInfoStage.setClickListener(() -> {
			removeStage(gestureDetector, figureInfoStage.touchInterruption);
			figureInfoStage.hide(() -> removeStage(figureInfoStage));
		});
	}

	private void showDestroyFigureStage(Figure f, Runnable onDestroyed) {
		DestroyFigureStage destroyFigureStage = new DestroyFigureStage(sP);
		destroyFigureStage.setFigure(f);
		ScrollableGestureDetector gestureDetector = addStage(destroyFigureStage);
		destroyFigureStage.setClickListener(new DestroyFigureStage.ClickListener() {
			@Override public void onCancelClick() {
				removeStage(gestureDetector, destroyFigureStage.touchInterruption);
				destroyFigureStage.hide(() -> removeStage(destroyFigureStage));
			}

			@Override public void onDestroyClick(int money, float love) {
				removeStage(gestureDetector, destroyFigureStage.touchInterruption);
				destroyFigureStage.hide(() -> removeStage(destroyFigureStage));

				stats.figures.remove(f);
				for(Figure figure : stats.figures)
					figure.orientInSpace(stats);

				stats.money += money;
				stats.love += love;

				onDestroyed.run();
			}
		});
	}

	@Override public void nextDayButtonClicked() {
		for(Figure f : stats.figures)
			if(f instanceof Road) ((Road) f).checked = false;

		stats.nextDay();

		if(stats.love <= 0) endListener.run();
	}

	public StartScreen setOnEndListener(Runnable endListener) {
		this.endListener = endListener;
		return this;
	}
}