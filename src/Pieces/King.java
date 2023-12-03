package Pieces;

import java.util.ArrayList;
import java.util.List;

import Board.Board;
import Board.Move;
import Board.Tile;
import util.BoardTools;
import util.Colour;

public class King extends Piece{

    private final static int[] POSSIBLE_MOVES_VECTORS = {-9, -8, -7, -1, 1, 7, 8, 9};

    public King(int coordinates, Colour colour) {
        super(coordinates, colour);
    }

    @Override
    public List<Move> calculateLegalMoves(Board board) {
        
        LegalMoves = new ArrayList<>();

        for (final int currentVector : POSSIBLE_MOVES_VECTORS) {
            int destination = this.coordinates + currentVector;

            if (!BoardTools.isValid(destination) || 
                    isFirstColumnExclusion(this.coordinates, currentVector) ||
                    isEigthColumnExclusion(this.coordinates, currentVector)) {
                continue;
            }
            final Tile targetTile = board.getTile(destination);

            if (targetTile.isOccupied() == false) {
                LegalMoves.add(new Move(board, this, destination, false));
            } else {
                final Piece targetPiece = targetTile.getPiece();
                if (targetPiece.getColor() != this.colour) {
                    LegalMoves.add(new Move(board, this, targetPiece, destination, true));

                }
            }
        }
        return LegalMoves;
    }

    private static boolean isFirstColumnExclusion(int current, int offset) {
        return BoardTools.FIRST_COLUMN[current] && ((offset == -9) || (offset == -1) 
                || (offset == 7));
    }
    private static boolean isEigthColumnExclusion(int current, int offset) {
        return BoardTools.EIGTH_COLUMN[current] && ((offset == -7) || (offset == 1) 
                || (offset == 9));
    }   
}
