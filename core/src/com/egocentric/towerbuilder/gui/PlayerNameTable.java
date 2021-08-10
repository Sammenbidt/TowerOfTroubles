package com.egocentric.towerbuilder.gui;

import com.badlogic.gdx.scenes.scene2d.ui.*;

/**
 * Created by Peter on 26/01/18.
 */
public class PlayerNameTable extends Table {

	private Label nameLabel;
	public Button deleteButton;
	private Label spacerLabel; // Maybe use use a cell ?

	// TODO: Change the name ?
	public PlayerNameTable( Skin skin, String playerName)
	{
		this(skin, "", playerName);

	}

	public PlayerNameTable(Skin skin, String styleName, String playerName)
	{
		super();
		if(styleName.trim().length() == 0)
			styleName = null;

		nameLabel = styleName == null ? new Label(playerName, skin) : new Label(playerName, skin, styleName);

		spacerLabel = new Label("", skin);

		deleteButton = new Button(skin, "delete");

		this.add(nameLabel);
		this.add(spacerLabel).expandX().fillX();
		this.add(deleteButton).height(24).width(24);

	}

	public String getName() { return nameLabel.getText().toString(); }
	public void setName(String name) { nameLabel.setText(name); }

	@Override
	public float getPrefHeight()
	{
		return 36;
	}
}
