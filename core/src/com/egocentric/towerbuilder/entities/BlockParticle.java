package com.egocentric.towerbuilder.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by Peter on 04/07/2018.
 */
public class BlockParticle {

	private static final Random rand = new Random();
	private static final Vector2 v2 = new Vector2();
	final static float PARTICLE_VELOCITY = 7f;
	private static final float PARTICLE_SCALE = 0.25f;

	Sprite sprite; // Maybe just use a texture instead ?

	Vector2 goalPosition = new Vector2();
	Vector2 velocity = new Vector2();
	Vector2 position = new Vector2();


	private boolean done = false;

	public BlockParticle(Sprite sprite, float x, float y)
	{
		position.set(x,y);
		this.sprite = new Sprite(sprite);
		this.sprite.setOrigin(sprite.getOriginX(), sprite.getOriginY());
		this.sprite.setScale(PARTICLE_SCALE);
	}

	public void update(float delta)
	{
		if(done)
			return;

		sprite.rotate(10f); // maybe this should speed up ??
		position.add(velocity);

		v2.set(goalPosition).sub(position);

		float diffAngle = v2.angle() - velocity.angle();

		velocity.rotate(MathUtils.clamp(diffAngle, -1.15f ,1.15f));


		v2.set(goalPosition).sub(position);


		if(v2.isZero( 15f) || v2.y > Gdx.graphics.getHeight())
		{
			done = true;
		}

		// Remove if taken to long time ?

	}

	public void render(SpriteBatch batch)
	{
		sprite.setCenter(position.x, position.y);
		sprite.draw(batch);

	}

	public boolean isDone()
	{
		return done;
	}

	public void setGoalPosition(float x, float y)
	{
		// Make goal position static !
		goalPosition.set(x,y);
		v2.set(goalPosition);
		v2.sub(position);

		v2.set(PARTICLE_VELOCITY, 0f);

		if(position.x < Gdx.graphics.getWidth()/2f) // x is below center screen
		{

			v2.setAngle(rand.nextInt(10) - 5 + 45);
			// Change velocity ?
		}else
		{
			v2.setAngle(135f + rand.nextInt(10 ) - 5);
		}

		velocity.set(v2);

	}
}
