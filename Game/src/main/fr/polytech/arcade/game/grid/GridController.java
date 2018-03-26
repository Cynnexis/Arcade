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

/**
 * The controller of Grid. It implements the Observer pattern to receive the update from its models, and then notify
 * its view.
 * @author Valentin Berger
 * @see Observer
 * @see Grid
 * @see GridView
 */
@SuppressWarnings("NullableProblems")
public class GridController implements Observer {
	
	/**
	 * The model
	 */
	@NotNull
	private Grid grid;
	
	/**
	 * The view of the grid
	 */
	@NotNull
	private GridView view;
	
	/**
	 * The list of the grid handlers (event handlers)
	 */
	@NotNull
	private ArrayList<GridHandler> gridHandlers;
	
	/**
	 * Constructor for GridController
	 * @param grid The instance of the model
	 * @param view The instance of the view
	 */
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
							gh.onTileClicked(x, y, event.getButton());
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
	
	/**
	 * Constructor for GridController. The instance of the view is automatically created
	 * @param grid The instance of the model
	 */
	public GridController(@NotNull Grid grid) {
		this(grid, new GridView());
	}
	
	/**
	 * Constructor for GridController. The instance of the view is automatically created
	 * @param nbColumns The number of columns for the model
	 * @param nbRows The number of rows for the model
	 */
	public GridController(int nbColumns, int nbRows) {
		this(new Grid(nbColumns, nbRows));
	}
	
	/**
	 * Default constructor for GridController. The instances of the model and the view are automatically created
	 */
	public GridController() {
		this(new Grid());
	}
	
	/**
	 * Put the focus of the view
	 */
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
