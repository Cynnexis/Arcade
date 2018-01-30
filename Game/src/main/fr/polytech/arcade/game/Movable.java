package main.fr.polytech.arcade.game;

import fr.berger.enhancedlist.Point;

public interface Movable {
	
	boolean move(Grid grid, Point destination);
}
