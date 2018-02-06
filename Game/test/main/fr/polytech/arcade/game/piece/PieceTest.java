package main.fr.polytech.arcade.game.piece;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {
	
	Piece piece;
	
	@BeforeEach
	void setUp() {
		piece = new Piece();
		piece.setShape(new Shape(2));
		piece.getShape().set(0, 0, false);
		piece.getShape().set(1, 0, true);
		piece.getShape().set(0, 1, true);
		piece.getShape().set(1, 1, true);
		piece.updateCenter();
	}
	
	@Test
	void test_PieceBuilder() {
		Piece p = new PieceBuilder()
				.setIsPlaced(false)
				.setShape(new int[][]{
						{0, 1},
						{1, 1}
				})
				.createPiece();
		
		System.out.println("PieceTest.test_PieceBuilder> piece:");
		System.out.println(p.toString());
		
		Assertions.assertTrue(Objects.equals(p, piece));
	}
	
	@Test
	void test_toString() {
		System.out.println("PieceTest.test_toString> piece:");
		System.out.println(piece.toString());
	}
}