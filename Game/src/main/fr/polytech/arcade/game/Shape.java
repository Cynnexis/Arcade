package main.fr.polytech.arcade.game;

import fr.berger.enhancedlist.Matrix;

public class Shape extends Matrix<Boolean> {
	
	public Shape() {
		super(1, 1, false);
	}
	public Shape(int dimension) {
		super(dimension, dimension, false);
	}
	public Shape(int x, int y) {
		super(x, y, false);
	}
}
