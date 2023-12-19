package Pieces;

import java.util.ArrayList;
import java.util.List;

import Board.Board;
import Board.Move;
import Board.MoveType;
import Board.Tile;
import util.BoardTools;
import util.Colour;

public class Pawn extends Piece{

    private final static int[] POSSIBLE_MOVES_VECTORS = {7, 8, 9, 16};

    public Pawn(int coordinates, Colour colour) {
        super(coordinates, colour, Name.PAWN, true);
    }
    public Pawn(int coordinates, Colour colour, boolean isFirstMove) {
        super(coordinates, colour, Name.PAWN, isFirstMove);
    }

    @Override
    public List<Move> calculateLegalMoves(Board board) {
        
        LegalMoves = new ArrayList<>();

        for (final int currentVector : POSSIBLE_MOVES_VECTORS) {

            int destination = this.coordinates + this.getColour().getDirection() * currentVector;

            if (!BoardTools.isValid(destination)) {
                continue;
            }

    
            final Tile targetTile = board.getTile(destination);
            
            //regular one space pawn move
            if (currentVector == 8 && !board.getTile(destination).isOccupied()) {
                //first check if promotional.
                if(BoardTools.isPromotionalTile(destination, colour)) {
                    LegalMoves.add(new Move(board, this, destination, MoveType.PAWN_PROMOTION));
                } else {
                    LegalMoves.add(new Move(board, this, destination, MoveType.PAWN_MOVE));
                }
                
            //double move when first move is true and pawn is in starting row for respective colour.
            } else if (currentVector == 16 && this.isFirstMove() && 
                        ((BoardTools.SECOND_ROW[this.coordinates] && this.colour == Colour.BLACK) ||
                        (BoardTools.SEVENTH_ROW[this.coordinates] && this.colour == Colour.WHITE))) {
                final int coordinateInfront = this.coordinates + (this.colour.getDirection() * 8);
                if (!board.getTile(coordinateInfront).isOccupied() && 
                    !targetTile.isOccupied()) {
                        LegalMoves.add(new Move(board, this, destination, MoveType.PAWN_JUMP));
                    }
            //pawn attack
            } else if (currentVector == 7 &&
                        !((BoardTools.EIGTH_COLUMN[this.coordinates] && this.colour == Colour.WHITE) ||
                         (BoardTools.FIRST_COLUMN[this.coordinates] && this.colour == Colour.BLACK)) ) {
                if (targetTile.isOccupied()) {
                    final Piece targetPiece = targetTile.getPiece();
                        if (targetPiece.getColour() != this.colour) {
                            //catch promotion
                            if(BoardTools.isPromotionalTile(destination, colour)) {
                                LegalMoves.add(new Move(board, this, targetPiece, destination, MoveType.PAWN_PROMOTION_ATTACK));
                            } else {
                                LegalMoves.add(new Move(board, this, targetPiece, destination, MoveType.PAWN_ATTACK));
                            }
                            
                        }
                        
                } else if (board.getEnpassantPawn() != null) {                           
                    if (board.getEnpassantPawn().getCoordinates() == (this.coordinates + -1 * (this.colour.getDirection()))) {
                        final Piece pieceOnCandidate = board.getEnpassantPawn();
                        if (this.colour != pieceOnCandidate.getColour()) {
                            LegalMoves.add(new Move(board, this, pieceOnCandidate, destination, MoveType.PAWN_ENPASSANT_ATTACK));
                        }
                    }
                }
            //pawn attack
            } else if (currentVector == 9 &&
                        !((BoardTools.FIRST_COLUMN[this.coordinates] && this.colour == Colour.WHITE) ||
                         (BoardTools.EIGTH_COLUMN[this.coordinates] && this.colour == Colour.BLACK)) ) {
                if (targetTile.isOccupied()) {
                    final Piece targetPiece = targetTile.getPiece();
                        if (targetPiece.getColour() != this.colour) {
                            //catch promotion
                            if(BoardTools.isPromotionalTile(destination, colour)) {
                                LegalMoves.add(new Move(board, this, targetPiece, destination, MoveType.PAWN_PROMOTION_ATTACK));
                            } else {
                                LegalMoves.add(new Move(board, this, targetPiece, destination, MoveType.PAWN_ATTACK));
                            }
                        }
                        
                    } else if (board.getEnpassantPawn() != null) {                           
                    if (board.getEnpassantPawn().getCoordinates() == (this.coordinates - -1 * (this.colour.getDirection()))) {
                        final Piece pieceOnCandidate = board.getEnpassantPawn();
                        if (this.colour != pieceOnCandidate.getColour()) {
                            LegalMoves.add(new Move(board, this, pieceOnCandidate, destination, MoveType.PAWN_ENPASSANT_ATTACK));
                        }
                    }
                }
            }
        }

        return LegalMoves;
    }

    @Override
    public String toString() {
        return Name.PAWN.toString();
    }
    @Override
    public Piece movePiece(Move move) {
        return new Pawn(move.getDestinationCoordinates(), move.getMoveColour());
    }

    @Override
    public int getValue() {
        return 1;
    }
    
}
