package Pieces;

import java.util.List;

import Board.Board;
import Board.Move;
import util.Colour;

public abstract class Piece {
    
    public int coordinates;

    protected Colour colour;

    protected List<Move> LegalMoves;

    protected boolean isFirstMove;

    public Piece(int coordinates, Colour colour) {
        this.coordinates = coordinates;
        this.colour = colour;
        this.isFirstMove = true;
    }
    public Colour getColour() {
        return colour;
    }
    protected boolean isFirstMove() {
        return isFirstMove;
    }
    
    public int getCoordinates() {
        return coordinates;
    }

    public String getPieceDetails() {
        return colour + " " + this + " at " + String.valueOf(coordinates);
    }

    public abstract List<Move> calculateLegalMoves(final Board board);

    public enum Name {
        PAWN("P"),
        ROOK("R"),
        KNIGHT("N"),
        BISHOP("B"),
        QUEEN("Q"),
        KING("K");

        private String Name;

        Name(final String pieceName) {
            this.Name = pieceName;
        }

        @Override
        public String toString() {
            return this.Name;
        }

    }

}
