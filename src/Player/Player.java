package Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.management.RuntimeErrorException;

import Board.Board;
import Board.Move;
import Board.MoveStatus;
import Board.MoveTransition;
import Pieces.King;
import Pieces.Piece;
import util.Colour;

public class Player {

    private final Board board;
    private final Collection<Move> ourLegalMoves;
    private final Collection<Move> opponentLegalMoves;
    private final Colour colour;
    private final King king;
    private final boolean isInCheck;

    public Player(Board board, Collection<Move> whiteLegalMoves,
                    Collection<Move> blackLegalMoves, Colour colour) {
        this.board = board;

        this.colour = colour;
        this.king = (King) board.getPiece("K", colour);
        if (king == null) {

            throw new RuntimeException("Invalid board! No " + colour + " king.");
        }
        if (colour == Colour.WHITE) {
            this.ourLegalMoves = whiteLegalMoves;
            this.opponentLegalMoves = blackLegalMoves;
            this.isInCheck = !Player.calculateAttacks(king, blackLegalMoves).isEmpty();
        } else {
            this.isInCheck = !Player.calculateAttacks(king, whiteLegalMoves).isEmpty();
            this.opponentLegalMoves = whiteLegalMoves;
            this.ourLegalMoves = blackLegalMoves;
        } 
        
    }

    private static Collection<Move> calculateAttacks(Piece attackedPiece, Collection<Move> moves) {
        final List<Move> attackMoves = new ArrayList<>();
        for (final Move move: moves) {
            if (attackedPiece.getCoordinates() == move.getDestinationCoordinates()) {
                //if piece is attacked 
                attackMoves.add(move);
            }
        }
        return Collections.unmodifiableList(attackMoves);
    }

    public Player getOpponent() {
        if (colour == Colour.WHITE) {
            return board.getPlayer(Colour.BLACK);
        } else {
            return board.getPlayer(Colour.WHITE);
        }
    }

    public King getKing() {
        return king;
    }

    public Collection<Move> getLegalMoves() {
        return ourLegalMoves;
    }

    public boolean isMoveLegal(final Move move) {
        return ourLegalMoves.contains(move);
        
    }
    public boolean isInCheck() {
        return isInCheck;
    }
    public boolean isInCheckmate() {
        return isInCheck && !hasEscapeMoves();
    }

    private boolean hasEscapeMoves() {
        for (final Move move : ourLegalMoves) {
            final MoveTransition transition = makeMove(move);
            if(transition.getMoveStatus().isDone()) {
                return true;
            }
        }
        return false;
    }

    public boolean isInStaleMate() {
        return !isInCheck && !hasEscapeMoves();
    }
    public boolean isCastled() {
        return false;
    }

    public MoveTransition makeMove(final Move move) {
        if (!isMoveLegal(move)) {
            //If move is illegal, return same board.
            return new MoveTransition(board, move, MoveStatus.ILLEGAL_MOVE);
        }

        //Execute move.
        final Board newBoard = move.execute();
        
        //Get our king in this new board state
        King k = newBoard.getCurrentPlayer().getOpponent().getKing();

        //Now check if the resulting board states leaves us in check. 
        final Collection<Move> attacksOnKing = Player.calculateAttacks(
                                        k, newBoard.getCurrentPlayer().getLegalMoves());
        if (!attacksOnKing.isEmpty()) {
            return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }

        return new MoveTransition(newBoard, move, MoveStatus.DONE);
    }

}
