package fr.polytech.arcade.rushhour;

import fr.berger.enhancedlist.Point;
import javafx.scene.paint.Color;
import main.fr.polytech.arcade.game.piece.Piece;
import main.fr.polytech.arcade.game.piece.PieceBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class GeneratePiece {
	
	private static Random random = null;
	
	@NotNull
	public static Piece generate() {
		// If random == HORIZONTAL_PIECE, generate horizontal piece
		if (getRandom().nextBoolean())
			return getHorizontalPiece();
		else
			return getVerticalPiece();
	}
	
	@NotNull
	public static Piece getHorizontalPiece(int length, @NotNull Point position) {
		if (length != 2 && length != 3)
			length = 2;
		
		int[][] sh = new int[1][length];
		
		for (int i = 0; i < length; i++) {
			sh[0][i] = 1;
		}
		
		return new PieceBuilder()
				.setShape(sh)
				.setColor(availableColors().get(getRandom().nextInt(availableColors().size())))
				.setPosition(position)
				.createPiece();
	}
	@NotNull
	public static Piece getHorizontalPiece(@NotNull Point position) {
		return getHorizontalPiece(2, position);
	}
	@NotNull
	public static Piece getHorizontalPiece(int length) {
		return getHorizontalPiece(length, new Point(0, 0));
	}
	@NotNull
	public static Piece getHorizontalPiece(int x, int y) {
		return getHorizontalPiece(new Point(x, y));
	}
	@NotNull
	public static Piece getHorizontalPiece() {
		return getHorizontalPiece(0, 0);
	}
	
	@NotNull
	public static Piece getVerticalPiece(int length, @NotNull Point position) {
		if (length != 2 && length != 3)
			length = 2;
		
		int[][] sh = new int[length][1];
		
		for (int i = 0; i < length; i++) {
			sh[i][0] = 1;
		}
		
		return new PieceBuilder()
				.setShape(new int[][] {
						{1},
						{1}
				})
				.setColor(availableColors().get(getRandom().nextInt(availableColors().size())))
				.setPosition(position)
				.createPiece();
	}
	@NotNull
	public static Piece getVerticalPiece(@NotNull Point position) {
		return getVerticalPiece(2, position);
	}
	@NotNull
	public static Piece getVerticalPiece(int length) {
		return getVerticalPiece(length, new Point(0, 0));
	}
	@NotNull
	public static Piece getVerticalPiece(int x, int y) {
		return getVerticalPiece(new Point(x, y));
	}
	@NotNull
	public static Piece getVerticalPiece() {
		return getVerticalPiece(0, 0);
	}
	
	@NotNull
	public static ArrayList<Color> availableColors() {
		ArrayList<Color> colors = new ArrayList<>();
		
		colors.add(Color.rgb(138, 47, 98));
		colors.add(Color.rgb(72, 47, 117));
		colors.add(Color.rgb(36, 106, 101));
		colors.add(Color.rgb(42, 125, 73));
		colors.add(Color.rgb(98, 153, 52));
		colors.add(Color.rgb(172, 172, 59));
		colors.add(Color.rgb(172, 134, 59));
		colors.add(Color.rgb(172, 110, 59));
		colors.add(Color.rgb(172, 62, 59));
		
		return colors;
	}
	
	@NotNull
	public static Color getPrimaryColor() {
		return Color.rgb(221, 240, 240);
	}
	
	public static boolean isHorizontal(@NotNull Piece piece) {
		if (piece == null)
			throw new NullPointerException();
		
		return piece.getShape().getNbColumns() > 1 && piece.getShape().getNbRows() < piece.getShape().getNbColumns();
	}
	
	public static boolean isVertical(@NotNull Piece piece) {
		if (piece == null)
			throw new NullPointerException();
		
		return piece.getShape().getNbRows() > 1 && piece.getShape().getNbRows() > piece.getShape().getNbColumns();
	}
	
	@NotNull
	public static Random getRandom() {
		if (random == null)
			random = new Random(System.currentTimeMillis());
		
		return random;
	}
}
