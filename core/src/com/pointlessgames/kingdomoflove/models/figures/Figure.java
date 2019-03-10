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

	public void draw(SpriteBatch sP, CustomShapeRenderer sR, float tileX, float tileY, float alpha) {
		drawTexture(sP, tileX, tileY, alpha);

		if(hasLevels()) drawLevel(sP, sR, tileX, tileY, alpha);

		drawAbilityTip(sP, tileX, tileY, alpha);
	}

	public void draw(SpriteBatch sP, CustomShapeRenderer sR, float tileX, float tileY) {
		draw(sP, sR, tileX, tileY, 1);
	}

	protected void drawAbilityTip(SpriteBatch sP, float tileX, float tileY, float alpha) {
		if(abilityTip != null) {
			sP.begin();
			font.getData().setScale(0.3f);
			font.setColor(abilityTip.getColor().cpy().mul(1, 1, 1, alpha));
			font.draw(sP, abilityTip.getName(), tileX + tileSize - 25 * ratio + abilityTip.getX(), tileY + tileSize - 50 * ratio + abilityTip.getY(), 30 * ratio, Align.center, false);
			sP.setColor(new Color(1, 1, 1, abilityTip.getColor().a * alpha));
			sP.draw(((Texture) abilityTip.getUserObject()), tileX + tileSize - 25 * ratio + abilityTip.getX(), tileY + tileSize - 50 * ratio + abilityTip.getY(), 30 * ratio, 30 * ratio);
			sP.setColor(Color.WHITE);
			sP.end();
		}
	}

	protected void drawLevel(SpriteBatch sP, CustomShapeRenderer sR, float tileX, float tileY, float alpha) {
//		if(alpha != 1) {
//			Gdx.gl.glEnable(GL20.GL_BLEND);
//			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//		}

		sR.begin(ShapeRenderer.ShapeType.Filled);
		sR.setColor(Colors.loveColor.cpy().mul(1, 1, 1, alpha));
		float size = tileSize * 0.2f;
		float halfSize = size * 0.5f;
		sR.rect(tileX + halfSize, tileY + halfSize, halfSize, halfSize, size, size, getScaleX(), getScaleY(), 45);
		sR.end();

		sP.begin();
		font.getData().setScale(size / (150f * ratio));
		font.setColor(Colors.text3Color.cpy().mul(1, 1, 1, alpha));
		font.draw(sP, String.valueOf(level), tileX + halfSize, tileY + size * 7f / 6f, size, Align.center, false);
		font.setColor(Color.WHITE);
		sP.end();

//		if(alpha != 1) Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	protected void drawTexture(SpriteBatch sP, float tileX, float tileY, float alpha) {
		sP.begin();
		sP.setColor(new Color(1, 1, 1, alpha));
		sP.draw(texture, tileX + getX(), tileY + getY(), getScaleX() * width, getScaleY() * height);
		sP.setColor(Color.WHITE);
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
