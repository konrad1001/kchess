package Pieces;

import java.util.ArrayList;
import java.util.List;

import Board.Board;
import Board.Move;
import Board.Tile;
import util.BoardTools;
import util.Colour;

public class Pawn extends Piece{

    private final static int[] POSSIBLE_MOVES_VECTORS = {7, 8, 9, 16};

    public Pawn(int coordinates, Colour colour) {
        super(coordinates, colour);
    }

    @Override
    public List<Move> calculateLegalMoves(Board board) {
        
        LegalMoves = new ArrayList<>();

        for (final int currentVector : POSSIBLE_MOVES_VECTORS) {
            int destination = this.coordinates + this.getColor().getDirection() * currentVector;

            if (!BoardTools.isValid(destination)) {
                continue;
            }

            final Tile targetTile = board.getTile(destination);
            
            //regular one space pawn move
            if (currentVector == 8 && !board.getTile(destination).isOccupied()) {
                LegalMoves.add(new Move(board, this, destination, false));  
            
            //double move when first move is true and pawn is in starting row for respective colour.
            } else if (currentVector == 16 && this.isFirstMove() && 
                        ((BoardTools.SECOND_ROW[this.coordinates] && this.colour == Colour.BLACK) ||
                        (BoardTools.SEVENTH_ROW[this.coordinates] && this.colour == Colour.WHITE))) {
                final int coordinateInfront = this.coordinates + (this.colour.getDirection() * 8);
                if (!board.getTile(coordinateInfront).isOccupied() && 
                    targetTile.isOccupied()) {
                        LegalMoves.add(new Move(board, this, destination, false));
                    }

            } else if (currentVector == 7 &&
                        !((BoardTools.EIGTH_COLUMN[this.coordinates] && this.colour == Colour.WHITE) ||
                         (BoardTools.FIRST_COLUMN[this.coordinates] && this.colour == Colour.BLACK)) ) {
                if (targetTile.isOccupied()) {
                    final Piece targetPiece = targetTile.getPiece();
                        if (targetPiece.getColor() != this.colour) {
                            //catch promotion
                            LegalMoves.add(new Move(board, this, targetPiece, destination, true));
                        }
                        break;
                }
            } else if (currentVector == 9 &&
                        !((BoardTools.FIRST_COLUMN[this.coordinates] && this.colour == Colour.WHITE) ||
                         (BoardTools.EIGTH_COLUMN[this.coordinates] && this.colour == Colour.BLACK)) ) {
                if (targetTile.isOccupied()) {
                    final Piece targetPiece = targetTile.getPiece();
                        if (targetPiece.getColor() != this.colour) {
                            //catch promotion
                            LegalMoves.add(new Move(board, this, targetPiece, destination, true));
                        }
                        break;
                    }
                }
        }

        return LegalMoves;
    }


    
}
