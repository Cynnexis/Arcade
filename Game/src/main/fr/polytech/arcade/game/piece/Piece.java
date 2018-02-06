package main.fr.polytech.arcade.game.piece;

import com.sun.istack.internal.NotNull;
import fr.berger.enhancedlist.Point;
import main.fr.polytech.arcade.game.grid.Grid;

import java.util.Objects;

public class Piece extends RotableObject implements Movable, Invertible {
	
	private boolean isPlaced;
	@NotNull
	private Shape shape;
	
	/* CONSTRUCTORS */
	
	public Piece() {
		setPlaced(false);
		setShape(new Shape());
		updateCenter();
	}
	public Piece(boolean isPlaced) {
		setPlaced(isPlaced);
		setShape(new Shape());
		updateCenter();
	}
	public Piece(@NotNull Shape shape) {
		setPlaced(false);
		setShape(shape);
		updateCenter();
	}
	public Piece(boolean isPlaced, @NotNull Shape shape) {
		setPlaced(isPlaced);
		setShape(shape);
		updateCenter();
	}
	
	public Point updateCenter() {
		setCentre(new Point(getShape().getNbColumns()/2, getShape().getNbRows()/2));
		
		return getCentre();
	}
	
	/* GETTERS & SETTERS */
	
	public boolean isPlaced() {
		return isPlaced;
	}
	
	public void setPlaced(boolean placed) {
		isPlaced = placed;
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
	
	/* OVERRIDES */
	
	@Override
	public boolean rotate(Grid grid, Direction direction) {
		return false;
	}
	
	@Override
	public boolean move(Grid grid, Point destination) {
		return false;
	}
	
	@Override
	public void invert(Axis axis) {
		// Invert piece
	}
	
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
