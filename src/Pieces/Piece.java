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
    public Colour getColor() {
        return colour;
    }
    protected boolean isFirstMove() {
        return isFirstMove;
    }

    public abstract List<Move> calculateLegalMoves(final Board board);

}
