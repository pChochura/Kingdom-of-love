package com.pointlessgames.kingdomoflove.stages.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.pointlessgames.kingdomoflove.renderers.CustomShapeRenderer;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.stages.GestureStage;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.TextureManager;
import com.pointlessgames.kingdomoflove.utils.Utils;

import java.util.Locale;

import static com.pointlessgames.kingdomoflove.screens.StartScreen.font;
import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;

public class StartUIStage extends GestureStage {

	private final float topBarHeight = 150 * ratio;

	private ButtonClickListener buttonClickListener;
	private CustomShapeRenderer sR;
	private SpriteBatch sP;
	private Stats stats;

	public StartUIStage(SpriteBatch sP, CustomShapeRenderer sR, Stats stats) {
		this.sP = sP;
		this.sR = sR;
		this.stats = stats;
	}

	private void drawStats() {
		drawTopBar();
		drawMoney();
		drawPopulation();
		drawLove();
		drawDay();
		drawNextDayButton();
	}

	private void drawTopBar() {
		sR.begin(ShapeRenderer.ShapeType.Filled);
		sR.setColor(Colors.barColor);
		sR.rect(0, Gdx.graphics.getHeight() - topBarHeight, Gdx.graphics.getWidth(), topBarHeight);
		sR.end();
	}

	private void drawMoney() {
		sP.begin();
		font.getData().setScale(0.4f);
		font.setColor(Colors.textColor);
		font.draw(sP, String.format(Locale.getDefault(), "%d (%+d)", stats.money, stats.getMoneyProduction()), 135 * ratio, Gdx.graphics.getHeight() - 60 * ratio, 100 * ratio, Align.left, false);
		sP.setColor(Color.WHITE);
		sP.draw(TextureManager.getInstance().money, 50 * ratio, Gdx.graphics.getHeight() - 110 * ratio, 75 * ratio, 75 * ratio);
		sP.end();
	}

	private void drawPopulation() {
		sP.begin();
		font.getData().setScale(0.4f);
		font.setColor(Colors.textColor);
		font.draw(sP, String.valueOf(stats.getPopulation()), Gdx.graphics.getWidth() / 2 - 25 * ratio, Gdx.graphics.getHeight() - 60 * ratio, 100 * ratio, Align.left, false);
		sP.setColor(Color.WHITE);
		sP.draw(TextureManager.getInstance().capacity, Gdx.graphics.getWidth() / 2 - 100 * ratio, Gdx.graphics.getHeight() - 110 * ratio, 75 * ratio, 75 * ratio);
		sP.end();
	}

	private void drawLove() {
		sR.begin(ShapeRenderer.ShapeType.Filled);
		sR.setColor(Colors.inactiveColor);
		sR.roundedRect(Gdx.graphics.getWidth() - 350 * ratio, Gdx.graphics.getHeight() - 100 * ratio, 200 * ratio, 50 * ratio, 25 * ratio);

		float width = Utils.map(stats.love, 0, 100, 0, 200 * ratio);
		sR.setColor(Colors.loveColor);
		sR.roundedRect(Gdx.graphics.getWidth() - 350 * ratio, Gdx.graphics.getHeight() - 100 * ratio, width, 50 * ratio, stats.love >= 25 * ratio ? 25 * ratio : stats.love);
		sR.end();

		sP.begin();
		font.getData().setScale(0.25f);
		font.setColor(Colors.text3Color);
		font.draw(sP, String.format(Locale.getDefault(), "%d (%+.1f)", MathUtils.round(stats.love), stats.getLoveProduction()),
				Gdx.graphics.getWidth() - 350 * ratio + width / 2 - 25 * ratio, Gdx.graphics.getHeight() - 70 * ratio, 50 * ratio, Align.center, false);
		sP.setColor(Color.WHITE);
		sP.draw(TextureManager.getInstance().love, Gdx.graphics.getWidth() - 125 * ratio, Gdx.graphics.getHeight() - 110 * ratio, 75 * ratio, 75 * ratio);
		sP.end();
	}

	private void drawDay() {
		sP.begin();
		font.getData().setScale(0.65f);
		font.setColor(Colors.textColor);
		font.draw(sP, String.format(Locale.getDefault(), "Day %d", stats.day), 0, Gdx.graphics.getHeight() - 1.5f * topBarHeight, Gdx.graphics.getWidth(), Align.center, false);
		sP.setColor(Color.WHITE);
		sP.end();
	}

	private void drawNextDayButton() {
		sR.begin(ShapeRenderer.ShapeType.Filled);
		sR.setColor(Colors.buttonColor);
		sR.roundedRect(Gdx.graphics.getWidth() - 350 * ratio, 150 * ratio, 300 * ratio, 75 * ratio, 25 * ratio);
		sR.end();

		sP.begin();
		font.getData().setScale(0.35f);
		font.setColor(Colors.text3Color);
		font.draw(sP, "Next day", Gdx.graphics.getWidth() - 350 * ratio, 200 * ratio, 300 * ratio, Align.center, false);
		sP.setColor(Color.WHITE);
		sP.end();
	}

	@Override public void draw() {
		drawStats();
	}

	@Override public boolean tap(float x, float y, int count, int button) {
		Vector2 pos = new Vector2(x, Gdx.graphics.getHeight() - y);
		if(pos.x >= Gdx.graphics.getWidth() - 350 * ratio && pos.y <= Gdx.graphics.getWidth() - 50 * ratio &&
				pos.y >= 150 * ratio && pos.y <= 225 * ratio) {
			buttonClickListener.nextDayButtonClicked();
			return true;
		}
		return false;
	}

	public StartUIStage setOnButtonClickListener(ButtonClickListener buttonClickListener) {
		this.buttonClickListener = buttonClickListener;
		return this;
	}

	public interface ButtonClickListener {
		void nextDayButtonClicked();
	}
}