package main.fr.polytech.arcade.game;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import main.fr.polytech.arcade.game.piece.Piece;

public interface EngineListener {
	
	void onCurrentPieceChanged(@Nullable Piece newCurrentPiece);
}
