package main.fr.polytech.arcade.game.ui;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import org.jetbrains.annotations.NotNull;

/**
 * Event handler interface for GridView
 * @author Valentin Berger
 * @see GridView
 * @see main.fr.polytech.arcade.game.grid.GridController
 */
public interface GridHandler {
	
	/**
	 * Call when a tile received a click from the mouse event handler.
	 * @param x The coordinate x of the tile in the grid
	 * @param y The coordinate y of the tile in the grid
	 * @param mouseButton The information about the mouse event
	 */
	void onTileClicked(int x, int y, @NotNull MouseButton mouseButton);
	
	/**
	 * Call when a key is pressed while the grid has the focus
	 * @param code The information about the key event
	 */
	void onKeyPressed(@NotNull KeyCode code);
}
