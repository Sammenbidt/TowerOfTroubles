package com.egocentric.towerbuilder.entities;

import com.badlogic.gdx.math.Vector2;

import static com.egocentric.towerbuilder.entities.ShapeType.Square;
/**
 * Created by Peter on 26/12/17.
 */


public enum BlockType {

	// TODO: Add offset here and vertices ! !
	GroundRect(Square, 4f, 1f, "Ground", new float[]{0f, 0f}), // TODO: Maybe remove the ground rect ?
	SmallSquare(Square, 1f, 1f, "Square", new float[]
			{
					-0.5f, -0.5f,
					0.5f, -0.5f,
					0.5f, 0.5f,
					-0.5f, 0.5f,
			}), // 1 x 1 (70 x 70)
	Rectangle(Square, 2f, 1f, "Rectangle", new float[]
			{
					-1.0f, -0.5f,
					1.0f, -0.5f,
					1.0f, 0.5f,
					-1.0f, 0.5f,
			}), // 2 x 1 (140 x 70)
	LargeRectangle(Square, 3f, 1f, "LargeRectangle", new float[]
			{
					-1.5f, -0.5f,
					1.5f, -0.5f,
					1.5f, 0.5f,
					-1.5f, 0.5f,
			}), // 3 x 1 (220 x 70)
	MediumSquare(Square, 2f, 2f, "MediumSquare", new float[]
			{
					-1.0f, -1.0f,
					1.0f, -1.0f,
					1.0f, 1.0f,
					-1.0f, 1.0f,
			}), // 2 x 2 ( 140 x 140)
	ThickLargeRectangle(Square, 3f, 2f, "ThickLargeRectangle", new float[]
			{
					-1.5f, -1.0f,
					1.5f, -1.0f,
					1.5f, 1.0f,
					-1.5f, 1.0f


			}), // 3 x 2 (220 x 140)
	//Vector2 origin = new Vector2(0.33f * 70, 0.33f * 70);
	Triangle(ShapeType.Triangle, 1f, 1f, "Triangle", 0.16665f, 0.16665f, 0.33f, 0.33f, new float[]
			{
					0f - 0.333f, 0f - 0.333f,
					1f - 0.333f, 0f - 0.333f,
					0f - 0.333f, 1f - 0.333f,
			}), // 1 x 1 (70 x 70)
	LargeTriangle(ShapeType.Triangle, 2f, 1f, "LargeTriangle", new float[]
			{
					0f - 1.000f, 0f - 0.333f,
					2f - 1.000f, 0f - 0.333f,
					1f - 1.000f, 1f - 0.333f,
			}), // 2 x 1 (140 x 70)
	Circle(ShapeType.Circle, 1f, 1f, "Circle", new float[]{}); // 1 x 1 ( 70 x 70),

	//  3 x 2(220x 140)


	private ShapeType shape;
	private float width;
	private float height;
	private String textureName;

	public Vector2 offset;
	public Vector2 origin;
	private float[] vertices;


	BlockType(ShapeType shape, float width, float height, String textureName, float vertices[])
	{
		this(shape, width, height, textureName, 0f, 0f, 0.5f, 0.5f, vertices);
	}

	BlockType(ShapeType shape, float width, float height, String textureName, float offsetX, float offsetY, float originX, float originY, float[] vertices)
	{
		this.shape = shape;
		this.width = width;
		this.height = height;
		this.textureName = textureName;
		this.vertices = vertices;
		offset = new Vector2(offsetX, offsetY);
		origin = new Vector2(originX, originY);

	}



	public ShapeType getShape()
	{
		return shape;
	}

	public float getHeight() {return height;}
	public float getWidth() {return width;}
	public String getTextureName() { return textureName; }
	public float[] getVertices() { return vertices; }




}
