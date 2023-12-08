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
import Board.MoveType;
import Board.Tile;
import Pieces.King;
import Pieces.Piece;
import Pieces.Piece.Name;
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
            // this.ourLegalMoves = whiteLegalMoves;
            // Collection<Move> kingCastles = calculateKingCastles(whiteLegalMoves, blackLegalMoves);
            this.ourLegalMoves = new ArrayList<>(whiteLegalMoves);
            ourLegalMoves.addAll(calculateKingCastles(whiteLegalMoves, blackLegalMoves));
            this.opponentLegalMoves = blackLegalMoves;
            this.isInCheck = !Player.calculateAttacks(king, blackLegalMoves).isEmpty();
        } else {
            this.isInCheck = !Player.calculateAttacks(king, whiteLegalMoves).isEmpty();
            this.opponentLegalMoves = whiteLegalMoves;
            this.ourLegalMoves = new ArrayList<>(blackLegalMoves);
            ourLegalMoves.addAll(calculateKingCastles(blackLegalMoves, whiteLegalMoves));
        } 
        
    }

    private static Collection<Move> calculateAttacks(final Piece attackedPiece, final Collection<Move> moves) {
        final List<Move> attackMoves = new ArrayList<>();
        for (final Move move: moves) {
            if (attackedPiece.getCoordinates() == move.getDestinationCoordinates()) {
                //if piece is attacked 
                attackMoves.add(move);
            }
        }
        return Collections.unmodifiableList(attackMoves);
    }
    private static Collection<Move> calculateAttacks(final Tile attackedTile, final Collection<Move> moves) {
        final List<Move> attackMoves = new ArrayList<>();
        for (final Move move: moves) {
            if (attackedTile.getCoordinates() == move.getDestinationCoordinates()) {
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

    public Collection<Move> calculateKingCastles(final Collection<Move> ourLegalMoves, final Collection<Move> opponentLegalMoves) {
        final List<Move> kingCastles = new ArrayList<>();

        if (king.isFirstMove() && !isInCheck()) {
            if (colour == Colour.WHITE) {
                //King side castle
                if(!board.getTile(61).isOccupied() && 
                    !board.getTile(62).isOccupied()) {
                    final Tile rookTile = this.board.getTile(63); //get unmoved rook tile
                    //ensure it's a rook
                    if(rookTile.isOccupied() && 
                    rookTile.getPiece().isFirstMove() && 
                    rookTile.getPiece().getName() == Name.ROOK) {
                        //ensure no tiles in the path are attacked.
                        if(calculateAttacks(board.getTile(61), opponentLegalMoves).isEmpty() &&
                        calculateAttacks(board.getTile(62), opponentLegalMoves).isEmpty()) {
                            kingCastles.add(new Move(board, 
                                    king, rookTile.getPiece(), 
                                    62, 61, 
                                    MoveType.CASTLE));
                        }
                        
                    }
                }
                //Queen side castle 
                if(!board.getTile(59).isOccupied() && 
                    !board.getTile(58).isOccupied() &&
                    !board.getTile(57).isOccupied()) {
                    final Tile rookTile = this.board.getTile(56); //get unmoved rook tile
                    if(rookTile.isOccupied() && 
                    rookTile.getPiece().isFirstMove() && 
                    rookTile.getPiece().getName() == Name.ROOK) {
                        //ensure no tiles in the path are attacked.
                        if(calculateAttacks(board.getTile(59), opponentLegalMoves).isEmpty() &&
                        calculateAttacks(board.getTile(58), opponentLegalMoves).isEmpty() &&
                        calculateAttacks(board.getTile(57), opponentLegalMoves).isEmpty()) {
                            kingCastles.add(new Move(board, 
                                    king, rookTile.getPiece(), 
                                    58, 59, 
                                    MoveType.CASTLE));
                        }
                        
                    }
                } else {
                //King side castle
                if(!board.getTile(5).isOccupied() && 
                    !board.getTile(6).isOccupied()) {
                    final Tile rookTile = this.board.getTile(7); //get unmoved rook tile
                    //ensure it's a rook
                    if(rookTile.isOccupied() && 
                    rookTile.getPiece().isFirstMove() && 
                    rookTile.getPiece().getName() == Name.ROOK) {
                        //ensure no tiles in the path are attacked.
                        if(calculateAttacks(board.getTile(5), opponentLegalMoves).isEmpty() &&
                        calculateAttacks(board.getTile(6), opponentLegalMoves).isEmpty()) {
                            kingCastles.add(new Move(board, 
                                    king, rookTile.getPiece(), 
                                    6, 5,
                                    MoveType.CASTLE));
                        }
                        
                    }
                }
                //Queen side castle 
                if(!board.getTile(1).isOccupied() && 
                    !board.getTile(2).isOccupied() &&
                    !board.getTile(3).isOccupied()) {
                    final Tile rookTile = this.board.getTile(0); //get unmoved rook tile
                    if(rookTile.isOccupied() && 
                    rookTile.getPiece().isFirstMove() && 
                    rookTile.getPiece().getName() == Name.ROOK) {
                        //ensure no tiles in the path are attacked.
                        if(calculateAttacks(board.getTile(1), opponentLegalMoves).isEmpty() &&
                        calculateAttacks(board.getTile(2), opponentLegalMoves).isEmpty() &&
                        calculateAttacks(board.getTile(3), opponentLegalMoves).isEmpty()) {
                            kingCastles.add(new Move(board, 
                                    king, rookTile.getPiece(), 
                                    2, 3, 
                                    MoveType.CASTLE));
                        }
                        }
                        
                    }
                }
            }
        }

        return Collections.unmodifiableList(kingCastles);
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
