package com.egocentric.towerbuilder.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.egocentric.towerbuilder.GameController;

/**
 * Created by Peter on 05/01/18.
 */
public class MenuScreen extends AbstractScreen {

	private Table rootTable;
	private BitmapFont font;
	private SpriteBatch guiBatch;
	public MenuScreen(GameController gameController)
	{
		super(gameController);
		font = gameController.font;
		guiBatch = gameController.guiBatch;
	//	initGui();
	}

	public void show()
	{
		super.show();

	}

	private boolean initializedGui = false;

	private TextButton button;
	private void initGui()
	{



		initializedGui = true;


		rootTable = new Table();
		rootTable.setFillParent(true);

		Skin skin = gameController.assetManager.get("menu_ui.json", Skin.class);

		Label label = new Label("Click screen to start", skin);

		rootTable.add(label).expand().center().bottom().padBottom(75);

		stage.addActor(rootTable);

		stage.setDebugAll(true);

		SequenceAction sequence = new SequenceAction();


		AlphaAction fadeIn = Actions.alpha( 1.0f, 1.0f);
		DelayAction wait = Actions.delay(1.0f);
		AlphaAction fadeOut = Actions.alpha(0.25f, 1.0f);

		sequence.addAction(fadeIn);
		sequence.addAction(wait);
		sequence.addAction(fadeOut);


		label.addAction(Actions.forever(sequence));
		rootTable.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				screenClicked();

			}
		});

	}





	@Override
	protected void draw(float delta)
	{

	}

	boolean finishedLoading = false;

	private void screenClicked()
	{
		gameController.setScreen(gameController.newGameScreen);
	}

	@Override
	protected void update(float delta)
	{
		if(gameController.assetManager.update())
		{
			finishedLoading = true;
			gameController.onAssetsDoneLoading();
			if(!initializedGui)
				initGui();
		}

		if(finishedLoading && Gdx.input.isTouched())
		{
			screenClicked();
		}
		// TODO: Write loading somewhere !?
	}

	@Override
	public void initAssets(AssetManager assetManager)
	{
		// Nothing is done here !
	}

	@Override
	public void loadAssets(AssetManager assetManager)
	{

	}
}
