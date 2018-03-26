package main.fr.polytech.arcade.game.ui;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.fr.polytech.arcade.game.grid.Grid;
import main.fr.polytech.arcade.game.piece.Piece;
import org.jetbrains.annotations.NotNull;

/**
 * The view for Grid and GridController. It inherits from BorderPane (JavaFX element)
 * @author Valentin Berger
 * @see Grid
 * @see main.fr.polytech.arcade.game.grid.GridController
 */
public class GridView extends BorderPane {
	
	/**
	 * The background color of the grid. Default is white.
	 */
	@NotNull
	private Color backgroundColor;
	
	/**
	 * The color of the lines between the tiles. Default is gray.
	 */
	@NotNull
	private Color lineColor;
	
	
	/**
	 * The width of the lines between the tiles. Default is 1.0.
	 */
	private double lineStroke;
	
	/**
	 * The size of a tile n*n. Default is 50.
	 */
	private int tileDimension;
	
	/**
	 * Default constructor.
	 */
	public GridView() {
		super();
		setBackgroundColor(Color.WHITE);
		setLineColor(Color.GRAY);
		setTileDimension(50);
		setLineStroke(1d);
	}
	
	/**
	 * Update its components according to <c>grid</c>
	 * @param grid The grid containing all the information to display through this view. Please see GridController for
	 *             further information.
	 * @see main.fr.polytech.arcade.game.grid.GridController
	 */
	public void update(@NotNull Grid grid) {
		if (grid == null)
			throw new NullPointerException();
		
		GridPane gp_center = new GridPane();
		
		gp_center.setGridLinesVisible(true);
		
		for (int i = 0; i < grid.getWidth(); i++) {
			for (int j = 0; j < grid.getHeight(); j++) {
				Piece currentPiece = grid.get(i, j);
				
				Rectangle rect = new Rectangle(getTileDimension(), getTileDimension());
				
				rect.setStroke(getLineColor());
				rect.setStrokeWidth(getLineStroke());
				
				if (currentPiece != null) {
					int x = i - currentPiece.getPosition().getX();
					int y = j - currentPiece.getPosition().getY();
					
					try {
						if (currentPiece.getShape() != null && currentPiece.getPosition() != null &&
								currentPiece.getShape().get(x, y)) {
								rect.setFill(currentPiece.getColor());
						}
						else
							rect.setFill(getBackgroundColor());
					} catch (IndexOutOfBoundsException ignored) {
						rect.setFill(getBackgroundColor());
					}
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
	
	public double getLineStroke() {
		return lineStroke;
	}
	
	public void setLineStroke(double lineStroke) {
		if (lineStroke < 0)
			throw new IllegalArgumentException("Line stroke must be greater than 0.");
		
		this.lineStroke = lineStroke;
	}
	
	public int getTileDimension() {
		return tileDimension;
	}
	
	public void setTileDimension(int dimension) {
		if (dimension <= 0)
			throw new IllegalArgumentException("The dimension of the tiles must be greater than 0.");
		
		this.tileDimension = dimension;
	}
}
