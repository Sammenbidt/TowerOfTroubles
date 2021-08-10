package com.egocentric.towerbuilder.utils.b2d;

/**
 * Created by Peter on 26/12/17.
 */
public class CollisionFilters {


	public static final short MASK_GROUND = 0x01;
	public static final short MASK_BLOCK = 0x02;
	public static final short MASK_SENSOR = 0x03;


	public static final short COLLISION_GROUND 	= MASK_GROUND | MASK_BLOCK;
	public static final short COLLISION_BLOCK	= MASK_GROUND | MASK_BLOCK | MASK_SENSOR;
	public static final short COLLISION_SENSOR	= MASK_BLOCK | MASK_SENSOR;




}
