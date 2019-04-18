package com.pointlessgames.kingdomoflove.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.pointlessgames.kingdomoflove.utils.Settings;

public class Button extends Actor {

	private final Color unpressedColor;
	private final Color pressedColor;
	private final Color inactiveColor;

	private boolean selected;
	private boolean inactive;

	public Button(Color unpressedColor, Color pressedColor) {
		this(unpressedColor, pressedColor, null);
	}

	public Button(Color unpressedColor, Color pressedColor, Color inactiveColor) {
		this.unpressedColor = unpressedColor;
		this.pressedColor = pressedColor;
		this.inactiveColor = inactiveColor;
		setColor(unpressedColor);
	}

	public void touchUp() {
		if(!inactive) {
			clearActions();
			addAction(Actions.color(selected ? pressedColor : unpressedColor, Settings.duration / 2));
		}
	}

	public void touchDown(float x, float y) {
		if(!inactive && hit(x - getX(), y - getY(), true) != null) {
			clearActions();
			addAction(Actions.color(pressedColor, Settings.duration / 2));
		}
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return selected;
	}

	@Override public void setTouchable(Touchable touchable) {
		super.setTouchable(touchable);
		if(inactiveColor != null && touchable != Touchable.enabled) {
			setColor(inactiveColor);
			inactive = true;
		} else if(inactive) {
			setColor(unpressedColor);
			inactive = false;
		}
	}
}
