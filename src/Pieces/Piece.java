package Pieces;

import java.util.Collection;
import java.util.List;

import Board.Board;
import Board.Move;
import util.Colour;

public abstract class Piece {
    
    public int coordinates;

    protected Colour colour;

    protected List<Move> LegalMoves;

    protected boolean isFirstMove;

    private final int myHashCode;

    final Name name;

    public Piece(int coordinates, Colour colour, Name name, final Boolean isFirstMove) {
        this.coordinates = coordinates;
        this.colour = colour;
        this.isFirstMove = true;
        this.name = name;
        this.isFirstMove = isFirstMove;
        this.myHashCode = calculateHash();
    }
    private int calculateHash() {
        int result = name.hashCode();
        result = 31 * result + colour.hashCode();
        result = 31 * result + coordinates;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }
    public Colour getColour() {
        return colour;
    }
    public boolean isFirstMove() {
        return isFirstMove;
    }

    @Override
    public boolean equals(final Object other) {
        if(this == other) {
            return true;
        }
        if(!(other instanceof Piece)) {
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return coordinates == otherPiece.getCoordinates() && name == otherPiece.getName() &&
                colour == otherPiece.getColour() && isFirstMove == otherPiece.isFirstMove();
    }

    @Override 
    public int hashCode() {
        return myHashCode;
    }


    public abstract Piece movePiece(Move move);

    public abstract int getValue();
    
    public int getCoordinates() {
        return coordinates;
    }

    public String getPieceDetails() {
        return colour + " " + this + " at " + String.valueOf(coordinates) + " is first move " + isFirstMove;
    }
    public Name getName() {
        return name;
    }
    public String getFullName() {
        return name.getFullName();
    }

    public abstract List<Move> calculateLegalMoves(final Board board);

    public List<Move> getLegalMoves() {
        return LegalMoves;
    }
    public void addLegalMoves(Collection<Move> moves) {
        
        LegalMoves.addAll(moves);
    }

    public enum Name {
        PAWN("P"),
        ROOK("R"),
        KNIGHT("N"),
        BISHOP("B"),
        QUEEN("Q"),
        KING("K");

        private String Name;
        

        public String getFullName() {
            String FullName;
            switch (Name) {
                case "P": FullName = "Pawn"; break;
                case "R" : FullName = "Rook"; break;
                case "N" : FullName = "Knight"; break;
                case "B" : FullName = "Bishop"; break;
                case "Q" : FullName = "Queen"; break;
                case "K" : FullName = "King"; break;
                default : FullName = "Null";
            }
            return FullName;
        }


        Name(final String pieceName) {
            this.Name = pieceName;
            
        }
        

        @Override
        public String toString() {
            return this.Name;
        }

    }

}
