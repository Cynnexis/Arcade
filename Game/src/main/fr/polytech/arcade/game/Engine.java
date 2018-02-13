package main.fr.polytech.arcade.game;

import com.sun.istack.internal.NotNull;
import main.fr.polytech.arcade.game.grid.Grid;

public abstract class Engine {
	
	@NotNull
	protected Grid grid;
	
	public Engine() {
		setGrid(new Grid());
	}
	
	public @NotNull Grid getGrid() {
		return grid;
	}
	
	public void setGrid(@NotNull Grid grid) {
		if (grid == null)
			throw new NullPointerException();
		
		this.grid = grid;
	}
}
