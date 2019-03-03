package com.pointlessgames.kingdomoflove.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class GestureStage extends Stage {

	private Vector2 initialPointer1 = new Vector2();
	private Vector2 initialPointer2 = new Vector2();
	private Vector2 pointer1 = new Vector2();
	private Vector2 pointer2 = new Vector2();
	private Vector2 offset = new Vector2();

	private boolean dragging;

	@Override public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(pointer == 0)
			initialPointer1.set(screenX - offset.x, Gdx.graphics.getHeight() - screenY - offset.y);
		else if(Gdx.input.isTouched(0))
			initialPointer2.set(screenX - offset.x, Gdx.graphics.getHeight() - screenY - offset.y);
		return false;
	}

	@Override public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(pointer == 0) {
			pointer1.set(screenX, Gdx.graphics.getHeight() - screenY);
			if(isDraggableWithOneFinger() && !Gdx.input.isTouched(1)) {
				float x = pointer1.x - initialPointer1.x;
				float y = pointer2.y - initialPointer2.y;
				if(dragged(new Vector2(x, y))) {
					offset.set(x, y);
					dragging = initialPointer1.cpy().add(offset).dst(pointer1) > 10;
					return dragging;
				}
			}
		} else if(!isDraggableWithOneFinger() && pointer == 1) {
			pointer2.set(screenX, Gdx.graphics.getHeight() - screenY);
			if(Gdx.input.isTouched(0)) {
				float x = (pointer1.x + pointer2.x - initialPointer1.x - initialPointer2.x) * 0.5f;
				float y = (pointer1.y + pointer2.y - initialPointer1.y - initialPointer2.y) * 0.5f;
				if(dragged(new Vector2(x, y))) {
					offset.set(x, y);
					dragging = initialPointer1.cpy().add(offset).dst(pointer1) > 10;
					return pinched(initialPointer1, initialPointer2, pointer1, pointer2);
				}
			}
		}
		return false;
	}

	@Override public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(!dragging && pointer == 0 && !Gdx.input.isTouched(1) && initialPointer1.cpy().add(offset).dst(pointer1) < 10f) {
			dragging = false;
			return tapped(screenX, screenY);
		} else if(dragging && pointer == 0 && Gdx.input.isTouched(1))
			if(pinched(initialPointer1, initialPointer2, pointer1, pointer2))
				return true;
		return dragging = false;
	}

	public abstract boolean tapped(int screenX, int screenY);

	public abstract boolean dragged(Vector2 offset);

	public boolean pinched(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

	public boolean isDraggableWithOneFinger() {
		return false;
	}

	protected void setOffset(Vector2 offset) {
		this.offset = offset;
	}
}
