package main.fr.polytech.arcade.game.grid;

import fr.berger.enhancedlist.Point;
import fr.berger.enhancedlist.matrix.Matrix;
import main.fr.polytech.arcade.game.piece.Piece;
import main.fr.polytech.arcade.game.piece.Shape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Observable;

public class Grid extends Observable {
	
	@NotNull
	private ArrayList<Piece> pieces;
	
	@NotNull
	private Point dimension;
	
	public Grid(int nbColumns, int nbRows) {
		setDimension(nbColumns, nbRows);
		pieces = new ArrayList<>(10);
	}
	public Grid(int dimension) {
		this(dimension, dimension);
	}
	public Grid() {
		this(10, 10);
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
	
	public boolean checkIndexes(@NotNull Point indexes) {
		if (indexes == null)
			throw new NullPointerException();
		
		return (indexes.getX() >= 0 && indexes.getX() < getWidth()) &&
				(indexes.getY() >= 0 && indexes.getY() < getHeight());
	}
	public boolean checkIndexes(int x, int y) {
		return checkIndexes(new Point(x, y));
	}
	
	public boolean checkIfPieceCanBePlaced(@NotNull Piece piece, @NotNull Point destination) {
		if (piece == null || destination == null)
			throw new NullPointerException();
		
		if (piece.getShape() == null)
			throw new NullPointerException();
		
		for (int i = destination.getX(); i < destination.getX() + piece.getShape().getNbColumns(); i++)
			for (int j = destination.getY(); j < destination.getX() + piece.getShape().getNbRows(); j++)
				if (get(i, j) != null)
					return false;
		
		return true;
	}
	public boolean checkIfPieceCanBePlaced(@NotNull Piece piece) {
		if (piece == null)
			throw new NullPointerException();
		
		return checkIfPieceCanBePlaced(piece, piece.getPosition());
	}
	
	/* GETTER & SETTER */
	
	public @Nullable Piece get(@NotNull Point point) {
		if (point == null)
			throw new NullPointerException();
		
		if (!checkIndexes(point))
			throw new ArrayIndexOutOfBoundsException();
		
		for (int i = 0; i < pieces.size(); i++) {
			Piece currentPiece = pieces.get(i);
			
			if (currentPiece != null) {
				Point pos = currentPiece.getPosition();
				Shape sh = currentPiece.getShape();
				
				if (pos != null && sh != null) {
					int minX = pos.getX();
					int minY = pos.getY();
					
					int maxX = minX + sh.getNbColumns();
					int maxY = minY + sh.getNbRows();
					
					if (minX <= point.getX() && point.getX() <= maxX &&
						minY <= point.getY() && point.getY() <= maxY)
						return currentPiece;
				}
			}
		}
		
		return null;
	}
	public @Nullable Piece get(int x, int y) {
		return get(new Point(x, y));
	}
	
	public boolean add(@NotNull Piece piece) {
		// Check if the piece and its components are not null
		if (piece == null)
			throw new NullPointerException();
		
		if (piece.getPosition() == null || piece.getShape() == null || piece.getCentre() == null || piece.getColor() == null)
			throw new NullPointerException();
		
		// Check if the position of the piece is in the grid
		if (!checkIndexes(piece.getPosition()))
			throw new ArrayIndexOutOfBoundsException();
		
		// Check if the dimension of the piece (position + shape) is not out of the grid
		if (!checkIndexes(piece.getDimension()))
			throw new ArrayIndexOutOfBoundsException();
		
		// Check if there is another piece already in the grid at the position of 'piece'
		if (!checkIfPieceCanBePlaced(piece))
			return false;
		
		// Now, the piece can be placed
		getPieces().add(piece);
		
		return true;
	}
	public boolean add(@NotNull Piece piece, @NotNull Point destination) {
		if (piece == null)
			throw new NullPointerException();
		
		if (destination == null)
			throw new NullPointerException();
		
		piece.setPosition(destination);
		return add(piece);
	}
	
	public ArrayList<Piece> getPieces() {
		if (pieces == null)
			pieces = new ArrayList<>(0);
		
		return pieces;
	}
	
	public void setPieces(ArrayList<Piece> pieces) {
		this.pieces = pieces;
	}
	
	public @NotNull Point getDimension() {
		return dimension;
	}
	
	public int getWidth() {
		return getDimension().getX();
	}
	
	public int getHeight() {
		return getDimension().getY();
	}
	
	public void setDimension(@NotNull Point dimension) {
		if (dimension == null)
			throw new NullPointerException();
		
		if (dimension.getX() < 0 || dimension.getY() < 0)
			throw new IllegalArgumentException("Dimension of the grid must be greater or equal to 0.");
		
		this.dimension = dimension;
	}
	public Point setDimension(int width, int height) {
		setDimension(new Point(width, height));
		
		return getDimension();
	}
	
	public void setWidth(int width) {
		setDimension(new Point(width, getDimension().getY()));
	}
	
	public void setHeight(int height) {
		setDimension(new Point(getDimension().getX(), height));
	}
	
	/* OVERRIDES */
	
	@Override
	public String toString() {
		return "Grid{" +
				"pieces=" + pieces +
				'}';
	}
}
