package com.pointlessgames.kingdomoflove.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.pointlessgames.kingdomoflove.stages.BaseStage;
import com.pointlessgames.kingdomoflove.utils.overridden.ScrollableGestureDetector;
import com.pointlessgames.kingdomoflove.utils.Settings;

import java.util.ArrayList;

public class BaseScreen implements Screen {

	private ArrayList<BaseStage> stages;
	private StretchViewport viewport;

	private ArrayList<SnapshotArray<InputProcessor>> processors;

	private float screenHeight;
	private Color backgroundColor;
	private InputMultiplexer inputMultiplexer;

	BaseScreen(float screenHeight, Color backgroundColor) {
		this.screenHeight = screenHeight;
		this.backgroundColor = backgroundColor;

		stages = new ArrayList<>();
		processors = new ArrayList<>();
		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Gdx.input.setCatchBackKey(true);
	}

	public ScrollableGestureDetector addStage(int index, BaseStage stage) {
		stages.add(index, stage);
		ScrollableGestureDetector processor = new ScrollableGestureDetector(stage);
		if(!stage.touchInterruption) {
			processors.add(new SnapshotArray<>(inputMultiplexer.getProcessors()));
			inputMultiplexer.clear();
		}
		inputMultiplexer.addProcessor(0, processor);
		return processor;
	}

	public ScrollableGestureDetector addStage(BaseStage stage) {
		return addStage(Math.max(stages.size(), 0), stage);
	}

	public void removeStage(BaseStage stage) {
		removeStage(stage, null, false);
	}

	public void removeStage(ScrollableGestureDetector gestureDetector, boolean touchInterruption) {
		removeStage(null, gestureDetector, touchInterruption);
	}

	public void removeStage(BaseStage stage, ScrollableGestureDetector gestureDetector, boolean touchInterruption) {
		if(stage != null) stages.remove(stage);
		if(gestureDetector != null) {
			inputMultiplexer.removeProcessor(gestureDetector);
			if(!touchInterruption) {
				inputMultiplexer.setProcessors(processors.get(processors.size() - 1));
				processors.remove(processors.size() - 1);
			}
		}
	}

	public int getStagesCount() {
		return stages.size();
	}

	private void setScreenSize() {
		float aspectRatio = (float)Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
		viewport = new StretchViewport(screenHeight / aspectRatio, screenHeight, new OrthographicCamera(screenHeight / aspectRatio, screenHeight));
		Settings.refreshRatio();
	}

	@Override public void show() {
		setScreenSize();
	}

	@Override public void render(float delta) {
		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		viewport.getCamera().update();

		for(int i = 0; i < stages.size(); i++) stages.get(i).act(delta);

		for(int i = 0; i < stages.size(); i++) stages.get(i).draw();
	}

	@Override public void resize(int width, int height) {
		setScreenSize();
	}

	@Override public void pause() {
	}

	@Override public void resume() {
	}

	@Override public void hide() {
	}

	@Override public void dispose() {
		for(Stage s : stages)
			s.dispose();
	}
}