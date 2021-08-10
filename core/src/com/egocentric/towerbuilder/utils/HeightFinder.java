package com.egocentric.towerbuilder.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.egocentric.towerbuilder.entities.BlockType;
import com.egocentric.towerbuilder.entities.ShapeType;

/**
 * Created by Peter on 26/12/17.
 */
public class HeightFinder {

	private static final int UP = 1;
	private static final int DOWN = -1;

	private static final float LEFT__X_START = -240;
	private static final float RIGHT_X_START = 240;

	private static final float DEFAULT_DELTA = 0.1f;
	private static final int DEFAULT_N_ITTERATIONS = 3;

	private static final Vector2 left = new Vector2(LEFT__X_START , 0);
	private static final Vector2 right = new Vector2(RIGHT_X_START , 0);

	public static float findHeight(World world, float startValue, float stepValue)
	{
		float maxHeight = startValue;
		float height = startValue;
		hit = false;

		left.x = LEFT__X_START;
		right.x = RIGHT_X_START;
		left.y = startValue;
		right.y = startValue;

		System.out.println("StartValue : " + startValue);

		// Check if the current value is correct.
		hit = checkHeight(world, height, DEFAULT_N_ITTERATIONS, DEFAULT_DELTA);


		int direction;

		direction = hit ? UP: DOWN;


		if(direction == UP)
		{
			System.out.println("Going up!");
			do
			{
				height += stepValue;
				//left.y += height ;
				//right.y += height;

				//System.out.println("Right: " + right);
				//hit = false;

				hit = checkHeight(world, height, DEFAULT_N_ITTERATIONS, DEFAULT_DELTA );
				//height += stepValue;
				//world.rayCast(callback, left, right );
				//maxHeight = height -= stepValue;
				//maxHeight = right.y - stepValue;
				maxHeight = height - stepValue;
			}while(hit == true);
		}else
		{
			while(hit == false)
			{
				height -= stepValue;
				hit = checkHeight(world, height, DEFAULT_N_ITTERATIONS, DEFAULT_DELTA);
				maxHeight = height;
			}
		}
		return maxHeight;
	}

	static boolean hit = false;

	private static boolean checkHeight(World world, float height, int steps, float delta)
	{
		hit = false;
		float h;
		for(int i = 0; i < steps; i++)
		{
			h = height + i * delta;
			left.y = h;
			right.y = h;

			world.rayCast(callback, left, right);

			if(hit)
				return true;

		}
		return false;
	}



	private static RayCastCallback callback = new RayCastCallback() {

		/**
		 *  return -1 : ignore the fixture and continue
		 *  return  0 : Terminate the ray cast return fraction
		 *  return  1 : Don't clup the ray and continue
		 */

		@Override
		public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction)
		{
			// TODO: Take into account the fixture filterdata and mask bits...
			hit = true;
			reportBody = fixture.getBody();
			return 0;
		}
	};

	/**
	 * Returns true if the position is valid !
	 * @param world
	 * @param centerPosition
	 * @param blockType
	 * @return
	 */
	public static boolean checkPosition(World world, Vector2 centerPosition, BlockType blockType)
	{
		if(blockType.getShape() == ShapeType.Circle)
		{
			// TODO: Implmenet !
			return true;
		}
		boolean valid = true;
		reportBody = null;
		hit = false;
		float[] vertices = blockType.getVertices();
		for(int i = 0; i < vertices.length; i += 2)
		{
			int v1x, v1y, v2x, v2y;
			v1x = i;
			v1y = i+1;

			if( i + 3 > vertices.length)
			{
				v2x = 0;
				v2y = 1;
			}else
			{
				v2x = i + 2;
				v2y = i + 3;

			}
			left.set(centerPosition.x + vertices[v1x], centerPosition.y + vertices[v1y]);
			right.set(centerPosition.x + vertices[v2x], centerPosition.y + vertices[v2y]);


			world.rayCast(callback, left, right);
			if(hit)
				return false;

		}
		return true;
	}

	/**
	 * Used for debugging !
	 */
	public static Body reportBody;
}
