package com.pointlessgames.kingdomoflove.stages.story;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.pointlessgames.kingdomoflove.renderers.CustomShapeRenderer;
import com.pointlessgames.kingdomoflove.stages.GestureStage;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.TextureManager;

import java.util.ArrayList;

import static com.pointlessgames.kingdomoflove.screens.StoryScreen.font;
import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;

public class StartStoryStage extends GestureStage {

	private float duration = 2f;
	private CustomShapeRenderer sR;
	private SpriteBatch sP;

	private String text1 = "Once upon a time, there was a kingdom.";
	private String text2 = "Where lived people guided only by love and money...";
	private String text3 = "The Lord, wanting to leave a legacy built a monument at the center of the Kingdom.";
	private String text4 = "The inhabitants decided to take part in the creation of the history...";
	private String text5 = "What could go wrong?";

	private ArrayList<Actor> actors;
	private Runnable endListener;
	private int index = 1;

	public StartStoryStage(SpriteBatch sP, CustomShapeRenderer sR) {
		this.sP = sP;
		this.sR = sR;

		actors = new ArrayList<>();

		Actor a = new Actor();
		a.setName(text1);
		a.setScale(0.65f);
		a.setX(200 * ratio);
		a.setY(Gdx.graphics.getHeight() - 500 * ratio);
		a.setColor(Colors.textColor.cpy().mul(1, 1, 1, 0));
		a.addAction(Actions.sequence(Actions.delay(duration), Actions.alpha(1, duration, Interpolation.exp5Out)));

		actors.add(a);

		a = new Actor();
		a.setName(text2);
		a.setScale(0.65f);
		a.setX(200 * ratio);
		a.setY(Gdx.graphics.getHeight() - 800 * ratio);
		a.setColor(Colors.text2Color.cpy().mul(1, 1, 1, 0));
		a.addAction(Actions.parallel(Actions.moveBy(0, 50, duration, Interpolation.exp5Out),
				Actions.alpha(1, duration, Interpolation.exp5Out)));

		actors.add(a);

		a = new Actor();
		a.setName(text3);
		a.setScale(0.65f);
		a.setX(200 * ratio);
		a.setY(Gdx.graphics.getHeight() - 600 * ratio);
		a.setColor(Colors.textColor.cpy().mul(1, 1, 1, 0));
		a.addAction(Actions.sequence(Actions.delay(duration),
				Actions.parallel(Actions.moveBy(0, 50, duration, Interpolation.exp5Out),
						Actions.alpha(1, duration, Interpolation.exp5Out))));

		actors.add(a);

		a = new Actor();
		a.setName(text4);
		a.setScale(0.65f);
		a.setX(200 * ratio);
		a.setY(Gdx.graphics.getHeight() - 1000 * ratio);
		a.setColor(Colors.text2Color.cpy().mul(1, 1, 1, 0));
		a.addAction(Actions.parallel(Actions.moveBy(0, 50, duration, Interpolation.exp5Out),
				Actions.alpha(1, duration, Interpolation.exp5Out)));

		actors.add(a);

		a = new Actor();
		a.setName(text5);
		a.setScale(0.85f);
		a.setX(200 * ratio);
		a.setY(Gdx.graphics.getHeight() / 2f + 100 * ratio);
		a.setColor(Colors.textColor.cpy().mul(1, 1, 1, 0));
		a.addAction((Actions.sequence(Actions.delay(duration),
				Actions.alpha(1, duration, Interpolation.exp5Out),
				Actions.run(() -> endListener.run()))));

		actors.add(a);
	}

	@Override public void draw() {
		sP.begin();
		sP.draw(TextureManager.getInstance().getTexture(TextureManager.BACKGROUND), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		for(int i = 0; i < index && i < actors.size(); i++) {
			if(actors != null && !actors.isEmpty() && actors.get(i) != null) {
				font.getData().setScale(actors.get(i).getScaleX());
				font.setColor(actors.get(i).getColor());
				font.draw(sP, actors.get(i).getName(), actors.get(i).getX(), actors.get(i).getY(), Gdx.graphics.getWidth() - 400 * ratio, Align.center, true);
			}
		}
		sP.end();
	}

	@Override public void act(float delta) {
		for(int i = 0; i < index && i < actors.size(); i++)
			actors.get(i).act(delta);
	}

	@Override public boolean tap(float x, float y, int count, int button) {
		if(index % 2 == 0)
			for(int i = 0; i < index && i < actors.size(); i++)
				if(actors != null && !actors.isEmpty() && actors.get(i) != null)
					actors.get(i).addAction(Actions.alpha(0, duration, Interpolation.exp5In));
		index++;
		return true;
	}

	public StartStoryStage setOnEndListener(Runnable endListener) {
		this.endListener = endListener;
		return this;
	}
}
