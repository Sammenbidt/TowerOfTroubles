package com.egocentric.towerbuilder.utils.b2d;

import com.badlogic.gdx.physics.box2d.*;
import com.egocentric.towerbuilder.entities.BlockType;
import com.egocentric.towerbuilder.entities.MaterialType;

/**
 * Created by Peter on 25/12/17.
 */
public class BodyBuilder {


	// TODO: Clean up code and remove unnecessary comments.
	public static Body createRectangle(World world, boolean fixedRotation, boolean isStatic, boolean senor, float width, float height, float x, float y)
	{
		Body body;
		BodyDef bDef;

		bDef = new BodyDef();
		bDef.fixedRotation = fixedRotation;
		bDef.type = isStatic ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;

		bDef.position.set(x,y);

		body = world.createBody(bDef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width/2f, height/2f);

		FixtureDef fDef = new FixtureDef();
		fDef.isSensor = senor;
		fDef.density = 1.0f;
		fDef.friction = 0.0f;
		fDef.restitution = 0.0f;
		fDef.shape = shape;

		body.createFixture(fDef);


		shape.dispose();
		body.setTransform(x,y, 0);
		return body;
	}

	public static Body createRectangle(World world, boolean fixedRotation, boolean isStatic, float width, float height, float x, float y, float density, float restitution, float friction)
	{
		Body body;
		BodyDef bDef;

		bDef = new BodyDef();
		bDef.fixedRotation = fixedRotation;
		bDef.type =  isStatic ?  BodyDef.BodyType.StaticBody :BodyDef.BodyType.DynamicBody;
		//bDef.type = BodyDef.BodyType.StaticBody;

		bDef.position.set(x,y);


		body = world.createBody(bDef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width/2f, height/2f);

		FixtureDef fDef = new FixtureDef();
		fDef.shape = shape;

		fDef.density = density;
		fDef.friction = friction;
		fDef.restitution = restitution;

		body.createFixture(fDef);

		shape.dispose();

		return body;
	}

	public static Body createBlock(World world, boolean isStatic, boolean fixedRotation,  BlockType blockType, MaterialType matType, float centerX, float centerY)
	{
		Body body;
		BodyDef bDef;

		bDef = new BodyDef();
		bDef.fixedRotation = fixedRotation;

		bDef.type = isStatic ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
		//bDef.type = BodyDef.BodyType.DynamicBody;


		body = world.createBody(bDef);

		FixtureDef fDef = new FixtureDef();
		Shape shape = shapeFromBlockType(blockType);

		fDef.shape = shape;

		setFixtureDefinition(fDef, matType);


		body.createFixture(fDef);

		shape.dispose();

		return body;
	}
	public static Body createBlock(World world, BlockType blockType, MaterialType matType, float centerX, float centerY)
	{
		return createBlock(world, false, false, blockType, matType, centerX, centerY);
	}


	private static void setFixtureDefinition(FixtureDef fDef, MaterialType matType)
	{
		fDef.restitution = matType.restitution;
		fDef.friction = matType.friction;
		fDef.density = matType.density;

	}

	// 1/3, 1/3
	/*
	private static float[] triangleVertices = {
			0f - 0.333f, 0f - 0.333f,
			1f - 0.333f, 0f - 0.333f,
			0f - 0.333f, 1f - 0.333f,

	};


	// 140 x 70
	// 1, 1/3
	private static float[] triangleLargeVetices = {
			0f - 1.000f, 0f - 0.333f,
			2f - 1.000f, 0f - 0.333f,
			1f - 1.000f, 1f - 0.333f,

	};
	*/

	private static Shape shapeFromBlockType(BlockType type)
	{


		//PolygonShape shape = new PolygonShape();
		switch(type.getShape())
		{


			case Square: {
				// Use vertices instead ?
				PolygonShape shape = new PolygonShape();
				shape.setAsBox(type.getWidth() / 2f, type.getHeight() / 2f);
				return shape;

			}
			case Triangle: {

				PolygonShape shape = new PolygonShape();
				shape.set(type.getVertices());
				return shape;
				/*
				if (type == BlockType.Triangle) {
					PolygonShape shape = new PolygonShape();
					shape.set(triangleVertices);
					return shape;

				} else if (type == BlockType.LargeTriangle) {
					PolygonShape shape = new PolygonShape();
					shape.set(triangleLargeVetices);
					return shape;

				}
				*/

			}
			case Circle: {
				CircleShape shape= new CircleShape();
				shape.setRadius(type.getWidth()/2f);
				return shape;
			}
		}

		return null;

	}

	public static Body createRectangle(World world, boolean fixedRotation, boolean isStatic, float width, float height, float x, float y, MaterialType materialType)
	{
		return createRectangle(world, fixedRotation, isStatic, width, height, x, y, materialType.density, materialType.restitution, materialType.friction );
	}
}
