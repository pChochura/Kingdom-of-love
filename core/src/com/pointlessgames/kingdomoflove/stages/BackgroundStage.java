package com.pointlessgames.kingdomoflove.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pointlessgames.kingdomoflove.renderers.CustomShapeRenderer;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.Noise;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.TextureManager;
import com.pointlessgames.kingdomoflove.utils.Utils;

import static com.pointlessgames.kingdomoflove.utils.Settings.HEIGHT;
import static com.pointlessgames.kingdomoflove.utils.Settings.WIDTH;
import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class BackgroundStage extends Stage {

	private OnTileClickedListener tileClickedListener;
	private CustomShapeRenderer sR;
	private SpriteBatch sP;
	private Stats stats;

	public BackgroundStage(SpriteBatch sP, CustomShapeRenderer sR, Stats stats) {
		this.sP = sP;
		this.sR = sR;
		this.stats = stats;
	}

	private void drawBackground() {
		sP.begin();
		sP.draw(TextureManager.background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		sP.end();
	}

	private void drawMap() {
		float offsetX = (Gdx.graphics.getWidth() - WIDTH * tileSize) / 2;
		float offsetY = (Gdx.graphics.getHeight() - HEIGHT * tileSize) / 2;

		sR.begin(ShapeRenderer.ShapeType.Filled);

		//Tiles
		for(int i = 0; i < WIDTH; i++) for(int j = 0; j < HEIGHT; j++) {
			sR.setColor(Colors.tileColor.cpy().lerp(Colors.tile2Color, Math.abs(Noise.noise((float) i / WIDTH, (float) j / HEIGHT, 0))));
			sR.polygon(new float[]{
					offsetX + i * tileSize + getOffset(i + WIDTH, j + HEIGHT + 4), offsetY + j * tileSize + getOffset(i + WIDTH + 4, j + HEIGHT),
					offsetX + i * tileSize + tileSize - getOffset(i + WIDTH + 1, j + HEIGHT + 5), offsetY + j * tileSize + getOffset(i + WIDTH + 5, j + HEIGHT + 1),
					offsetX + i * tileSize + tileSize - getOffset(i + WIDTH + 2, j + HEIGHT + 6), offsetY + j * tileSize + tileSize - getOffset(i + WIDTH + 6, j + HEIGHT + 2),
					offsetX + i * tileSize + getOffset(i + WIDTH + 3, j + HEIGHT + 7), offsetY + j * tileSize + tileSize - getOffset(i + WIDTH + 7, j + HEIGHT + 3)
			});
		}

		sR.end();
	}

	private float getOffset(int x, int y) {
		return Math.abs(Noise.noise(Utils.map(x, 0, 2 * WIDTH, 0, 1), Utils.map(y, 0, 2 * HEIGHT, 0, 1), 0)) * 10f;
	}

	@Override public void draw() {
		drawBackground();
		drawMap();
	}

	@Override public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		float offsetX = (Gdx.graphics.getWidth() - WIDTH * tileSize) / 2;
		float offsetY = (Gdx.graphics.getHeight() - HEIGHT * tileSize) / 2;
		Vector2 pos = new Vector2(screenX, Gdx.graphics.getHeight() - screenY).sub(offsetX, offsetY);

		int mapX = MathUtils.floor(pos.x / tileSize);
		int mapY = MathUtils.floor(pos.y / tileSize);
		if(mapX >= 0 && mapX < WIDTH && mapY >= 0 && mapY < HEIGHT) {
			tileClickedListener.onEmptyTileClicked(mapX, mapY);
			return true;
		}
		return false;
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

	public interface OnTileClickedListener {
		void onEmptyTileClicked(int mapX, int mapY);
	}
}





