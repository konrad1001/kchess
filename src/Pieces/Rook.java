package Pieces;

import java.util.ArrayList;
import java.util.List;

import Board.Board;
import Board.Move;
import Board.MoveType;
import Board.Tile;
import util.BoardTools;
import util.Colour;

public class Rook extends Piece{

    private int[] POSSIBLE_MOVES_VECTORS = {8, -8, 1, -1};

    public Rook(int coordinates, Colour colour) {
        super(coordinates, colour, Name.ROOK, true);
    }
    public Rook(int coordinates, Colour colour, boolean isFirstMove) {
        super(coordinates, colour, Name.ROOK, isFirstMove);
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
                if (BoardTools.isValid(destination)) {
                    
                    final Tile targetTile = board.getTile(destination);
                    if (targetTile.isOccupied() == false) {
                        LegalMoves.add(new Move(board, this, destination, MoveType.STANDARD));
                    } else {
                        final Piece targetPiece = targetTile.getPiece();
                        if (targetPiece.getColour() != this.colour) {
                            LegalMoves.add(new Move(board, this, targetPiece, destination, MoveType.ATTACK));
                        }
                        break;
                    }                    
                }                
            }
        }

        return LegalMoves;
    }
    private static boolean isFirstColumnExclusion(int current, int offset) {
        return BoardTools.FIRST_COLUMN[current] && (offset == -1);
    }
    private static boolean isEigthColumnExclusion(int current, int offset) {
        return BoardTools.EIGTH_COLUMN[current] && (offset == 1);
    }
    @Override
    public String toString() {
        return Name.ROOK.toString();
    }
    @Override
    public Piece movePiece(Move move) {
        return new Rook(move.getDestinationCoordinates(), move.getMoveColour(), false);
    }

    @Override
    public int getValue() {
        return 500;
    }
}
