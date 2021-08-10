package com.egocentric.towerbuilder;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.egocentric.towerbuilder.screens.GameScreen;
import com.egocentric.towerbuilder.screens.MenuScreen;
import com.egocentric.towerbuilder.screens.NewGameScreen;
import com.egocentric.towerbuilder.screens.TestScreen;

public class GameController extends Game {

	public SpriteBatch batch;

	public SpriteBatch guiBatch;
	public BitmapFont font;

	public AssetManager assetManager;

	public OrthographicCamera cam;

	// TODO: Make a handler ?
	public GameScreen gameScreen;
	public MenuScreen menuScreen;
	public NewGameScreen newGameScreen;

	public TestScreen testScreen;

	public InputMultiplexer inputMultiplexer;

	public GameOptions gameOptions = new GameOptions();

	private boolean debug = true;

	// TODO: Move !
	public Skin skin;
	@Override
	public void create () {

		assetManager = new AssetManager();
		batch = new SpriteBatch();

		cam = new OrthographicCamera();
		cam.setToOrtho(false, 480, 800);



		guiBatch = new SpriteBatch();
		if( isMobile() )guiBatch.getProjectionMatrix().setToOrtho2D(0,0, 480, 800);
		font = new BitmapFont();

		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);
		/*
		if(isMobile())
		{
			font.getData().setScale(3f);
		}
		*/
		//skin = new Skin(Gdx.files.internal("uiskin.json"));

		// TODO: Don't generate these until the fonts have loaded.
		gameScreen = new GameScreen(this);
		menuScreen = new MenuScreen(this);
		newGameScreen = new NewGameScreen(this);
		testScreen = new TestScreen(this);
		loadAssets();
		this.setScreen(menuScreen);


		//this.setScreen(testScreen);
		// 1440 x 900
	}


	public Skin menuSkin;
	private void loadAssets()
	{
		gameScreen.loadAssets(assetManager);
		menuScreen.loadAssets(assetManager);
		newGameScreen.loadAssets(assetManager);
		assetManager.load("menu_ui.json", Skin.class);
		Background.loadAssets(assetManager);
		assetManager.update();

		//assetManager.finishLoading();


		// TODO: Use the asset manager ?


	}
	@Override
	public void render () {
		Gdx.gl.glClearColor(208/255f, 244/255f, 247/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();



	}

	@Override
	public void dispose()
	{
		super.dispose();
		batch.dispose();

		guiBatch.dispose();
		font.dispose();
		gameScreen.dispose();
		menuScreen.dispose();
		newGameScreen.dispose();
	}

	@Override
	public void resize(int width, int height)
	{
		super.resize(width, height);

		if(!isMobile())
			guiBatch.getProjectionMatrix().setToOrtho2D(0,0,width,height);

	}

	public boolean isDesktop()
	{
		return Gdx.app.getType() == Application.ApplicationType.Desktop;
	}

	public boolean isMobile()
	{
		return Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS;
	}

	public boolean isDebug() { return debug; }

	public void startGame()
	{
		// TODO: Reset the gameScreen screen, and set the data !
		gameScreen.initAssets(assetManager);
		gameScreen.initGui();
		// TODO: This shouldn't be done here.

		gameScreen.newGame(gameOptions);
		this.setScreen(gameScreen);
	}


	private boolean initializedAssets = false;
	public void onAssetsDoneLoading()
	{
		if(initializedAssets)
			return;

		Background.initAll(assetManager);

		initializedAssets = true;

	}
}
