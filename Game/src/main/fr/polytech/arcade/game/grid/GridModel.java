package main.fr.polytech.arcade.game.grid;

import com.sun.istack.internal.Nullable;

import java.util.Observable;

@Deprecated
public class GridModel extends Observable {
	
	@Nullable
	private Grid grid;
	
	public GridModel(@Nullable Grid grid) {
		setGrid(grid);
	}
	public GridModel() {
		this(null);
	}
	
	public void setGrid(@Nullable Grid grid) {
		this.grid = grid;
		setChanged();
		notifyObservers();
	}
	
	public @Nullable Grid getGrid() {
		return grid;
	}
}
