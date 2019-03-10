package com.pointlessgames.kingdomoflove.stages.story;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.pointlessgames.kingdomoflove.renderers.CustomShapeRenderer;
import com.pointlessgames.kingdomoflove.utils.Colors;
import com.pointlessgames.kingdomoflove.utils.TextureManager;

import java.util.ArrayList;

import static com.pointlessgames.kingdomoflove.screens.EndScreen.font;
import static com.pointlessgames.kingdomoflove.utils.Settings.ratio;

public class EndStoryStage extends Stage {

	private float duration = 2f;
	private CustomShapeRenderer sR;
	private SpriteBatch sP;

	private String text1 = "Everything went wrong!";
	private String text2 = "The desire to create a legacy ended up so badly.";
	private String text3 = "Looking through the prism of money has blinded each one of them.";
	private String text4 = "The only thing that mattered was showing the better side of themselves...";
	private String text5 = "But at what price?";

	private ArrayList<Actor> actors;
	private int index = 1;
	private Runnable endListener;

	public EndStoryStage(SpriteBatch sP, CustomShapeRenderer sR) {
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
		a.setY(Gdx.graphics.getHeight() / 2f);
		a.setColor(Colors.textColor.cpy().mul(1, 1, 1, 0));
		a.addAction((Actions.sequence(Actions.delay(duration),
				Actions.alpha(1, duration, Interpolation.exp5Out),
				Actions.run(() -> endListener.run()))));

		actors.add(a);
	}

	@Override public void draw() {
		if(font != null) {
			sP.begin();
			sP.draw(TextureManager.getInstance().background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			for(int i = 0; i < index && i < actors.size(); i++) {
				if(actors != null && !actors.isEmpty() && actors.get(i) != null) {
					font.getData().setScale(actors.get(i).getScaleX());
					font.setColor(actors.get(i).getColor());
					font.draw(sP, actors.get(i).getName(), actors.get(i).getX(), actors.get(i).getY(), Gdx.graphics.getWidth() - 400 * ratio, Align.center, true);
				}
			}
			sP.end();
		}
	}

	@Override public void act(float delta) {
		for(int i = 0; i < index && i < actors.size(); i++)
			if(actors != null && !actors.isEmpty() && actors.get(i) != null) actors.get(i).act(delta);
	}

	@Override public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(index % 2 == 0)
			for(int i = 0; i < index && i < actors.size(); i++)
				if(actors != null && !actors.isEmpty() && actors.get(i) != null) actors.get(i).addAction(Actions.alpha(0, duration, Interpolation.exp5In));
		index++;
		return true;
	}

	public EndStoryStage setOnEndListener(Runnable endListener) {
		this.endListener = endListener;
		return this;
	}
}
