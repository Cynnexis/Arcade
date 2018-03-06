package main.fr.polytech.arcade.game.grid;

import main.fr.polytech.arcade.game.ui.GridView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Observable;
import java.util.Observer;

public class GridController implements Observer {
	
	@NotNull
	private Grid grid;
	@NotNull
	private GridView view;
	
	public GridController(@NotNull Grid grid, @NotNull GridView view) {
		setGrid(grid);
		setView(view);
	}
	public GridController(@NotNull Grid grid) {
		this(grid, new GridView());
	}
	public GridController() {
		this(new Grid());
	}
	
	/* CONTROL */
	
	
	
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
