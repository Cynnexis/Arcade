package main.fr.polytech.arcade.game.grid;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import fr.berger.enhancedlist.Point;
import fr.berger.enhancedlist.matrix.Matrix;
import main.fr.polytech.arcade.game.piece.Piece;

import java.util.ArrayList;
import java.util.Objects;

public class Grid extends Matrix<Square> {
	
	@NotNull
	private ArrayList<Piece> pieces;
	
	public Grid() {
		super(10, 10, new Square());
		pieces = new ArrayList<>(0);
		init();
	}
	public Grid(int dimension) {
		super(dimension, dimension, new Square());
		pieces = new ArrayList<>(0);
		init();
	}
	public Grid(int nbColumns, int nbRows) {
		super(nbColumns, nbRows, new Square());
		pieces = new ArrayList<>(0);
		init();
	}
	
	private void init() {
		for (int i = 0; i < getNbColumns(); i++) {
			for (int j = 0; j < getNbRows(); j++) {
				set(i, j, new Square(i, j));
			}
		}
	}
	
	public @Nullable Piece getPiece(int i, int j) {
		if (!(0 <= i && i < getNbColumns() && 0 <= j && j < getNbRows()))
			throw new IndexOutOfBoundsException();
		
		Square s = get(i, j);
		
		if (s == null)
			return null;
		
		if (s.getType() == SquareType.EMPTY)
			return null;
		
		return pieces.get(s.getIndexToPiece());
	}
	
	public synchronized boolean addPiece(@NotNull Piece piece, @NotNull Point dest) {
		if (piece == null)
			throw new NullPointerException();
		
		if (!checkIfPieceCanBePlaced(piece, dest))
			return false;
		
		// TODO: Check 'dest' values, and test if piece.getPosition() + dest is out of bounds
		
		// Check if there is no other piece in the destination
		for (int i = 0; i < piece.getShape().getNbColumns(); i++) {
			for (int j = 0; j < piece.getShape().getNbRows(); j++) {
				if (piece.getShape().get(i, j)) {
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
		for (int i = dest.getX(), maxi = piece.getShape().getNbColumns() + dest.getX(); i < maxi; i++) {
			for (int j = dest.getY(), maxj = piece.getShape().getNbRows() + dest.getY(); j < maxj; j++) {
				if (piece.getShape().get(i - dest.getX(), j - dest.getY())) {
					get(i, j).setIndexToPiece(getPieces().size() - 1);
					get(i, j).setType(SquareType.HALF);
				}
			}
		}
		
		get(dest.getX(), dest.getY()).setType(SquareType.FULL);
		
		return true;
	}
	public synchronized boolean addPiece(@NotNull Piece piece, int x, int y) {
		return addPiece(piece, new Point(x, y));
	}
	
	public synchronized boolean move(@NotNull Piece piece, @NotNull Point dest) {
		if (piece == null || dest == null)
			throw new NullPointerException();
		
		if (!checkIfPieceCanBePlaced(piece, dest))
			return false;
		
		int index = getIdFromPiece(piece);
		
		// TODO: Check 'dest' values, and test if piece.getPosition() + dest is out of bounds
		
		// If not such piece exist in the list, then we cannot move a piece which is not in the grid
		if (index == -1)
			return false;
		
		// Search the first square which hold the piece
		Square s = null;
		
		for (int i = 0; i < getNbColumns() && s == null; i++)
			for (int j = 0; j < getNbRows() && s == null; j++)
				if (get(i, j) != null && get(i, j).getIndexToPiece() == index)
					s = get(i, j);
		
		// Now, free the squares
		for (int i = s.getPosition().getX(); i < piece.getShape().getNbColumns() + s.getPosition().getX(); i++) {
			for (int j = s.getPosition().getY(); j < piece.getShape().getNbRows() + s.getPosition().getY(); j++) {
				if (piece.getShape().get(i - s.getPosition().getX(), j - s.getPosition().getY())) {
					get(i, j).setType(SquareType.EMPTY);
					get(i, j).setIndexToPiece(-1);
				}
			}
		}
		
		// Finally, place the piece to the destination
		for (int i = dest.getX(); i < piece.getShape().getNbColumns() + dest.getX(); i++) {
			for (int j = dest.getY(); j < piece.getShape().getNbRows() + dest.getY(); j++) {
				if (piece.getShape().get(i - dest.getX(), j - dest.getY())) {
					get(i, j).setType(SquareType.HALF);
					get(i, j).setIndexToPiece(index);
				}
			}
		}
		
		get(dest.getX(), dest.getY()).setType(SquareType.HALF);
		
		return true;
	}
	
	public boolean checkIfPieceCanBePlaced(@NotNull Piece piece, @NotNull Point dest) {
		if (piece == null || dest == null)
			throw new NullPointerException();
		
		for (int i = dest.getX(); i < piece.getShape().getNbColumns() + dest.getX(); i++)
			for (int j = dest.getY(); j < piece.getShape().getNbRows() + dest.getY(); j++)
				if (piece.getShape().get(i - dest.getX(), j - dest.getY()) && get(i, j) != null && get(i, j).getType() != SquareType.EMPTY)
					return false;
		
		return true;
	}
	
	/**
	 * Search the piece in the list {@code pieces}. When it is found, return the index to this piece
	 * @param piece The instance of piece to search, different from {@code null}
	 * @return Return the index of {@code piece}, or {@code -1} if not found
	 */
	public int getIdFromPiece(@NotNull Piece piece) {
		if (piece == null)
			throw new NullPointerException();
		
		for (int i = 0; i < getPieces().size(); i++)
			if (Objects.equals(getPieces().get(i), piece))
				return i;
		
		return -1;
	}
	
	/* GETTER & SETTER */
	
	public ArrayList<Piece> getPieces() {
		return pieces;
	}
	
	public void setPieces(ArrayList<Piece> pieces) {
		this.pieces = pieces;
	}
	
	/* OVERRIDES */
	
	@Override
	public String toString() {
		return "Grid{" +
				"pieces=" + pieces +
				'}';
	}
}
