package main.fr.polytech.arcade.game.grid;

import fr.berger.enhancedlist.Point;
import main.fr.polytech.arcade.game.AbstractModel;
import main.fr.polytech.arcade.game.piece.Piece;
import main.fr.polytech.arcade.game.piece.Shape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

public class Grid extends AbstractModel implements Observer {
	
	/**
	 * The ArrayList which contains all the piece.
	 * Grid does not actually contains a matrix of tiles, but only a set of pieces with a position (x ; y) for each
	 * of them. This level of abstraction is managed through get(int, int) and set(int, int)
	 * @see Piece
	 */
	@NotNull
	private ArrayList<Piece> pieces;
	
	/**
	 * The index of <c>pieces</c> of the focused piece. If <c>currentPiece < 0</c>, then there is not active piece.
	 */
	private int currentPiece;
	
	/**
	 * The dimension of the Grid (width ; height)
	 */
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
				
				ArrayList<Piece> currentPieces;
				try {
					currentPieces = getAll(i, j);
				} catch (ArrayIndexOutOfBoundsException ignored) {
					return false;
				}
				for (Piece currentPiece : currentPieces) {
					if (currentPiece != null && !Objects.equals(currentPiece, piece)) {
						Shape sh = currentPiece.getShape();
						Point pos = currentPiece.getPosition();
						
						int x = i - pos.getX();
						int y = j - pos.getY();
						
						if (sh != null &&
								pos != null &&
								sh.get(x, y) &&
								currentPiece.isPlaced() &&
								piece.getShape().get(i - destination.getX(), j - destination.getY()))
							return false;
					}
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
	
	/**
	 * Indicate if the given piece can be in its current position
	 * @param piece the piece to check
	 * @return true if the piece can stay at its place, false otherwise
	 * @see Piece
	 */
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
			return false;
		
		// Check if the dimension of the piece (position + shape) is not out of the grid
		if (!checkIndexes(piece.getDimension()))
			return false;
		
		// Check if there is another piece already in the grid at the position of 'piece'
		if (!checkIfPieceCanBePlaced(piece))
			return false;
		
		// The piece can go there
		return true;
	}
	
	/**
	 * Move <c>piece</c> at the coordinates <c>destination</c> if it is possible (no overlap, and not out of bound)
	 * @param piece
	 * @param destination
	 * @return
	 */
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
			snap();
		
		return result;
	}
	public boolean move(@NotNull Piece piece, int x, int y) {
		return move(piece, new Point(x, y));
	}
	
	/**
	 * Move <c>piece</c> at the coordinates <c>destination</c> regardless of the grid
	 * @param piece
	 * @param destination
	 */
	public void forceMove(@NotNull Piece piece, @NotNull Point destination) {
		if (piece == null || destination == null)
			throw new NullPointerException();
		
		// Check if the piece is already in the list
		if (getIdFromPiece(piece) == -1)
			return;
		
		piece.setPosition(destination);
		snap();
	}
	public void forceMove(@NotNull Piece piece, int x, int y) {
		forceMove(piece, new Point(x, y));
	}
	
	public boolean rotate(@NotNull Piece piece, int degreeClockwise) {
		if (piece == null)
			throw new NullPointerException();
		
		// Check if the piece is already in the list
		int index = -1;
		if ((index = getIdFromPiece(piece)) == -1)
			return false;
		
		// Make the piece in the list as not placed
		getPieces().get(index).setPlaced(false);
		Piece copy = new Piece(piece);
		copy.rotate(degreeClockwise);
		copy.setPlaced(true);
		
		if (checkIfPieceCanBePlaced(copy)) {
			getPieces().set(index, copy);
			snap();
			return true;
		}
		else {
			getPieces().get(index).setPlaced(true);
			return false;
		}
	}
	public boolean rotate(int index, int degreeClockwise) {
		if (!(0 <= index && index < getPieces().size()))
			throw new ArrayIndexOutOfBoundsException();
		
		return rotate(getPieces().get(index), degreeClockwise);
	}
	
	public boolean deleteAt(@NotNull Point point) {
		if (point == null)
			throw new NullPointerException();
		
		if (!checkIndexes(point))
			return false;
		
		ArrayList<Piece> pieces = getAll(point);
		
		if (pieces.size() == 0)
			return false;
		
		boolean hasDeleteAtLeastOneTile = false;
		
		for (Piece piece : pieces) {
			int x = point.getX() - piece.getPosition().getX();
			int y = point.getY() - piece.getPosition().getY();
			
			if (piece.getShape().get(x, y))
				hasDeleteAtLeastOneTile = true;
			
			piece.getShape().set(x, y, false);
		}
		
		if (hasDeleteAtLeastOneTile)
			snap();
		
		return hasDeleteAtLeastOneTile;
	}
	public boolean deleteAt(int x, int y) {
		return deleteAt(new Point(x, y));
	}
	
	@NotNull
	public ArrayList<Piece> getAll(@NotNull Point point) {
		if (point == null)
			throw new NullPointerException();
		
		if (!checkIndexes(point))
			throw new ArrayIndexOutOfBoundsException();
		
		ArrayList<Piece> list = new ArrayList<>();
		
		for (int i = 0; i < getPieces().size(); i++) {
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
							minY <= point.getY() && point.getY() <= maxY)
						list.add(currentPiece);
				}
			}
		}
		
		return list;
	}
	public ArrayList<Piece> getAll(int x, int y) {
		return getAll(new Point(x, y));
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
		
		snap();
		
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
		
		snap();
	}
	
	protected boolean addPiece(@NotNull Piece piece) {
		if (piece == null)
			throw new NullPointerException();
		
		piece.addObserver(this);
		boolean result = getPieces().add(piece);
		
		if (result)
			setFocusedPiece(getPieces().size() - 1);
		
		return result;
	}
	
	protected int getCurrentPiece() {
		return currentPiece;
	}
	
	protected void setCurrentPiece(int currentPiece) {
		this.currentPiece = currentPiece;
	}
	
	/**
	 * Indicate if there is a focused piece
	 * @return Return true if there is a current piece, false otherwise
	 */
	public boolean hasFocusedPiece() {
		return this.currentPiece >= 0;
	}
	
	/**
	 * Getter for the current focused piece. The focused piece is given by <c>currentPiece</c>. If <c>currentPiece</c>
	 * is out of the array, <c>null</c> is returned.
	 * @return Return the focused piece or null if no piece are currently focused
	 */
	@Nullable
	public Piece getFocusedPiece() {
		if (getCurrentPiece() < 0)
			return null;
		else {
			try {
				return getPieces().get(getCurrentPiece());
			} catch (IndexOutOfBoundsException ignored) {
				setCurrentPiece(-1);
				return null;
			}
		}
	}
	
	/**
	 * Set the focused piece to the given index
	 * @param index The index of the ArrayList <c>Grid.getPieces()</c>. If index is out of bound, then reset the focus
	 *              (no piece will have the focus)
	 */
	public void setFocusedPiece(int index) {
		if (0 <= index && index < getPieces().size())
			setCurrentPiece(index);
		else
			setCurrentPiece(-1);
	}
	
	/**
	 * Set the focused piece to the given piece
	 * @param piece The piece to put the focus on. If the piece is null or does not belong to the grid, then reset the
	 *              focus (no piece will have the focus)
	 */
	public void setFocusedPiece(@Nullable Piece piece) {
		if (piece == null)
			setFocusedPiece(-1);
		else
			setCurrentPiece(getIdFromPiece(piece));
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
		
		snap();
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
			snap();
	}
}
