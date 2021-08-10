package com.egocentric.towerbuilder;

import com.egocentric.towerbuilder.screens.GameScreen;

/**
 * Created by Peter on 15/02/18.
 */
public enum GameState {

	PlayerTurn {

		@Override
		public void onEnter(GameScreen gameScreen)
		{

			gameScreen.enableInventory();
			//gameScreen.showInventory();


			//waitTime = gameOptions.stabilityDelay;
			//waitedTime = 0.0f;
			//state = GameState.PlayerTurn;

		}

		@Override
		public void onLeave(GameScreen gameScreen)
		{
			gameScreen.hideInventory();
			gameScreen.disableInventory();
		}
	},


	WaitingForBricks {

		@Override
		public void onEnter(GameScreen gameScreen)
		{
			gameScreen.hideInventory();
			gameScreen.disableInventory();
			// The two are not needed, but just in case.

			gameScreen.resetWaitTime();
			//waitTime = gameOptions.stabilityDelay;


			// Set the values


		}

		@Override
		public void onLeave(GameScreen gameScreen)
		{

		}
	},
	EndOfGame {

		@Override
		public void onEnter(GameScreen gameScreen)
		{

		}

		@Override
		public void onLeave(GameScreen gameScreen)
		{

		}
	};





	public abstract void onEnter(GameScreen gameScreen);


	public abstract void onLeave(GameScreen gameScreen);

}
