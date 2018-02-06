package main.fr.polytech.arcade.game.grid;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import fr.berger.enhancedlist.Point;
import fr.berger.enhancedlist.matrix.Matrix;
import main.fr.polytech.arcade.game.piece.Piece;

import java.util.ArrayList;

public class Grid extends Matrix<Square> {
	
	@NotNull
	private ArrayList<Piece> pieces;
	
	public Grid() {
		super(10, 10, new Square());
		pieces = new ArrayList<>(0);
	}
	public Grid(int dimension) {
		super(dimension, dimension, new Square());
		pieces = new ArrayList<>(0);
	}
	public Grid(int nbColumns, int nbRows) {
		super(nbColumns, nbRows, new Square());
		pieces = new ArrayList<>(0);
	}
	
	public @Nullable Piece getPiece(int i, int j) {
		if (!(0 <= i && i < getNbColumns() && 0 <= j && j < getNbRows()))
			throw new IndexOutOfBoundsException();
		
		Square s = get(i, j);
		
		if (s == null)
			return null;
		
		return pieces.get(s.getIndexToPiece());
	}
	
	public synchronized boolean addPiece(@NotNull Piece piece, @NotNull Point dest) {
		if (piece == null)
			throw new NullPointerException();
		
		// TODO: Check 'dest' values, and test if piece.getPosition() + dest is out of bounds
		
		// Check if there is no other piece in the destination
		for (int i = 0; i < piece.getShape().getNbColumns(); i++) {
			for (int j = 0; j < piece.getShape().getNbRows(); j++) {
				if (piece.getShape().get(i, j)) {
					// TODO: Check it doesn't overflow on another piece in the grid
					Square s = get(i + dest.getX(), j + dest.getY());
					// If a square which is not empty is found, return false
					if (s.getType() != SquareType.EMPTY)
						return false;
				}
			}
		}
		
		// Add the piece to the grid
		getPieces().add(piece);
		
		// Update matrix:
		for (int i = dest.getX(); i < piece.getShape().getNbColumns(); i++) {
			for (int j = dest.getY(); j < piece.getShape().getNbRows(); j++) {
				get(i, j).setIndexToPiece(getPieces().size()-1);
				get(i, j).setType(SquareType.HALF);
			}
		}
		
		get(dest.getX(), dest.getY()).setType(SquareType.FULL);
		return true;
	}
	public synchronized boolean addPiece(@NotNull Piece piece, int x, int y) {
		return addPiece(piece, new Point(x, y));
	}
	
	public void reset() {
		for (int i = 0; i < getNbColumns(); i++)
			for (int j = 0; j < getNbRows(); j++)
				set(i, j, new Square(i, j));
	}
	
	/* GETTER & SETTER */
	
	public ArrayList<Piece> getPieces() {
		return pieces;
	}
	
	public void setPieces(ArrayList<Piece> pieces) {
		this.pieces = pieces;
	}
}
