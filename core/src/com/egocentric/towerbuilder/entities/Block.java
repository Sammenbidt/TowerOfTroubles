package com.egocentric.towerbuilder.entities;

/**
 * Created by Peter on 30/12/17.
 */
public class Block {

	public BlockType blockType;
	public MaterialType matType;

	public Block()
	{

	}

	public Block(Block block)
	{
		this.blockType = block.blockType;
		this.matType = block.matType;
	}
	public Block(BlockType blockType, MaterialType matType)
	{
		this.setData(blockType, matType);
	}

	public void setData(BlockType blockType, MaterialType matType)
	{
		this.blockType = blockType;
		this.matType = matType;
	}

	public Block copy()
	{
		Block block = new Block(this);
		return block;
	}

}
