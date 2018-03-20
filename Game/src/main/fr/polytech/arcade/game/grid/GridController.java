package main.fr.polytech.arcade.game.grid;

import fr.berger.enhancedlist.Point;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import main.fr.polytech.arcade.game.piece.Piece;
import main.fr.polytech.arcade.game.piece.Shape;
import main.fr.polytech.arcade.game.ui.GridHandler;
import main.fr.polytech.arcade.game.ui.GridView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("NullableProblems")
public class GridController implements Observer {
	
	@NotNull
	private Grid grid;
	@NotNull
	private GridView view;
	
	@NotNull
	private ArrayList<GridHandler> gridHandlers;
	
	public GridController(@NotNull Grid grid, @NotNull GridView view) {
		setGrid(grid);
		setView(view);
		setGridHandlers(new ArrayList<>());
		
		getView().addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				getView().requestFocus();
				
				int x = (int) Math.floor(event.getX() / (getView().getTileDimension() + getView().getLineStroke()));
				int y = (int) Math.floor(event.getY() / (getView().getTileDimension() + getView().getLineStroke()));
				
				//System.out.println("[" + x + " ; " + y + "]");
				
				for (GridHandler gh : getGridHandlers()) {
					if (gh != null) {
						try {
							gh.onTileClicked(x, y);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		});
		
		getView().setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				for (GridHandler gh : getGridHandlers()) {
					if (gh != null) {
						try {
							gh.onKeyPressed(event.getCode());
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		});
		
		getView().requestFocus();
	}
	public GridController(@NotNull Grid grid) {
		this(grid, new GridView());
	}
	public GridController(int nbColumns, int nbRows) {
		this(new Grid(nbColumns, nbRows));
	}
	public GridController() {
		this(new Grid());
	}
	
	public void requestFocus() {
		getView().requestFocus();
	}
	
	/* GETTERS & SETTERS */
	
	public @NotNull Grid getGrid() {
		return grid;
	}
	
	public void setGrid(@NotNull Grid grid) {
		if (grid == null)
			throw new NullPointerException();
		
		this.grid = grid;
		
		// Adding the current instance of GridController as an observer of 'grid'.
		this.grid.addObserver(this);
	}
	
	public @NotNull GridView getView() {
		return view;
	}
	
	public void setView(@NotNull GridView view) {
		if (view == null)
			throw new NullPointerException();
		
		this.view = view;
		this.view.requestFocus();
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
		
		getGridHandlers().add(gridHandler);
	}
	
	/* OVERRIDES */
	
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
		if (o != null && o instanceof Grid && getView() != null) {
			setGrid((Grid) o);
			update();
		}
	}
	public void update() {
		if (getGrid() != null && getView() != null)
			getView().update(getGrid());
	}
}
