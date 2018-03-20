package test.testarea;

import fr.berger.enhancedlist.Point;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.fr.polytech.arcade.game.grid.GridController;
import main.fr.polytech.arcade.game.piece.Piece;
import main.fr.polytech.arcade.game.piece.PieceBuilder;
import main.fr.polytech.arcade.game.ui.GridHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class TestArea extends Application {
	
	private GridController g_controller;
	
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
		g_controller = new GridController();
		
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
		
		boolean r3 = g_controller.getGrid().add(new PieceBuilder()
				.setPosition(4, 4)
				.setShape(new int[][]
						{
								{1, 0, 1, 1},
								{1, 1, 1, 0}
						})
				.setColor(Color.GREEN)
				.setCenter(1, 1)
				.createPiece());
		
		g_controller.addGridHandler(new GridHandler() {
			@Override
			public void onTileClicked(int x, int y, @NotNull MouseButton mouseButton) {
				System.out.print("TestArea.onTileClicked> (" + x + " ; " + y + ")");
				
				if (mouseButton == MouseButton.PRIMARY) {
					Piece clickedPiece = g_controller.getGrid().get(x, y);
					g_controller.getGrid().setFocusedPiece(clickedPiece);
					
					if (clickedPiece != null)
						System.out.print(" piece :\n" + clickedPiece.toString());
				}
				else if (mouseButton == MouseButton.SECONDARY) {
					g_controller.getGrid().deleteAt(x, y);
				}
				
				System.out.println();
			}
			
			@Override
			public void onKeyPressed(@NotNull KeyCode code) {
				Piece piece = g_controller.getGrid().getFocusedPiece();
				
				if (piece != null) {
					switch (code)
					{
						case Z:
							g_controller.getGrid().move(piece, new Point(piece.getPosition().getX(), piece.getPosition().getY() - 1));
							break;
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
		
		System.out.println("TestArea.start> r1 = " + r1 + " ; r2 = " + r2 + " ; r3 = " + r3);
		
		for (int i = 0; i < g_controller.getGrid().getPieces().size(); i++)
			System.out.println("TestArea.start> piece n°" + i + ":\n" + g_controller.getGrid().getPieces().get(i).toString());
		
		BorderPane bp_main = new BorderPane();
		
		bp_main.setCenter(g_controller.getView());
		bp_main.setTop(new Text("TestArea"));
		
		Scene scene = new Scene(bp_main);
		
		primaryStage.setTitle("TestArea");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(@NotNull String[] args) {
		launch(args);
	}
}
