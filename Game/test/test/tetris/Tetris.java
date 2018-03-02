package test.tetris;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.fr.polytech.arcade.game.grid.GridController;
import main.fr.polytech.arcade.game.piece.Piece;
import main.fr.polytech.arcade.game.piece.PieceBuilder;
import main.fr.polytech.arcade.game.ui.GridView;

public class Tetris extends Application {
	
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
	public void start(Stage primaryStage) throws Exception {
		GridController g_controller = new GridController();
		
		boolean r1 = g_controller.getGrid().add(new PieceBuilder()
				.setPosition(0, 0)
				.setShape(new int[][]
						{
								{1, 0},
								{1, 1}
						})
				.setColor(Color.CYAN)
				.createPiece());
		
		boolean r2 = g_controller.getGrid().add(new PieceBuilder()
				.setPosition(1, 0)
				.setShape(new int[][]
						{
								{1, 1},
								{0, 1}
						})
				.setColor(Color.RED)
				.createPiece());
		
		System.out.println("Tetris.start> r1 = " + r1 + " ; r2 = " + r2);
		
		for (int i = 0; i < g_controller.getGrid().getPieces().size(); i++)
			System.out.println("Tetris.start> piece n°" + i + ":\n" + g_controller.getGrid().getPieces().get(i).toString());
		
		Scene scene = new Scene(g_controller.getView());
		
		primaryStage.setTitle("Tetris");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
