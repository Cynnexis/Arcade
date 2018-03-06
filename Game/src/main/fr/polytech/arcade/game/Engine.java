package main.fr.polytech.arcade.game;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import main.fr.polytech.arcade.game.grid.Grid;
import main.fr.polytech.arcade.game.piece.Piece;

import java.util.ArrayList;

@SuppressWarnings("NullableProblems")
public class Engine {
	
	@Nullable
	protected Piece currentPiece;
	
	@NotNull
	private GameState state;
	
	@NotNull
	private EngineListener engineListener;
	
	@NotNull
	private long gameStart;
	@NotNull
	private Thread gameThread;
	
	public Engine() {
		setCurrentPiece(null);
		setState(GameState.INITIALIZING);
		setEngineListener(new EngineListener() {
			@Override
			public void onCurrentPieceChanged(Piece newCurrentPiece) { }
			@Override
			public void tick(long time) { }
		});
		
		setGameStart(System.currentTimeMillis());
		setGameThread(new Thread(new Runnable() {
			@Override
			public void run() {
				while (GameState.isGameContinuing(getState())) {
					getEngineListener().tick(System.currentTimeMillis() - getGameStart());
				}
			}
		}));
	}
	
	/* GETTERS & SETTERS */
	
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
	
	public @NotNull GameState getState() {
		if (state == null)
			state = GameState.INITIALIZING;
		
		return state;
	}
	
	public void setState(@NotNull GameState state) {
		if (state == null)
			throw new NullPointerException();
		
		this.state = state;
		
		if (GameState.isGameContinuing(this.state))
			getGameThread().start();
	}
	
	public @NotNull EngineListener getEngineListener() {
		if (engineListener == null)
			engineListener = new EngineListener() {
				@Override
				public void onCurrentPieceChanged(Piece newCurrentPiece) { }
				@Override
				public void tick(long time) { }
			};
		
		return engineListener;
	}
	
	public void setEngineListener(@NotNull EngineListener engineListener) {
		if (engineListener == null)
			throw new NullPointerException();
		
		this.engineListener = engineListener;
	}
	
	protected long getGameStart() {
		return gameStart;
	}
	
	private void setGameStart(long gameStart) {
		if (gameStart < 0)
			throw new IllegalArgumentException();
		
		this.gameStart = gameStart;
	}
	
	protected @NotNull Thread getGameThread() {
		return gameThread;
	}
	
	protected void setGameThread(@NotNull Thread gameThread) {
		if (gameThread == null)
			throw new NullPointerException();
		
		this.gameThread = gameThread;
	}
}
