package fr.polytech.arcade.rushhour;

import com.sun.javaws.jnl.ResourcesDesc;
import fr.berger.enhancedlist.Point;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import main.fr.polytech.arcade.game.grid.GridController;
import main.fr.polytech.arcade.game.piece.Piece;
import main.fr.polytech.arcade.game.piece.PieceBuilder;
import main.fr.polytech.arcade.game.ui.GridHandler;
import org.jetbrains.annotations.NotNull;

public class Main extends Application {
 
	@NotNull
	private GridController grid;
	
	private int mainPieceIndex = 0;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		grid = new GridController(6, 6);
		
		grid.addGridHandler(new GridHandler() {
			@Override
			public void onTileClicked(int x, int y, @NotNull MouseButton mouseButton) {
				if (grid != null)
					grid.getGrid().setFocusedPiece(grid.getGrid().get(x, y));
			}
		
			@Override
			public void onKeyPressed(@NotNull KeyCode keyCode) {
				if (grid != null) {
					Piece piece = grid.getGrid().getFocusedPiece();
				
					if (piece != null) {
						switch (keyCode) {
							case UP:
							case Z:
								if (GeneratePiece.isVertical(piece))
									grid.getGrid().move(piece, piece.getPosition().getX(), piece.getPosition().getY() - 1);
								break;
							case LEFT:
							case Q:
								if (GeneratePiece.isHorizontal(piece))
									grid.getGrid().move(piece, piece.getPosition().getX() - 1, piece.getPosition().getY());
								break;
							case DOWN:
							case S:
								if (GeneratePiece.isVertical(piece))
									grid.getGrid().move(piece, piece.getPosition().getX(), piece.getPosition().getY() + 1);
								break;
							case RIGHT:
							case D:
								if (GeneratePiece.isHorizontal(piece))
									grid.getGrid().move(piece, piece.getPosition().getX() + 1, piece.getPosition().getY());
								break;
						}
					}
				}
			}
		});
		
		// The first piece to create will be the main
		grid.getGrid().add(new PieceBuilder(GeneratePiece.getHorizontalPiece())
				.setPosition(0, 2)
				.setColor(GeneratePiece.getPrimaryColor())
				.createPiece());
		
		grid.getGrid().add(GeneratePiece.getVerticalPiece(0, 0));
		grid.getGrid().add(GeneratePiece.getVerticalPiece(1, 0));
		grid.getGrid().add(GeneratePiece.getHorizontalPiece(2, 0));
		grid.getGrid().add(GeneratePiece.getHorizontalPiece(2, 1));
		grid.getGrid().add(GeneratePiece.getVerticalPiece(2, 2));
		grid.getGrid().add(GeneratePiece.getVerticalPiece(2, 4));
		grid.getGrid().add(GeneratePiece.getVerticalPiece(3, new Point(3, 2)));
		
		primaryStage.setTitle("Rush Hour");
		primaryStage.setScene(new Scene(grid.getView()));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
