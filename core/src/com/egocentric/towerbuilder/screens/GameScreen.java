package com.egocentric.towerbuilder.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.egocentric.towerbuilder.*;
import com.egocentric.towerbuilder.entities.*;
import com.egocentric.towerbuilder.gui.BlockActor;
import com.egocentric.towerbuilder.gui.IDialogCloseListener;
import com.egocentric.towerbuilder.gui.PlayerTurnDialog;
import com.egocentric.towerbuilder.utils.Constants;
import com.egocentric.towerbuilder.utils.HeightFinder;
import com.egocentric.towerbuilder.utils.b2d.BodyBuilder;

import java.util.Random;

import static com.egocentric.towerbuilder.utils.Constants.*;
/**
 * Created by Peter on 25/12/17.
 */
public class GameScreen extends AbstractScreen implements GestureDetector.GestureListener {

	World world;
	Box2DDebugRenderer boxRenderer;

	//private static final Color INVALID_PLACEMENT_COLOR 	= new Color( 1.0f, 0.0f, 0.0f, 1.0f);
	//private static final Color VALID_PLACEMENT_COLOR 	= new Color(1.0f, 1.0f, 1.0f, 1.0f);

	//Body groundBody;
	private Body groundBody;
	private Entity groundEntity;

	private Body activeBody;

	//private float height = 3; // in meters.

	Background background;

	ShapeRenderer shapeRenderer;

	private OrthographicCamera cam;

	private Body garbageZone;

	//Array<Entity> entities = new Array<Entity>();
	Array<Entity> renderEntities = new Array<Entity>();


	// Move to an assets folder and class ?
	private TextureRegion[][] blockTextures;

	private Skin skin;

	private Table mainTable;

	private Label playerTurnLabel;

	//private Vector2 inventoryPosition;

	GameState state = GameState.PlayerTurn;

	private Label bricksLeftLabel;

	private static final Vector2 v2 = new Vector2();
	private static final Vector3 v3 = new Vector3();


	/**  TEST **/
	private Stack stack;

	public GameScreen(GameController gameController) {
		super(gameController);
		cam = gameController.cam;

		gameController.assetManager.load("bricks.atlas", TextureAtlas.class);

		world = new World(new Vector2(0, -9.92f), true);
		world.setContactListener(contactListener);

		boxRenderer = new Box2DDebugRenderer();

		// TODO : Any reason not moving the camera to here ?
		gameController.cam.position.set(0,0,0);

		// Used to draw the black line !
		shapeRenderer = new ShapeRenderer();

		// This is always here
		garbageZone = BodyBuilder.createRectangle(world, true, true, true, 20, 2, 0, -2);

		checkCameraPosition();
	}

	@Override
	public void initAssets(AssetManager assetManager)
	{

		System.out.println("******************************");
		System.out.println("INIT ASSETS");
		System.out.println("******************************");
		blockTextures = new TextureRegion[BlockType.values().length][MaterialType.values().length];
		TextureAtlas bricks = assetManager.get("bricks.atlas", TextureAtlas.class);


		bricks.getRegions().get(1).getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		for(BlockType blockType : BlockType.values())
		{
			for(MaterialType matType : MaterialType.values())
			{
				String s = (blockType.getTextureName() + "" + matType.name());
				TextureRegion region = bricks.findRegion(s);

				if(region != null)
				{
					// TODO: Move to an assets class instead ?
					blockTextures[blockType.ordinal()][matType.ordinal()] = region;

				}else
				{
					System.err.println("Couldn't find texture region with name: " + s);
				}

			}
		}
		// TODO: Read as a variable from the gameOptions

		skin = assetManager.get("menu_ui.json", Skin.class);

	}

	@Override
	public void loadAssets(AssetManager assetManager)
	{
		assetManager.load("bricks.atlas", TextureAtlas.class);
		assetManager.load("colored_grass.png", Texture.class);
	}


	@Override
	public void show()
	{
		//super.show();
		gameController.inputMultiplexer.clear();
		gameController.inputMultiplexer.addProcessor(stage);
		gameController.inputMultiplexer.addProcessor(new GestureDetector(this));



		//Gdx.input.setInputProcessor(new GestureDetector(this));
		//gameController.inputMultiplexer.addProcessor(new GestureDetector(this));
		if(!guiInitialized)
		{
			//TODO:  This shouldn't be called here !
			initAssets(gameController.assetManager);
			initGui();
		}
	}

	private boolean guiInitialized = false;


	private Image inventoryImage;

	// Maybe make an inventory for each player, instead of recreating the boxes every turn.
	HorizontalGroup inventory;
	ScrollPane scrollPane;


	private Drawable openInventoryDrawable;
	private Drawable closedInventoryDrawable;

	 // This is the new version of the initGui.
	public void initGui()
	{
	// TODO: Split into several methods.
		int nColumns = 1;
		if(guiInitialized)
			return;



		System.out.println("Initializing gui ");
		mainTable = new Table();
		mainTable.setFillParent(true);


		closedInventoryDrawable = skin.getDrawable("inventory_closed");
		openInventoryDrawable = skin.getDrawable("inventory_open");

		inventoryImage = new Image(skin, "inventory_closed");


		inventoryImage.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				toggleInventory();
			}
		});


		playerTurnLabel = new Label("CurrentPlayer",skin, "playerTurn");




		Table bricksLeftTable = new Table(skin);
		bricksLeftTable.setBackground("grey_circle");
		//Table bricksLeftBackgroundTable = new Table();




		//Stack bricksLeftStack = new Stack();
		//Image bricksLeftImage = new Image(skin, "grey_circle");
		bricksLeftLabel = new Label("99", skin, "playerBricksLeft");
		bricksLeftLabel.setFontScale(0.85f);

		//bricksLeftLabel.setFillParent(false);

		//bricksLeftImage.setFillParent(true);


		bricksLeftTable.add(bricksLeftLabel).center();


		// New inventory

		inventory = new HorizontalGroup();
		inventory.center().bottom().padBottom(10f);

		scrollPane = new ScrollPane(inventory, skin);
		scrollPane.setScrollingDisabled(false, true);
		scrollPane.setFadeScrollBars(true);




		//bricksLeftStack.add(bricksLeftImage);
		//bricksLeftStack.add(bricksLeftLabel);




		Table topTable = new Table(skin);
		topTable.setBackground("BluePanel");

		topTable.add(inventoryImage).left().width(44).height(48);
		topTable.add(bricksLeftTable).left().padLeft(-18).bottom().width(24).height(24);
		topTable.add(playerTurnLabel).expandX().left().padLeft(10);

		mainTable.add(topTable).colspan(nColumns).expandX().fillX().height(52);
		mainTable.row();
		mainTable.add(scrollPane).expandX().top().fillX().pad(10).height(140).expandY().colspan(nColumns).padTop(-10);
		mainTable.row();
		//mainTable.add(inventoryImage).left().width(44).height(48);
		//mainTable.add(playerTurnLabel).expandX().left().padLeft(10);

		scrollPane.setZIndex(0);
		//topTable.setZIndex(0);




		mainTable.row();

		guiInitialized = true;

		dragAndDrop.addTarget(new DragAndDrop.Target(scrollPane) {
			@Override
			public boolean drag(DragAndDrop.Source source, Payload payload, float x, float y, int pointer)
			{
				System.out.println("DRAG !");
				//showInventory();
				//clearBodyInWay();
				//source.getActor().setColor(VALID_PLACEMENT_COLOR);
				return true;
			}

			@Override
			public void drop(DragAndDrop.Source source, Payload payload, float x, float y, int pointer)
			{
				// TODO: Do something !
				System.out.println("Do something !");

				Block block = (Block) payload.getObject();
				returnBlockToPlayer(block);
				//returnBlockToPlayer(payload.getDragActor());
				// Add the item
				//showInventory();
				System.out.println("DROP !");
				clearBodyInWay();
				//source.getActor().setColor(VALID_PLACEMENT_COLOR);


			}

			@Override
			public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload)
			{

				//hideInventory();
				System.out.println("RESET !");
			}
		});



		playerTurnDialog = new PlayerTurnDialog(skin, "");
		playerTurnDialog.setDialogListener(playerTurnDialogListener);




		darkenImage = new Image(skin, "Black");
		darkenImage.setFillParent(true);


		/*
		darken = new Table(skin);
		darken.setFillParent(true);
		darken.setBackground("White");
		darken.setColor(0.0f, 0.0f, 0.0f, 0.8f);
		*/
		stack = new Stack(mainTable, darkenImage);
		darkenImage.setDebug(true);
		stack.setFillParent(true);

		//stage.addActor(mainTable);

		darkenImage.setVisible(false);
		//darken.setVisible(false);


		stage.addActor(stack);

	}




	private void returnBlockToPlayer(Block block)
	{
		BlockActor blockActor = new BlockActor(skin, block);

		initDragAndDrop(blockActor, inventory);
		inventory.addActor(blockActor);

		showInventory();

	}


	Image darkenImage;

	final DragAndDrop dragAndDrop = new DragAndDrop();



	// TEST METHOD FOR NEW DRAG AND DROP !


	private MouseJoint mouseJoint;
	private void initDragAndDrop(final BlockActor actor, final HorizontalGroup inventory)
	{
		dragAndDrop.addSource(new DragAndDrop.Source(actor) {
			@Override
			public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer)
			{
				if(inventoryShown == false || state != GameState.PlayerTurn)
					return null;
				Payload payload = new Payload();
				payload.setObject(actor.block);
				payload.setDragActor(null);
				currentPlayer.removeBlock(actor.block);

				// New


				v3.set(x,y,0);
				cam.unproject(v3);
				v2.set(v3.x, v3.y);
				Constants.scaleToWorld(v2);



				activeBody = createBlock(v2, actor.block);
				//activeBody = createBlock(v2, actor.block, 0f, 0f);

				// SETTING DRAG DATA !
				activeBody.getFixtureList().first().setRestitution(0.0f);

				activeBody.setAngularDamping(1f);

				Vector2 pos = unproject();
				activeBody.setTransform(pos.x, pos.y , 0);
				draggingBody = true;

				hideInventory();
				return payload;
			}

			@Override
			public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target)
			{

			}
		});
	}


	private final float MIN_DROP_HEIGHT = 3.5f;

/*
	private boolean blockDropped(Vector2 screenPos, Block block)
	{
		v3.set(screenPos,0);
		cam.unproject(v3);
		v2.set(v3.x, v3.y);
		Constants.scaleToWorld(v2);

		if(v2.y < MIN_DROP_HEIGHT) // Check we're above the minimum height
			return false;

		clearBodyInWay(HeightFinder.reportBody);
		canDrop = HeightFinder.checkPosition(world, v2, block.blockType);
		if(canDrop)
		{
			return true;
		}else
		{
			colorBodyInWay(HeightFinder.reportBody);
			return false;
		}
	}
*/
	private boolean canDrop = true;


	private void clearBodyInWay()
	{
		clearBodyInWay(HeightFinder.reportBody);
	}
	// Debug method
	private void clearBodyInWay(Body body)
	{
		if(body != null)
		{
			Entity e = (Entity) body.getUserData();
			Sprite sprite = e.getSprite();
			sprite.setColor(1f,1f,1f,1);

		}
	}

	// Debug method !
	private void colorBodyInWay(Body body)
	{

		if(body == null)
		{
			return;
		}
		if(body.getUserData() == null)
		{
			return;
		}
		Entity ee = (Entity) body.getUserData();
		ee.getSprite().setColor(1.0f, 0f, 0f, 1f);
	}





	private Body createBlock(Vector2 pos, Block block)
	{
		Body body =  BodyBuilder.createBlock(world, block.blockType, block.matType, pos.x, pos.y);
		body.setTransform(pos.x, pos.y, 0);

		BlockEntity e  = new BlockEntity();
		e.setBlock(block);
		e.setBody(body);

		body.setUserData(e);

		BlockType blockType = block.blockType;
		MaterialType matType = block.matType;

		e.setSpriteOffset(Constants.scaleToScreen(  blockType.offset.x), Constants.scaleToScreen(blockType.offset.y));
		TextureRegion region = blockTextures[blockType.ordinal()][matType.ordinal()];
		Vector2 origin = new Vector2( blockType.origin.x * region.getRegionWidth(), blockType.origin.y * region.getRegionHeight());
		e.setTexture(blockTextures[blockType.ordinal()][matType.ordinal()], origin);

		return body;
	}
	private Body createBlock(Vector2 pos, Block block, float imageWidth, float imageHeight)
	{
		return createBlock(pos, block);
		/*
		Body body =  BodyBuilder.createBlock(world, block.blockType, block.matType, pos.x, pos.y);
		body.setTransform(pos.x, pos.y, 0);

		BlockEntity e  = new BlockEntity();
		e.setBlock(block);
		e.setBody(body);

		body.setUserData(e);

		BlockType blockType = block.blockType;
		MaterialType matType = block.matType;

		e.setSpriteOffset(Constants.scaleToScreen(  blockType.offset.x), Constants.scaleToScreen(blockType.offset.y));
		TextureRegion region = blockTextures[blockType.ordinal()][matType.ordinal()];
		Vector2 origin = new Vector2( blockType.origin.x * region.getRegionWidth(), blockType.origin.y * region.getRegionHeight());
		e.setTexture(blockTextures[blockType.ordinal()][matType.ordinal()], origin);

		return body;
		*/
	}



	private boolean draggingBody = false;
	private static final float MIN_WAIT_TIME = 1.0f;
	//Array<Block> blocksToPlayer = new Array<Block>();

	protected void update(float delta)
	{
		if(Gdx.input.isKeyPressed(Input.Keys.D))
		{ // TODO: Remove
			mainTable.setDebug(!mainTable.getDebug());
		}
		// TODO: Add fling...
		// TODO: Make camera follow newly dropped blocks.
		//gameController.cam.position.add(move.x, move.y, 0);



		if(state == GameState.PlayerTurn)
		{
			if(draggingBody)
			{

				// Drag the body
				if( Gdx.input.isTouched())
				{
					activeBody.getLinearVelocity().set(0,0);

					Vector2 touchPos = new Vector2(unproject());
					touchPos = touchPos.sub(activeBody.getPosition());
					touchPos = touchPos.scl(1f/TIME_STEP);
					// TODO: MAKE STATIC Variable, maybe change value ??
					touchPos.clamp(0,10);

					activeBody.setLinearVelocity(touchPos.x,touchPos.y);



					// Check position, and move camera.
					//touchPos.x = Gdx.input.getX();
					touchPos.y = Gdx.input.getY();

					//
					System.out.println("Y = " + touchPos.y);
					if(touchPos.y < 50)
					{
						cam.position.y += 5f;
					}

					if(touchPos.y > Gdx.graphics.getHeight() - 50)
						cam.position.y -= 5f;

				}else
				{
					draggingBody = false; // Set state to moving !!
					changeState(GameState.WaitingForBricks);

					// Reset the data back !
					activeBody.setAngularDamping(0.0f);
					BlockEntity block = (BlockEntity)activeBody.getUserData();
					activeBody.getFixtureList().first().setRestitution(block.getBlock().matType.restitution);


				}

			}

		}


		if(state == GameState.WaitingForBricks) // We also update when it's the players turn.
		{

			if(!cameraMoved)
			{
				//
				if(activeBody != null)
				{
					v2.set( 0f, cam.position.y);
					Constants.scaleToWorld(v2);
					v2.lerp(activeBody.getPosition().add(0, -3.5f), 0.15f); // TODO: Don't use magic numbers !

					Constants.scaleToScreen(v2);
					cam.position.set(0f, v2.y, 0f);

					// Need to change to world data i think.


				}

			}
		}



		// The camera should be moved to here.





		// We only update the world, when we're in the state PlayerTurn

		if(state == GameState.WaitingForBricks || state == GameState.PlayerTurn)
		{
			accumulation += delta;
			while (accumulation >= TIME_STEP)
			{
				world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);

				accumulation -= TIME_STEP;
			}

			for (Body b : bodiesToRemove)
			{
				Object object = b.getUserData();
				if (object != null)
				{
					// This shouldn't be needed
					if (object instanceof BlockEntity)
					{
						returnBlockEntityToPlayer((BlockEntity) object, currentPlayer);
						//blocksToPlayer.add( ((BlockEntity) object).getBlock());
					} else
					{
						System.out.println("Not blockEntity! " + object.getClass());
					}
				} else
				{
					System.err.println("No userdata in object !");
				}
				world.destroyBody(b);
			}

			bodiesToRemove.clear();

		}

		checkCameraPosition();
		cam.update();
		// Move to the enum instead ??
		switch(state)
		{
			case PlayerTurn:
				// handled above.
				break;
			case WaitingForBricks:
				waitedTime += delta;

				// Check if all blocks are standing still, or if the time has passed.
				boolean noMovingBlocks;

				noMovingBlocks = checkForMovingBlocks(world);

				float maxVelocity = maxVelocity(world);

				if(waitedTime > MIN_WAIT_TIME && noMovingBlocks)
				{ // No blocks are moving, and the minimum required time has expired.

					if(particles.size == 0 )
						nextPlayer();
					break;
				}

				if(maxVelocity > 0.2f) // This is a very high velocity, it should never be meet. This might not be the right way to do this.
					break;
				if( waitTime < 0)
				{ // We have waited for maximum allowed time, to bad for the next player.
					// Maybe still check if any blocks are moving very fast...
					if(particles.size == 0)
						nextPlayer();
					break;

				}

				waitTime -= delta;

				break;
			case EndOfGame:
				// TODO: Do something !
				break;
		}


		for(Particle p : particles)
		{
			p.update(delta);
			if(p.isDone())
			{
				particles.removeValue(p, true);
				changeBricksLabel(1);
				animateBricksLeftLabel();
			}
		}
	}

	private void returnBlockEntityToPlayer(BlockEntity entity, Player player)
	{
		// TODO: Animate
		currentPlayer.addBlock(entity.getBlock());
		//entity.getBlock();
	}

	private float waitedTime = 0f;
	private void nextPlayer()
	{
		// First give all the bricks to the current player.
		//System.out.println("Size : blockToPlayer: " + blocksToPlayer.size);
		//currentPlayer.addBlocks(blocksToPlayer);
		//blocksToPlayer.clear();

		// TODO: Check if the player has won !
		nPlayer++;
		if(nPlayer >= players.size)
			nPlayer = 0;

		currentPlayer = players.get(nPlayer);
		setPlayerTurn(currentPlayer);


		//setInventoryBlocks(currentPlayer);

		changeState(GameState.PlayerTurn);
		waitTime = gameOptions.stabilityDelay;
		waitedTime = 0.0f;
		state = GameState.PlayerTurn;


		Action darkenAction = Actions.alpha(0.85f, 0.5f);
		Action hide = Actions.run(new Runnable() {
			@Override
			public void run()
			{
				darkenImage.setVisible(true);
				//darken.setVisible(true);
			}
		});
		Action darkenStageAction = Actions.sequence(hide, darkenAction);
		//darkenStageAction.restart();
		darkenImage.addAction(darkenStageAction);
		//darken.addAction(darkenStageAction);


		playerTurnDialog.setData(currentPlayer);
		playerTurnDialog.show(this.stage);

	}

	private IDialogCloseListener playerTurnDialogListener = new IDialogCloseListener() {
		@Override
		public void onResult(int resultCode, Object object)
		{
			showInventory();

			Action undarkenAction = Actions.alpha(0.0f, 0.5f);
			Action unHide = Actions.run(new Runnable() {
				@Override
				public void run()
				{
					darkenImage.setVisible(false);
					//darken.setVisible(false);
					showInventory();
				}
			});
			Action lightenStageAction = Actions.sequence(undarkenAction, unHide);

			darkenImage.addAction(lightenStageAction);
			//darken.addAction(lightenStageAction);
		}
	};

	private PlayerTurnDialog playerTurnDialog;

	float accumulation = 0f;
	float waitTime = 0; // Not good way to do it.



	private QueryCallback renderCallback = new QueryCallback() {
		@Override
		public boolean reportFixture(Fixture fixture)
		{
			Object o = fixture.getBody().getUserData();
			if( o != null)
				renderEntities.add( (Entity) o);
			return true;
		}
	};

	// Used by Box2dRenderer
	Matrix4 debugMatrix = new Matrix4();


	// Debug variables.
	private Vector2 mousePosWorld = new Vector2();
	private Vector2 mousePosScreen = new Vector2();

	private Vector2 viewportLower = new Vector2();
	private Vector2 viewportUpper = new Vector2();

	protected void draw(float delta)
	{
		Gdx.gl.glClearColor(background.backgroundColor.r, background.backgroundColor.g, background.backgroundColor.b, background.backgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gameController.batch.setProjectionMatrix(gameController.cam.combined);
		gameController.batch.begin();
		gameController.batch.disableBlending();
			//gameController.batch.draw(backgroundTexture, 0,0);
			gameController.batch.draw(background.region,-240, 0,480,800);
			gameController.batch.enableBlending();
		gameController.batch.end();

		// TODO: Add effects like skies etc.

		renderEntities.clear();
		// lowerX, lowerY, upperX, upperY

		viewportLower.set(
				gameController.cam.position.x - gameController.cam.viewportWidth/2f,
				gameController.cam.position.y - gameController.cam.viewportHeight/2f);
		viewportUpper.set(
				gameController.cam.position.x + gameController.cam.viewportWidth/2f,
				gameController.cam.position.y + gameController.cam.viewportHeight/2f

		);

		Constants.scaleToWorld(viewportLower, viewportLower);
		Constants.scaleToWorld(viewportUpper, viewportUpper);

		world.QueryAABB(renderCallback, viewportLower.x, viewportLower.y, viewportUpper.x, viewportUpper.y);

		gameController.batch.setProjectionMatrix(gameController.cam.combined);

		gameController.batch.begin();
			for(Entity e : renderEntities)
			{
				e.render(gameController.batch);
			}
		gameController.batch.end();



/*
		debugMatrix.set(gameController.cam.combined);
		debugMatrix.scl(Constants.PIXELS_PR_METER);

		boxRenderer.render(world, debugMatrix);

		*/
		/*

		shapeRenderer.setProjectionMatrix(gameController.cam.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

		shapeRenderer.setColor(Color.BLACK);

		shapeRenderer.rect( -240, Constants.scaleToScreen(height)  -1 , 480, 3);


		//shapeRenderer.line(-240, Constants.scaleToScreen(height) , 240, Constants.scaleToScreen(height));


		shapeRenderer.end();
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.rect(Constants.scaleToScreen(xMin), Constants.scaleToScreen(yMin), Constants.scaleToScreen(xMax - xMin), Constants.scaleToScreen(yMax - yMin));

		shapeRenderer.end();
*/


/*
		// TODO: Don't use magic numbers !
		gameController.guiBatch.begin();
			//gameController.font.draw(gameController.guiBatch, "Pos: " + groundBody.getPosition(), 10, 800 - 20);
			gameController.font.draw(gameController.guiBatch, "CamPos: " + gameController.cam.position, 10, 800 - 20);
			gameController.font.draw(gameController.guiBatch, "Height: " + height, 10, 800 - 40);

			gameController.font.draw(gameController.guiBatch, "Sprites Rendered: " + renderEntities.size, 10, 800 - 60);
			String s = String.format("ScreenPos: %3d, %3d", Gdx.input.getX(), Gdx.input.getY());
			gameController.font.draw(gameController.guiBatch, s, 10, 800 - 80);
			s = String.format("CameraPos: %.3f, %.3f", mousePosScreen.x, mousePosScreen.y );
			gameController.font.draw(gameController.guiBatch, s, 10, 800 - 100);
			s = String.format("WorldPos: %.3f, %.3f", mousePosWorld.x, mousePosWorld.y);
			gameController.font.draw(gameController.guiBatch, s, 10, 800 - 120);
			s = String.format("Current Player [%d] : %s", nPlayer, currentPlayer.getName() );
			gameController.font.draw(gameController.guiBatch, s, 10, 800 - 140);
			s = String.format("Current State: %s" , state.name() );
			gameController.font.draw(gameController.guiBatch, s, 10, 800 - 160);

			s = String.format("Time Left: %.2f" , waitTime );
			gameController.font.draw(gameController.guiBatch, s, 10, 800 - 180);



		gameController.guiBatch.end();
		*/

	}

	protected void postDraw()
	{
		gameController.guiBatch.begin();;

		for(Particle p : particles)
		{
			p.render(gameController.guiBatch);
		}
		gameController.guiBatch.end();

	}


	private void toggleInventory()
	{
		if(inventoryDisabled)
		{
			return;
		}

		if(inventoryShown)
			hideInventory();
		else
			showInventory();

	}


	boolean inventoryShown = true;

	// This does it, so you can't put it back...
	public void hideInventory()
	{
		//Action hideAction = Actions.fadeOut(0.2f);
		Action hideAction = Actions.sequence(
				Actions.fadeOut(0.2f),
				Actions.run(new Runnable() {
					@Override
					public void run()
					{
						//scrollPane.setVisible(false);
					}
				})
		);
		scrollPane.addAction(hideAction);
		inventoryImage.setDrawable(closedInventoryDrawable);
		inventoryShown = false;

	}
	private boolean inventoryDisabled = false;

	public void disableInventory()
	{
		inventoryDisabled = true;
		hideInventory();

	}

	public void enableInventory()
	{
		inventoryDisabled = false;
	}

	public void showInventory()
	{
		//Action showAction = Actions.fadeIn(0.2f);
		Action showAction = Actions.sequence(
				Actions.run(new Runnable() {
					@Override
					public void run()
					{
						scrollPane.setVisible(true);
					}
				}),
				Actions.fadeIn(0.2f)

		);
		scrollPane.addAction(showAction);
		inventoryImage.setDrawable(openInventoryDrawable);
		inventoryShown = true;
	}


	@Override
	public void dispose()
	{
		super.dispose();
		world.dispose();
	}


	private void checkCameraPosition()
	{
		if(gameController.cam.position.y - gameController.cam.viewportHeight/2f < 0)
			gameController.cam.position.y = gameController.cam.viewportHeight/2f;
	}


	// TODO: Maybe move all this to the drop Zone Image instead ??
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button)
	{
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		System.out.println("Fling !");
		// TODO: Implement camera velocity
		return true;
	}

	boolean cameraMoved = false;

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY)
	{
		cameraMoved = true;
		gameController.cam.position.y += (deltaY);
		checkCameraPosition();
		return true;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		//System.out.println("Zoom !");
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		//System.out.println("Pinch !");
		return false;
	}


	private Array<Body> bodiesToRemove = new Array<Body>();

	private ContactListener contactListener = new ContactListener() {

		@Override
		public void beginContact(Contact contact)
		{
			Fixture fixtureA = contact.getFixtureA();
			Fixture fixtureB = contact.getFixtureB();
			Body bodyA = fixtureA.getBody();
			Body bodyB = fixtureB.getBody();

			if(bodyA == garbageZone)
			{
				bodiesToRemove.add(bodyB);
				createParticle((BlockEntity)bodyB.getUserData());

			}else if(bodyB == garbageZone)
			{
				bodiesToRemove.add(bodyA);
				createParticle((BlockEntity)bodyA.getUserData());
			}
		}

		@Override
		public void endContact(Contact contact) {

		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {

		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {

		}
	};


	private void createParticle(BlockEntity entity)
	{
		// Lets test something




		// We only care about the x value, in theory.
		float x = entity.getPositionX();

		// Translate from world to camera coordinates.
		x *= Constants.PIXELS_PR_METER;
		x += Gdx.graphics.getWidth()/2f;

		Particle particle = new Particle(entity.getSprite(),  x, 0 );

		particles.add(particle);

		Vector2 goal = inventoryImage.localToStageCoordinates( new Vector2(inventoryImage.getX(), inventoryImage.getY()));
		goal.x += inventoryImage.getWidth()/2f;
		goal.y += inventoryImage.getHeight()/2f;
		particle.setGoalPosition(goal.x, goal.y);
	}
	private GameOptions gameOptions;

	public void newGame(GameOptions gameOptions)
	{
		this.gameOptions = gameOptions;

		reset();

		setGroundZone(gameOptions);

		setBackground(gameOptions.background);

		this.players.addAll(gameOptions.players);

		distributeBlocks(gameOptions);

		this.players.shuffle();
		currentPlayer = players.get(nPlayer);

		setPlayerTurn(currentPlayer);

	}


	private void changePlayerLabel(final Player player)
	{
		final float posX = playerTurnLabel.getX();
		final float posY = playerTurnLabel.getY();

		// Move out action.


		Action hideAction = Actions.fadeOut(0.5f);


		//Action hideAction = Actions.moveBy(0, 100, 0.5f);
		Runnable changeName = new Runnable() {
			@Override
			public void run()
			{
				playerTurnLabel.setText(player.getName() + "'s turn");
				bricksLeftLabel.setText(player.getBricksLeft() + "");
			}
		};
		Action renameAction = Actions.run(changeName);



//		Action showAction =  Actions.moveBy(0, 0, 0.5f);
		Action showAction = Actions.fadeIn(0.5f);


		//Action sequence = renameAction;
		Action sequence = Actions.sequence(hideAction, Actions.delay(0.1f), renameAction, Actions.delay(0.1f),showAction);

		Action after = Actions.after(sequence);
		after.setActor(playerTurnLabel);
		playerTurnLabel.addAction(after);
		//playerTurnLabel.addAction(Actions.after((sequence)));
		//playerTurnLabel.getActions()

	}
	private void setPlayerTurn(Player player)
	{
		changePlayerLabel(player);
		//playerTurnLabel.setText(player.getName() + "'s turn");
		setInventoryBlocks(currentPlayer);
	}

	private Player currentPlayer;
	private int nPlayer = 0;
	private Array<Player> players = new Array<Player>();

	/**
	 * Clears the world of all bodies, except the garbage zone.
	 * This includes the 'ground'.
	 */
	private void clearWorld()
	{
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		for(Body body : bodies)
		{
			if(body == garbageZone)
				continue;

			world.destroyBody(body);
		}
	}

	private void setGroundZone(GameOptions gameOptions)
	{
		// TODO: Read from game options.
		groundBody = BodyBuilder.createBlock(world, true, true, BlockType.GroundRect, MaterialType.Metal, -1, 1 );
		groundBody.setTransform(0, 2, 0);
		groundEntity = new Entity();
		groundBody.setUserData(groundEntity);
		groundEntity.setBody(groundBody);

		TextureAtlas bricks = gameController.assetManager.get("bricks.atlas", TextureAtlas.class);
		groundEntity.setTexture(bricks.findRegion("groundRectMetalTrans"));
	}

	private void setBackground(Background background)
	{
		// TODO: Is this it ?
		this.background = background;
	}

	public void reset()
	{

		clearWorld();
		this.players.clear();
		this.currentPlayer = null;
		this.nPlayer = 0;
		inventory.clear();
		state = GameState.PlayerTurn;
	}

	private void changeState(GameState newState)
	{
		this.state.onLeave(this);
		this.state = newState;
		newState.onEnter(this);
	}

	private void distributeBlocks(GameOptions gameOptions)
	{
		int nBricks = gameOptions.startBricks;
		// Make it an enum, if more than two are needed.
		if(gameOptions.mirrorBricks)
		{
			for(int i = 0; i < nBricks; i++)
			{
				Block original = generateBlock(gameOptions);
				// Give the block to all players.
				for(Player player : players)
				{
					Block block = new Block(original);
					player.addBlock(block);
				}
			}


		}else
		{
			for(Player player : players)
			{
				for(int i = 0; i < nBricks; i++)
				{
					Block block = generateBlock(gameOptions);
					player.addBlock(block);
				}
			}

		}
	}

	private Random rand = new Random();
	private Block generateBlock(GameOptions gameOptions)
	{
		// TODO: Use the gameOptions distribution.

		// Only two different blocks at the moment
		// TODO: Add the rest of the block types

		BlockType blockType;
		int n = rand.nextInt(3);
		switch (n)
		{
			case 0:
				blockType = BlockType.Triangle;
				break;

			case 1:
				blockType = BlockType.SmallSquare;
				break;

			case 2:
				blockType = BlockType.Rectangle;
				break;

			case 3:
			default :
				blockType = BlockType.MediumSquare;


				break;
		}
		//BlockType
		MaterialType matType = MaterialType.values()[rand.nextInt(MaterialType.values().length)];

		return new Block(blockType, matType);
	}

	// This is a test function. It might be better to just create an inventory for each player.
	// Either by adding it to the player class or using a Map<Player, HorizontalGroup>
	private void setInventoryBlocks(Player player)
	{
		inventory.clear();

		for(Block block : player.getBlocks())
		{
			BlockActor blockActor = new BlockActor(skin, block);

			initDragAndDrop(blockActor, inventory);
			inventory.addActor(blockActor);
		}

	}

	private final Array<Body> allBodies = new Array<Body>();
	private static final float MIN_VELOCITY_SQR = 0.1f * 0.1f;
	private final Vector2 linearVelocity = new Vector2();
	private float angularVelocity;

	/**
	 * Checks if any of the bodies in the world are moving.
	 *
	 * @param world - The world to be checked
	 * @return - True if no moving bodies exists, else false.
	 */
	private boolean checkForMovingBlocks(World world)
	{
		allBodies.clear();
		world.getBodies(allBodies);

		for(Body body : allBodies)
		{
			if(body.isAwake())
			{
				linearVelocity.set(body.getLinearVelocity());
				angularVelocity = body.getAngularVelocity();

				if(linearVelocity.isZero(0.0005f) && angularVelocity == 0.0f && waitedTime > 2.0f)
				{
					body.setAwake(false);
				}else
				{
					return false;
				}

				return false;
			}

		}
		return true;
	}




	private float maxVelocity(World world)
	{
		float max = 0.0f;
		allBodies.clear();
		world.getBodies(allBodies);
		for(Body body : allBodies)
		{
			if(body.getLinearVelocity().len2() > max)
				max = body.getLinearVelocity().len2();
		}

		return max;
	}



	public void resetWaitTime()
	{
		waitTime = gameOptions.stabilityDelay;
		waitedTime = 0.0f;
	}


	private static final float PARTICLE_SCALE = 0.25f;
	private Array<Particle> particles = new Array<Particle>();

	// TODO: Move to own class and use Pooling
	private class Particle{
		final static float PARTICLE_VELOCITY = 250;
		Sprite sprite; // Maybe just use a texture instead ?

		Vector2 goalPosition = new Vector2();
		//Vector2 velocity = new Vector2();
		Vector2 position = new Vector2();

		private final Vector2 v2 = new Vector2(); // This should be a static reference
		private boolean done = false;
		public Particle(Sprite sprite, float x, float y)
		{


			position.set(x,y);
			this.sprite = new Sprite(sprite);
			this.sprite.setOrigin(sprite.getOriginX(), sprite.getOriginY());
			this.sprite.setScale(PARTICLE_SCALE);
		}

		public void update(float delta)
		{
			if(done)
				return;

			sprite.rotate(10f); // maybe this should speed up ??

			v2.set(goalPosition);
			v2.sub(position);

			v2.nor();

			v2.scl(PARTICLE_VELOCITY * delta);
			position.add(v2);
			//position.add(velocity);
			Vector2 temp = new Vector2(goalPosition);
			temp.sub(position);


			v2.set(goalPosition).sub(position);
			if(v2.len2() > (temp.len2()))
			{
				done = true;
				System.out.println("Inside bounds !");
			}
			if(v2.isZero( 15f) || v2.y > Gdx.graphics.getHeight())
			{
				System.out.println("Removing the particle: " + v2.len());

				done = true;
			}
			System.out.println("Goal : " + goalPosition);
			System.out.print("Pos : " + position);
			v2.set(goalPosition);
			v2.sub(position);
			System.out.println("Diff :" + v2);

		}

		public void render(SpriteBatch batch)
		{
			sprite.setCenter(position.x, position.y);
			sprite.draw(batch);

		}

		public boolean isDone()
		{
			return done;
		}

		public void setGoalPosition(float x, float y)
		{
			// Make goal position static !
			goalPosition.set(x,y);
			/*
			v2.set(goalPosition);

			v2.sub(position);

			v2.set(PARTICLE_VELOCITY, 0f);

			if(position.x < Gdx.graphics.getWidth()/2f) // x is below center screen
			{

				v2.setAngle(rand.nextInt(10) - 5 + 45);
				// Change velocity ?
			}else
			{
				v2.setAngle(135f + rand.nextInt(10 ) - 5);
			}

			velocity.set(v2);
			*/

		}
	}



	private void updateBricksLeftLabel(Player currentPlayer)
	{
		updateBricksLeftLabel(currentPlayer.blocksLeft());
	}

	private void updateBricksLeftLabel(int amount)
	{
		bricksLeftLabel.setText(amount + "");
	}

	private void animateBricksLeftLabel()
	{
		inventoryImage.addAction(
			Actions.sequence(
					Actions.moveBy(-2 , 0, 0.05f),
					Actions.moveBy(4, 0, 0.05f),
					Actions.moveBy(-4, 0, 0.05f),
					Actions.moveBy(4, 0, 0.05f),
					Actions.moveBy(-4, 0, 0.05f),
					Actions.moveBy(4, 0, 0.05f),
					Actions.moveBy(-2, 0, 0.05f)

					)
		);
	}

	private void changeBricksLabel(int diff)
	{
		int old = Integer.parseInt(bricksLeftLabel.getText().toString());
		updateBricksLeftLabel(old + diff);
	}

	private void setActiveBody(Body body)
	{
		activeBody = body;
		cameraMoved = false;
	}


	private Vector2 unproject(Vector2 target, float screenX, float screenY)
	{
		v3.set(screenX, screenY, 0);
		cam.unproject(v3);
		target.set(v3.x, v3.y);
		target.scl(Constants.METERS_PR_PIXEL);
		return target;
	}

	private Vector2 unproject(float screenX, float screenY)
	{
		return unproject(v2, screenX, screenY);

		// Scale to world coordinates
	}

	private Vector2 unproject(Vector2 target)
	{
		return unproject(target, Gdx.input.getX(), Gdx.input.getY());
	}
	private Vector2 unproject()
	{
		return unproject(Gdx.input.getX(), Gdx.input.getY());
	}
}
