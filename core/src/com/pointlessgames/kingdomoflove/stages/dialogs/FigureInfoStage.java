package com.pointlessgames.kingdomoflove.stages.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.pointlessgames.kingdomoflove.models.Ability;
import com.pointlessgames.kingdomoflove.models.figures.Figure;
import com.pointlessgames.kingdomoflove.models.figures.Structure;
import com.pointlessgames.kingdomoflove.utils.overridden.CustomShapeRenderer;
import com.pointlessgames.kingdomoflove.stages.BaseStage;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.Settings;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.managers.TextureManager;

import java.util.Locale;

import static com.pointlessgames.kingdomoflove.screens.StartScreen.font;
import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;

public class FigureInfoStage extends BaseStage {

	private Runnable onHideListener;
	private ClickListener clickListener;
	private CustomShapeRenderer sR;
	private SpriteBatch sP;
	private Stats stats;

	private Figure figure;
	private Actor dialog;

	private float time;
	private float alpha;
	private boolean hiding;

	private float textureSize;
	private float offset;

	public FigureInfoStage(SpriteBatch sP, CustomShapeRenderer sR, Stats stats) {
		this.sP = sP;
		this.sR = sR;
		this.stats = stats;

		touchInterruption = false;

		dialog = new Actor();
		dialog.setSize(900 * ratio, 1200 * ratio);
		dialog.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, Align.center);

		textureSize = dialog.getWidth() / 2;
		offset = 50 * ratio;
	}

	private void drawBackground() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		sR.begin(ShapeRenderer.ShapeType.Filled);
		sR.setColor(Color.BLACK.cpy().mul(1, 1, 1, 0.5f * alpha));
		sR.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		sR.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	private void drawDialog() {
		if(alpha != 1) {
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		}

		sR.begin(ShapeRenderer.ShapeType.Filled);
		sR.setColor(Colors.barColor.cpy().mul(1, 1, 1, alpha));
		sR.rect(dialog.getX(), dialog.getY(), dialog.getWidth(), dialog.getHeight(), 6 * ratio);
		sR.end();

		if(alpha != 1) Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	private void drawName() {
		if(alpha != 1) {
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		}

		float width = 650 * ratio;
		float height = 125 * ratio;
		float x = (Gdx.graphics.getWidth() - width) / 2;
		float y = dialog.getY() + dialog.getHeight() - height / 2;
		sR.begin(ShapeRenderer.ShapeType.Filled);
		sR.setColor(Colors.inactiveColor.cpy().mul(1, 1, 1, alpha));
		sR.cutRect(x, y, width, height, MathUtils.PI / 18, 4 * ratio);
		sR.end();

		sP.begin();

		font.getData().setScale(0.65f);
		font.setColor(Colors.tileColor.cpy().mul(1, 1, 1, alpha * alpha));
		font.draw(sP, figure.getName(), x, y + (height + font.getCapHeight()) / 2, width, Align.center, true);

		sP.end();

		if(alpha != 1) Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	private void drawLevel() {
		if(alpha != 1) {
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		}

		sR.begin(ShapeRenderer.ShapeType.Filled);
		sR.setColor(Colors.loveColor.cpy().mul(1, 1, 1, alpha * alpha));
		float size = textureSize * 0.2f;
		float halfSize = size * 0.5f;
		float x = dialog.getX() + offset + halfSize;
		float y = dialog.getY() + dialog.getHeight() - textureSize - 2 * offset + halfSize;
		sR.rect(x, y, halfSize, halfSize, size, size, 1, 1, 45);
		sR.end();

		sP.begin();
		font.getData().setScale(size / (150f * ratio));
		font.setColor(Colors.text3Color.cpy().mul(1, 1, 1, alpha * alpha));
		font.draw(sP, String.valueOf(figure.getLevel()), x, y + (size + font.getCapHeight()) / 2, size, Align.center, false);
		font.setColor(Color.WHITE);
		sP.end();

		if(alpha != 1) Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	private void drawDivider() {
		if(alpha != 1) {
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		}

		sR.begin(ShapeRenderer.ShapeType.Filled);

		float radius = 15 * ratio;
		float x = Gdx.graphics.getWidth() / 2;
		float width = 300 * ratio;
		float y = dialog.getY() + dialog.getHeight() / 2;
		sR.setColor(Colors.tile2Color.cpy().mul(1, 1, 1, alpha * alpha));
		sR.rectLine(x - width, y, x - radius, y, 4 * ratio);
		sR.rectLine(x + radius, y, x + width, y, 4 * ratio);

		sR.set(ShapeRenderer.ShapeType.Line);
		Gdx.gl.glLineWidth(4 * ratio);
		sR.circle(x, y, radius);

		sR.end();

		if(alpha != 1) Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	private void drawFigureInfo() {
		drawName();

		float y = dialog.getY() + dialog.getHeight() - 2 * offset;

		sP.begin();
		sP.setColor(Color.WHITE.cpy().mul(1, 1, 1, alpha * alpha));
		sP.draw(figure.getTexture(), dialog.getX() + offset, y - textureSize, textureSize, textureSize);

		Ability ability = figure.getAbility(stats);
		int capacity = figure instanceof Structure ? ((Structure) figure).getCapacity() : 0;
		int moneyProduction = ability.getProductionType() == Ability.ProductionType.MONEY ? (int)ability.getAmount() : 0;
		float loveProduction = ability.getProductionType() == Ability.ProductionType.LOVE ? ability.getAmount() : 0;

		float iconSize = (textureSize - 4 * offset) / 3;
		sP.draw(TextureManager.getInstance().getTexture(TextureManager.CAPACITY), dialog.getX() + textureSize + 2 * offset, y - iconSize - offset, iconSize, iconSize);
		sP.draw(TextureManager.getInstance().getTexture(TextureManager.MONEY), dialog.getX() + textureSize + 2 * offset, y - 2 * iconSize - 2 * offset, iconSize, iconSize);
		sP.draw(TextureManager.getInstance().getTexture(TextureManager.LOVE), dialog.getX() + textureSize + 2 * offset, y - 3 * iconSize - 3 * offset, iconSize, iconSize);

		font.getData().setScale(0.4f);
		font.setColor(Colors.textColor.cpy().mul(1, 1, 1, alpha * alpha));
		font.draw(sP, String.format(Locale.getDefault(), "%d", capacity),
				dialog.getX() + textureSize + 2 * offset + iconSize, y + 0.5f * (iconSize + font.getCapHeight()) - iconSize - offset, iconSize, Align.left, false);
		font.draw(sP, String.format(Locale.getDefault(), "%+d$", moneyProduction),
				dialog.getX() + textureSize + 2 * offset + iconSize, y + 0.5f * (iconSize + font.getCapHeight()) - 2 * iconSize - 2 * offset, iconSize, Align.left, false);
		font.draw(sP, String.format(Locale.getDefault(), "%+.1f%%", loveProduction),
				dialog.getX() + textureSize + 2 * offset + iconSize, y + 0.5f * (iconSize + font.getCapHeight()) - 3 * iconSize - 3 * offset, iconSize, Align.left, false);

		font.getData().setScale(0.5f);
		font.setColor(Colors.textColor.cpy().mul(1, 1, 1, alpha * alpha));
		font.draw(sP, figure.getAbilityDescription(), dialog.getX() + offset, y - textureSize - 2 * offset, dialog.getWidth() - 2 * offset, Align.left, true);

		sP.end();

		drawDivider();

		if(figure.hasLevels())
			drawLevel();
	}

	@Override public void draw() {
		drawBackground();
		drawDialog();
		drawFigureInfo();
	}

	@Override public void act(float delta) {
		if(hiding && time > 0 || !hiding && time < Settings.duration) {
			time += hiding ? -delta : delta;
			alpha = hiding ? Interpolation.exp5In.apply(time / Settings.duration) : Interpolation.exp5Out.apply(time / Settings.duration);
		} else if(hiding && onHideListener != null) onHideListener.run();
	}

	@Override public boolean tap(float x, float y, int count, int button) {
		Vector2 pos = new Vector2(x, Gdx.graphics.getHeight() - y);
		if(dialog.hit(pos.x - dialog.getX(), pos.y - dialog.getY(), true) == null) clickListener.onCancelClick();
		return true;
	}

	@Override public boolean keyDown(int keyCode) {
		if(keyCode == Input.Keys.BACK || keyCode == Input.Keys.ESCAPE) {
			clickListener.onCancelClick();
			return true;
		} else return false;
	}

	public void hide(Runnable onHideListener) {
		hiding = true;
		this.onHideListener = onHideListener;
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