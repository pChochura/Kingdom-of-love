package com.pointlessgames.kingdomoflove.stages.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;
import com.pointlessgames.kingdomoflove.actors.Button;
import com.pointlessgames.kingdomoflove.models.Ability;
import com.pointlessgames.kingdomoflove.models.figures.Figure;
import com.pointlessgames.kingdomoflove.models.figures.Structure;
import com.pointlessgames.kingdomoflove.stages.BaseStage;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.Settings;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.managers.TextureManager;

import java.util.Locale;

import static com.pointlessgames.kingdomoflove.screens.StartScreen.font;
import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;

public class UpgradeFigureStage extends BaseStage {

	private Runnable onHideListener;
	private ClickListener clickListener;
	private SpriteBatch sP;
	private Stats stats;

	private Figure figure;
	private Button buttonUpgrade;
	private Actor dialog;

	private float time;
	private float alpha;
	private boolean hiding;

	private float offset;
	private float iconSize;

	private int prevCapacity;
	private int capacity;
	private int prevMoneyProduction;
	private String prevAbilityDescription;
	private int moneyProduction;
	private float prevLoveProduction;
	private float loveProduction;
	private String abilityDescription;

	public UpgradeFigureStage(SpriteBatch sP, Stats stats) {
		this.sP = sP;
		this.stats = stats;

		touchInterruption = false;

		dialog = new Actor();
		dialog.setSize(900 * ratio, 1200 * ratio);
		dialog.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, Align.center);

		buttonUpgrade = new Button(Colors.buttonColor, Colors.tile2Color, Colors.inactiveColor);
		buttonUpgrade.setSize(500 * ratio, 100 * ratio);
		buttonUpgrade.setPosition(Gdx.graphics.getWidth() / 2, dialog.getY(), Align.center);

		offset = 50 * ratio;
		iconSize = 75 * ratio;
	}

	private void setDialogHeight() {
		float prevAbilityHeight = new GlyphLayout(font, prevAbilityDescription, Colors.textColor, dialog.getWidth() - 2 * offset, Align.center, true).height;
		float abilityHeight = new GlyphLayout(font, abilityDescription, Colors.textColor, dialog.getWidth() - 2 * offset, Align.center, true).height;
		float dividerHeight = 40 * ratio;
		float height = 7 * offset + 2 * iconSize + prevAbilityHeight + abilityHeight + dividerHeight;
		dialog.setHeight(height);
		dialog.setY(0.5f * Gdx.graphics.getHeight(), Align.center);
		buttonUpgrade.setY(dialog.getY(), Align.center);
	}

	private void drawBackground() {
		sP.setColor(Color.BLACK.cpy().mul(1, 1, 1, 0.5f * alpha));
		TextureManager.getInstance().filledRect.draw(sP, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	private void drawDialog() {
		sP.setColor(Colors.barColor.cpy().mul(1, 1, 1, alpha));
		TextureManager.getInstance().rect.draw(sP, dialog.getX(), dialog.getY(), dialog.getWidth(), dialog.getHeight());
	}

	private void drawDivider() {
		float size = 40 * ratio;
		float halfSize = 0.5f * size;
		float width = 300 * ratio;
		float x = dialog.getX() + 0.5f * (dialog.getWidth() - size);
		float y = dialog.getY() + 0.5f * (dialog.getHeight() - size);

		sP.setColor(Colors.tile2Color.cpy().mul(1, 1, 1, alpha * alpha));
		TextureManager.getInstance().filledRect.draw(sP, x - width, y - 1.5f / ratio + halfSize, width - 1.5f / ratio, 3 / ratio);
		TextureManager.getInstance().outlineRect.draw(sP, x, y, halfSize, halfSize, size, size, 1, 1, 45);
		TextureManager.getInstance().filledRect.draw(sP, x + size + 1.5f / ratio, y - 1.5f / ratio + halfSize, width - 1.5f / ratio, 3 / ratio);
	}

	private void drawFigureInfo() {
		float y = dialog.getY() + dialog.getHeight() - offset;
		sP.setColor(Color.WHITE.cpy().mul(1, 1, 1, alpha * alpha));
		sP.draw(TextureManager.getInstance().getTexture(TextureManager.MONEY), dialog.getX() + offset, y - iconSize, iconSize, iconSize);
		sP.draw(TextureManager.getInstance().getTexture(TextureManager.CAPACITY), dialog.getX() + 0.5f * dialog.getWidth() - iconSize, y - iconSize, iconSize, iconSize);
		sP.draw(TextureManager.getInstance().getTexture(TextureManager.LOVE), dialog.getX() + dialog.getWidth() - 5 * offset, y - iconSize, iconSize, iconSize);

		font.getData().setScale(0.4f);
		font.setColor(Colors.textColor.cpy().mul(1, 1, 1, alpha * alpha));
		font.draw(sP, String.format(Locale.getDefault(), "%+d$", prevMoneyProduction),
				dialog.getX() + offset + iconSize, y - iconSize + 0.5f * (iconSize + font.getCapHeight()));
		font.draw(sP, String.format(Locale.getDefault(), "%d", prevCapacity),
				dialog.getX() + 0.5f * dialog.getWidth(), y - iconSize + 0.5f * (iconSize + font.getCapHeight()));
		font.draw(sP, String.format(Locale.getDefault(), "%+.1f%%", prevLoveProduction),
				dialog.getX() + dialog.getWidth() - 5 * offset + iconSize, y - iconSize + 0.5f * (iconSize + font.getCapHeight()));

		font.getData().setScale(0.5f);
		font.draw(sP, prevAbilityDescription, dialog.getX() + offset, y - iconSize - offset, dialog.getWidth() - 2 * offset, Align.center, true);

		drawDivider();

		y -= 0.5f * dialog.getHeight();

		sP.setColor(Color.WHITE.cpy().mul(1, 1, 1, alpha * alpha));
		sP.draw(TextureManager.getInstance().getTexture(TextureManager.MONEY), dialog.getX() + offset, y - iconSize, iconSize, iconSize);
		sP.draw(TextureManager.getInstance().getTexture(TextureManager.CAPACITY), dialog.getX() + 0.5f * dialog.getWidth() -  iconSize, y - iconSize, iconSize, iconSize);
		sP.draw(TextureManager.getInstance().getTexture(TextureManager.LOVE), dialog.getX() + dialog.getWidth() - 5 * offset, y - iconSize, iconSize, iconSize);

		font.getData().setScale(0.4f);
		font.setColor(Colors.textColor.cpy().mul(1, 1, 1, alpha * alpha));
		font.draw(sP, String.format(Locale.getDefault(), "%+d$", moneyProduction),
				dialog.getX() + offset + iconSize, y - iconSize + 0.5f * (iconSize + font.getCapHeight()));
		font.draw(sP, String.format(Locale.getDefault(), "%d", capacity),
				dialog.getX() + 0.5f * dialog.getWidth(), y - iconSize + 0.5f * (iconSize + font.getCapHeight()));
		font.draw(sP, String.format(Locale.getDefault(), "%+.1f%%", loveProduction),
				dialog.getX() + dialog.getWidth() - 5 * offset + iconSize, y - iconSize + 0.5f * (iconSize + font.getCapHeight()));

		font.getData().setScale(0.5f);
		font.draw(sP, abilityDescription, dialog.getX() + offset, y - iconSize - offset, dialog.getWidth() - 2 * offset, Align.center, true);
	}

	private void drawUpgradeButton() {
		sP.setColor(buttonUpgrade.getColor().cpy().mul(1, 1, 1, alpha));
		TextureManager.getInstance().cutRect.draw(sP, buttonUpgrade.getX(), buttonUpgrade.getY(), buttonUpgrade.getWidth(), buttonUpgrade.getHeight());

		font.getData().setScale(0.45f);
		font.setColor(Colors.tileColor.cpy().mul(1, 1, 1, alpha * alpha));
		font.draw(sP, String.format(Locale.getDefault(), "Upgrade (%d$)", figure.getUpgradeCost()),
				buttonUpgrade.getX(), buttonUpgrade.getY() + font.getCapHeight() / 2 + buttonUpgrade.getHeight() / 2, buttonUpgrade.getWidth(), Align.center, false);
	}

	@Override public void draw() {
		sP.begin();

		drawBackground();
		drawDialog();
		drawFigureInfo();
		drawUpgradeButton();

		sP.setColor(Color.WHITE);
		sP.end();
	}

	@Override public void act(float delta) {
		buttonUpgrade.act(delta);

		if(hiding && time > 0 || !hiding && time < Settings.duration) {
			time += hiding ? -delta : delta;
			alpha = hiding ? Interpolation.exp5In.apply(time / Settings.duration) : Interpolation.exp5Out.apply(time / Settings.duration);
		} else if(hiding && onHideListener != null) onHideListener.run();
	}

	@Override public boolean tap(float x, float y, int count, int button) {
		buttonUpgrade.touchUp();
		Vector2 pos = new Vector2(x, Gdx.graphics.getHeight() - y);
		if(buttonUpgrade.hit(pos.x - buttonUpgrade.getX(), pos.y - buttonUpgrade.getY(), false) != null) {
			clickListener.onUpgradeClick();
			buttonUpgrade.clearActions();
			buttonUpgrade.setTouchable(figure.canUpgrade(stats) ? Touchable.enabled : Touchable.disabled);
			return true;
		} if(dialog.hit(pos.x - dialog.getX(), pos.y - dialog.getY(), false) == null) clickListener.onCancelClick();
		return true;
	}

	@Override public boolean pan(float x, float y, float deltaX, float deltaY) {
		buttonUpgrade.touchUp();
		return super.pan(x, y, deltaX, deltaY);
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		buttonUpgrade.touchUp();
		return super.pinch(initialPointer1, initialPointer2, pointer1, pointer2);
	}

	@Override public boolean touchDown(float x, float y, int pointer, int button) {
		buttonUpgrade.touchDown(x, Gdx.graphics.getHeight() - y);
		return super.touchDown(x, y, pointer, button);
	}

	@Override public boolean touchUp(float x, float y, int pointer, int button) {
		buttonUpgrade.touchUp();
		return super.touchUp(x, y, pointer, button);
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
		buttonUpgrade.setTouchable(figure.canUpgrade(stats) ? Touchable.enabled : Touchable.disabled);

		Ability ability = figure.getAbility(stats);
		prevCapacity = figure instanceof Structure ? ((Structure) figure).getCapacity() : 0;
		prevMoneyProduction = (int)ability.getAmount(Ability.ProductionType.MONEY);
		prevLoveProduction = ability.getAmount(Ability.ProductionType.LOVE);
		prevAbilityDescription = figure.getAbilityDescription();

		figure.setLevel(figure.getLevel() + 1);
		ability = figure.getAbility(stats);
		capacity = figure instanceof Structure ? ((Structure) figure).getCapacity() : 0;
		moneyProduction = (int)ability.getAmount(Ability.ProductionType.MONEY);
		loveProduction = ability.getAmount(Ability.ProductionType.LOVE);
		abilityDescription = figure.getAbilityDescription();

		figure.setLevel(figure.getLevel() - 1);

		setDialogHeight();
	}

	public UpgradeFigureStage setClickListener(ClickListener clickListener) {
		this.clickListener = clickListener;
		return this;
	}

	public interface ClickListener {
		void onCancelClick();
		void onUpgradeClick();
	}
}