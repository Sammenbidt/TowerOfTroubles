package com.egocentric.towerbuilder.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.egocentric.towerbuilder.GameController;
import com.egocentric.towerbuilder.GameOptions;
import com.egocentric.towerbuilder.gui.CreatePlayerDialog;
import com.egocentric.towerbuilder.gui.IDialogCloseListener;
import com.egocentric.towerbuilder.gui.PlayerNameTable;
import com.egocentric.towerbuilder.utils.UI;

/**
 * Created by Peter on 21/01/18.
 */
public class NewGameScreen extends AbstractScreen {

	public static final int SUB_TITLE_PAD_TOP = -30;
	public static final int SUB_TITLE_PAD_LEFT = 10;

	public static final int WINDOW_WIDTH = 400;

	public static final int MAX_PLAYERS = 6; // Maybe change to 8 ??
	int nPlayers = 0;
	// TODO: Move to other place
	private static final int DEFAULT_NUMBER_OF_BRICKS = 10;
	private static final int DEFAULT_SECONDS_DELAY = 5;

	private Table rootTable;
	private Table gameOptionsTable;
	private Table playerTable;

	private Slider brickSlider;
	private Slider delaySlider;

	private Label nBricksLabel;
	private Label nSecondsDelayLabel;

	private Skin skin;

	private TextButton addPlayerButton;
	private VerticalGroup playerList;

	private TextButton startGameButton;
	private TextButton advancedButton;


	public NewGameScreen(GameController gameController)
	{
		super(gameController);


		stage.setDebugAll(debug);
	}

	@Override
	public void initAssets(AssetManager assetManager)
	{
		initGui();
	}

	@Override
	public void loadAssets(AssetManager assetManager)
	{

	}

	private boolean guiInitialized = false;
	private void initGui()
	{
		rootTable = new Table();
		rootTable.setFillParent(true);
		stage.addActor(rootTable);

		// Test test !!
		skin = gameController.assetManager.get("menu_ui.json", Skin.class);

		gameOptionsTable = new Table(skin);

		gameOptionsTable.setBackground(skin.getDrawable("GreyPanel") );

		Label label = new Label("Game Options", skin, "gameOptionsSplitter");



		rootTable.add(gameOptionsTable).center().width(WINDOW_WIDTH);
		gameOptionsTable.add(label).left().top().colspan(2).expandX().padTop(SUB_TITLE_PAD_TOP).padLeft(SUB_TITLE_PAD_LEFT);//.padTop(-25).padLeft(10);
		gameOptionsTable.row();

		// TODO: Add GameMode


		/**
		 *  Bricks
		 */
		Label bricksLabel = new Label("Start Bricks: ", skin);
		nBricksLabel = new Label("" +DEFAULT_NUMBER_OF_BRICKS, skin);

		gameOptionsTable.add(bricksLabel).left();
		gameOptionsTable.add(nBricksLabel).left().expandX().fillX().padLeft( 10);
		gameOptionsTable.row();

		brickSlider = new Slider(5, 30, 1, false, skin, "blue-horizontal");
		brickSlider.setValue(DEFAULT_NUMBER_OF_BRICKS);

		brickSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				nBricksLabel.setText( "" + ((int)brickSlider.getValue()));

			}
		});

		Table brickTable = UI.createSliderTable(skin, brickSlider);
		gameOptionsTable.add(brickTable).center().colspan(2);
		gameOptionsTable.row();


		/**
		 * Delay between turns
		 */
		Label delayLabel = new Label("Delay between turns: ", skin);
		nSecondsDelayLabel = new Label("" + DEFAULT_SECONDS_DELAY, skin);

		Table delayTable = new Table();
		delayTable.add(delayLabel).left();
		delayTable.add(nSecondsDelayLabel).left().expandX().fillX().padLeft(10);

		gameOptionsTable.add(delayTable).colspan(2).left().expandX().fillX();

		gameOptionsTable.row();

		delaySlider = new Slider(1, 10, 0.5f, false, skin, "blue-horizontal");
		delaySlider.setValue(DEFAULT_SECONDS_DELAY);
		delaySlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				nSecondsDelayLabel.setText( delaySlider.getValue() + "");
			}
		});
		Table delaySliderTable = UI.createSliderTable(skin, delaySlider);
		gameOptionsTable.add(delaySliderTable).center().colspan(2);

		gameOptionsTable.row();
		Label spaceLabel = new Label(" ", skin);
		gameOptionsTable.add( spaceLabel).height(30);
		gameOptionsTable.row();

		/**
		 * The player information
		 */
		playerTable = new Table();
		playerList = new VerticalGroup();
		playerList.left();
		playerList.fill();
		playerList.space(8);

		// Changed from yellowPanel
		playerTable.setBackground(skin.getDrawable("GreyPanel"));

		Label playerLabel = new Label("   Players   ", skin, "gameOptionsSplitter");
		playerTable.add(playerLabel).colspan(2).left().expandX().top().left().padTop( SUB_TITLE_PAD_TOP);
		playerTable.row();

		playerTable.add(playerList).expandX().fillX().top().padTop(8);
		playerTable.row();

		playerTable.row().expandY().fillY();

		addPlayerButton = new TextButton("Add Player", skin, "blue");

		playerTable.add(addPlayerButton).width(250).bottom().maxHeight(40f);

		gameOptionsTable.add(playerTable).colspan(2).expandX().fillX().height(300).maxHeight(300).minHeight(300).width(WINDOW_WIDTH);
		gameOptionsTable.row();

		addPlayerButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				addPlayerClicked();
			}
		});

		/**
		 *  Creating the advanced part of the options
		 *
		 */
/*
		gameOptionsTable.add(" ").height(24).colspan(2);
		gameOptionsTable.row();

		advancedButton = new TextButton("Advanced Options", skin, "blue");

		gameOptionsTable.add(advancedButton).colspan(2).center().expandX();

		gameOptionsTable.row();
		gameOptionsTable.add(" ").height(12).colspan(2);

		gameOptionsTable.row();

*/

		startGameButton = new TextButton("Start Game", skin, "green");

		gameOptionsTable.add(startGameButton).center().bottom().width(WINDOW_WIDTH - 50).colspan(2).expandY().padTop(100);



		startGameButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				startGame();
			}
		});


		guiInitialized = true;
	}

	private void startGame()
	{
		// TODO: Implement.
		System.out.println("Start Game !");

		GameOptions gOptions = gameController.gameOptions;
		if(gOptions == null)
		{ // If it doesn't exist. Create it !
			gOptions = new GameOptions();
			gameController.gameOptions = gOptions;
		}

		gOptions.reset();

		// Read the data
		String[] names = new String[playerList.getChildren().size];
		int n = 0;
		for(Actor actor : playerList.getChildren())
		{
			PlayerNameTable nTable = (PlayerNameTable) actor;
			names[n++] = nTable.getName();
		}

		gOptions.createPlayers(names);
		gOptions.startBricks = getStartBricks();
		gOptions.stabilityDelay = getStabilityDelay();

		gameController.startGame();

	}

	private int getStartBricks()
	{
		return (int)brickSlider.getValue();
	}

	private float getStabilityDelay()
	{
		return delaySlider.getValue();
	}

	private void addPlayerClicked()
	{
		// Check if the maximum number of players is reached.
		if(nPlayers == MAX_PLAYERS)
		{
			// This should never happen !
			// TODO: Show an error code !
			return;
		}

		final CreatePlayerDialog dialog = new CreatePlayerDialog("Create New Player", skin);
		dialog.setDialogListener(createPlayerListener);
		dialog.show(stage);
		stage.setKeyboardFocus(dialog.nameField);


		Action action = Actions.fadeOut(0.5f);
		playerList.addAction(action);
		//rootTable.addAction(Actions.);

		// Hmm
	}

	private IDialogCloseListener createPlayerListener = new IDialogCloseListener() {

		@Override
		public void onResult(int resultCode, Object object)
		{

			if(resultCode == CreatePlayerDialog.CODE_SUCCES)
			{
				String name = (String) object;
				addPlayerName(name);
			}

			Action action = Actions.fadeIn(0.5f);
			playerList.addAction(action);
		}
	};

	private void addPlayerName(String playerName)
	{
		final PlayerNameTable nameTable = new PlayerNameTable(skin, playerName);

		nameTable.setBackground(skin.getDrawable("YellowPanel"));

		nameTable.deleteButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				removePlayer(nameTable);

				/*
				removePlayer(nameTable);
				playerRemoved(nameTable.getTextureName());
				*/
			}
		});

		nameTable.setHeight(300);
		playerList.addActor(nameTable);
		nPlayers++;
		if(nPlayers == MAX_PLAYERS)
		{
			// TODO: Maybe add an action here ??
			addPlayerButton.setVisible(false);
		}

		if(nPlayers > 1)
			startGameButton.setDisabled(false);

	}


	private void removePlayer(PlayerNameTable nameTable)
	{
		playerList.removeActor(nameTable);
		nPlayers--;
		if(nPlayers < MAX_PLAYERS && !addPlayerButton.isVisible())
			addPlayerButton.setVisible(true);

		if(nPlayers < 2)
		{
			startGameButton.setDisabled(true);
		}
	}

	/*
	private void playerRemoved(String player)
	{
		nPlayers--;
		if(!addPlayerButton.isVisible())
			addPlayerButton.setVisible(true);
	}
	*/


	@Override
	public void show()
	{
		super.show();

		if(!guiInitialized)
			initGui();
		checkButtons();


	}

	private void checkButtons()
	{
		addPlayerButton.setDisabled(nPlayers == MAX_PLAYERS);
		startGameButton.setDisabled( nPlayers < 2);
	}

	@Override
	protected void draw(float delta)
	{

	}

	private boolean debug = false;


	@Override
	protected void update(float delta)
	{
		// TODO: Remove !
		if(Gdx.input.isKeyJustPressed(Input.Keys.D))
		{
			debug = !debug;
			stage.setDebugAll(debug);
		}
	}
}
