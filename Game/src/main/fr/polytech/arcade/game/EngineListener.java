package main.fr.polytech.arcade.game;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import main.fr.polytech.arcade.game.piece.Piece;

@Deprecated
public interface EngineListener {
	
	void onCurrentPieceChanged(@Nullable Piece newCurrentPiece);
	
	void tick(long time);
}
