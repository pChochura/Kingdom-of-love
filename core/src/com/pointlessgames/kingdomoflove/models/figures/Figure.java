package com.pointlessgames.kingdomoflove.models.figures;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.pointlessgames.kingdomoflove.models.Ability;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.Settings;
import com.pointlessgames.kingdomoflove.utils.Stats;
import com.pointlessgames.kingdomoflove.utils.managers.TextureManager;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.pointlessgames.kingdomoflove.screens.StartScreen.font;
import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;
import static com.pointlessgames.kingdomoflove.utils.Settings.tileSize;

public abstract class Figure extends Actor {

	protected TextureRegion texture;
	protected float width;
	protected float height;
	protected int level;

	protected int mapX, mapY;

	protected Actor abilityTip;

	Figure(Texture texture) {
		if(texture != null)
			this.texture = new TextureRegion(texture);
		refreshSize();

		level = 1;
	}

	public void refreshSize() {
		height = tileSize;
		width = height * texture.getTexture().getWidth() / texture.getTexture().getHeight();
	}

	@Override public void act(float delta) {
		for(Action a : getActions())
			a.act(delta);

		if(abilityTip != null) abilityTip.act(delta);

		setPos();
	}

	public void draw(SpriteBatch sP, float tileX, float tileY) {
		draw(sP, tileX, tileY, 1);
	}

	public void draw(SpriteBatch sP, float tileX, float tileY, float alpha) {
		drawTexture(sP, tileX, tileY, alpha);

		if(hasLevels()) drawLevel(sP, tileX, tileY, alpha);

		drawAbilityTip(sP, tileX, tileY, alpha);
	}

	protected void drawAbilityTip(SpriteBatch sP, float tileX, float tileY, float alpha) {
		if(abilityTip != null) {
			font.getData().setScale(0.35f);
			font.setColor(abilityTip.getColor().cpy().mul(1, 1, 1, alpha));
			font.draw(sP, abilityTip.getName(), tileX + tileSize - 27 * ratio + abilityTip.getX(), tileY + tileSize - 60 * ratio + abilityTip.getY(), 35 * ratio, Align.center, false);
			sP.setColor(Color.WHITE.cpy().mul(1, 1, 1, abilityTip.getColor().a * alpha));
			sP.draw(((Texture) abilityTip.getUserObject()), tileX + tileSize - 27 * ratio + abilityTip.getX(), tileY + tileSize - 60 * ratio + abilityTip.getY(), 35 * ratio, 35 * ratio);
		}
	}

	protected void drawLevel(SpriteBatch sP, float tileX, float tileY, float alpha) {
		float size = tileSize * 0.2f;
		float halfSize = size * 0.5f;

		sP.setColor(Colors.loveColor.cpy().mul(1, 1, 1, alpha));
		TextureManager.getInstance().filledRect.draw(sP, tileX + halfSize, tileY + halfSize, halfSize, halfSize, size, size, getScaleX(), getScaleY(), 45);

		font.getData().setScale(size / (150f * ratio));
		font.setColor(Colors.text3Color.cpy().mul(1, 1, 1, alpha));
		font.draw(sP, String.valueOf(level), tileX + halfSize, tileY + halfSize + (size + font.getCapHeight()) / 2, size, Align.center, false);
	}

	protected void drawTexture(SpriteBatch sP, float tileX, float tileY, float alpha) {
		sP.setColor(Color.WHITE.cpy().mul(1, 1, 1, alpha));
		sP.draw(texture, tileX + getX(), tileY + getY(), getScaleX() * width, getScaleY() * height);
	}

	public void setMapPos(int x, int y) {
		this.mapX = x;
		this.mapY = y;
	}

	public int getMapX() {
		return mapX;
	}

	public int getMapY() {
		return mapY;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return this.level;
	}

	public void levelUp() {
		level++;
	}

	public int getUpgradeCost() {
		level++;
		int cost = getCost();
		level--;
		return cost;
	}

	@Override public float getWidth() {
		return width * getScaleX();
	}

	@Override public float getHeight() {
		return height * getScaleY();
	}

	public TextureRegion getTexture() {
		return texture;
	}

	public void dispose() {
		//Dispose useless files
	}

	public abstract void triggerAbility(Stats stats);

	public String getName() {
		return getClass().getSimpleName();
	}

	public abstract String getAbilityDescription();

	public abstract int getCost();

	public abstract int getLove();

	public abstract Ability getAbility(Stats stats);

	public abstract boolean isUpgradable();

	protected abstract void setPos();

	public abstract boolean canUpgrade(Stats stats);

	public boolean hasLevels() {
		return true;
	}

	public boolean isDestroyable() {
		return true;
	}

	public void orientInSpace(Stats stats) {}

	protected void resetAbilityTip(String name, Texture texture) {
		if(abilityTip == null) abilityTip = new Actor();
		else abilityTip.clear();
		abilityTip.setName(name);
		abilityTip.setUserObject(texture);
		abilityTip.setPosition(0, 0);
		abilityTip.setColor(Color.BLACK.cpy().add(0, 0, 0, 1));
		abilityTip.addAction(Actions.sequence(
				Actions.parallel(Actions.alpha(0, Settings.duration), Actions.moveBy(0, 30 * ratio, Settings.duration, Interpolation.exp10Out)),
				Actions.run(() -> abilityTip = null)));
	}

	protected void destroy(Stats stats) {
		addAction(sequence(scaleTo(0, 0, Settings.duration, Interpolation.swingIn), run(() -> stats.removeFigure(this))));
	}
}
