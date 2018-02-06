package main.fr.polytech.arcade.game.piece;

import fr.berger.enhancedlist.Point;
import main.fr.polytech.arcade.game.grid.Grid;

public interface Movable {
	
	boolean move(Grid grid, Point destination);
}
