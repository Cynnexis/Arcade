package main.fr.polytech.arcade.game.ui;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import fr.berger.enhancedlist.Point;
import main.fr.polytech.arcade.game.grid.Grid;
import main.fr.polytech.arcade.game.grid.GridModel;
import main.fr.polytech.arcade.game.piece.Piece;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Observable;
import java.util.Observer;

public class GridView extends BorderPane {
	
	public GridView() {
		super();
	}
	
	public void update(@NotNull Grid grid) {
		if (grid == null)
			throw new NullPointerException();
		
		GridPane gp_center = new GridPane();
		
		gp_center.setGridLinesVisible(true);
		
		for (int i = 0; i < grid.getWidth(); i++) {
			for (int j = 0; j < grid.getHeight(); j++) {
				Piece currentPiece = grid.get(i, j);
				
				/* TODO: Resolve this bug: When two pieces are overlaid (with one at 'false' and the other at 'true'
				 *for their shape), the first of the list is given.
				 * Solution: change Grid.get() method to do the following:
				 * -> Get the first piece which meet the conditions. However, if the piece is false for the condition,
				 * continue to parse the list.
				 * -> If another piece meets the conditions AND is true for them, take this one in priority, and stop
				 * the loop.
				 * -> Return the default piece or the top priority piece if it exists
				*/
				
				Rectangle rect = new Rectangle(50, 50);
				
				rect.setStroke(Color.GRAY);
				
				if (currentPiece != null) {
					int x = i - currentPiece.getPosition().getX();
					int y = j - currentPiece.getPosition().getY();
					
					if (currentPiece.getShape() != null && currentPiece.getPosition() != null &&
							currentPiece.getShape().get(x, y))
						rect.setFill(currentPiece.getColor());
				}
				else
					rect.setFill(Color.WHITE);
				
				gp_center.add(rect, i, j);
			}
		}
		
		this.setCenter(gp_center);
	}
}
