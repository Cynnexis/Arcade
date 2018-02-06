package main.fr.polytech.arcade.game.grid;

/**
 * Give the type of the square in the grid.
 * @see Square
 * @see Grid
 * @see main.fr.polytech.arcade.game.piece.Piece
 */
public enum SquareType {
	/**
	 * Indicate that a piece is in this square
	 */
	FULL,
	
	/**
	 * Indicate that a piece is in this piece, even if the square is not directly linked to the piece
	 */
	HALF,
	
	/**
	 * Indicate that the square is empty
	 */
	EMPTY
}
