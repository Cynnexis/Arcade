package main.fr.polytech.arcade.game.ui;

import com.sun.corba.se.impl.orb.ParserTable;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import main.fr.polytech.arcade.game.grid.Grid;
import main.fr.polytech.arcade.game.grid.Square;
import main.fr.polytech.arcade.game.piece.Piece;

public class VisualGrid extends BorderPane {
	
	@Nullable
	private Grid grid = null;
	
	@NotNull
	private GridPane gp_center = new GridPane();
	
	public VisualGrid() {
		super();
		setGrid(null);
	}
	public VisualGrid(@Nullable Grid grid) {
		super();
		setGrid(grid);
	}
	
	public void update(@NotNull Grid grid) {
		if (grid == null)
			throw new NullPointerException();
		
		gp_center = new GridPane();
		
		gp_center.setGridLinesVisible(true);
		
		for (int i = 0; i < grid.getNbColumns(); i++) {
			for (int j = 0; j < grid.getNbRows(); j++) {
				Piece currentPiece = grid.getPiece(i, j);
				
				Rectangle rect = new Rectangle(50, 50);
				
				rect.setStroke(Color.GRAY);
				
				if (currentPiece != null)
					rect.setFill(currentPiece.getColor());
				else
					rect.setFill(Color.WHITE);
				
				gp_center.add(rect, i, j);
			}
		}
		
		this.setCenter(gp_center);
	}
	public void update() {
		if (getGrid() != null)
			update(getGrid());
	}
	
	/* GETTERS & SETTERS */
	
	public @Nullable Grid getGrid() {
		return grid;
	}
	
	public void setGrid(@Nullable Grid grid) {
		this.grid = grid;
	}
}