package fr.polytech.arcade.tetris;

import fr.berger.enhancedlist.Point;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.fr.polytech.arcade.game.grid.GridController;
import main.fr.polytech.arcade.game.piece.Piece;
import main.fr.polytech.arcade.game.piece.PieceBuilder;
import main.fr.polytech.arcade.game.ui.GridHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Main extends Application {

	private GridController g_controller;

	private long lastUpdateNano = 0;
	private boolean continueGravity = true;

	/**
	 * buffer fot the next piece
	 */
	private Piece buffer = null;
	private Random random;

	/**
	 * The main entry point for all JavaFX applications.
	 * The start method is called after the init method has returned,
	 * and after the system is ready for the application to begin running.
	 * <p>
	 * <p>
	 * NOTE: This method is called on the JavaFX Application Thread.
	 * </p>
	 *
	 * @param primaryStage the primary stage for this application, onto which
	 *                     the application scene can be set. The primary stage will be embedded in
	 *                     the browser if the application was launched as an applet.
	 *                     Applications may create other stages, if needed, but they will not be
	 *                     primary stages and will not be embedded in the browser.
	 */
	@Override
	public void start(@NotNull Stage primaryStage) throws Exception {
		g_controller = new GridController(10, 15);

		g_controller.addGridHandler(new GridHandler() {
			@Override
			public void onTileClicked(int x, int y, @NotNull MouseButton mouseButton) {
				Piece clickedPiece = g_controller.getGrid().get(x, y);
				Piece focusedPiece = g_controller.getGrid().getFocusedPiece();
				
				if (focusedPiece != null) {
					boolean tryToMovePiece = true;
					
					if (clickedPiece != null && Objects.equals(clickedPiece, focusedPiece)) {
						g_controller.getGrid().rotate(clickedPiece, 90);
						tryToMovePiece = false;
					}
					
					if (tryToMovePiece) {
						if (x < focusedPiece.getPosition().getX())
							g_controller.getGrid().move(focusedPiece, new Point(focusedPiece.getPosition().getX() - 1, focusedPiece.getPosition().getY()));
						else if (x > focusedPiece.getPosition().getX())
							g_controller.getGrid().move(focusedPiece, new Point(focusedPiece.getPosition().getX() + 1, focusedPiece.getPosition().getY()));
					}
				}
			}

			@Override
			public void onKeyPressed(@NotNull KeyCode code) {
				Piece piece = g_controller.getGrid().getFocusedPiece();

				if (piece != null) {
					switch (code)
					{
						case Q:
							g_controller.getGrid().move(piece, new Point(piece.getPosition().getX() - 1, piece.getPosition().getY()));
							break;
						case S:
							g_controller.getGrid().move(piece, new Point(piece.getPosition().getX(), piece.getPosition().getY() + 1));
							break;
						case D:
							g_controller.getGrid().move(piece, new Point(piece.getPosition().getX() + 1, piece.getPosition().getY()));
							break;
						case SPACE:
						case R:
							g_controller.getGrid().rotate(piece, 90);
							break;
					}
				}
			}
		});
		g_controller.update();

		for (int i = 0; i < g_controller.getGrid().getPieces().size(); i++)
			System.out.println("Tetris.start> piece n°" + i + ":\n" + g_controller.getGrid().getPieces().get(i).toString());

		BorderPane bp_main = new BorderPane();

		bp_main.setCenter(g_controller.getView());
		bp_main.setTop(new Text("Test"));

		Scene scene = new Scene(bp_main);

		new AnimationTimer() {
			@Override
			public void handle(long nowNano) {
				if (continueGravity && nowNano - lastUpdateNano >= 1000000000) {
					// Simulate gravity
					boolean result = gravity();
					
					// Check if row can be deleted
					if (rowsToDelete().size() > 0)
						System.out.println("Tetris> Rows to delete: " + rowsToDelete().toString());
					deleteLastFilledRows();
					
					// Generate new piece
					if (!result)
						generate();

					lastUpdateNano = nowNano;
				}
			}
		}.start();

		primaryStage.setTitle("Tetris");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		g_controller.requestFocus();
	}

	/**
	 * Simulate gravity force for the current piece
	 */
	private boolean gravity() {
		if (g_controller != null) {
			Piece currentPiece = g_controller.getGrid().getFocusedPiece();

			if (currentPiece != null) {
				return g_controller.getGrid().move(currentPiece, new Point(currentPiece.getPosition().getX(), currentPiece.getPosition().getY() + 1));
			}
		}

		return false;
	}

	private void generate() {
		if (random == null)
			random = new Random();

		// If it is the first time that this method is called
		if (buffer == null) {
			g_controller.getGrid().add(PieceBuilder.randomTemplate());
			buffer = PieceBuilder.randomTemplate();
		}
		else {
			g_controller.getGrid().add(buffer);
			buffer = PieceBuilder.randomTemplate();
		}

		g_controller.getGrid().setFocusedPiece(g_controller.getGrid().getPieces().size() - 1);
	}
	
	private boolean deleteLastFilledRows() {
		ArrayList<Integer> rows = rowsToDelete();
		
		if (rows.isEmpty())
			return false;
		
		boolean hasDeletedAtLeastOneRow = false;
		
		for (Integer y : rows)
			for (int x = 0; x < g_controller.getGrid().getWidth(); x++)
				if (g_controller.getGrid().deleteAt(x, y))
					hasDeletedAtLeastOneRow = true;
		
		// If at least one row has been deleted, shift all pieces to the bottom
		for (Piece piece : g_controller.getGrid().getPieces()) {
			g_controller.getGrid().forceMove(piece, piece.getPosition().getX(), piece.getPosition().getY() + rows.size());
		}
		
		return hasDeletedAtLeastOneRow;
	}
	
	/**
	 * Search all filled rows in the grid.
	 * @return Return a list of row indexes that must be deleted.
	 */
	@NotNull
	private ArrayList<Integer> rowsToDelete() {
		ArrayList<Integer> rows = new ArrayList<>();
		
		if (g_controller != null) {
			
			/**
			 * Tell if a non-filled row has been found
			 */
			boolean nonFilledRow = false;
			
			/**
			 * Tell if the current row contains only filled tiles
			 */
			boolean currentRowComplete = true;
			
			// For every row in the grid (starting from the end)...
			for (int y = g_controller.getGrid().getHeight() - 1; y >= 0 && !nonFilledRow; y--) {
				
				currentRowComplete = true;
				
				// For every column in the row number 'y' and while currentRowComplete is true...
				for (int x = 0; x < g_controller.getGrid().getWidth() && currentRowComplete; x++) {
					// If the current tile (x ; y) is not filled, then the row cannot be deleted.
					if (g_controller.getGrid().get(x, y) == null)
						currentRowComplete = false;
					// If a piece has been found at (x ; y), that does not mean that all the tiles of the piece are occupied.
					else {
						Piece piece = g_controller.getGrid().get(x, y);
						@SuppressWarnings("ConstantConditions")
						int xInPiece = x - piece.getPosition().getX();
						int yInPiece = y - piece.getPosition().getY();
						
						if (!piece.getShape().get(xInPiece, yInPiece))
							currentRowComplete = false;
					}
				}
				
				// If all the tile in the current row are filled, add the current to the list
				if (currentRowComplete)
					rows.add(y);
				else
					nonFilledRow = true;
			}
		}
		
		return rows;
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		continueGravity = false;
	}
    
    public static void main(String[] args) {
        launch(args);
    }
}
