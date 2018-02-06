package main.fr.polytech.arcade.game.grid;

import com.sun.istack.internal.NotNull;
import fr.berger.enhancedlist.Point;
import main.fr.polytech.arcade.game.piece.Piece;

public class Square {
	
	@NotNull
	private Point position;
	@NotNull
	private int indexToPiece;
	@NotNull
	private SquareType type;
	
	public Square() {
		setPosition(new Point(-1, -1));
		setIndexToPiece(-1);
		setType(SquareType.EMPTY);
	}
	public Square(@NotNull Point position) {
		setPosition(position);
		setIndexToPiece(-1);
		setType(SquareType.EMPTY);
	}
	public Square(int x, int y) {
		setPosition(new Point(x, y));
		setIndexToPiece(-1);
		setType(SquareType.EMPTY);
	}
	
	/* GETTERS & SETTERS */
	
	public @NotNull Point getPosition() {
		if (this.position == null)
			this.position = new Point(-1, -1);
		
		return position;
	}
	
	public void setPosition(@NotNull Point position) {
		if (position == null)
			throw new NullPointerException();
		
		this.position = position;
	}
	
	public int getIndexToPiece() {
		return indexToPiece;
	}
	
	public void setIndexToPiece(int indexToPiece) {
		this.indexToPiece = indexToPiece;
	}
	
	public SquareType getType() {
		return type;
	}
	
	public void setType(SquareType type) {
		this.type = type;
	}
}
