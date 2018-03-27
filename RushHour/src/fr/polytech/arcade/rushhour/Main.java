package fr.polytech.arcade.rushhour;

import com.sun.javaws.jnl.ResourcesDesc;
import fr.berger.enhancedlist.Point;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.fr.polytech.arcade.game.GameState;
import main.fr.polytech.arcade.game.grid.GridController;
import main.fr.polytech.arcade.game.piece.Piece;
import main.fr.polytech.arcade.game.piece.PieceBuilder;
import main.fr.polytech.arcade.game.ui.GridHandler;
import org.jetbrains.annotations.NotNull;

public class Main extends Application {
 
	@NotNull
	private GridController grid;
	private BorderPane bp_main;
	private Button b_play;
	private Button b_exit;
	private Text info;
	
	private Piece mainPiece;
	
	private AnimationTimer aTimer;
	private long lastUpdate;
	private GameState state;
	private int numberOfMove;
	
	private int mainPieceIndex = 0;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		state = GameState.INITIALIZING;
		grid = new GridController(6, 6);
		
		bp_main = new BorderPane();
		b_play = new Button("Play");
		b_exit = new Button("Rush Quit");
		info = new Text("");
		
		grid.addGridHandler(new GridHandler() {
			@Override
			public void onTileClicked(int x, int y, @NotNull MouseButton mouseButton) {
				if (grid != null)
					grid.getGrid().setFocusedPiece(grid.getGrid().get(x, y));
			}
			
			@Override
			public void onKeyPressed(@NotNull KeyCode keyCode) {
				if (grid != null && state == GameState.PLAYING) {
					Piece piece = grid.getGrid().getFocusedPiece();
				
					if (piece != null) {
						switch (keyCode) {
							case UP:
							case Z:
								if (GeneratePiece.isVertical(piece))
									if (grid.getGrid().move(piece, piece.getPosition().getX(), piece.getPosition().getY() - 1))
										numberOfMove++;
								break;
							case LEFT:
							case Q:
								if (GeneratePiece.isHorizontal(piece))
									if (grid.getGrid().move(piece, piece.getPosition().getX() - 1, piece.getPosition().getY()))
										numberOfMove++;
								break;
							case DOWN:
							case S:
								if (GeneratePiece.isVertical(piece))
									if (grid.getGrid().move(piece, piece.getPosition().getX(), piece.getPosition().getY() + 1))
										numberOfMove++;
								break;
							case RIGHT:
							case D:
								if (GeneratePiece.isHorizontal(piece))
									if (grid.getGrid().move(piece, piece.getPosition().getX() + 1, piece.getPosition().getY()))
										numberOfMove++;
								break;
						}
					}
				}
			}
		});
		grid.getView().setLineStroke(0.0);
		
		b_play.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				newGame();
			}
		});
		
		b_exit.setTooltip(new Tooltip("I rush quit!\n#LéaChemoul"));
		b_exit.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				primaryStage.close();
			}
		});
		
		aTimer = new AnimationTimer() {
			@Override
			public void handle(long nowNano) {
				if (nowNano - lastUpdate >= 100000000) {
					lastUpdate = nowNano;
					info.setText("Number of Move(s): " + numberOfMove);
					
					if (state == GameState.PLAYING && mainPiece.getPosition().getX() >= 4 && mainPiece.getPosition().getY() == 2) {
						state = GameState.WIN;
						info.setText(info.getText() + " | You win!");
						Alert alert = new Alert(Alert.AlertType.INFORMATION, "Congratulation! You win!", ButtonType.OK);
						alert.show();
					}
				}
			}
		};
		
		bp_main.setPadding(new Insets(10.0));
		bp_main.setTop(new HBox(b_play, b_exit));
		bp_main.setCenter(grid.getView());
		bp_main.setRight(new Text("\n\n\n\n\n\n → Exit"));
		bp_main.setBottom(info);
		
		newGame();
		
		primaryStage.setTitle("Rush Hour");
		primaryStage.setScene(new Scene(bp_main));
		primaryStage.show();
	}
	
	private void newGame() {
		state = GameState.INITIALIZING;
		lastUpdate = 0;
		numberOfMove = 0;
		grid.getGrid().getPieces().clear();
		
		// The first piece to create will be the main
		mainPiece = new PieceBuilder(GeneratePiece.getHorizontalPiece())
				.setPosition(0, 2)
				.setColor(GeneratePiece.getPrimaryColor())
				.createPiece();
		
		grid.getGrid().add(mainPiece);
		
		grid.getGrid().add(GeneratePiece.getVerticalPiece(0, 0));
		grid.getGrid().add(GeneratePiece.getVerticalPiece(1, 0));
		grid.getGrid().add(GeneratePiece.getHorizontalPiece(2, 0));
		grid.getGrid().add(GeneratePiece.getHorizontalPiece(2, 1));
		grid.getGrid().add(GeneratePiece.getVerticalPiece(2, 2));
		grid.getGrid().add(GeneratePiece.getVerticalPiece(2, 4));
		grid.getGrid().add(GeneratePiece.getVerticalPiece(3, new Point(3, 2)));
		grid.getGrid().add(GeneratePiece.getVerticalPiece(4, 0));
		grid.getGrid().add(GeneratePiece.getVerticalPiece(4, 3));
		
		state = GameState.PLAYING;
		aTimer.start();
	}
	
	@Override
	public void stop() throws Exception {
		super.stop();
		if (aTimer != null)
			aTimer.stop();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
