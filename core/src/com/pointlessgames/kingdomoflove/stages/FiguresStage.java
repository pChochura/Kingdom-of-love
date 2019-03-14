package com.pointlessgames.kingdomoflove.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pointlessgames.kingdomoflove.models.figures.Figure;
import com.pointlessgames.kingdomoflove.renderers.CustomShapeRenderer;
import com.pointlessgames.kingdomoflove.utils.Stats;

import java.util.Collections;

import static com.pointlessgames.kingdomoflove.utils.Settings.HEIGHT;
import static com.pointlessgames.kingdomoflove.utils.Settings.WIDTH;
import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class FiguresStage extends GestureStage {

	private OnFigureClickListener figureClickListener;
	private CustomShapeRenderer sR;
	private SpriteBatch sP;
	private Stats stats;

	private Rectangle screenRect;

	public FiguresStage(SpriteBatch sP, CustomShapeRenderer sR, Stats stats) {
		this.sP = sP;
		this.sR = sR;
		this.stats = stats;

		screenRect = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	private void drawFigures() {
		float cx = (Gdx.graphics.getWidth() - WIDTH * tileSize) / 2 + stats.mapOffset.x;
		float cy = (Gdx.graphics.getHeight() - HEIGHT * tileSize) / 2 + stats.mapOffset.y;
		for(int i = stats.figures.size() - 1; i >= 0; i--) {
			Figure f = stats.figures.get(i);
			float x = cx + f.getMapX() * tileSize;
			float y = cy + f.getMapY() * tileSize;
			Rectangle rect = new Rectangle(x, y, f.getWidth(), f.getHeight());
			if(!rect.overlaps(screenRect)) continue;

			sP.setColor(Color.WHITE);
			if(stats.selectedFigures == null || stats.selectedFigures.contains(f))
				f.draw(sP, sR, x, y);
			else f.draw(sP, sR, x, y, 0.3f);
		}
	}

	@Override public void draw() {
		drawFigures();
	}

	private void updateFigures(float delta) {
		for(int i = stats.figures.size() - 1; i >= 0; i--)
			stats.figures.get(i).act(delta);
	}

	@Override public void act(float delta) {
		updateFigures(delta);
	}

	@Override public boolean tap(float x, float y, int count, int button) {
		float offsetX = (Gdx.graphics.getWidth() - WIDTH * tileSize) / 2 + stats.mapOffset.x;
		float offsetY = (Gdx.graphics.getHeight() - HEIGHT * tileSize) / 2 + stats.mapOffset.y;
		Vector2 pos = new Vector2(x, Gdx.graphics.getHeight() - y).sub(offsetX, offsetY);

		int mapX = (int) (pos.x / tileSize);
		int mapY = (int) (pos.y / tileSize);
		if(mapX >= 0 && mapX < WIDTH && mapY >= 0 && mapY < HEIGHT) {
			for(Figure f : stats.figures)
				if(f.getMapX() == mapX && f.getMapY() == mapY) {
					figureClickListener.onFigureClick(f);
					return true;
				}
		}
		return false;
	}

	@Override public boolean longPress(float x, float y) {
		float offsetX = (Gdx.graphics.getWidth() - WIDTH * tileSize) / 2 + stats.mapOffset.x;
		float offsetY = (Gdx.graphics.getHeight() - HEIGHT * tileSize) / 2 + stats.mapOffset.y;
		Vector2 pos = new Vector2(x, Gdx.graphics.getHeight() - y).sub(offsetX, offsetY);

		int mapX = (int) (pos.x / tileSize);
		int mapY = (int) (pos.y / tileSize);
		if(mapX >= 0 && mapX < WIDTH && mapY >= 0 && mapY < HEIGHT) {
			for(Figure f : stats.figures)
				if(f.getMapX() == mapX && f.getMapY() == mapY) {
//					figureClickListener.onFigureClick(f);
//					Gdx.app.log("LOG!", "long pressed!");
					return true;
				}
		}
		return false;
	}

	public FiguresStage setOnFigureClickedListener(OnFigureClickListener figureClickListener) {
		this.figureClickListener = figureClickListener;
		return this;
	}

	public interface OnFigureClickListener {
		void onFigureClick(Figure f);
	}
}