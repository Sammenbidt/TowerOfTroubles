package com.egocentric.towerbuilder;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Peter on 06/02/18.
 */
public enum Background {
	//Gdx.gl.glClearColor(208/255f, 244/255f, 247/255f, 1);
	Land("colored_land.png", new Color(208/255f, 244/255f, 247/255f, 1f)),
	Desert("colored_desert.png", new Color(208/255f, 244/255f, 247/255f, 1f)),
	Grass("colored_grass.png", new Color(208/255f, 244/255f, 247/255f, 1f)),
	Shroom("colored_shroom.png", new Color(208/255f, 244/255f, 247/255f, 1f));


	public final String textureName;
	// TODO: Change to a texture Region.
	public Texture region;
	public Color backgroundColor;



	Background(String textureName, Color backgroundColor)
	{
		this.textureName = textureName;
		this.backgroundColor = backgroundColor;

	}

	public void init(AssetManager assetManager)
	{
		this.region = assetManager.get(textureName, Texture.class);
	}

	public static void initAll(AssetManager assetManager)
	{
		for(Background type : Background.values())
		{
			type.init(assetManager);
		}
	}

	public static void loadAssets(AssetManager assetManager)
	{
		for(Background type : Background.values())
		{
			assetManager.load(type.textureName, Texture.class);
		}
	}

}
