package com.egocentric.towerbuilder.gui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.egocentric.towerbuilder.Player;

/**
 * Created by Peter on 14/02/18.
 */
public class PlayerTurnDialog extends Dialog {

	private static final String PLAYER_TURN_DIALOG_STYLE_NAME = "";

	private IDialogCloseListener listener;
	private TextButton okButton;
	private Label nameLabel;
	private Label bricksLabel;
	public PlayerTurnDialog( Skin skin, Player player)
	{
		this(skin, player.getName());
	}

	public PlayerTurnDialog(Skin skin, String name)
	{
		super("", skin , "playerTurn");
		init(skin);
	}


	private void init(Skin skin)
	{
		nameLabel = new Label("", skin, "playerTurnLabel"); // TODO :Add Style name
		bricksLabel = new Label("", skin, "playerTurnLabel");




		okButton = new TextButton("Ok", skin, "green" );

		Table table = this.getContentTable();
		table.setBackground("GreyPanel");
		table.row();

		table.add(nameLabel).center().expandX();
		table.row();

		table.add(bricksLabel).center();
		table.row();
		table.add(okButton).center().padTop(80).expandX().fillX();

		okButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				okPressed();
			}
		});


	}

	public void setData(Player player)
	{
		this.setPlayerName(player.getName());
		this.setData(player.getName(), player.getBricksLeft());

	}

	public void setPlayerName(String name)
	{
		if(name.charAt(name.length() -1) == 's')
		{
			nameLabel.setText(name + "' turn.");
		}else
		{
			nameLabel.setText(name + "'s turn.");
		}

		//nameLabel.setText(name);
	}
	public void setBricksLeft(int bricksLeft)
	{
		if(bricksLeft == 1)
		{
			bricksLabel.setText("With " + bricksLeft + " brick left.");
		}else
		{
			bricksLabel.setText("With " + bricksLeft + " bricks left.");
		}
	}

	public void setData(String name, int bricksLeft)
	{
		this.setPlayerName(name);
		this.setBricksLeft(bricksLeft);
	}


	public void setDialogListener(IDialogCloseListener listener)
	{
		this.listener = listener;
	}


	public void okPressed()
	{
		if(this.listener != null)
			listener.onResult(CreatePlayerDialog.CODE_SUCCES, null);
		this.hide();
	}

	@Override
	public float getPrefWidth()
	{
		return 200f;
	}


}
