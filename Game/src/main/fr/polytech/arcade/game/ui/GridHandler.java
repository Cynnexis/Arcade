package main.fr.polytech.arcade.game.ui;

import javafx.scene.input.KeyCode;
import org.jetbrains.annotations.NotNull;

public interface GridHandler {
	
	void onTileClicked(int x, int y);
	void onKeyPressed(@NotNull KeyCode code);
}
