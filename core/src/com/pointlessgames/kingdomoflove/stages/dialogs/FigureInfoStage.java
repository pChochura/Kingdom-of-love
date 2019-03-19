package com.pointlessgames.kingdomoflove.stages.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.pointlessgames.kingdomoflove.models.Ability;
import com.pointlessgames.kingdomoflove.models.figures.Figure;
import com.pointlessgames.kingdomoflove.models.figures.Plant;
import com.pointlessgames.kingdomoflove.models.figures.Structure;
import com.pointlessgames.kingdomoflove.stages.BaseStage;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.Settings;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.managers.TextureManager;

import java.util.Locale;

import static com.pointlessgames.kingdomoflove.screens.StartScreen.font;
import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;
import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class FigureInfoStage extends BaseStage {

	private Runnable onHideListener;
	private ClickListener clickListener;
	private SpriteBatch sP;
	private Stats stats;

	private Figure figure;
	private Actor dialog;

	private float time;
	private float alpha;
	private boolean hiding;

	private float textureSize;
	private float offset;

	private boolean existing;

	public FigureInfoStage(SpriteBatch sP, Stats stats) {
		this.sP = sP;
		this.stats = stats;

		touchInterruption = false;

		dialog = new Actor();
		dialog.setSize(900 * ratio, 1200 * ratio);
		dialog.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, Align.center);

		textureSize = dialog.getWidth() / 2;
		offset = 50 * ratio;
	}

	private void setDialogHeight() {
		float dividerHeight = 40 * ratio;

		font.getData().setScale(0.5f);
		float abilityHeight = new GlyphLayout(font, figure.getAbilityDescription(), Colors.textColor, dialog.getWidth() - 2 * offset, Align.center, true).height;

		float height = 5 * offset + textureSize + dividerHeight + abilityHeight;
		dialog.setHeight(height);
		dialog.setY(0.5f * Gdx.graphics.getHeight(), Align.center);
	}

	private void drawBackground() {
		sP.setColor(Color.BLACK.cpy().mul(1, 1, 1, 0.5f * alpha));
		TextureManager.getInstance().filledRect.draw(sP, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	private void drawDialog() {
		sP.setColor(Colors.barColor.cpy().mul(1, 1, 1, alpha));
		TextureManager.getInstance().rect.draw(sP, dialog.getX(), dialog.getY(), dialog.getWidth(), dialog.getHeight());
	}

	private void drawName() {
		float width = 650 * ratio;
		float height = 125 * ratio;
		float x = (Gdx.graphics.getWidth() - width) / 2;
		float y = dialog.getY() + dialog.getHeight() - height / 2;

		sP.setColor(Colors.inactiveColor.cpy().mul(1, 1, 1, alpha));
		TextureManager.getInstance().cutRect.draw(sP, x, y, width, height);

		font.getData().setScale(0.65f);
		font.setColor(Colors.tileColor.cpy().mul(1, 1, 1, alpha * alpha));
		font.draw(sP, figure.getName(), x, y + (height + font.getCapHeight()) / 2, width, Align.center, true);
	}

	private void drawLevel() {
		float size = textureSize * 0.2f;
		float halfSize = size * 0.5f;
		float x = dialog.getX() + offset + halfSize;
		float y = dialog.getY() + dialog.getHeight() - textureSize - 2 * offset;

		sP.setColor(Colors.loveColor.cpy().mul(1, 1, 1, alpha * alpha));
		TextureManager.getInstance().filledRect.draw(sP, x, y, halfSize, halfSize, size, size, 1, 1, 45);

		font.getData().setScale(size / (150f * ratio));
		font.setColor(Colors.text3Color.cpy().mul(1, 1, 1, alpha * alpha));
		font.draw(sP, String.valueOf(figure.getLevel()), x, y + (size + font.getCapHeight()) / 2, size, Align.center, false);
	}

	private void drawLifeBar() {
		float size = textureSize * 0.2f;
		float halfSize = size * 0.5f;
		float x = dialog.getX() + offset + size;
		float y = dialog.getY() + dialog.getHeight() - textureSize - 2 * offset;
		float lifePercentage = ((Plant)figure).getLife();
		float maxLife = ((Plant)figure).getMaxLife();
		float life = lifePercentage * maxLife;

		sP.setColor(Colors.inactiveColor.cpy().mul(1, 1, 1, alpha * alpha));
		TextureManager.getInstance().filledRect.draw(sP, x, y, 0, halfSize, textureSize - (size + halfSize), size, 1, 0.75f, 0);

		sP.setColor(Colors.loveColor.cpy().mul(1, 1, 1, alpha * alpha));
		TextureManager.getInstance().filledRect.draw(sP, x, y, 0, halfSize, MathUtils.lerp(0, textureSize - (size + halfSize), lifePercentage), size, 1, 0.75f, 0);

		font.getData().setScale(0.3f);
		font.setColor(Colors.tile2Color.cpy().mul(1, 1, 1, alpha * alpha));
		font.draw(sP, String.format(Locale.getDefault(), "%.1f/%.0f", life, maxLife), x, y + halfSize + 0.5f * font.getCapHeight(), textureSize - (size + halfSize), Align.center, false);
	}

	private void drawDivider() {
		float size = 40 * ratio;
		float halfSize = 0.5f * size;
		float width = 300 * ratio;
		float x = dialog.getX() + 0.5f * (dialog.getWidth() - size);
		float y = dialog.getY() + dialog.getHeight() - textureSize - 3 * offset - 0.25f * size;

		sP.setColor(Colors.tile2Color.cpy().mul(1, 1, 1, alpha * alpha));
		TextureManager.getInstance().filledRect.draw(sP, x - width, y - 1.5f / ratio + halfSize, width - 1.5f / ratio, 3 / ratio);
		TextureManager.getInstance().outlineRect.draw(sP, x, y, halfSize, halfSize, size, size, 1, 1, 45);
		TextureManager.getInstance().filledRect.draw(sP, x + size + 1.5f / ratio, y - 1.5f / ratio + halfSize, width - 1.5f / ratio, 3 / ratio);
	}

	private void drawFigureInfo() {
		drawName();

		float iconSize = (textureSize - 4 * offset) / 3;
		float y = dialog.getY() + dialog.getHeight() - 2 * offset;

		Ability ability = figure.getAbility(stats);
		int capacity = figure instanceof Structure ? ((Structure)figure).getCapacity() : 0;
		int moneyProduction = (int)ability.getAmount(Ability.ProductionType.MONEY);
		float loveProduction = ability.getAmount(Ability.ProductionType.LOVE);

		if(existing) {
			sP.setColor(Color.WHITE.cpy().mul(1, 1, 1, alpha * alpha));
			sP.draw(figure.getTexture(), dialog.getX() + offset, y - textureSize, textureSize, textureSize);

			sP.draw(TextureManager.getInstance().getTexture(TextureManager.MONEY), dialog.getX() + textureSize + 2 * offset, y - iconSize - offset, iconSize, iconSize);
			sP.draw(TextureManager.getInstance().getTexture(TextureManager.CAPACITY), dialog.getX() + textureSize + 2 * offset, y - 2 * iconSize - 2 * offset, iconSize, iconSize);
			sP.draw(TextureManager.getInstance().getTexture(TextureManager.LOVE), dialog.getX() + textureSize + 2 * offset, y - 3 * iconSize - 3 * offset, iconSize, iconSize);

			font.getData().setScale(0.4f);
			font.setColor(Colors.textColor.cpy().mul(1, 1, 1, alpha * alpha));
			font.draw(sP, String.format(Locale.getDefault(), "%+d$", moneyProduction),
					dialog.getX() + textureSize + 2 * offset + iconSize, y + 0.5f * (iconSize + font.getCapHeight()) - iconSize - offset, iconSize, Align.left, false);
			font.draw(sP, String.format(Locale.getDefault(), "%d", capacity),
					dialog.getX() + textureSize + 2 * offset + iconSize, y + 0.5f * (iconSize + font.getCapHeight()) - 2 * iconSize - 2 * offset, iconSize, Align.left, false);
			font.draw(sP, String.format(Locale.getDefault(), "%+.1f%%", loveProduction),
					dialog.getX() + textureSize + 2 * offset + iconSize, y + 0.5f * (iconSize + font.getCapHeight()) - 3 * iconSize - 3 * offset, iconSize, Align.left, false);
		} else {
			sP.setColor(Color.WHITE.cpy().mul(1, 1, 1, alpha * alpha));
			sP.draw(figure.getTexture(), dialog.getX() + 0.5f * (dialog.getWidth() - textureSize), y - textureSize, textureSize, textureSize);

			if(capacity != 0) {
				sP.draw(TextureManager.getInstance().getTexture(TextureManager.CAPACITY), dialog.getX() + offset, y - textureSize, iconSize, iconSize);

				font.getData().setScale(0.4f);
				font.setColor(Colors.textColor.cpy().mul(1, 1, 1, alpha * alpha));
				font.draw(sP, String.format(Locale.getDefault(), "%+d", capacity),
						dialog.getX() + offset + iconSize, y + 0.5f * (iconSize + font.getCapHeight()) - textureSize, iconSize, Align.left, false);
			}
		}

		font.getData().setScale(0.5f);
		font.setColor(Colors.textColor.cpy().mul(1, 1, 1, alpha * alpha));
		font.draw(sP, figure.getAbilityDescription(), dialog.getX() + offset, y - textureSize - 2 * offset, dialog.getWidth() - 2 * offset, Align.center, true);

		drawDivider();

		if(existing && figure instanceof Plant)
			drawLifeBar();

		if(existing && figure.hasLevels())
			drawLevel();
	}

	@Override public void draw() {
		sP.begin();

		drawBackground();
		drawDialog();
		drawFigureInfo();

		sP.setColor(Color.WHITE);
		sP.end();
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
		this.existing = stats.figures.contains(figure);
		setDialogHeight();
	}

	public FigureInfoStage setClickListener(ClickListener clickListener) {
		this.clickListener = clickListener;
		return this;
	}

	public interface ClickListener {
		void onCancelClick();
	}
}