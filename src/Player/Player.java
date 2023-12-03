package Player;

import java.util.Collection;

import javax.management.RuntimeErrorException;

import Board.Board;
import Board.Move;
import Board.MoveTransition;
import Pieces.King;
import util.Colour;

public class Player {

    private final Board board;
    private final Collection<Move> whiteLegalMoves;
    private final Collection<Move> blackLegalMoves;
    private final Colour colour;
    private final King king;

    public Player(Board board, Collection<Move> whiteLegalMoves,
                    Collection<Move> blackLegalMoves, Colour colour) {
        this.board = board;
        this.whiteLegalMoves = whiteLegalMoves;
        this.blackLegalMoves = blackLegalMoves;
        this.colour = colour;
        this.king = (King) board.getPiece("K", colour);
        if (king == null) {
            throw new RuntimeException("Invalid board! No " + colour + " king.");
        }
    }

    public MoveTransition makeMove(final Move move) {
        return null;
    }

    public Player getOpponent() {
        if (colour == Colour.WHITE) {
            return board.getPlayer(Colour.BLACK);
        } else {
            return board.getPlayer(Colour.WHITE);
        }
    }
    public boolean isMoveLegal(final Move move) {
        if (colour == Colour.WHITE) {
            return whiteLegalMoves.contains(move);
        } else {
            return blackLegalMoves.contains(move);
        } 
    }
    public boolean isInCheck() {
        return false;
    }
    public boolean isInCheckmate() {
        return false;
    }
    public boolean isInStaleMate() {
        return false;
    }
    public boolean isCastled() {
        return false;
    }

}
