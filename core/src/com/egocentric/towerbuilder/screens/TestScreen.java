package com.egocentric.towerbuilder.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.egocentric.towerbuilder.GameController;
import com.egocentric.towerbuilder.utils.Constants;
import com.egocentric.towerbuilder.utils.b2d.BodyBuilder;

import static com.egocentric.towerbuilder.utils.Constants.*;

/**
 * Created by Peter on 29/06/18.
 */
public class TestScreen extends AbstractScreen implements InputProcessor{


	World world;

	Box2DDebugRenderer box2DDebugRenderer;
	Matrix4 debugMatrix;

	Body activeBody;
	Body groundBody;
	MouseJoint mouseJoint;
	MouseJointDef mouseJointDef;
	public TestScreen(GameController gameController)
	{
		super(gameController);
		world = new World(new Vector2(0, -9.92f), true);
		box2DDebugRenderer = new Box2DDebugRenderer();
		debugMatrix = new Matrix4();


		//BodyBuilder.createRectangle(world, true, true, false, 20, 2, 0, -2);
		groundBody = BodyBuilder.createRectangle(world, true, true, false, 3, 1, 0, 1);
		BodyBuilder.createRectangle(world, true, true, false, 0.5f, 3f, -1.75f, 3);
		BodyBuilder.createRectangle(world, true, true, false, 0.5f, 3f,  1.75f, 3);
		activeBody = BodyBuilder.createRectangle(world, false, false, false, 0.5f, 0.5f, 0, 2);

		//activeBody.setGravityScale(0.0f);
		// Create Mouse Joint
		/*
		mouseJointDef = new MouseJointDef();
		mouseJointDef.bodyA = groundBody;
		mouseJointDef.bodyB = activeBody;
		mouseJointDef.maxForce = 10; // Use the mass of the object ?!
		mouseJointDef.collideConnected = true;
		mouseJoint = (MouseJoint) world.createJoint(mouseJointDef);
		*/
	}

	@Override
	public void show()
	{
		Gdx.input.setInputProcessor(this);
	}
	@Override
	public void initAssets(AssetManager assetManager)
	{

	}

	@Override
	public void loadAssets(AssetManager assetManager)
	{

	}



	@Override
	protected void draw(float delta)
	{
		gameController.cam.update();

		debugMatrix.set(gameController.cam.combined);
		debugMatrix.scl(Constants.PIXELS_PR_METER);

		box2DDebugRenderer.render(world, debugMatrix);

	}

	float accumulation = 0.0f;

	@Override
	protected void update(float delta)
	{
		if(Gdx.input.isTouched())
		{

			if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT))
			{
				Vector2 pos = unproject(gameController.cam, Gdx.input.getX(), Gdx.input.getY());
				BodyBuilder.createRectangle(world,  false, false, false, 0.5f, 0.5f, pos.x, pos.y);

			}else
			{
				Vector2 pos = unproject(gameController.cam, Gdx.input.getX(), Gdx.input.getY());
				//pos.nor();
				pos = pos.sub(activeBody.getPosition());
				//System.out.println("POS : " + pos);
				//pos.nor();
				pos.scl(1f/TIME_STEP);
				pos.scl(1f/activeBody.getMass());
				activeBody.setLinearVelocity(0,0);
				activeBody.applyForceToCenter(pos, true);
			}


		}

		accumulation += delta;
		while (accumulation >= TIME_STEP)
		{
			world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);

			accumulation -= TIME_STEP;
		}



	}


	static Vector2 vec2 = new Vector2();

	static Vector3 vec3 = new Vector3();
	Vector2 unproject(OrthographicCamera cam, float screenX, float screenY)
	{
		vec3.set(screenX, screenY, 0);
		cam.unproject(vec3);
		vec2.set(vec3.x, vec3.y);
		vec2 = vec2.scl(Constants.METERS_PR_PIXEL);
		return vec2;
	}


	@Override
	public boolean keyDown(int keycode)
	{
		return false;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		return false;
	}

	@Override
	public boolean keyTyped(char character)
	{
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		if(mouseJoint != null)
		{
			Vector3 vec = new Vector3(screenX, screenY, 0);
			gameController.cam.unproject(vec);
			Vector2 target = new Vector2(vec.x, vec.y);
			target.scl(Constants.METERS_PR_PIXEL);
			mouseJoint.setTarget(target);

		}

		return true;

	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		return false;
	}

	@Override
	public boolean scrolled(int amount)
	{
		return false;
	}
}
