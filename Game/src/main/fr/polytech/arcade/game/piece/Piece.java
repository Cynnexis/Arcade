package main.fr.polytech.arcade.game.piece;

import com.sun.istack.internal.NotNull;
import fr.berger.enhancedlist.Point;
import javafx.scene.paint.Color;
import main.fr.polytech.arcade.game.grid.Grid;

import java.util.Objects;
import java.util.Observable;

public class Piece extends Observable {
	
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
		this(false, position, shape, color);
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
	public Piece() {
		this(new Shape());
	}
	
	/**
	 * Update the gravity center of the piece according to the shape. Notify the observers at the end
	 * @return Return the new gravity center of the piece
	 */
	public Point updateCenter() {
		setCentre(new Point(getShape().getNbColumns()/2, getShape().getNbRows()/2));
		
		setChanged();
		notifyObservers();
		
		return getCentre();
	}
	
	/* GETTERS & SETTERS */
	
	public boolean isPlaced() {
		return isPlaced;
	}
	
	public void setPlaced(boolean placed) {
		isPlaced = placed;
		
		setChanged();
		notifyObservers();
	}
	
	public Point getPosition() {
		return position;
	}
	
	public void setPosition(@NotNull Point position) {
		if (position == null)
			throw new NullPointerException();
		
		this.position = position;
		
		setChanged();
		notifyObservers();
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
		
		setChanged();
		notifyObservers();
	}
	
	public @NotNull Color getColor() {
		return color;
	}
	
	public void setColor(@NotNull Color color) {
		if (color == null)
			throw new NullPointerException();
		
		this.color = color;
		
		setChanged();
		notifyObservers();
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
		
		for (int i = 0; i < getShape().getNbColumns(); i++) {
			for (int j = 0; j < getShape().getNbRows(); j++) {
				if (getShape().get(i, j)) {
					if (Objects.equals(getCentre(), new Point(i, j)))
						representation.append(centerSquare);
					else
						representation.append(c);
				}
				else {
					if (Objects.equals(getCentre(), new Point(i, j)))
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
