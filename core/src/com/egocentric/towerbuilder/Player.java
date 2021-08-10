package com.egocentric.towerbuilder;

import com.badlogic.gdx.utils.Array;
import com.egocentric.towerbuilder.entities.Block;
import com.egocentric.towerbuilder.entities.BlockType;
import com.egocentric.towerbuilder.entities.MaterialType;

/**
 * Created by Peter on 02/02/18.
 */
public class Player {

	private String name;

	private Array<Block> blocks;

	public Player(String name)
	{
		this.name = name;
		blocks = new Array<Block>();
	}

	public String getName()
	{
		return name;
	}

	public void addBlock(Block block)
	{
		blocks.add(block);
	}

	public Block addBlock(BlockType blockType, MaterialType matType)
	{
		Block block = new Block(blockType, matType);
		addBlock(block);
		return block;
	}

	/**
	 * Returns true if the block was found
	 * @param block
	 * @return true if the block was found, else false
	 */
	public boolean removeBlock(Block block)
	{
		return blocks.removeValue(block, true);
	}

	public int blocksLeft() { return blocks.size; }

	public Array<Block> getBlocks() { return blocks; }

	public void addBlocks(Array<Block> blocks)
	{
		this.blocks.addAll(blocks);
	}

	public int getBricksLeft()
	{
		return blocks.size;
	}


}
