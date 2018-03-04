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
	
	@NotNull
	private Color backgroundColor;
	@NotNull
	private Color lineColor;
	
	public GridView() {
		super();
		setBackgroundColor(Color.WHITE);
		setLineColor(Color.GRAY);
	}
	
	public void update(@NotNull Grid grid) {
		if (grid == null)
			throw new NullPointerException();
		
		GridPane gp_center = new GridPane();
		
		gp_center.setGridLinesVisible(true);
		
		for (int i = 0; i < grid.getWidth(); i++) {
			for (int j = 0; j < grid.getHeight(); j++) {
				Piece currentPiece = grid.get(i, j);
				
				Rectangle rect = new Rectangle(50, 50);
				
				rect.setStroke(getLineColor());
				
				if (currentPiece != null) {
					int x = i - currentPiece.getPosition().getX();
					int y = j - currentPiece.getPosition().getY();
					
					if (currentPiece.getShape() != null && currentPiece.getPosition() != null &&
							currentPiece.getShape().get(x, y))
						rect.setFill(currentPiece.getColor());
					else
						rect.setFill(getBackgroundColor());
				}
				else
					rect.setFill(getBackgroundColor());
				
				gp_center.add(rect, i, j);
			}
		}
		
		this.setCenter(gp_center);
	}
	
	/* GETTERS & SETTERS */
	
	public @NotNull Color getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setBackgroundColor(@NotNull Color backgroundColor) {
		if (backgroundColor == null)
			throw new NullPointerException();
		
		this.backgroundColor = backgroundColor;
	}
	
	public @NotNull Color getLineColor() {
		return lineColor;
	}
	
	public void setLineColor(@NotNull Color lineColor) {
		if (lineColor == null)
			throw new NullPointerException();
		
		this.lineColor = lineColor;
	}
}
