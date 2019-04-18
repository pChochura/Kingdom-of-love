package com.pointlessgames.kingdomoflove.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.Noise;
import com.pointlessgames.kingdomoflove.utils.Settings;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.Utils;
import com.pointlessgames.kingdomoflove.utils.managers.TextureManager;

import static com.pointlessgames.kingdomoflove.utils.Settings.HEIGHT;
import static com.pointlessgames.kingdomoflove.utils.Settings.WIDTH;
import static com.pointlessgames.kingdomoflove.utils.Settings.WINDOW_HEIGHT;
import static com.pointlessgames.kingdomoflove.utils.Settings.WINDOW_WIDTH;
import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class BackgroundStage extends BaseStage {

	private ClickListener clickListener;
	private final SpriteBatch sP;
	private final Stats stats;

	private float initialScale;

	public BackgroundStage(SpriteBatch sP, Stats stats) {
		this.sP = sP;
		this.stats = stats;

		stats.mapOffset.set(0, 0);
	}

	private void drawBackground() {
		sP.setColor(Color.WHITE);
		sP.draw(TextureManager.getInstance().getTexture(TextureManager.BACKGROUND), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	private void drawMap() {
		float offsetX = (Gdx.graphics.getWidth() - WIDTH * tileSize) / 2;
		float offsetY = (Gdx.graphics.getHeight() - HEIGHT * tileSize) / 2;

		for(int i = 0; i < WIDTH; i++) for(int j = 0; j < HEIGHT; j++) {
			float y = offsetY + stats.mapOffset.y + j * tileSize;
			float x = offsetX + stats.mapOffset.x + i * tileSize;

			if(!Utils.overlaps(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), x, y, tileSize, tileSize)) continue;

			Color color = Colors.tileColor.cpy().lerp(Colors.tile2Color, Math.abs(Noise.noise((float)i / WINDOW_WIDTH, (float)j / WINDOW_HEIGHT, 0)));
			if(!stats.isTileAvailable(i, j)) color.a = 0.5f;
			sP.setColor(color);
			TextureManager.getInstance().filledRect.draw(sP, x, y, 0.5f * tileSize, 0.5f * tileSize, tileSize, tileSize, 0.99f, 0.99f, getOffset(i + WINDOW_WIDTH, j + WINDOW_HEIGHT));
		}
	}

	private float getOffset(int x, int y) {
		return Noise.noise(Utils.map(x, 0, 2 * WINDOW_WIDTH, 0, 1), Utils.map(y, 0, 2 * WINDOW_HEIGHT, 0, 1), 0) * tileSize * 0.01f;
	}

	@Override public void draw() {
		sP.begin();

		drawBackground();
		drawMap();

		sP.setColor(Color.WHITE);
		sP.end();
	}

	@Override public boolean keyDown(int keyCode) {
		if(keyCode == Input.Keys.BACK || keyCode == Input.Keys.ESCAPE) {
			clickListener.onBackPressed();
			return true;
		}
		return false;
	}

	public BackgroundStage setOnTileClickedListener(ClickListener tileClickedListener) {
		this.clickListener = tileClickedListener;
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
			clickListener.onTileClicked(mapX, mapY);
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
		Vector2 initialPos = initialPointer1.cpy().add(initialPointer2).scl(0.5f).sub(Gdx.graphics.getWidth() / 2f + stats.mapOffset.x, Gdx.graphics.getHeight() / 2f + stats.mapOffset.y);

		Vector2 beforeZoom = initialPos.cpy().scl(Settings.scale);

		Settings.scale = MathUtils.clamp(Utils.map(initialPointer1.dst(initialPointer2) - pointer1.dst(pointer2), 0, 500, initialScale, initialScale - 0.5f), 0.3f, 1.5f);
		Settings.refreshTileSize(stats);

		Vector2 afterZoom = initialPos.cpy().scl(Settings.scale);

		Vector2 sub = beforeZoom.cpy().sub(afterZoom);
		stats.mapOffset.add(sub.x, -sub.y).limit(WIDTH * tileSize / 2);
		return true;
	}

	@Override public boolean scrolled(int amount) {
		Vector2 initialPos = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()).sub(Gdx.graphics.getWidth() / 2f + stats.mapOffset.x, Gdx.graphics.getHeight() / 2f + stats.mapOffset.y);

		Vector2 beforeZoom = initialPos.cpy().scl(Settings.scale);

		Settings.scale = MathUtils.clamp(Settings.scale - 0.05f * amount, 0.3f, 1f);
		Settings.refreshTileSize(stats);

		Vector2 afterZoom = initialPos.cpy().scl(Settings.scale);

		Vector2 sub = beforeZoom.sub(afterZoom);
		stats.mapOffset.add(sub.x, sub.y).limit(WIDTH * tileSize / 2);
		return true;
	}

	public interface ClickListener {
		void onTileClicked(int mapX, int mapY);
		void onBackPressed();
	}
}








