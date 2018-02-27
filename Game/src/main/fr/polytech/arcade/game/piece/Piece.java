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
	private Shape shape;
	@NotNull
	private Point centre;
	@NotNull
	private Color color;
	
	/* CONSTRUCTORS */
	
	public Piece() {
		setPlaced(false);
		setShape(new Shape());
		setColor(Color.BLACK);
		updateCenter();
	}
	public Piece(boolean isPlaced) {
		setPlaced(isPlaced);
		setShape(new Shape());
		setColor(Color.BLACK);
		updateCenter();
	}
	public Piece(@NotNull Shape shape) {
		setPlaced(false);
		setShape(shape);
		setColor(Color.BLACK);
		updateCenter();
	}
	public Piece(@NotNull Color color) {
		setPlaced(false);
		setShape(shape);
		setColor(color);
		updateCenter();
	}
	public Piece(boolean isPlaced, @NotNull Shape shape, @NotNull Color color) {
		setPlaced(isPlaced);
		setShape(shape);
		setColor(color);
		updateCenter();
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
