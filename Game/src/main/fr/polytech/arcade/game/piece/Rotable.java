package main.fr.polytech.arcade.game.piece;

import main.fr.polytech.arcade.game.grid.Grid;

public interface Rotable {
	
	boolean rotate(Grid grid, Direction direction);
}
