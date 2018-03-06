package main.fr.polytech.arcade.game.ui;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
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

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class GridView extends BorderPane {
	
	@NotNull
	private Color backgroundColor;
	@NotNull
	private Color lineColor;
	private double lineStroke;
	private int tileDimension;
	
	@NotNull
	private ArrayList<GridHandler> gridHandlers;
	
	public GridView() {
		super();
		setBackgroundColor(Color.WHITE);
		setLineColor(Color.GRAY);
		setTileDimension(50);
		setLineStroke(1d);
		
		addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				GridView.this.requestFocus();
				
				int x = (int) Math.floor(event.getX() / (getTileDimension() + getLineStroke()));
				int y = (int) Math.floor(event.getY() / (getTileDimension() + getLineStroke()));
				
				System.out.println("[" + x + " ; " + y + "]");
				
				for (GridHandler gh : getGridHandlers())
					gh.onTileClicked(x, y);
			}
		});
	}
	
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
	
	public @NotNull ArrayList<GridHandler> getGridHandlers() {
		if (gridHandlers == null)
			gridHandlers = new ArrayList<>();
		
		return gridHandlers;
	}
	
	public void setGridHandlers(@NotNull ArrayList<GridHandler> gridHandlers) {
		if (gridHandlers == null)
			throw new NullPointerException();
		
		this.gridHandlers = gridHandlers;
	}
	
	public void addGridHandler(@NotNull GridHandler gridHandler) {
		if (gridHandler == null)
			throw new NullPointerException();
		
		this.gridHandlers.add(gridHandler);
	}
}
