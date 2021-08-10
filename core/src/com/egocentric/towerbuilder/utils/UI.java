package com.egocentric.towerbuilder.utils;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Created by Peter on 24/01/18.
 */
public class UI {

	public static final Table createSliderTable(Skin skin , Slider slider, Drawable background, boolean roundToInt)
	{
		Table table = new Table();
		if(background != null)
		{
			table.setBackground(background);
		}


		String s = roundToInt ? (int)(slider.getMinValue()) + "" : slider.getMinValue() + "";
		Label minLabel = new Label(s, skin, "SliderData");

		s = roundToInt ? (int)(slider.getMaxValue()) + "" : slider.getMaxValue() + "";
		Label maxLabel = new Label( s, skin , "SliderData");


		Image knobLeft = new Image(skin.getDrawable("GreySliderEnd"));
		Image knobRight = new Image(skin.getDrawable("GreySliderEnd"));

		table.add(minLabel).padRight(5);

		table.add(knobLeft);
		table.add(slider).width(250);
		table.add(knobRight);
		table.add(maxLabel).padLeft(5);






		return table;
	}

	public static final Table createSliderTable(Skin skin, Slider slider)
	{
		return createSliderTable(skin, slider, null, true);
	}

	public static final Table createSliderTable(Skin skin, Slider slider, boolean roundToInt)
	{
		return createSliderTable(skin, slider, null, roundToInt);
	}


	public static final Table createPlayerNameTable(Skin skin, String playerName)
	{
		return createPlayerNameTable(skin, null, playerName);
		/*
		Table table = new Table();

		Label nameLabel = new Label(playerName, skin);
		table.add(nameLabel);

		Label spacerLabel = new Label("", skin);

		table.add(spacerLabel).expandX().fillX();


		// Image button
		ImageButton deleteButton = new ImageButton(skin.getDrawable("DeleteButton"));

		table.add(deleteButton).right();


		return table;
		*/

	}
	public static final Table createPlayerNameTable(Skin skin, String styleName, String playerName)
	{


		Table table = new Table();
		// TODO: Add the stylename here !
		Label nameLabel;
		if(styleName == null)
			nameLabel = new Label(playerName, skin);
		else
			nameLabel = new Label(playerName, skin, styleName);
		table.add(nameLabel);

		Label spacerLabel = new Label("", skin);

		table.add(spacerLabel).expandX().fillX();


		// Image button
		Button deleteButton = new Button(skin, "delete");

		// TODO: Change the size in the sprites instead?
		table.add(deleteButton).right().size(27);


		return table;
	}




}
