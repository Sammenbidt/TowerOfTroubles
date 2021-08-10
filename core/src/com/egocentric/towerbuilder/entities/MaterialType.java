package com.egocentric.towerbuilder.entities;

/**
 * Created by Peter on 25/12/17.
 */
public enum MaterialType {

	/** Density, Friction, Restitution */
	Wood	(5.00f, 0.50f, 0.25f, "Wood"),  // 0.6 -> 0.9 ,
	Stone	(7.50f, 0.65f, 0.15f, "Stone"),
	Metal	(10.00f, 0.25f, 0.05f, "Metal"),
	Glass	(0.25f, 0.10f, 0.20f, "Glass");
/*	Ground	(1.00f, 0.90f, 0.01f, "Ground");*/


   // Density Values :  2.0 - 1.5f - 1.0f 0.25f
	/**
	 Wood - Medium Density, medium restitution, medium friction,

	 Stone - High Density, medium-low restitution, high friction

	 Glass - Low Density, , Low Friction

	 Metal - Highest Density, low restitution, low friction.
	 */

	MaterialType(float density, float friction, float restitution, String name)
	{
		this.density = density;
		this.friction = friction;
		this.restitution = restitution;
		this.name = name;
	}
	public float density;
	public float restitution;
	public float friction;
	public String name;

}
