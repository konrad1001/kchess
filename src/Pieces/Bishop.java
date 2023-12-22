package Pieces;

import java.util.ArrayList;
import java.util.List;

import Board.Board;
import Board.Move;
import Board.MoveType;
import Board.Tile;
import util.BoardTools;
import util.Colour;

public class Bishop extends Piece{

    private int[] POSSIBLE_MOVES_VECTORS = {-9, -7, 7, 9};

    public Bishop(int coordinates, Colour colour) {
        super(coordinates, colour, Name.BISHOP, true, true);
    }
    public Bishop(int coordinates, Colour colour, boolean isFirstMove) {
        super(coordinates, colour, Name.BISHOP, isFirstMove, true);
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
        return BoardTools.FIRST_COLUMN[current] && ((offset == -9) || (offset == 7));
    }
    private static boolean isEigthColumnExclusion(int current, int offset) {
        return BoardTools.EIGTH_COLUMN[current] && ((offset == 9) || (offset == -7));
    }

    @Override
    public String toString() {
        return Name.BISHOP.toString();
    }

    @Override
    public Piece movePiece(Move move) {
        return new Bishop(move.getDestinationCoordinate(), move.getMoveColour(), false);
    }
    @Override
    public int getValue() {
        return 300;
    }
    @Override
    public int onFavouriteTile() {
        if (colour == Colour.WHITE) {
            //strong corner position
            if (coordinates == 49 || coordinates == 54 || coordinates == 34 || coordinates == 37 || coordinates == 30 || coordinates == 25) {
                return 10;
            } else {
                return 1;
            } 
        } else {
            if (coordinates == 9 || coordinates == 14 || coordinates == 29 || coordinates == 38 || coordinates == 26 || coordinates == 33) {
                return 10;
            } else {
                return 1;
            } 
        }
    }
}
