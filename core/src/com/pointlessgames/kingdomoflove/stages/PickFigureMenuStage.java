package com.pointlessgames.kingdomoflove.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.Settings;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.managers.TextureManager;
import com.pointlessgames.kingdomoflove.utils.overridden.CustomShapeRenderer;

import static com.pointlessgames.kingdomoflove.utils.Settings.HEIGHT;
import static com.pointlessgames.kingdomoflove.utils.Settings.WIDTH;
import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;
import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class PickFigureMenuStage extends BaseStage {

	private Runnable onHideListener;
	private CustomShapeRenderer sR;
	private SpriteBatch sP;
	private Stats stats;

	private ClickListener clickListener;

	private Figure figure;
	private Figure[][] map;

	private Button buttonConfirm;
	private Button buttonCancel;

	private boolean hiding;
	private float time;
	private float alpha;

	public PickFigureMenuStage(SpriteBatch sP, CustomShapeRenderer sR, Stats stats) {
		this.sP = sP;
		this.sR = sR;
		this.stats = stats;

		buttonConfirm = new Button(Colors.buttonColor, Colors.tile2Color, Colors.inactiveColor);
		buttonCancel = new Button(Colors.buttonColor, Colors.tile2Color);

		float buttonSize = 100 * ratio;
		buttonConfirm.setSize(buttonSize, buttonSize);
		buttonCancel.setSize(buttonSize, buttonSize);

		map = new Figure[WIDTH][HEIGHT];
		for(Figure f : stats.figures)
			map[f.getMapX()][f.getMapY()] = f;
	}

	private void setButtonsPosition() {
		float cx = (Gdx.graphics.getWidth() - WIDTH * tileSize) / 2;
		float cy = (Gdx.graphics.getHeight() - HEIGHT * tileSize) / 2;
		Vector2 pos = new Vector2(figure.getMapX(), figure.getMapY()).scl(tileSize).add(cx + tileSize / 2, cy);
		buttonConfirm.setPosition(pos.x + stats.mapOffset.x - buttonConfirm.getWidth() * 0.6f, pos.y + stats.mapOffset.y + 1.3f * tileSize - 25 * ratio, Align.center);
		buttonCancel.setPosition(pos.x + stats.mapOffset.x + buttonCancel.getWidth() * 0.6f, pos.y + stats.mapOffset.y + 1.3f * tileSize - 25 * ratio, Align.center);
	}

	private void drawButtons() {
		if(alpha != 1) {
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		}

		sR.begin(ShapeRenderer.ShapeType.Filled);

		sR.setColor(buttonConfirm.getColor().cpy().mul(1, 1, 1, alpha));
		sR.rect(buttonConfirm.getX(), buttonConfirm.getY(), buttonConfirm.getWidth(), buttonConfirm.getHeight(), 4 * ratio);

		sR.setColor(buttonCancel.getColor().cpy().mul(1, 1, 1, alpha));
		sR.rect(buttonCancel.getX(), buttonCancel.getY(), buttonCancel.getWidth(), buttonCancel.getHeight(), 4 * ratio);

		sR.end();

		if(alpha != 1) Gdx.gl.glDisable(GL20.GL_BLEND);

		sP.begin();

		Texture texture;

		sP.setColor(Colors.barColor.cpy().mul(1, 1, 1, alpha * alpha));
		texture = TextureManager.getInstance().getTexture(TextureManager.CONFIRM);
		sP.draw(texture, buttonConfirm.getX(), buttonConfirm.getY(), buttonConfirm.getWidth() * 0.5f, buttonConfirm.getHeight() * 0.5f,
				buttonConfirm.getWidth(), buttonConfirm.getHeight(), 0.65f, 0.65f, 0, 0, 0, texture.getWidth(), texture.getHeight(), false, false);

		sP.setColor(Colors.bgColor.cpy().mul(1, 1, 1, alpha * alpha));
		texture = TextureManager.getInstance().getTexture(TextureManager.CANCEL);
		sP.draw(texture, buttonCancel.getX(), buttonCancel.getY(), buttonCancel.getWidth() * 0.5f, buttonCancel.getHeight() * 0.5f,
				buttonCancel.getWidth(), buttonCancel.getHeight(), 0.65f, 0.65f, 0, 0, 0, texture.getWidth(), texture.getHeight(), false, false);

		sP.setColor(Color.WHITE);
		sP.end();
	}


	private void drawFigure() {
		float cx = (Gdx.graphics.getWidth() - WIDTH * tileSize) / 2 + stats.mapOffset.x;
		float cy = (Gdx.graphics.getHeight() - HEIGHT * tileSize) / 2 + stats.mapOffset.y;
		float x = cx + figure.getMapX() * tileSize;
		float y = cy + figure.getMapY() * tileSize;
		sP.setColor(Color.WHITE);
		sR.setColor(Color.WHITE);
		figure.draw(sP, sR, x, y, hiding ? alpha : 1);
	}

	@Override public void draw() {
		drawFigure();
		drawButtons();
	}

	@Override public void act(float delta) {
		setButtonsPosition();

		buttonConfirm.act(delta);
		buttonCancel.act(delta);
		figure.act(delta);

		if(hiding && time > 0 || !hiding && time < Settings.duration) {
			time += hiding ? -delta : delta;
			alpha = hiding ? Interpolation.exp5In.apply(time / Settings.duration) : Interpolation.exp5Out.apply(time / Settings.duration);
		} else if(hiding && onHideListener != null) onHideListener.run();
	}

	@Override public boolean tap(float x, float y, int count, int button) {
		buttonConfirm.touchUp();
		buttonCancel.touchUp();
		if(buttonConfirm.hit(x - buttonConfirm.getX(), Gdx.graphics.getHeight() - y - buttonConfirm.getY(), false) != null) {
			if(buttonConfirm.isTouchable())
				clickListener.onConfirmClick();
			return true;
		} else if(buttonCancel.hit(x - buttonCancel.getX(), Gdx.graphics.getHeight() - y - buttonCancel.getY(), false) != null) {
			if(buttonCancel.isTouchable())
				clickListener.onCancelClick();
			return true;
		}

		float offsetX = (Gdx.graphics.getWidth() - WIDTH * tileSize) / 2 + stats.mapOffset.x;
		float offsetY = (Gdx.graphics.getHeight() - HEIGHT * tileSize) / 2 + stats.mapOffset.y;
		Vector2 pos = new Vector2(x, Gdx.graphics.getHeight() - y).sub(offsetX, offsetY);

		int mapX = MathUtils.floor(pos.x / tileSize);
		int mapY = MathUtils.floor(pos.y / tileSize);
		if(mapX >= 0 && mapX < WIDTH && mapY >= 0 && mapY < HEIGHT && stats.isTileAvailable(mapX, mapY)) {
			figure.setMapPos(mapX, mapY);
			buttonConfirm.clearActions();
			buttonConfirm.setTouchable(map[mapX][mapY] == null ? Touchable.enabled : Touchable.disabled);
		}

		return true;
	}

	@Override public boolean pan(float x, float y, float deltaX, float deltaY) {
		buttonConfirm.touchUp();
		buttonCancel.touchUp();
		return super.pan(x, y, deltaX, deltaY);
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		buttonConfirm.touchUp();
		buttonCancel.touchUp();
		return super.pinch(initialPointer1, initialPointer2, pointer1, pointer2);
	}

	@Override public boolean touchDown(float x, float y, int pointer, int button) {
		buttonConfirm.touchDown(x, Gdx.graphics.getHeight() - y);
		buttonCancel.touchDown(x, Gdx.graphics.getHeight() - y);
		return super.touchDown(x, y, pointer, button);
	}

	@Override public boolean touchUp(float x, float y, int pointer, int button) {
		buttonConfirm.touchUp();
		buttonCancel.touchUp();
		return super.touchUp(x, y, pointer, button);
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

	public void setClickListener(ClickListener clickListener) {
		this.clickListener = clickListener;
	}

	public interface ClickListener {
		void onCancelClick();
		void onConfirmClick();
	}
}
