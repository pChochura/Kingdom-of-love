package com.pointlessgames.kingdomoflove.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pointlessgames.kingdomoflove.renderers.CustomShapeRenderer;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.Noise;
import com.pointlessgames.kingdomoflove.utils.Settings;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.TextureManager;
import com.pointlessgames.kingdomoflove.utils.Utils;

import static com.pointlessgames.kingdomoflove.utils.Settings.HEIGHT;
import static com.pointlessgames.kingdomoflove.utils.Settings.WIDTH;
import static com.pointlessgames.kingdomoflove.utils.Settings.WINDOW_HEIGHT;
import static com.pointlessgames.kingdomoflove.utils.Settings.WINDOW_WIDTH;
import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class BackgroundStage extends GestureStage {

	private OnTileClickedListener tileClickedListener;
	private CustomShapeRenderer sR;
	private SpriteBatch sP;
	private Stats stats;

	private float initialScale;
	private Rectangle screenRect;

	public BackgroundStage(SpriteBatch sP, CustomShapeRenderer sR, Stats stats) {
		this.sP = sP;
		this.sR = sR;
		this.stats = stats;
		screenRect = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	private void drawBackground() {
		sP.begin();
		sP.draw(TextureManager.background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		sP.end();
	}

	private void drawMap() {
		float offsetX = (Gdx.graphics.getWidth() - WIDTH * tileSize) / 2;
		float offsetY = (Gdx.graphics.getHeight() - HEIGHT * tileSize) / 2;
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		sR.begin(ShapeRenderer.ShapeType.Filled);

		Polygon polygon = new Polygon();
		for(int i = 0; i < WIDTH; i++) for(int j = 0; j < HEIGHT; j++) {
			float[] vertices = {
			/* x */	stats.mapOffset.x + offsetX + i * tileSize + getOffset(i + WINDOW_WIDTH, j + WINDOW_HEIGHT + 4),
			/* y */	stats.mapOffset.y + offsetY + j * tileSize + getOffset(i + WINDOW_WIDTH + 4, j + WINDOW_HEIGHT),
			/* x */	stats.mapOffset.x + offsetX + i * tileSize + tileSize - getOffset(i + WINDOW_WIDTH + 1, j + WINDOW_HEIGHT + 5),
			/* y */	stats.mapOffset.y + offsetY + j * tileSize + getOffset(i + WINDOW_WIDTH + 5, j + WINDOW_HEIGHT + 1),
			/* x */	stats.mapOffset.x + offsetX + i * tileSize + tileSize - getOffset(i + WINDOW_WIDTH + 2, j + WINDOW_HEIGHT + 6),
			/* y */	stats.mapOffset.y + offsetY + j * tileSize + tileSize - getOffset(i + WINDOW_WIDTH + 6, j + WINDOW_HEIGHT + 2),
			/* x */	stats.mapOffset.x + offsetX + i * tileSize + getOffset(i + WINDOW_WIDTH + 3, j + WINDOW_HEIGHT + 7),
			/* y */	stats.mapOffset.y + offsetY + j * tileSize + tileSize - getOffset(i + WINDOW_WIDTH + 7, j + WINDOW_HEIGHT + 3)
			};

			polygon.setVertices(vertices);
			if(!polygon.getBoundingRectangle().overlaps(screenRect)) continue;

			Color color = Colors.tileColor.cpy().lerp(Colors.tile2Color, Math.abs(Noise.noise((float)i / WINDOW_WIDTH, (float)j / WINDOW_HEIGHT, 0)));
			if(!stats.isTileAvailable(i, j)) color.a = 0.5f;
			sR.setColor(color);
			sR.polygon(vertices);
		}

		sR.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	private float getOffset(int x, int y) {
		return Math.abs(Noise.noise(Utils.map(x, 0, 2 * WINDOW_WIDTH, 0, 1), Utils.map(y, 0, 2 * WINDOW_HEIGHT, 0, 1), 0)) * tileSize * 0.02f;
	}

	@Override public void draw() {
		drawBackground();
		drawMap();
	}

	@Override public boolean keyDown(int keyCode) {
		if(keyCode == Input.Keys.BACK) {
			stats.save();
			Gdx.app.exit();
		}
		return super.keyDown(keyCode);
	}

	public BackgroundStage setOnTileClickedListener(OnTileClickedListener tileClickedListener) {
		this.tileClickedListener = tileClickedListener;
		return this;
	}

	@Override public boolean touchDown(float x, float y, int pointer, int button) {
		initialScale = Settings.scale;
		return false;
	}

	@Override public boolean tap(float x, float y, int count, int button) {
		float offsetX = (Gdx.graphics.getWidth() - WIDTH * tileSize) / 2 + stats.mapOffset.x;
		float offsetY = (Gdx.graphics.getHeight() - HEIGHT * tileSize) / 2 + stats.mapOffset.y;
		Vector2 pos = new Vector2(x, Gdx.graphics.getHeight() - y).sub(offsetX, offsetY);

		int mapX = MathUtils.floor(pos.x / tileSize);
		int mapY = MathUtils.floor(pos.y / tileSize);
		if(mapX >= 0 && mapX < WIDTH && mapY >= 0 && mapY < HEIGHT && stats.isTileAvailable(mapX, mapY)) {
			tileClickedListener.onEmptyTileClicked(mapX, mapY);
			return true;
		}
		return false;
	}

	@Override public boolean pan(float x, float y, float deltaX, float deltaY) {
		stats.mapOffset.add(deltaX, -deltaY).limit(WIDTH * tileSize / 2);
		return true;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		Vector2 initialPos = initialPointer1.cpy().add(initialPointer2).scl(0.5f).sub(Gdx.graphics.getWidth() / 2 + stats.mapOffset.x, Gdx.graphics.getHeight() / 2 + stats.mapOffset.y);

		Vector2 beforeZoom = initialPos.cpy().scl(Settings.scale);

		Settings.scale = MathUtils.clamp(Utils.map(initialPointer1.dst(initialPointer2) - pointer1.dst(pointer2), 0, 500, initialScale, initialScale - 0.5f), 0.3f, 1.5f);
		Settings.refreshTileSize(stats);

		Vector2 afterZoom = initialPos.cpy().scl(Settings.scale);

		Vector2 sub = beforeZoom.cpy().sub(afterZoom);
		stats.mapOffset.add(sub.x, -sub.y).limit(WIDTH * tileSize / 2);
		return true;
	}

	public interface OnTileClickedListener {
		void onEmptyTileClicked(int mapX, int mapY);
	}
}








