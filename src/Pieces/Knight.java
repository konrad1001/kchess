package Pieces;


import java.util.ArrayList;
import java.util.List;

import Board.Board;
import Board.Move;
import Board.MoveType;
import Board.Tile;
import util.BoardTools;
import util.Colour;

public class Knight extends Piece{
    private final static int[] POSSIBLE_MOVES = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(int coordinates, Colour colour) {
        super(coordinates, colour, Name.KNIGHT, true, true);
    }
    public Knight(int coordinates, Colour colour, boolean isFirstMove) {
        super(coordinates, colour, Name.KNIGHT, isFirstMove, true);
    }

    @Override
    public List<Move> calculateLegalMoves(final Board board) {

        LegalMoves = new ArrayList<>();

        for (final int current : POSSIBLE_MOVES) {
            
            int destination = this.coordinates + current;

            
            
            if (BoardTools.isValid(destination)) {
                final Tile targetTile = board.getTile(destination);

                //skip if within any columns that result in out of bounds move.
                if (isFirstColumnExclusion(this.coordinates, current) ||
                isSecondColumnExclusion(this.coordinates, current) ||
                isSeventhColumnExclusion(this.coordinates, current) ||
                isEigthColumnExclusion(this.coordinates, current)) {
                    continue;
                }
                
                if (targetTile.isOccupied() == false) {
                    LegalMoves.add(new Move(board, this, destination, MoveType.STANDARD));
                } else {
                    final Piece targetPiece = targetTile.getPiece();
                    if (targetPiece.getColour() != this.colour) {
                        LegalMoves.add(new Move(board, this, targetPiece, destination, MoveType.ATTACK));

                    }
                }
            }
            
        }

        return LegalMoves;
    }

    private static boolean isFirstColumnExclusion(int current, int offset) {
        return BoardTools.FIRST_COLUMN[current] && ((offset == -17) || (offset == -10) 
                || (offset == 6) || (offset == 15));
    }
    private static boolean isSecondColumnExclusion(int current, int offset) {
        return BoardTools.SECOND_COLUMN[current] && ((offset == -10) || (offset == 6));
    }
    private static boolean isSeventhColumnExclusion(int current, int offset) {
        return BoardTools.SEVENTH_COLUMN[current] && ((offset == -6) || (offset == 10));
    }  
    private static boolean isEigthColumnExclusion(int current, int offset) {
        return BoardTools.EIGTH_COLUMN[current] && ((offset == 10) || (offset == -6) 
                || (offset == 17) || (offset == -15));
    }
    @Override
    public String toString() {
        return Name.KNIGHT.toString();
    }
    @Override
    public Piece movePiece(Move move) {
        return new Knight(move.getDestinationCoordinate(), move.getMoveColour(), false);
    }
    
    @Override
    public int getValue() {
        return 300;
    }

    @Override
    public int onFavouriteTile() {
        //strong centre position
        if (colour == Colour.WHITE) {
            if (this.coordinates == 42 || this.coordinates == 45) {
                return 10;    
            } else {
                return 1;
            }
        } else {
            if (this.coordinates == 18 || this.coordinates == 21) {
                return 10;
            } else {
                return 1;
            }
        }
    }
}


