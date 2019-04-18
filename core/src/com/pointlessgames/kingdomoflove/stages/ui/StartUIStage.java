package com.pointlessgames.kingdomoflove.stages.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.pointlessgames.kingdomoflove.actors.Button;
import com.pointlessgames.kingdomoflove.stages.BaseStage;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.Strings;
import com.pointlessgames.kingdomoflove.utils.Utils;
import com.pointlessgames.kingdomoflove.utils.managers.TextureManager;

import java.util.Locale;

import static com.pointlessgames.kingdomoflove.screens.StartScreen.font;
import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;

public class StartUIStage extends BaseStage {

	private final float topBarHeight = 150 * ratio;

	private ButtonClickListener buttonClickListener;
	private final SpriteBatch sP;
	private final Stats stats;

	private final Button buttonNextDay;

	public StartUIStage(SpriteBatch sP, Stats stats) {
		this.sP = sP;
		this.stats = stats;

		buttonNextDay = new Button(Colors.buttonColor, Colors.tile2Color);
		buttonNextDay.setBounds(Gdx.graphics.getWidth() / 2f - 350 * ratio, 100 * ratio, 700 * ratio, 125 * ratio);
	}

	private void drawStats() {
		sP.begin();

		drawTopBar();
		drawMoney();
		drawPopulation();
		drawLove();
		drawDay();
		drawNextDayButton();

		sP.setColor(Color.WHITE);
		sP.end();
	}

	private void drawTopBar() {
		sP.setColor(Colors.barColor);
		TextureManager.getInstance().filledRect.draw(sP, 0, Gdx.graphics.getHeight() - topBarHeight, Gdx.graphics.getWidth(), topBarHeight);
	}

	private void drawMoney() {
		font.getData().setScale(0.4f);
		font.setColor(Colors.textColor);
		font.draw(sP, String.format(Locale.getDefault(), "%d (%+d)", stats.money, stats.moneyProduction), 135 * ratio, Gdx.graphics.getHeight() - 60 * ratio, 100 * ratio, Align.left, false);
		sP.setColor(Color.WHITE);
		sP.draw(TextureManager.getInstance().getTexture(TextureManager.MONEY), 50 * ratio, Gdx.graphics.getHeight() - 110 * ratio, 75 * ratio, 75 * ratio);
	}

	private void drawPopulation() {
		font.getData().setScale(0.4f);
		font.setColor(Colors.textColor);
		font.draw(sP, String.valueOf(stats.getPopulation()), Gdx.graphics.getWidth() / 2f - 25 * ratio, Gdx.graphics.getHeight() - 60 * ratio, 100 * ratio, Align.left, false);
		sP.setColor(Color.WHITE);
		sP.draw(TextureManager.getInstance().getTexture(TextureManager.CAPACITY), Gdx.graphics.getWidth() / 2f - 100 * ratio, Gdx.graphics.getHeight() - 110 * ratio, 75 * ratio, 75 * ratio);
	}

	private void drawLove() {
		float loveBarWidth = Math.max(Gdx.graphics.getWidth() / 4 - 200, 200) * ratio;
		float width = Utils.map(stats.love, 0, 100, 0, loveBarWidth);

		sP.setColor(Colors.inactiveColor);
		TextureManager.getInstance().filledRect.draw(sP, Gdx.graphics.getWidth() - loveBarWidth - 150 * ratio, Gdx.graphics.getHeight() - 100 * ratio, loveBarWidth, 50 * ratio);

		sP.setColor(Colors.loveColor);
		TextureManager.getInstance().filledRect.draw(sP, Gdx.graphics.getWidth() - loveBarWidth - 150 * ratio, Gdx.graphics.getHeight() - 100 * ratio, width, 50 * ratio);

		font.getData().setScale(0.25f);
		font.setColor(Colors.text3Color);
		font.draw(sP, String.format(Locale.getDefault(), "%d%% (%+.1f%%)", MathUtils.round(stats.love), stats.loveProduction),
				Gdx.graphics.getWidth() - loveBarWidth - 150 * ratio, Gdx.graphics.getHeight() - 70 * ratio, loveBarWidth, Align.center, false);

		sP.setColor(Color.WHITE);
		sP.draw(TextureManager.getInstance().getTexture(TextureManager.LOVE), Gdx.graphics.getWidth() - 125 * ratio, Gdx.graphics.getHeight() - 110 * ratio, 75 * ratio, 75 * ratio);
	}

	private void drawDay() {
		font.getData().setScale(0.65f);
		font.setColor(Colors.textColor);
		font.draw(sP, String.format(Locale.getDefault(), Strings.DAY, stats.day), 0, Gdx.graphics.getHeight() - 1.5f * topBarHeight, Gdx.graphics.getWidth(), Align.center, false);
	}

	private void drawNextDayButton() {
		sP.setColor(buttonNextDay.getColor());
		TextureManager.getInstance().cutRect.draw(sP, buttonNextDay.getX(), buttonNextDay.getY(), buttonNextDay.getWidth(), buttonNextDay.getHeight());

		font.getData().setScale(0.55f);
		font.setColor(Colors.tileColor);
		font.draw(sP, Strings.NEXT_DAY, buttonNextDay.getX(), buttonNextDay.getY() + font.getCapHeight() / 2 + buttonNextDay.getHeight() / 2, buttonNextDay.getWidth(), Align.center, false);
	}

	@Override public void act(float delta) {
		buttonNextDay.act(delta);
	}

	@Override public void draw() {
		drawStats();
	}

	@Override public boolean tap(float x, float y, int count, int button) {
		Vector2 pos = new Vector2(x, Gdx.graphics.getHeight() - y);
		buttonNextDay.touchUp();
		if(buttonNextDay.hit(pos.x - buttonNextDay.getX(), pos.y - buttonNextDay.getY(), true) != null) {
			buttonClickListener.nextDayButtonClicked();
			return true;
		}
		return false;
	}

	@Override public boolean pan(float x, float y, float deltaX, float deltaY) {
		buttonNextDay.touchUp();
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		buttonNextDay.touchUp();
		return super.pinch(initialPointer1, initialPointer2, pointer1, pointer2);
	}

	@Override public boolean touchDown(float x, float y, int pointer, int button) {
		Vector2 pos = new Vector2(x, Gdx.graphics.getHeight() - y);
		buttonNextDay.touchDown(pos.x, pos.y);
		return false;
	}

	@Override public boolean touchUp(float x, float y, int pointer, int button) {
		buttonNextDay.touchUp();
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