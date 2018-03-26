package main.fr.polytech.arcade.game;

import org.jetbrains.annotations.NotNull;

/**
 * Enumerate all possible state of a game.
 * @author Valentin Berger
 */
public enum GameState {
	/**
	 * Indicate that the program is initializing or loading content
	 */
	INITIALIZING,
	
	/**
	 * Indicate that the game is in play mode
	 */
	PLAYING,
	
	/**
	 * The game is paused
	 */
	PAUSE,
	
	/**
	 * The game is stopped
	 */
	STOP,
	
	/**
	 * The game is stopped and the player won
	 */
	WIN,
	
	/**
	 * The game is stopped and the player loose
	 */
	GAMEOVER,
	
	/**
	 * The game is stopped and the player neither won nor loose.
	 */
	DRAW;
	
	/**
	 * If <c>state</c> is PLAYING, PAUSE or STOP, return true, otherwise false
	 * @param state The gamestate to evaluate
	 * @return If <c>state</c> is PLAYING, PAUSE or STOP, return true, otherwise false
	 */
	public static boolean isGameContinuing(@NotNull GameState state) {
		if (state == null)
			throw new NullPointerException();
		
		return state == PLAYING || state == PAUSE || state == STOP;
	}
}
