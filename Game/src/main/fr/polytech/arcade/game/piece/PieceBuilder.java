package main.fr.polytech.arcade.game.piece;

import com.sun.istack.internal.NotNull;
import fr.berger.enhancedlist.Point;
import javafx.scene.paint.Color;

public class PieceBuilder {
	
	@NotNull
	private Piece piece;
	
	public PieceBuilder() {
		piece = new Piece();
	}
	
	public PieceBuilder setIsPlaced(boolean isPlaced) {
		piece.setPlaced(isPlaced);
		return this;
	}
	
	public PieceBuilder setShape(@NotNull Shape shape) {
		piece.setShape(shape);
		return this;
	}
	public PieceBuilder setShape(@NotNull boolean[][] shape) {
		// Check if argument is null
		if (shape == null)
			throw new NullPointerException();
		
		// Check if array is not empty
		if (shape.length <= 0)
			throw new IllegalArgumentException();
		
		// Chekc if all rows have the same length
		int dim = shape[0].length;
		for (int i = 0; i < shape.length; i++)
			if (shape[i].length != dim)
				throw new ArrayIndexOutOfBoundsException();
		
		// Now, set shape
		/*piece.getShape().setNbRows(shape.length);
		piece.getShape().setNbColumns(dim);*/
		piece.setShape(new Shape(dim, shape.length));
		try {
			for (int i = 0; i < shape.length; i++)
				for (int j = 0; j < dim; j++)
					piece.getShape().set(j, i, shape[i][j]);
		} catch (IndexOutOfBoundsException ex) {
			ex.printStackTrace();
		}
		
		piece.updateCenter();
		
		return this;
	}
	public PieceBuilder setShape(@NotNull int[][] shape) {
		// Check if argument is null
		if (shape == null)
			throw new NullPointerException();
		
		// Check if array is not empty
		if (shape.length <= 0)
			throw new IllegalArgumentException();
		
		boolean[][] s = new boolean[shape.length][shape[0].length];
		
		for (int i = 0; i < shape.length; i++)
			for (int j = 0; j < shape[i].length; j++)
				s[i][j] = shape[i][j] == 1;
		
		return setShape(s);
	}
	
	public PieceBuilder setCenter(@NotNull Point center) {
		piece.setCentre(center);
		return this;
	}
	public PieceBuilder setCenter(int x, int y) {
		piece.setCentre(new Point(x, y));
		return this;
	}
	
	public PieceBuilder setColor(@NotNull Color color) {
		piece.setColor(color);
		return this;
	}
	
	public @NotNull Piece createPiece() {
		if (piece == null)
			piece = new Piece();
		
		piece.updateCenter();
		return piece;
	}
}
