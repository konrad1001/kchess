package Pieces;

import java.util.ArrayList;
import java.util.List;

import Board.Board;
import Board.Move;
import Board.MoveType;
import Board.Tile;
import util.BoardTools;
import util.Colour;

public class King extends Piece{

    private final static int[] POSSIBLE_MOVES_VECTORS = {-9, -8, -7, -1, 1, 7, 8, 9};

    public King(int coordinates, Colour colour) {
        super(coordinates, colour, Name.KING, true, true);
    }
    public King(int coordinates, Colour colour, boolean isFirstMove) {
        super(coordinates, colour, Name.KING, isFirstMove, true);
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
                LegalMoves.add(new Move(board, this, destination, MoveType.STANDARD));
            } else {
                final Piece targetPiece = targetTile.getPiece();
                if (targetPiece.getColour() != this.colour) {
                    LegalMoves.add(new Move(board, this, targetPiece, destination, MoveType.ATTACK));

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
    @Override
    public String toString() {
        return Name.KING.toString();
    }
    @Override
    public Piece movePiece(Move move) {
        return new King(move.getDestinationCoordinate(), move.getMoveColour(), false);
    }
    @Override
    public int getValue() {
        return 10000;
    }
    @Override
    public int onFavouriteTile() {
        //prefers castling
        if (colour == Colour.WHITE) {
            if (this.coordinates == 62 || this.coordinates == 58) {
                return 10;
            } else {
                return 1;
            }
        } else {
            if (this.coordinates == 6 || this.coordinates == 2) {
                return 10;
            } else {
                return 1;
            }
        }
    }
}
