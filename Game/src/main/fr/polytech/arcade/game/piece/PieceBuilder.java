package main.fr.polytech.arcade.game.piece;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import fr.berger.enhancedlist.Point;
import javafx.scene.paint.Color;

import java.util.Random;

public class PieceBuilder {
	
	@NotNull
	private Piece piece;
	
	public PieceBuilder(@NotNull Piece piece) {
		if (piece != null)
			this.piece = piece;
	}
	public PieceBuilder() {
		this(new Piece());
	}
	
	public PieceBuilder setIsPlaced(boolean isPlaced) {
		piece.setPlaced(isPlaced);
		return this;
	}
	
	public PieceBuilder setPosition(@NotNull Point position) {
		piece.setPosition(position);
		return this;
	}
	public PieceBuilder setPosition(int x, int y) {
		return setPosition(new Point(x, y));
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
		
		// Check if all rows have the same length
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
	
	/* TEMPLATES */
	
	public static  @NotNull Piece randomTemplate(@Nullable Random rand) {
		if (rand == null)
			rand = new Random();
		
		switch (rand.nextInt(7))
		{
			case 0:
				return templateI();
			case 1:
				return templateJ();
			case 2:
				return templateL();
			case 3:
				return templateO();
			case 4:
				return templateS();
			case 5:
				return templateT();
			default:
				return templateZ();
		}
	}
	public static  @NotNull Piece randomTemplate() {
		return randomTemplate(null);
	}
	
	// See https://en.wikipedia.org/wiki/Tetris#Tetromino_colors
	
	public static  @NotNull Piece templateI() {
		return new PieceBuilder()
				.setShape(new int[][]
				{
						{1, 1, 1, 1}
				})
				.setColor(Color.CYAN)
				.createPiece();
	}
	
	public static @NotNull Piece templateJ() {
		return new PieceBuilder()
				.setShape(new int[][]
				{
						{1, 1, 1},
						{0, 0, 1}
				})
				.setColor(Color.BLUE)
				.createPiece();
	}
	
	public static @NotNull Piece templateL() {
		return new PieceBuilder()
				.setShape(new int[][]
				{
						{1, 1, 1},
						{1, 0, 0}
				})
				.setColor(Color.ORANGE)
				.createPiece();
	}
	
	public static @NotNull Piece templateO() {
		return new PieceBuilder()
				.setShape(new int[][]
				{
						{1, 1},
						{1, 1}
				})
				.setColor(Color.YELLOW)
				.createPiece();
	}
	
	public static @NotNull Piece templateS() {
		return new PieceBuilder()
				.setShape(new int[][]
				{
						{0, 1, 1},
						{1, 1, 0}
				})
				.setColor(Color.GREEN)
				.createPiece();
	}
	
	public static  @NotNull Piece templateT() {
		return new PieceBuilder()
				.setShape(new int[][]
				{
						{1, 1, 1},
						{0, 1, 0}
				})
				.setColor(Color.PURPLE)
				.createPiece();
	}
	
	public static @NotNull Piece templateZ() {
		return new PieceBuilder()
				.setShape(new int[][]
				{
						{1, 1, 0},
						{0, 1, 1}
				})
				.setColor(Color.RED)
				.createPiece();
	}
}
