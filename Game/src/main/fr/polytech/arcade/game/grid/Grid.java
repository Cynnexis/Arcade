package main.fr.polytech.arcade.game.grid;

import fr.berger.enhancedlist.Point;
import fr.berger.enhancedlist.matrix.Matrix;
import main.fr.polytech.arcade.game.AbstractModel;
import main.fr.polytech.arcade.game.piece.Piece;
import main.fr.polytech.arcade.game.piece.Shape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

public class Grid extends AbstractModel implements Observer {
	
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
	
	public boolean checkIndexes(@NotNull Point indexes) {
		if (indexes == null)
			throw new NullPointerException();
		
		return (indexes.getX() >= 0 && indexes.getX() < getWidth()) &&
				(indexes.getY() >= 0 && indexes.getY() < getHeight());
	}
	public boolean checkIndexes(int x, int y) {
		return checkIndexes(new Point(x, y));
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
	
	public boolean checkIfPieceCanBePlaced(@NotNull Piece piece, @NotNull Point destination) {
		if (piece == null || destination == null)
			throw new NullPointerException();
		
		if (piece.getShape() == null)
			throw new NullPointerException();
		
		for (int i = destination.getX(); i < destination.getX() + piece.getShape().getNbColumns(); i++) {
			for (int j = destination.getY(); j < destination.getY() + piece.getShape().getNbRows(); j++) {
				
				Piece currentPiece = get(i, j);
				if (currentPiece != null && !Objects.equals(currentPiece, piece)) {
					Shape sh = currentPiece.getShape();
					Point pos = currentPiece.getPosition();
					
					int x = i - pos.getX();
					int y = j - pos.getY();
					
					if (sh != null &&
							pos != null &&
							sh.get(x, y) &&
							piece.getShape().get(i - destination.getX(), j - destination.getY()))
						return false;
				}
			}
		}
		
		return true;
	}
	public boolean checkIfPieceCanBePlaced(@NotNull Piece piece) {
		if (piece == null)
			throw new NullPointerException();
		
		return checkIfPieceCanBePlaced(piece, piece.getPosition());
	}
	
	private boolean move(@NotNull Piece piece) {
		if (piece == null)
			throw new NullPointerException();
		
		if (piece.getPosition() == null || piece.getShape() == null || piece.getCentre() == null || piece.getColor() == null)
			throw new NullPointerException();
		
		// Check if the piece is already in the list
		if (getIdFromPiece(piece) == -1)
			return false;
		
		// Check if the position of the piece is in the grid
		if (!checkIndexes(piece.getPosition()))
			throw new ArrayIndexOutOfBoundsException();
		
		// Check if the dimension of the piece (position + shape) is not out of the grid
		if (!checkIndexes(piece.getDimension()))
			throw new ArrayIndexOutOfBoundsException();
		
		// Check if there is another piece already in the grid at the position of 'piece'
		if (!checkIfPieceCanBePlaced(piece))
			return false;
		
		// The piece can go there
		return true;
	}
	public boolean move(@NotNull Piece piece, @NotNull Point destination) {
		if (piece == null || destination == null)
			throw new NullPointerException();
		
		// Check if the piece is already in the list
		if (getIdFromPiece(piece) == -1)
			return false;
		
		Point oldPos = piece.getPosition();
		piece.setPosition(destination);
		boolean result = move(piece);
		
		// If the piece cannot be moved, reset the piece position
		if (!result)
			piece.setPosition(oldPos);
		
		if (result)
			update();
		
		return result;
	}
	
	/**
	 * Get the piece at the coordinate <c>point</c>. If there is more than one piece at this coordinates (for example,
	 * there is one piece which is not exactly on this tile, but its dimension uses it, and a second uses entirely),
	 * then the piece which uses the most the tile at <c>point</c> will be returned.
	 * @param point The coordinates of the tile
	 * @return Return the piece at <c>point</c>. If there is no tile, return <c>null</c>.
	 */
	public @Nullable Piece get(@NotNull Point point) {
		if (point == null)
			throw new NullPointerException();
		
		if (!checkIndexes(point))
			throw new ArrayIndexOutOfBoundsException();
		
		Piece value = null;
		boolean notPlaced = false;
		
		for (int i = 0; i < getPieces().size() && (value == null || notPlaced); i++) {
			Piece currentPiece = getPieces().get(i);
			
			if (currentPiece != null) {
				Point pos = currentPiece.getPosition();
				Shape sh = currentPiece.getShape();
				
				if (pos != null && sh != null) {
					int minX = pos.getX();
					int minY = pos.getY();
					
					int maxX = minX + sh.getNbColumns() - 1;
					int maxY = minY + sh.getNbRows() - 1;
					
					if (minX <= point.getX() && point.getX() <= maxX &&
							minY <= point.getY() && point.getY() <= maxY) {
						value = currentPiece;
						try {
							notPlaced = !sh.get(Math.abs(minX - point.getX()), Math.abs(minY - point.getY()));
						} catch (IndexOutOfBoundsException ignored) {
							notPlaced = false;
						}
					}
				}
			}
		}
		
		return value;
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
		addPiece(piece);
		
		update();
		
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
	public boolean add(@NotNull Piece piece, int x, int y) {
		return add(piece, new Point(x, y));
	}
	
	/* GETTER & SETTER */
	
	public @NotNull ArrayList<Piece> getPieces() {
		if (pieces == null)
			pieces = new ArrayList<>();
		
		return pieces;
	}
	
	protected void setPieces(@NotNull ArrayList<Piece> pieces) {
		if (pieces == null)
			throw new NullPointerException();
		
		this.pieces = pieces;
		
		update();
	}
	
	protected void addPiece(@NotNull Piece piece) {
		if (piece == null)
			throw new NullPointerException();
		
		piece.addObserver(this);
		getPieces().add(piece);
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
		
		update();
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
	
	// If one piece notify the Grid, then Grid notify its controller which will notify its view (#Notifyception)
	@Override
	public void update(Observable observable, Object o) {
		if (observable != null && observable instanceof Piece)
			update();
	}
}
