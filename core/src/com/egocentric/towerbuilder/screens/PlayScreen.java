package com.egocentric.towerbuilder.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.egocentric.towerbuilder.GameController;
import com.egocentric.towerbuilder.entities.BlockEntity;
import com.egocentric.towerbuilder.entities.BlockParticle;
import com.egocentric.towerbuilder.entities.Entity;
import com.egocentric.towerbuilder.utils.Constants;
import com.egocentric.towerbuilder.utils.b2d.BodyBuilder;

/**
 * Created by Peter on 04/07/2018.
 */
public class PlayScreen extends AbstractScreen {


	private OrthographicCamera cam;

	private final World world;

	// The gound entity and body
	private Body groundBody;
	private Entity groundEntity;

	private Body activeBody;

	// Maybe instead just check if any of the moving blocks are below a Y-value
	private Body garbageZone;
	private final Array<Body> bodiesToRemove = new Array<Body>();
	private final Array<BlockParticle> blockParticles = new Array<BlockParticle>();

	private static final Vector2 v2 = new Vector2();
	private static final Vector3 v3 = new Vector3();


	/**
	 *  Gui variables
	 * */
	private Skin skin;
	private Table mainTable;
	private Table dropZone;
	private Label playerTurnLabel;
	private Label bricksLeftLabel;

	private Drawable closedInventoryDrawable;
	private Drawable openInventoryDrawable;

	private HorizontalGroup inventory;
	private ScrollPane scrollPane;

	private Image inventoryImage;





	public PlayScreen(GameController gameController)
	{
		super(gameController);

		// Init the camera.
		cam = new OrthographicCamera();
		cam.setToOrtho(false, 480, 800);


		// Init the Box2d World
		world = new World(new Vector2(0, -9.92f), true);
		world.setContactListener(contactListener);

		garbageZone = BodyBuilder.createRectangle(world, true, true, true, 20, 2, 0, -2);

	}

	private void initGui(Skin skin)
	{
		int nColumns = 1;

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
		bricksLeftLabel = new Label("99", skin, "playerBricksLeft");
		bricksLeftLabel.setFontScale(0.85f);

		bricksLeftTable.add(bricksLeftLabel).center();

		inventory = new HorizontalGroup();
		inventory.center().bottom().padBottom(10f);

		scrollPane = new ScrollPane(inventory, skin);
		scrollPane.setScrollingDisabled(false, true);
		scrollPane.setFadeScrollBars(true);


		mainTable.add(scrollPane).expandX().top().fillX().pad(10).height(140);
		mainTable.row();

		Table topTable = new Table(skin);
		topTable.setBackground("BluePanel");

		topTable.add(inventoryImage).left().width(44).height(48);
		topTable.add(bricksLeftTable).left().padLeft(-18).bottom().width(24).height(24);
		topTable.add(playerTurnLabel).expandX().left().padLeft(10);

		dropZone = new Table(skin);
		mainTable.add(dropZone).center().top().expandX().expandY().fillX().fillY().colspan(nColumns);
		mainTable.row();

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





	}

	@Override
	public void initAssets(AssetManager assetManager)
	{


	}

	@Override
	public void loadAssets(AssetManager assetManager)
	{
		assetManager.load("bricks.atlas", TextureAtlas.class);
	}

	@Override
	protected void draw(float delta)
	{

	}

	@Override
	protected void update(float delta)
	{

	}


	/**
	 * The custom contact manager. Handles contact between the garbage zone.
	 */
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
		float x = entity.getPositionX();

		// Translate from world to camera coordinates.
		x *= Constants.PIXELS_PR_METER;
		x += Gdx.graphics.getWidth()/2f;

		BlockParticle particle = new BlockParticle(entity.getSprite(),  x, -10 );
		blockParticles.add(particle);
		//System.out.println("Pos : " + inventoryImage.getX() + ", " + inventoryImage.getY());

		Vector2 goal = inventoryImage.localToStageCoordinates( new Vector2(inventoryImage.getX(), inventoryImage.getY()));
		goal.x += inventoryImage.getWidth()/2f;
		goal.y += inventoryImage.getHeight()/2f;
		particle.setGoalPosition(goal.x, goal.y);



	}

	/**
	 *  Gui Methods
	 */
	private void toggleInventory()
	{
		// TODO: Implement !
	}
}
