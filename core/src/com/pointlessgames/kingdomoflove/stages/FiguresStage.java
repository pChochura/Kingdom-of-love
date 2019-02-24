package com.pointlessgames.kingdomoflove.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pointlessgames.kingdomoflove.models.figures.Figure;
import com.pointlessgames.kingdomoflove.renderers.CustomShapeRenderer;
import com.pointlessgames.kingdomoflove.utils.Stats;

import java.util.Collections;

import static com.pointlessgames.kingdomoflove.utils.Settings.HEIGHT;
import static com.pointlessgames.kingdomoflove.utils.Settings.WIDTH;
import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;
import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public class FiguresStage extends Stage {

	private OnFigureClickListener figureClickListener;
	private CustomShapeRenderer sR;
	private SpriteBatch sP;
	private Stats stats;
	private Vector2 startPos;

	public FiguresStage(SpriteBatch sP, CustomShapeRenderer sR, Stats stats) {
		this.sP = sP;
		this.sR = sR;
		this.stats = stats;
	}

	private void sortFigures() {
		Collections.sort(stats.figures, (f2, f1) -> {
			if(f1.getMapY() > f2.getMapY()) return -1;
			else if(f1.getMapY() == f2.getMapY() && f1.getMapX() > f2.getMapX()) return 1;
			else return 0;
		});
	}

	private void drawFigures() {
		float cx = (Gdx.graphics.getWidth() - WIDTH * tileSize) / 2;
		float cy = (Gdx.graphics.getHeight() - HEIGHT * tileSize) / 2;
		for(int i = stats.figures.size() - 1; i >= 0; i--)
			stats.figures.get(i).draw(sP, sR, cx + stats.figures.get(i).getMapX() * tileSize, cy + stats.figures.get(i).getMapY() * tileSize);
	}

	@Override public void draw() {
		sortFigures();
		drawFigures();
	}

	private void updateFigures(float delta) {
		for(int i = stats.figures.size() - 1; i >= 0; i--)
			stats.figures.get(i).act(delta);
	}

	@Override public void act(float delta) {
		updateFigures(delta);
	}

	@Override public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(startPos == null) startPos = new Vector2(screenX, Gdx.graphics.getHeight() - screenY);
		else startPos.set(screenX, Gdx.graphics.getHeight() - screenY);
		return true;
	}

	@Override public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(startPos != null && startPos.dst(screenX, Gdx.graphics.getHeight() - screenY) < 5 * ratio) {
			float offsetX = (Gdx.graphics.getWidth() - WIDTH * tileSize) / 2;
			float offsetY = (Gdx.graphics.getHeight() - HEIGHT * tileSize) / 2;
			Vector2 pos = new Vector2(screenX, Gdx.graphics.getHeight() - screenY).sub(offsetX, offsetY);

			int mapX = (int) (pos.x / tileSize);
			int mapY = (int) (pos.y / tileSize);
			if(mapX >= 0 && mapX < WIDTH && mapY >= 0 && mapY < HEIGHT) {
				for(Figure f : stats.figures)
					if(f.getMapX() == mapX && f.getMapY() == mapY) {
						figureClickListener.onFigureClick(f);
						return true;
					}
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