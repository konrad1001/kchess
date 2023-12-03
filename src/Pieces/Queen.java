package Pieces;

import java.util.ArrayList;
import java.util.List;

import Board.Board;
import Board.Move;
import Board.Tile;
import util.BoardTools;
import util.Colour;

public class Queen extends Piece{

    private int[] POSSIBLE_MOVES_VECTORS = {8, -8, 1, -1, -9, -7, 7, 9};

    public Queen(int coordinates, Colour colour) {
        super(coordinates, colour);
    }
    
    @Override
    public List<Move> calculateLegalMoves(final Board board) {

        LegalMoves = new ArrayList<>();

        for (int currentVector : POSSIBLE_MOVES_VECTORS) {
            int destination = this.coordinates;
            while (BoardTools.isValid(destination)) {

                //if edge cases
                if (isFirstColumnExclusion(destination, currentVector) ||
                    isEigthColumnExclusion(destination, currentVector)) {
                        break;
                    }

                destination += currentVector;
                final Tile targetTile = board.getTile(destination);

                if (BoardTools.isValid(destination)) {
                    if (targetTile.isOccupied() == false) {
                        LegalMoves.add(new Move(board, this, destination, false));
                    } else {
                        final Piece targetPiece = targetTile.getPiece();
                        if (targetPiece.getColor() != this.colour) {
                            LegalMoves.add(new Move(board, this, targetPiece, destination, true));
                        }
                        break;
                    }                    
                }                
            }
        }

        return LegalMoves;
    }
    private static boolean isFirstColumnExclusion(int current, int offset) {
        return BoardTools.FIRST_COLUMN[current] && ((offset == -1) || (offset == -9) || (offset == 7));
    }
    private static boolean isEigthColumnExclusion(int current, int offset) {
        return BoardTools.EIGTH_COLUMN[current] && ((offset == 1) || (offset == -9) || (offset == 7));
    }
}
