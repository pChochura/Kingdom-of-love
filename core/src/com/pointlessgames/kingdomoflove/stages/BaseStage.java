package com.pointlessgames.kingdomoflove.stages;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pointlessgames.kingdomoflove.utils.overridden.ScrollableGestureDetector;

public class BaseStage extends Stage implements ScrollableGestureDetector.GestureListener {

	public boolean touchInterruption = true;

	@Override public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override public boolean tap(float x, float y, int count, int button) {
		return false;
	}

	@Override public boolean longPress(float x, float y) {
		return false;
	}

	@Override public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	@Override public boolean pan(float x, float y, float deltaX, float deltaY) {
		return false;
	}

	@Override public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override public boolean zoom(float initialDistance, float distance) {
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

	@Override public void pinchStop() {

	}

	@Override public boolean scrolled(int amount) {
		return super.scrolled(amount);
	}

	@Override public boolean touchUp(float x, float y, int pointer, int button) {
		return false;
	}
}