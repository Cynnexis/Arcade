package test.tetris;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
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
		TetrisEngine tengine = new TetrisEngine();
		GridView vg = new GridView(tengine.getGrid());
		
		tengine.getGrid().addPiece(new PieceBuilder()
				.setShape(new int[][]
						{
								{1, 0},
								{1, 1}
						})
				.setColor(Color.CYAN)
				.createPiece(), 0, 0);
		
		tengine.getGrid().addPiece(new PieceBuilder()
				.setShape(new int[][]
						{
								{1, 1},
								{0, 1}
						})
				.setColor(Color.RED)
				.createPiece(), 1, 0);
		
		vg.update();
		
		Scene scene = new Scene(vg);
		
		primaryStage.setTitle("Tetris");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
