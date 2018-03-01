package main.fr.polytech.arcade.game.ui;

import com.sun.corba.se.impl.orb.ParserTable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import main.fr.polytech.arcade.game.grid.Grid;
import main.fr.polytech.arcade.game.grid.GridModel;
import main.fr.polytech.arcade.game.grid.Square;
import main.fr.polytech.arcade.game.piece.Piece;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Observable;
import java.util.Observer;

public class GridView extends BorderPane implements Observer {
	
	@NotNull
	private GridModel grid = null;
	
	@NotNull
	private GridPane gp_center = new GridPane();
	
	public GridView() {
		super();
		setGrid(null);
	}
	public GridView(@Nullable Grid grid) {
		super();
		setGrid(grid);
	}
	
	public void update(@NotNull Grid grid) {
		if (grid == null)
			throw new NullPointerException();
		
		gp_center = new GridPane();
		
		gp_center.setGridLinesVisible(true);
		
		for (int i = 0; i < grid.getWidth(); i++) {
			for (int j = 0; j < grid.getHeight(); j++) {
				Piece currentPiece = grid.get(i, j);
				
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
		if (this.grid == null)
			this.grid = new GridModel();
		
		return this.grid.getGrid();
	}
	
	public void setGrid(@Nullable Grid grid) {
		if (this.grid == null)
			this.grid = new GridModel();
		
		this.grid.setGrid(grid);
		
		if (this.grid.getGrid() != null) {
			this.grid.addObserver(this);
		}
	}
	
	/* OVERRIDE */
	
	/**
	 * This method is called whenever the observed object is changed. An
	 * application calls an <tt>Observable</tt> object's
	 * <code>notifyObservers</code> method to have all the object's
	 * observers notified of the change.
	 *
	 * @param o   the observable object.
	 * @param arg an argument passed to the <code>notifyObservers</code>
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof GridModel && getGrid() != null)
			update(getGrid());
	}
}
