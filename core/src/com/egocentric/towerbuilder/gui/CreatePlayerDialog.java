package com.egocentric.towerbuilder.gui;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by Peter on 24/01/18.
 */
public class CreatePlayerDialog extends Dialog {

	public static final int CODE_SUCCES = 1;
	public static final int CODE_CANCEL = -1;
	public Button cancelButton;
	public TextButton saveButton;


	private IDialogCloseListener listener;
	public String name = "";

	public TextField nameField;

	public int returnCode = CODE_CANCEL;
	public CreatePlayerDialog(String title, Skin skin)
	{
		super(title, skin);
		init(skin);
	}

	public CreatePlayerDialog(String title, Skin skin, String windowStyleName)
	{
		super(title, skin, windowStyleName);
		init(skin);
	}

	private void init(Skin skin)
	{
		this.setMovable(false);
		//this.setDebug(true);
		cancelButton = new Button(skin,"delete" );
		saveButton = new TextButton("Add Player", skin, "green");

		nameField = new TextField("", skin, "nameInput");
		nameField.setMaxLength(20);
		nameField.setMessageText("Name");


		this.getTitleLabel().setStyle(skin.get("dialogTitle", Label.LabelStyle.class));

		this.getTitleTable().getCell(this.getTitleLabel()).padTop(-10);
		this.getTitleTable().add(cancelButton).right().expandY().padTop(-10).padLeft(40);


		// stage.setKeyboardFocus(textField)

		this.getContentTable().add(nameField).expandX().fillX();







		//this.getButtonTable().add(cancelButton).left().padLeft(20).expandX();
		this.getButtonTable().add(saveButton).center().expandX();


		cancelButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				cancelClicked();


			}
		});

		saveButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				addClicked();
			}
		});


		// Lets set the background
		//this.setBackground("RedPanel");


		nameField.getOnscreenKeyboard().show(true);
	}

	@Override
	public float getPrefWidth()
	{
		// Old 400
		return 400;
	}

	@Override
	public float getPrefHeight()
	{
		// Old 140
		return 140;
		//return 400;
	}



	private void cancelClicked()
	{
		returnCode = CODE_CANCEL;
		result();
		//this.hide();
	}

	private void addClicked()
	{
		name = nameField.getText();
		name.trim();

		System.out.println("Length of name: " + name.length());
		if(name.length() == 0)
		{
			// TODO: Implement Error
			return;
		}

		returnCode = CODE_SUCCES;

		System.out.println("ADD CLICKED !");
		//result(name);
		result();


		//this.hide();

		// TODO: Implement
	}

	// TODO: Maybe change to a string, or make the class more abstract.
	private Object object;

	private void result()
	{
		if(returnCode == CODE_SUCCES)
		{
			this.object = name;


		}else
		{
			object = null;

		}



		listener.onResult(returnCode, object);
		// TODO: Check if android ??
		nameField.getOnscreenKeyboard().show(false);

		this.hide();

	}

	/*
	public CreatePlayerDialog(String title, WindowStyle windowStyle)
	{
		super(title, windowStyle);
	}
	*/

	public void setDialogListener(IDialogCloseListener listener) { this.listener = listener; }
}
