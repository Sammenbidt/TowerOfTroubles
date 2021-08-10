package com.egocentric.towerbuilder.utils;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Peter on 25/12/17.
 */
public class Constants {

	public static final float TIME_STEP = 1/60f;
	public static final int VELOCITY_ITERATIONS = 6;
	public static final int POSITION_ITERATIONS = 2;

	// Used to be 70f, changing to 68 for tests
	public static final float PIXELS_PR_METER = 68f;
	public static final float METERS_PR_PIXEL = 1f / PIXELS_PR_METER;


	public static final float GROUND_WIDTH = 280;
	public static final float GROUND_HEIGHT = 35f;


	public static float scaleToWorld(float v) {
		return v * METERS_PR_PIXEL;
	}

	public static float scaleToScreen(float v)
	{
		return v * PIXELS_PR_METER;
	}




	public static Vector2 scaleToWorld(Vector2 v2)
	{
		return v2.scl(METERS_PR_PIXEL);
	}

	public static Vector2 scaleToScreen(Vector2 v2)
	{
		return v2.scl(PIXELS_PR_METER);
	}

	public static Vector2 scaleToWorld(Vector2 screenCoords, Vector2 target)
	{
		return scaleToWorld( target.set(screenCoords));

	}

	public static Vector2 scaleToScreen(Vector2 worldCoords, Vector2 target)
	{
		return scaleToScreen(target.set(worldCoords));
	}
}
