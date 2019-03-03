package com.pointlessgames.kingdomoflove.utils;

import com.badlogic.gdx.math.Rectangle;

public class Utils {
	public static float map(float value, float istart, float istop, float ostart, float ostop) {
		return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
	}

	public static Rectangle scale(Rectangle rect, float originX, float originY, float scaleX, float scaleY) {
		float worldOriginX = rect.getX() + originX;
		float worldOriginY = rect.getY() + originY;
		float fx = -originX;
		float fy = -originY;
		float fx2 = rect.getWidth() - originX;

		if (scaleX != 1 || scaleY != 1) {
			fx *= scaleX;
			fy *= scaleY;
			fx2 *= scaleX;
		}

		final float p1x = fx;
		final float p1y = fy;
		final float p4x = fx2;
		final float p4y = fy;

		float x1;
		float y1;
		float x4;
		float y4;

		x1 = p1x;
		y1 = p1y;

		x4 = p4x;
		y4 = p4y;

		x1 += worldOriginX;
		y1 += worldOriginY;
		x4 += worldOriginX;
		y4 += worldOriginY;

		return new Rectangle(x1, y1, x4 - x1, y4 - y1);
	}
}