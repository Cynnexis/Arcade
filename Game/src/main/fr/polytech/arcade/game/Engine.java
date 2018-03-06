package main.fr.polytech.arcade.game;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import main.fr.polytech.arcade.game.grid.Grid;
import main.fr.polytech.arcade.game.piece.Piece;

import java.util.ArrayList;

@Deprecated
public abstract class Engine {
	
	@NotNull
	protected Grid grid;
	
	@Nullable
	protected Piece currentPiece;
	
	@NotNull
	private EngineListener engineListener;
	
	public Engine() {
		setGrid(new Grid());
		setCurrentPiece(null);
		setEngineListener(new EngineListener() {
			@Override
			public void onCurrentPieceChanged(Piece newCurrentPiece) { }
		});
	}
	
	/* GETTERS & SETTERS */
	
	public @NotNull Grid getGrid() {
		return grid;
	}
	
	public void setGrid(@NotNull Grid grid) {
		if (grid == null)
			throw new NullPointerException();
		
		this.grid = grid;
	}
	
	public @NotNull Piece getCurrentPiece() {
		return currentPiece;
	}
	
	public void setCurrentPiece(@Nullable Piece currentPiece) {
		Piece oldValue = getCurrentPiece();
		
		if (!oldValue.equals(currentPiece)) {
			this.currentPiece = currentPiece;
			
			getEngineListener().onCurrentPieceChanged(this.currentPiece);
		}
	}
	
	public @NotNull EngineListener getEngineListener() {
		if (engineListener == null)
			engineListener = new EngineListener() {
				@Override
				public void onCurrentPieceChanged(Piece newCurrentPiece) {}
			};
		
		return engineListener;
	}
	
	public void setEngineListener(@NotNull EngineListener engineListener) {
		if (engineListener == null)
			throw new NullPointerException();
		
		this.engineListener = engineListener;
	}
}
