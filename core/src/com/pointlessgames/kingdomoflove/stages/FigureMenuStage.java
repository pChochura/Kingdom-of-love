package com.pointlessgames.kingdomoflove.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;
import com.pointlessgames.kingdomoflove.actors.Button;
import com.pointlessgames.kingdomoflove.models.figures.Figure;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.Settings;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.managers.TextureManager;

import static com.pointlessgames.kingdomoflove.utils.Settings.HEIGHT;
import static com.pointlessgames.kingdomoflove.utils.Settings.WIDTH;
import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;
import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class FigureMenuStage extends BaseStage {

	private Runnable onHideListener;
	private SpriteBatch sP;
	private Stats stats;

	private ClickListener clickListener;

	private Vector2 pos;

	private Button buttonUpgrade;
	private Button buttonInfo;
	private Button buttonDestroy;

	private boolean hiding;
	private float time;
	private float alpha;

	public FigureMenuStage(SpriteBatch sP, Stats stats) {
		this.sP = sP;
		this.stats = stats;

		buttonUpgrade = new Button(Colors.buttonColor, Colors.tile2Color, Colors.inactiveColor);
		buttonInfo = new Button(Colors.buttonColor, Colors.tile2Color);
		buttonDestroy = new Button(Colors.buttonColor, Colors.tile2Color, Colors.inactiveColor);

		buttonDestroy.setTouchable(Touchable.disabled); //TODO add ability to destroy

		float buttonSize = 100 * ratio;
		buttonUpgrade.setSize(buttonSize, buttonSize);
		buttonInfo.setSize(buttonSize, buttonSize);
		buttonDestroy.setSize(buttonSize, buttonSize);
	}

	private void setButtonsPosition() {
		float cx = (Gdx.graphics.getWidth() - WIDTH * tileSize) / 2;
		float cy = (Gdx.graphics.getHeight() - HEIGHT * tileSize) / 2;
		Vector2 pos = this.pos.cpy().scl(tileSize).add(cx + tileSize / 2, cy);
		buttonUpgrade.setPosition(pos.x + stats.mapOffset.x - buttonUpgrade.getWidth() * 1.2f, pos.y + stats.mapOffset.y + 1.3f * tileSize - 25 * ratio, Align.center);
		buttonInfo.setPosition(pos.x + stats.mapOffset.x, pos.y + stats.mapOffset.y + 1.3f * tileSize, Align.center);
		buttonDestroy.setPosition(pos.x + stats.mapOffset.x + buttonDestroy.getWidth() * 1.2f, pos.y + stats.mapOffset.y + 1.3f * tileSize - 25 * ratio, Align.center);
	}

	private void drawButtons() {
		sP.setColor(buttonUpgrade.getColor().cpy().mul(1, 1, 1, alpha));
		TextureManager.getInstance().rect.draw(sP, buttonUpgrade.getX(), buttonUpgrade.getY(), buttonUpgrade.getWidth(), buttonUpgrade.getHeight());

		sP.setColor(buttonInfo.getColor().cpy().mul(1, 1, 1, alpha));
		TextureManager.getInstance().rect.draw(sP, buttonInfo.getX(), buttonInfo.getY(), buttonInfo.getWidth(), buttonInfo.getHeight());

		sP.setColor(buttonDestroy.getColor().cpy().mul(1, 1, 1, alpha));
		TextureManager.getInstance().rect.draw(sP, buttonDestroy.getX(), buttonDestroy.getY(), buttonDestroy.getWidth(), buttonDestroy.getHeight());

		Texture texture;

		sP.setColor(Colors.barColor.cpy().mul(1, 1, 1, alpha * alpha));
		texture = TextureManager.getInstance().getTexture(TextureManager.UPGRADE);
		sP.draw(texture, buttonUpgrade.getX(), buttonUpgrade.getY(), buttonUpgrade.getWidth() * 0.5f, buttonUpgrade.getHeight() * 0.5f,
				buttonUpgrade.getWidth(), buttonUpgrade.getHeight(), 0.65f, 0.65f, 0, 0, 0, texture.getWidth(), texture.getHeight(), false, false);

		sP.setColor(Colors.bgColor.cpy().mul(1, 1, 1, alpha * alpha));
		texture = TextureManager.getInstance().getTexture(TextureManager.INFO);
		sP.draw(texture, buttonInfo.getX(), buttonInfo.getY(), buttonInfo.getWidth() * 0.5f, buttonInfo.getHeight() * 0.5f,
				buttonInfo.getWidth(), buttonInfo.getHeight(), 0.65f, 0.65f, 0, 0, 0, texture.getWidth(), texture.getHeight(), false, false);

		sP.setColor(Colors.loveColor.cpy().mul(1, 1, 1, alpha * alpha));
		texture = TextureManager.getInstance().getTexture(TextureManager.DESTROY);
		sP.draw(texture, buttonDestroy.getX(), buttonDestroy.getY(), buttonDestroy.getWidth() * 0.5f, buttonDestroy.getHeight() * 0.5f,
				buttonDestroy.getWidth(), buttonDestroy.getHeight(), 0.65f, 0.65f, 0, 0, 0, texture.getWidth(), texture.getHeight(), false, false);
	}

	@Override public void draw() {
		sP.begin();

		drawButtons();

		sP.setColor(Color.WHITE);
		sP.end();
	}

	@Override public void act(float delta) {
		setButtonsPosition();

		buttonUpgrade.act(delta);
		buttonInfo.act(delta);
		buttonDestroy.act(delta);

		if(hiding && time > 0 || !hiding && time < Settings.duration) {
			time += hiding ? -delta : delta;
			alpha = hiding ? Interpolation.exp5In.apply(time / Settings.duration) : Interpolation.exp5Out.apply(time / Settings.duration);
		} else if(hiding && onHideListener != null) onHideListener.run();
	}

	@Override public boolean tap(float x, float y, int count, int button) {
		buttonUpgrade.touchUp();
		buttonInfo.touchUp();
		buttonDestroy.touchUp();
		if(buttonUpgrade.hit(x - buttonUpgrade.getX(), Gdx.graphics.getHeight() - y - buttonUpgrade.getY(), false) != null) {
			if(buttonUpgrade.isTouchable())
				clickListener.onUpgradeClick();
		} else if(buttonInfo.hit(x - buttonInfo.getX(), Gdx.graphics.getHeight() - y - buttonInfo.getY(), false) != null) {
			if(buttonInfo.isTouchable())
				clickListener.onInfoClick();
		} else if(buttonDestroy.hit(x - buttonDestroy.getX(), Gdx.graphics.getHeight() - y - buttonDestroy.getY(), false) != null) {
			if(buttonDestroy.isTouchable())
				clickListener.onDestroyClick();
		} else clickListener.onCancelClick();
		return true;
	}

	@Override public boolean pan(float x, float y, float deltaX, float deltaY) {
		buttonUpgrade.touchUp();
		buttonInfo.touchUp();
		buttonDestroy.touchUp();
		return super.pan(x, y, deltaX, deltaY);
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		buttonUpgrade.touchUp();
		buttonInfo.touchUp();
		buttonDestroy.touchUp();
		return super.pinch(initialPointer1, initialPointer2, pointer1, pointer2);
	}

	@Override public boolean touchDown(float x, float y, int pointer, int button) {
		buttonUpgrade.touchDown(x, Gdx.graphics.getHeight() - y);
		buttonInfo.touchDown(x, Gdx.graphics.getHeight() - y);
		buttonDestroy.touchDown(x, Gdx.graphics.getHeight() - y);
		return super.touchDown(x, y, pointer, button);
	}

	@Override public boolean touchUp(float x, float y, int pointer, int button) {
		buttonUpgrade.touchUp();
		buttonInfo.touchUp();
		buttonDestroy.touchUp();
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
		this.pos = new Vector2(figure.getMapX(), figure.getMapY());
		buttonUpgrade.setTouchable(figure.isUpgradable() ? Touchable.enabled : Touchable.disabled);
	}

	public FigureMenuStage setClickListener(ClickListener clickListener) {
		this.clickListener = clickListener;
		return this;
	}

	public interface ClickListener {
		void onCancelClick();
		void onUpgradeClick();
		void onInfoClick();
		void onDestroyClick();
	}
}