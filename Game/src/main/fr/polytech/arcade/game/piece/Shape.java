package main.fr.polytech.arcade.game.piece;

import fr.berger.enhancedlist.Point;
import fr.berger.enhancedlist.matrix.Matrix;

/**
 * Class which describes a shape for a Piece.
 * @author Valentin Berger
 * @see Matrix
 * @see Piece
 * @see PieceBuilder
 */
public class Shape extends Matrix<Boolean> {
	
	public Shape() {
		super(1, 1, false);
	}
	public Shape(int dimension) {
		super(dimension, dimension, false);
	}
	public Shape(Point dimension) {
		super(dimension.getX(), dimension.getY(), false);
	}
	public Shape(int x, int y) {
		super(x, y, false);
	}
	
	/**
	 * Trim the matrix according to the biggest x and y '1' values.
	 */
	public void trim() {
		int newX = 0;
		int newY = 0;
		
		for (int i = 0; i < getNbColumns(); i++) {
			for (int j = 0; j < getNbColumns(); j++) {
				if (get(i, j)) {
					newX = i;
					newY = j;
				}
			}
		}
		
		if (newX + 1 != getNbColumns())
			setNbColumns(newX + 1);
		
		if (newY + 1 != getNbRows())
			setNbRows(newY + 1);
	}
}
