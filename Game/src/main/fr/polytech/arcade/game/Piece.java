package main.fr.polytech.arcade.game;

import fr.berger.enhancedlist.Point;

public class Piece extends RotableObject implements Movable {
	
	private boolean isPlaced;
	private Shape shape;
	
	/* GETTERS & SETTERS */
	
	/* OVERRIDES */
	
	@Override
	public boolean rotate(Grid grid, Direction direction) {
		return false;
	}
	
	@Override
	public boolean move(Grid grid, Point destination) {
		return false;
	}
}
