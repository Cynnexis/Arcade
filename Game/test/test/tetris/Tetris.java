package test.tetris;

import fr.berger.enhancedlist.Point;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.fr.polytech.arcade.game.grid.GridController;
import main.fr.polytech.arcade.game.piece.Axis;
import main.fr.polytech.arcade.game.piece.Direction;
import main.fr.polytech.arcade.game.piece.Piece;
import main.fr.polytech.arcade.game.piece.PieceBuilder;
import main.fr.polytech.arcade.game.ui.GridHandler;
import main.fr.polytech.arcade.game.ui.GridView;

import java.util.Objects;

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
			public void onTileClicked(int x, int y) {
				System.out.println("Tetris.onTileClicked> (" + x + " ; " + y + ")");
				
				/*Piece p = g_controller.getGrid().get(4, 4);
				
				if (p != null) {
					p.rotate(Direction.WEST);
					p.setColor(Color.PURPLE);
				}*/
				
				Piece clickedPiece = g_controller.getGrid().get(x, y);
				g_controller.getGrid().setFocusedPiece(clickedPiece);
			}
		});
		
		//g_controller.getView().addEventFilter(KeyEvent.KEY_PRESSED,
		g_controller.getView().setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				Piece piece = g_controller.getGrid().getFocusedPiece();
				
				//System.out.println("Tetris.handle (key)> key : " + event.getCode().name() + "\tpiece : " + Objects.toString(piece));
				
				if (piece != null) {
					switch (event.getCode())
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
						case R:
							piece.rotateAnticlockwise90();
					}
				}
			}
		});
		g_controller.update();
		
		System.out.println("Tetris.start> r1 = " + r1 + " ; r2 = " + r2 + " ; r3 = " + r3);
		
		for (int i = 0; i < g_controller.getGrid().getPieces().size(); i++)
			System.out.println("Tetris.start> piece n°" + i + ":\n" + g_controller.getGrid().getPieces().get(i).toString());
		
		BorderPane bp_main = new BorderPane();
		
		bp_main.setCenter(g_controller.getView());
		bp_main.setTop(new Text("Test"));
		
		Scene scene = new Scene(bp_main);
		
		primaryStage.setTitle("Tetris");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
