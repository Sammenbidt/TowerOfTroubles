package com.egocentric.towerbuilder.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.egocentric.towerbuilder.entities.Block;

/**
 * Created by Peter on 30/12/17.
 */
public class BlockActor extends Image{

	public Block block;

	public final static float DEFAULT_SCALE = 0.8f;
	public final static float NORMAL_SCALE = 1f / DEFAULT_SCALE;

	public BlockActor(Skin skin, String drawableName)
	{
		super(skin, drawableName);

		this.setScale(DEFAULT_SCALE);

	}

	public BlockActor(Skin skin, Block block)
	{
		super(skin, block.blockType.getTextureName() + block.matType.name() );
		this.setScale(DEFAULT_SCALE);
		this.setData(block);
	}


	// TODO: Create a construtor that sets the image based on the type and mat !

	public void setData(Block block)
	{
		this.block = block;
	}




	/*
	public static BlockActor createBlockActor(Skin skin, Block block)
	{
		String
	}
*/




}
