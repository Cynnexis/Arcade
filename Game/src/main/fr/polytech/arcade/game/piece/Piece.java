package main.fr.polytech.arcade.game.piece;

import fr.berger.enhancedlist.Point;
import javafx.scene.paint.Color;
import main.fr.polytech.arcade.game.AbstractModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Model of a Piece. It inherits from the Observable model, even if it does not have a controller nor a view (a piece
 * is managed by a <c>Grid</c> which respect the MVC model).
 * @see main.fr.polytech.arcade.game.grid.Grid
 * @see main.fr.polytech.arcade.game.grid.GridController
 * @see java.util.Observable
 * @see Shape
 * @see PieceBuilder
 */
public class Piece extends AbstractModel implements Serializable, Cloneable {
	
	/**
	 * The id of a piece is represented by UUID
	 */
	private String id;
	
	/**
	 * Indicate if the piece is placed in the grid or if it is a "ghost"
	 */
	private boolean isPlaced;
	
	/**
	 * The current position in the grid of the piece
	 */
	@NotNull
	private Point position;
	
	/**
	 * The shape of the piece (a matrix of boolean values)
	 */
	@NotNull
	private Shape shape;
	
	/**
	 * The gravity center of the piece (used for the rotation)
	 */
	@NotNull
	private Point centre;
	
	/**
	 * The color of the piece
	 */
	@NotNull
	private Color color;
	
	/* CONSTRUCTORS */
	
	/**
	 * Constructor for Piece.
	 * @param isPlaced
	 * @param position
	 * @param shape
	 * @param centre
	 * @param color
	 */
	public Piece(boolean isPlaced, @NotNull Point position, @NotNull Shape shape, @NotNull Point centre, @NotNull Color color) {
		setId(UUID.randomUUID().toString());
		setPlaced(isPlaced);
		setPosition(position);
		setShape(shape);
		setCentre(centre);
		setColor(color);
	}
	
	/**
	 * Constructor for Piece. The gravity center is automatically computed.
	 * @param isPlaced
	 * @param position
	 * @param shape
	 * @param color
	 */
	public Piece(boolean isPlaced, @NotNull Point position, @NotNull Shape shape, @NotNull Color color) {
		this(isPlaced, position, shape, new Point(0, 0), color);
		updateCenter();
	}
	
	/**
	 * Constructor for Piece. The gravity center is automatically computed and the piece is placed in the grid.
	 * @param position
	 * @param shape
	 * @param color
	 */
	public Piece(@NotNull Point position, @NotNull Shape shape, @NotNull Color color) {
		this(true, position, shape, color);
	}
	
	/**
	 * Constructor for Piece. The gravity center is automatically computed, the piece is placed in the grid and its
	 * color is black.
	 * @param position
	 * @param shape
	 */
	public Piece(@NotNull Point position, @NotNull Shape shape) {
		this(position, shape, Color.BLACK);
	}
	
	/**
	 * Constructor for Piece. The gravity center is automatically computed, the piece is placed in the grid, its color
	 * is black and it has no shape (empty matrix).
	 * @param position
	 */
	public Piece(@NotNull Point position) {
		this(position, new Shape());
	}
	
	/**
	 * Constructor for Piece. The gravity center is automatically computed, the piece is placed in the grid, its color
	 * is black and it is at (0 ; 0).
	 * @param shape
	 */
	public Piece(@NotNull Shape shape) {
		this(new Point(0, 0), shape);
	}
	
	/**
	 * Copy constructor
	 * @param copy The piece to copy the properties
	 */
	public Piece(@NotNull Piece copy) {
		this(copy.isPlaced(), copy.getPosition(), copy.getShape(), copy.getCentre(), copy.getColor());
	}
	
	/**
	 * The default constructor. The gravity center is automatically computed, the piece is placed in the grid, its color
	 * is black, it is at (0 ; 0) and it has no shape (empty matrix).
	 */
	public Piece() {
		this(new Shape());
	}
	
	/**
	 * Rotate the piece regardless of the grid.
	 * @param degreeClockwise Accepted value: 0, 90, 180, 270
	 * @return Return the instance of the rotated piece
	 */
	public @NotNull Piece rotate(int degreeClockwise) {
		switch (degreeClockwise)
		{
			case 90:
				rotateClockwise90();
				break;
			case 180:
				rotateClockwise90();
				rotateClockwise90();
				break;
			case 270:
				rotateClockwise90();
				rotateClockwise90();
				rotateClockwise90();
				break;
			default:
				break;
		}
		
		return this;
	}
	
	/**
	 * Rotate the piece 90° clockwise
	 * @return Return the instance of the rotated piece.
	 */
	public @NotNull Piece rotateClockwise90() {
		Shape rotated = new Shape(getShape().getNbRows(), getShape().getNbColumns());
		
		Point oldCenter = getCentre();
		
		int x, y;
		boolean result;
		
		for (int i = 0; i < getShape().getNbColumns(); i++) {
			for (int j = 0; j < getShape().getNbRows(); j++) {
				x = getShape().getNbRows() - j - 1;
				y = i;
				result = getShape().get(i, j);
				rotated.set(x, y, result);
			}
		}
		
		setShape(rotated);
		
		// Update gravity center
		setCentre(new Point(getShape().getNbColumns() - oldCenter.getY() - 1, oldCenter.getX()));
		
		// Update position according to the gravity center
		x = getPosition().getX();
		y = getPosition().getY();
		setPosition(new Point(x + (oldCenter.getX() - getCentre().getX()), y + (oldCenter.getY() - getCentre().getY())));
		
		return this;
	}
	
	/**
	 * Invert the piece
	 * @param axis The axis of inversion
	 * @return Return the instance of the inverted piece
	 */
	public @NotNull Piece invert(@NotNull Axis axis) {
		Shape inverted = new Shape(getShape().getNbColumns(), getShape().getNbRows());
		
		for (int i = 0; i < getShape().getNbColumns(); i++) {
			for (int j = 0; j < getShape().getNbRows(); j++) {
				if (axis == Axis.HORIZONTAL)
					inverted.set(getShape().getNbColumns() - i - 1, j, getShape().get(i, j));
				else
					inverted.set(i, getShape().getNbRows() - j - 1, getShape().get(i, j));
			}
		}
		
		setShape(inverted);
		return this;
	}
	
	/**
	 * Update the gravity center of the piece according to the shape. Notify the observers at the end
	 * @return Return the new gravity center of the piece
	 */
	public Point updateCenter() {
		setCentre(new Point(getShape().getNbColumns()/2, getShape().getNbRows()/2));
		
		return getCentre();
	}
	
	/* GETTERS & SETTERS */
	
	@NotNull
	public String getId() {
		if (this.id == null || Objects.equals(this.id, ""))
			this.id = UUID.randomUUID().toString();
		
		return id;
	}
	
	public void setId(@Nullable String id) {
		if (id == null || Objects.equals(id, ""))
			this.id = UUID.randomUUID().toString();
		else
			this.id = id;
	}
	
	public boolean isPlaced() {
		return isPlaced;
	}
	
	public void setPlaced(boolean placed) {
		isPlaced = placed;
		
		snap(isPlaced);
	}
	
	public @NotNull Point getPosition() {
		return position;
	}
	
	public void setPosition(@NotNull Point position) {
		if (position == null)
			throw new NullPointerException();
		
		this.position = position;
		
		snap(this.position);
	}
	
	public @NotNull Shape getShape() {
		if (this.shape == null)
			this.shape = new Shape();
		
		return shape;
	}
	
	public void setShape(@NotNull Shape shape) {
		if (shape == null)
			throw new NullPointerException();
		
		this.shape = shape;
		updateCenter();
	}
	
	/**
	 * Get the dimension of the piece according to its <c>position</c> and <c>shape</c>
	 * @return Return the result of the addition of the vectors <c>position</c> and <c>shape</c>.
	 */
	public @NotNull Point getDimension() {
		return new Point(getPosition().getX() + getShape().getNbColumns() - 1, getPosition().getY() + getShape().getNbRows() - 1);
	}
	
	public Point getCentre() {
		return centre;
	}
	
	public void setCentre(Point centre) {
		this.centre = centre;
		
		snap(this.centre);
	}
	
	public @NotNull Color getColor() {
		return color;
	}
	
	public void setColor(@NotNull Color color) {
		if (color == null)
			throw new NullPointerException();
		
		this.color = color;
		
		snap(this.color);
	}
	
	/* OVERRIDES */
	
	@Override
	public String toString() {
		char placed = '▨';
		char notPlaced = '⊡';
		
		char centerSquare = '⧇';
		char centerCircle = '⊚';
		
		char c = isPlaced() ? placed : notPlaced;
		
		StringBuilder representation = new StringBuilder("");
		
		for (int i = 0; i < getShape().getNbRows(); i++) {
			for (int j = 0; j < getShape().getNbColumns(); j++) {
				if (getShape().get(j, i)) {
					if (Objects.equals(getCentre(), new Point(j, i)))
						representation.append(centerSquare);
					else
						representation.append(c);
				}
				else {
					if (Objects.equals(getCentre(), new Point(j, i)))
						representation.append(centerCircle);
					else
						representation.append(' ');
				}
			}
			
			representation.append('\n');
		}
		
		return representation.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Piece)) return false;
		Piece piece = (Piece) o;
		return isPlaced() == piece.isPlaced() &&
				Objects.equals(getId(), piece.getId()) &&
				Objects.equals(getPosition(), piece.getPosition()) &&
				Objects.equals(getShape(), piece.getShape()) &&
				Objects.equals(getCentre(), piece.getCentre()) &&
				Objects.equals(getColor(), piece.getColor());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getId(), isPlaced(), getPosition(), getShape(), getCentre(), getColor());
	}
}
