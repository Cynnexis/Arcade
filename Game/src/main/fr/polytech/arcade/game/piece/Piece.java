package main.fr.polytech.arcade.game.piece;

import fr.berger.enhancedlist.Point;
import javafx.scene.paint.Color;
import main.fr.polytech.arcade.game.AbstractModel;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Piece extends AbstractModel {
	
	private boolean isPlaced;
	@NotNull
	private Point position;
	@NotNull
	private Shape shape;
	@NotNull
	private Point centre;
	@NotNull
	private Color color;
	
	/* CONSTRUCTORS */
	
	public Piece(boolean isPlaced, @NotNull Point position, @NotNull Shape shape, @NotNull Point centre, @NotNull Color color) {
		setPlaced(isPlaced);
		setPosition(position);
		setShape(shape);
		setCentre(centre);
		setColor(color);
	}
	public Piece(boolean isPlaced, @NotNull Point position, @NotNull Shape shape, @NotNull Color color) {
		this(isPlaced, position, shape, new Point(0, 0), color);
		updateCenter();
	}
	public Piece(@NotNull Point position, @NotNull Shape shape, @NotNull Color color) {
		this(true, position, shape, color);
	}
	public Piece(@NotNull Point position, @NotNull Shape shape) {
		this(position, shape, Color.BLACK);
	}
	public Piece(@NotNull Point position) {
		this(position, new Shape());
	}
	public Piece(@NotNull Shape shape) {
		this(new Point(0, 0), shape);
	}
	public Piece(@NotNull Piece copy) {
		this(copy.isPlaced(), copy.getPosition(), copy.getShape(), copy.getCentre(), copy.getColor());
	}
	public Piece() {
		this(new Shape());
	}
	
	public @NotNull Piece rotate(@NotNull Direction direction) {
		Shape rotated = new Shape(getShape().getNbColumns(), getShape().getNbRows());
		
		switch (direction)
		{
			case NORTH:
				break;
			case EAST:
				transpose();
				reverseColumns();
				break;
			case SOUTH:
				invert(Axis.HORIZONTAL);
				invert(Axis.VERTICAL);
				break;
			case WEST:
				transpose();
				reverseColumns();
				break;
		}
		
		//setShape(rotated);
		return this;
	}
	
	public @NotNull Piece rotateAnticlockwise90() {
		Shape rotated = new Shape(getShape().getNbRows(), getShape().getNbColumns());
		
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
		
		return this;
	}
	
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
	
	public @NotNull Piece transpose() {
		Shape transposed = new Shape(getShape().getNbRows(), getShape().getNbColumns());
		
		for (int i = 0; i < getShape().getNbColumns(); i++) {
			for (int j = 0; j < getShape().getNbRows(); j++) {
				try {
					transposed.set(j, i, getShape().get(i, j));
				} catch (IndexOutOfBoundsException ignored) { }
				
				try {
					transposed.set(i, j, getShape().get(j, i));
				} catch (IndexOutOfBoundsException ignored) { }
			}
		}
		
		setShape(transposed);
		return this;
	}
	
	public @NotNull Piece reverseColumns() {
		Shape reversed = new Shape(getShape().getNbColumns(), getShape().getNbRows());
		
		for (int i = 0; i < getShape().getNbColumns(); i++) {
			for (int j = 0, k = getShape().getNbRows() - 1; j < k; j++, k--) {
				try {
					reversed.set(j, i, getShape().get(k, i));
				} catch (IndexOutOfBoundsException ignored) { }
				try {
					reversed.set(k, i, getShape().get(j, i));
				} catch (IndexOutOfBoundsException ignored) { }
			}
		}
		
		setShape(reversed);
		return this;
	}
	
	/**
	 * Update the gravity center of the piece according to the shape. Notify the observers at the end
	 * @return Return the new gravity center of the piece
	 */
	public Point updateCenter() {
		setCentre(new Point(getShape().getNbColumns()/2, getShape().getNbRows()/2));
		
		snap();
		
		return getCentre();
	}
	
	/* GETTERS & SETTERS */
	
	public boolean isPlaced() {
		return isPlaced;
	}
	
	public void setPlaced(boolean placed) {
		isPlaced = placed;
		
		snap();
	}
	
	public @NotNull Point getPosition() {
		return position;
	}
	
	public void setPosition(@NotNull Point position) {
		if (position == null)
			throw new NullPointerException();
		
		this.position = position;
		
		snap();
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
		
		snap();
	}
	
	public @NotNull Color getColor() {
		return color;
	}
	
	public void setColor(@NotNull Color color) {
		if (color == null)
			throw new NullPointerException();
		
		this.color = color;
		
		snap();
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
				Objects.equals(getShape(), piece.getShape()) &&
				Objects.equals(getCentre(), piece.getCentre());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(isPlaced(), getShape(), getCentre());
	}
}
