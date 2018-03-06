package main.fr.polytech.arcade.game.piece;

import com.sun.corba.se.impl.encoding.CDROutputObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import fr.berger.enhancedlist.Couple;
import fr.berger.enhancedlist.Point;
import main.fr.polytech.arcade.game.grid.Grid;
import main.fr.polytech.arcade.game.grid.Square;
import main.fr.polytech.arcade.game.grid.SquareType;

@Deprecated
public class PieceController {
	
	public @NotNull Piece rotate(@NotNull Piece piece) {
		Piece rotatedPiece = new Piece();
		
		rotatedPiece.getShape().setNbColumns(piece.getShape().getNbRows());
		rotatedPiece.getShape().setNbRows(piece.getShape().getNbColumns());
		
		for (int i = 0; i < piece.getShape().getNbColumns(); i++)
			for (int j = 0; j < piece.getShape().getNbRows(); j++)
				rotatedPiece.getShape().set(j - i, i, piece.getShape().get(i, j));
		
		return piece;
	}
	
	public @NotNull Piece invert(@NotNull Piece piece, @NotNull Axis axis) {
		Piece invertedPiece = new Piece();
		
		return piece;
	}
	
	/*
	public synchronized @Nullable Couple<Grid, Piece> move(@NotNull Grid grid, @NotNull Piece piece, @NotNull Point destination) {
		if (grid == null || piece == null)
			throw new NullPointerException();
		
		if (!grid.checkIfPieceCanBePlaced(piece, destination))
			return null;
		
		int index = grid.getIdFromPiece(piece);
		
		// TODO: Check 'dest' values, and test if piece.getPosition() + dest is out of bounds
		
		// If not such piece exist in the list, then we cannot move a piece which is not in the grid
		if (index == -1)
			return null;
		
		// Search the first square which hold the piece
		Point pos = grid.getPiecePosition(index);
		
		if (pos == null)
			return null;
		
		// Now, free the squares
		for (int i = pos.getX(); i < piece.getShape().getNbColumns() + pos.getX(); i++) {
			for (int j = pos.getY(); j < piece.getShape().getNbRows() + pos.getY(); j++) {
				if (piece.getShape().get(i - pos.getX(), j - pos.getY())) {
					grid.get(i, j).setType(SquareType.EMPTY);
					grid.get(i, j).setIndexToPiece(-1);
				}
			}
		}
		
		// Finally, place the piece to the destination
		for (int i = destination.getX(); i < piece.getShape().getNbColumns() + destination.getX(); i++) {
			for (int j = destination.getY(); j < piece.getShape().getNbRows() + destination.getY(); j++) {
				if (piece.getShape().get(i - destination.getX(), j - destination.getY())) {
					grid.get(i, j).setType(SquareType.HALF);
					grid.get(i, j).setIndexToPiece(index);
				}
			}
		}
		
		grid.get(destination.getX(), destination.getY()).setType(SquareType.HALF);
		
		return new Couple<>(grid, piece);
	}
	public @NotNull Couple<Grid, Piece> move(@NotNull Grid grid, @NotNull Piece piece, int x, int y) {
		return move(grid, piece, new Point(x, y));
	}
	public @NotNull Couple<Grid, Piece> move(@NotNull Grid grid, @NotNull Piece piece, @NotNull Direction direction) {
		if (direction == null)
			throw new NullPointerException();
		
		// Search the first square which hold the piece
		Point pos = grid.getPiecePosition(piece);
		
		if (pos == null)
			return null;
		
		switch (direction)
		{
			case NORTH:
				return move(grid, piece, pos.getX(), pos.getY() - 1);
			case EAST:
				return move(grid, piece, pos.getX() + 1, pos.getY());
			case SOUTH:
				return move(grid, piece, pos.getX(), pos.getY() + 1);
			default:
				return move(grid, piece, pos.getX() - 1, pos.getY());
		}
	}*/
}
