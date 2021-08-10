package com.egocentric.towerbuilder.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.egocentric.towerbuilder.utils.Constants;

/**
 * Created by Peter on 26/12/17.
 */
public class Entity {

	protected Sprite sprite;
	protected Body body;
	protected Vector2 spriteOffset = new Vector2();


	private final static Vector2 v2 = new Vector2();
	public void render(SpriteBatch batch)
	{
		v2.set(spriteOffset);
		//v2.setAngleRad(body.getAngle());
		sprite.setCenter(
				Constants.scaleToScreen(body.getPosition().x) + v2.x,
				Constants.scaleToScreen(body.getPosition().y) + v2.y
				);

		//sprite.setCenter(body.getPosition().x, body.getPosition().y);
		sprite.setRotation(body.getAngle() * MathUtils.radDeg);

		sprite.draw(batch);
	}

	public void setTexture(Texture texture, Vector2 origin)
	{
		if(sprite == null)
		{
			sprite = new Sprite(texture);

		}else
		{
			sprite.setTexture(texture);
			// TODO: set texture coordinates!
		}
		if(origin == null)
			sprite.setOriginCenter();
		else
			sprite.setOrigin(origin.x, origin.y);

	}

	public void setTexture(Texture texture)
	{
		this.setTexture(texture, null);
	}



	public void setTexture(TextureRegion region, Vector2 origin)
	{
		if(sprite == null)
		{
			sprite = new Sprite(region);

		}else
		{
			sprite.setRegion(region);
		}

		if(origin == null)
			sprite.setOriginCenter();
		else
			sprite.setOrigin(origin.x, origin.y);

	}

	public void setTexture(TextureRegion region)
	{
		this.setTexture(region, null);
	}

	public void setBody(Body body)
	{
		this.body = body;
	}
	public void setSpriteOffset(Vector2 spriteOffset) { this.spriteOffset.set((spriteOffset));}
	public void setSpriteOffset(float x, float y) { this.spriteOffset.set(x,y);}
	public Sprite getSprite() { return sprite; }
	public Vector2 getPosition() { return body.getPosition();}
	public float getPositionX() { return body.getPosition().x; }
	public float getPositionY() { return body.getPosition().y; }
}
