package main.fr.polytech.arcade.game.grid;

import main.fr.polytech.arcade.game.piece.PieceBuilder;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class GridTest {
	
	Grid grid;
	
	@BeforeEach
	void setUp() {
		grid = new Grid(10, 10);
		grid.add(new PieceBuilder()
						.setPosition(0, 0)
						.setShape(new int[][]
								{
										{0, 0, 1},
										{1, 1, 1}
								})
						.setCenter(1, 1)
						.createPiece(),
					 0, 0);
	}
}