package com.pointlessgames.kingdomoflove.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;
import com.pointlessgames.kingdomoflove.actors.Button;
import com.pointlessgames.kingdomoflove.models.figures.Figure;
import com.pointlessgames.kingdomoflove.models.figures.Structure;
import com.pointlessgames.kingdomoflove.renderers.CustomShapeRenderer;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.Settings;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.TextureManager;

import java.util.Locale;

import static com.pointlessgames.kingdomoflove.screens.StartScreen.font;
import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;

public class FigureInfoStage extends GestureStage {

	private final float bottomBarHeight = 500 * ratio;
	private final float textureSize = 350 * ratio;
	private final float offsetX = 2 * 75 * ratio + textureSize;
	private boolean hiding;
	private float bottomBarY;
	private float time;

	private Runnable onHideListener;
	private ClickListener clickListener;
	private CustomShapeRenderer sR;
	private SpriteBatch sP;
	private Stats stats;

	private Figure figure;

	public FigureInfoStage(SpriteBatch sP, CustomShapeRenderer sR, Stats stats) {
		this.sP = sP;
		this.sR = sR;
		this.stats = stats;

		touchInterruption = false;
	}

	private void drawBottomBar() {
		sR.begin(ShapeRenderer.ShapeType.Filled);
		sR.setColor(Colors.barColor);
		sR.rect(0, bottomBarY, Gdx.graphics.getWidth(), bottomBarHeight);
		sR.end();
	}

	private void drawFigureInfo() {
		sP.begin();

		//Texture
		sP.draw(figure.getTexture(), 75 * ratio, 75 * ratio + bottomBarY, textureSize, textureSize);

		//Title
		font.getData().setScale(0.5f);
		font.setColor(Colors.textColor);
		font.draw(sP, String.format(Locale.getDefault(), "%s (lvl. %d)", figure.getName(), figure.getLevel()),
				offsetX, bottomBarHeight - 75 * ratio + bottomBarY, 100 * ratio, Align.left, false);

		//Ability description
		font.getData().setScale(0.3f);
		font.setColor(Colors.text2Color);
		font.draw(sP, figure.getAbilityDescription(), offsetX, bottomBarHeight - 135 * ratio + bottomBarY, Gdx.graphics.getWidth() - offsetX - 50 * ratio, Align.left, true);

		float height = new GlyphLayout(font, figure.getAbilityDescription(), Colors.text2Color, Gdx.graphics.getWidth() - offsetX - 50 * ratio, Align.left, true).height;

		if(figure instanceof Structure) {
			int capacity = ((Structure) figure).getCapacity();
			if(capacity != 0) {
				font.getData().setScale(0.3f);
				font.setColor(Colors.text2Color);
				sP.draw(TextureManager.getInstance().getTexture(TextureManager.CAPACITY), offsetX, bottomBarHeight - 210 * ratio + bottomBarY - height, 75 * ratio, 75 * ratio);
				font.getData().setScale(0.4f);
				font.draw(sP, String.valueOf(capacity), 2 * 75 * ratio + textureSize + 80 * ratio, bottomBarHeight - 155 * ratio + bottomBarY - height, 
						Gdx.graphics.getWidth() - 3 * 75 * ratio + textureSize, Align.left, true);
			}
		}

		sP.end();
	}

	@Override public void draw() {
		drawBottomBar();
		drawFigureInfo();
	}

	@Override public void act(float delta) {
		if(hiding && time > 0 || !hiding && time < Settings.duration) {
			time += hiding ? -delta : delta;
			float percent = hiding ? Interpolation.exp5In.apply(time / Settings.duration) : Interpolation.exp5Out.apply(time / Settings.duration);
			bottomBarY = MathUtils.lerp(-bottomBarHeight, 0, percent);
		} else {
			bottomBarY = 0;
			if(hiding && onHideListener != null) onHideListener.run();
		}
	}

	@Override public boolean tap(float x, float y, int count, int button) {
		Vector2 pos = new Vector2(x, Gdx.graphics.getHeight() - y);
		if(pos.y > bottomBarHeight) clickListener.onCancelClick();
		return true;
	}

	@Override public boolean keyDown(int keyCode) {
		if(keyCode == Input.Keys.BACK || keyCode == Input.Keys.ESCAPE) {
			clickListener.onCancelClick();
			return true;
		} else return false;
	}

	public void hide(Runnable onHideListener) {
		this.onHideListener = onHideListener;
		hiding = true;
	}

	public void setFigure(Figure figure) {
		this.figure = figure;
	}

	public FigureInfoStage setClickListener(ClickListener clickListener) {
		this.clickListener = clickListener;
		return this;
	}

	public interface ClickListener {
		void onCancelClick();
	}
}