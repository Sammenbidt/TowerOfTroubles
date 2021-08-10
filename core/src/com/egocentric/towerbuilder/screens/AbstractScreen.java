package com.egocentric.towerbuilder.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.egocentric.towerbuilder.GameController;


/**
 * Created by Peter on 25/12/17.
 */
public abstract class AbstractScreen implements Screen {

	protected final GameController gameController;
	protected Stage stage;


	public AbstractScreen(GameController gameController)
	{
		this.gameController = gameController;
		OrthographicCamera cam = new OrthographicCamera();
		cam.setToOrtho(false, 480, 800);
		Viewport viewport = new StretchViewport(480, 800);


		this.stage = new Stage(viewport, gameController.guiBatch);
		//stage.setViewport(new StretchViewport(480, 800));

		/*
		stage.getBatch().getProjectionMatrix().setToOrtho2D(0,0, 480, 800);
		stage.getViewport().setScreenSize(480, 800);
		*/
		//new Stage()


	}

	/**
	 * Should
	 */
	public abstract void initAssets(AssetManager assetManager);

	public abstract void loadAssets(AssetManager assetManager);

	@Override
	public void show() {
		// TODO: Use a multiplexer instead.
		gameController.inputMultiplexer.clear();

		gameController.inputMultiplexer.addProcessor(stage);
		//gameController.inputMultiplexer.addProcessor(this);

		//Gdx.input.setInputProcessor(stage);

	}

	@Override
	public void render(float delta)
	{
		update(delta);
		draw(delta);

		stage.act(delta);
		stage.draw();

		postDraw();


	}

	protected abstract void draw(float delta);
	protected abstract void update(float delta);

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose()
	{
		stage.dispose();

	}

	protected void postDraw()
	{

	}

}
