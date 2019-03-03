package com.pointlessgames.kingdomoflove.models.figures;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.pointlessgames.kingdomoflove.models.Ability;
import com.pointlessgames.kingdomoflove.renderers.CustomShapeRenderer;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.Settings;
import com.pointlessgames.kingdomoflove.utils.Stats;

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

	public void draw(SpriteBatch sP, CustomShapeRenderer sR, float tileX, float tileY) {
		drawTexture(sP, tileX, tileY);

		if(hasLevels())drawLevel(sP, sR, tileX, tileY);

		drawAbilityTip(sP, tileX, tileY);
	}

	protected void drawAbilityTip(SpriteBatch sP, float tileX, float tileY) {
		if(abilityTip != null) {
			sP.begin();
			font.getData().setScale(0.3f);
			font.setColor(abilityTip.getColor());
			font.draw(sP, abilityTip.getName(), tileX + tileSize - 25 * ratio + abilityTip.getX(), tileY + tileSize - 50 * ratio + abilityTip.getY(), 30 * ratio, Align.center, false);
			Color color = sP.getColor().cpy();
			sP.setColor(color.cpy().set(color.r, color.g, color.b, abilityTip.getColor().a));
			sP.draw(((Texture) abilityTip.getUserObject()), tileX + tileSize - 25 * ratio + abilityTip.getX(), tileY + tileSize - 50 * ratio + abilityTip.getY(), 30 * ratio, 30 * ratio);
			sP.setColor(color);
			sP.end();
		}
	}

	protected void drawLevel(SpriteBatch sP, CustomShapeRenderer sR, float tileX, float tileY) {
		sR.begin(ShapeRenderer.ShapeType.Filled);
		sR.setColor(Colors.loveColor);
		float size = tileSize * 0.15f * ratio;
		float halfSize = size * 0.5f;
		sR.rect(tileX + halfSize, tileY + halfSize, halfSize, halfSize, size, size, getScaleX(), getScaleY(), 45);
		sR.end();

		sP.begin();
		font.getData().setScale(size / 175f);
		font.setColor(Colors.text3Color);
		font.draw(sP, String.valueOf(level), tileX + halfSize, tileY + size * 7f / 6f, size, Align.center, false);
		sP.end();
	}

	protected void drawTexture(SpriteBatch sP, float tileX, float tileY) {
		sP.begin();
		sP.draw(texture, tileX + getX(), tileY + getY(), getScaleX() * width, getScaleY() * height);
		sP.end();
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

	public int getUpdateCost() {
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
		texture.getTexture().dispose();
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

	public boolean hasLevels() {
		return true;
	}

	public void orientInSpace(Stats stats) {}

	protected void resetAbilityTip(String name, Texture texture) {
		abilityTip = new Actor();
		abilityTip.setName(name);
		abilityTip.setUserObject(texture);
		abilityTip.setPosition(0, 0);
		abilityTip.setColor(Color.BLACK.cpy().add(0, 0, 0, 1));
		abilityTip.addAction(Actions.sequence(
				Actions.parallel(Actions.alpha(0, Settings.duration), Actions.moveBy(0, 30 * ratio, Settings.duration, Interpolation.exp10Out)),
				Actions.run(() -> abilityTip = null)));
	}
}
