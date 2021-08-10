package com.egocentric.towerbuilder;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Peter on 02/02/18.
 */
public class GameOptions {

	public static final int DEFAULT_START_BRICKS = 10;
	public static final float DEFAULT_STABILITY_DELAY = 5.0f;
	public static final boolean DEFAULT_MIRROR_BRICKS = false;
	public static final boolean DEFAULT_BRICKS_OVER_LINE = false;
	public static final Background DEFAULT_BACKGROUND = Background.Grass;

	public Array<Player> players = new Array<Player>();

	public int startBricks 		= DEFAULT_START_BRICKS;
	public float stabilityDelay = DEFAULT_STABILITY_DELAY;

	public boolean mirrorBricks = DEFAULT_MIRROR_BRICKS;
	public boolean bricksOverLine = DEFAULT_BRICKS_OVER_LINE;

	public Background background;

	// TODO: Add friction and restitution for bricks.
	// TODO: Add platform type.

	public GameOptions()
	{
		reset();
	}

	public void createPlayers(String... names)
	{
		for(String s : names)
		{
			Player p = new Player(s);
			players.add(p);
		}

	}

	public void createPlayers(Array<String> names)
	{
		this.createPlayers(names.toArray());
	}

	/**
	 * Clears all info in the current game options.
	 * Removes player names.
	 * Sets values to default
	 * startBricks, stabilityDelay...
	 */
	public void reset()
	{
		players.clear();
		startBricks = DEFAULT_START_BRICKS;
		stabilityDelay = DEFAULT_STABILITY_DELAY;
		bricksOverLine = DEFAULT_BRICKS_OVER_LINE;
		mirrorBricks = DEFAULT_MIRROR_BRICKS;
		background = DEFAULT_BACKGROUND;
	}


}
