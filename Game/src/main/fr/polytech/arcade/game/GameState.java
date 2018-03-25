package main.fr.polytech.arcade.game;

import org.jetbrains.annotations.NotNull;

public enum GameState {
	INITIALIZING,
	PLAYING,
	PAUSE,
	STOP,
	WIN,
	GAMEOVER,
	DRAW;
	
	public static boolean isGameContinuing(@NotNull GameState state) {
		if (state == null)
			throw new NullPointerException();
		
		return state == PLAYING || state == PAUSE || state == STOP;
	}
}
