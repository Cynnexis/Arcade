package main.fr.polytech.arcade.game.ui;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import org.jetbrains.annotations.NotNull;

public interface GridHandler {
	
	void onTileClicked(int x, int y, @NotNull MouseButton mouseButton);
	void onKeyPressed(@NotNull KeyCode code);
}
