package Board;

import Pieces.Piece;

public class Move {
    
    private final Board board;  //this board
    private final Piece movedPiece; //the piece to be moved
    private final int destination; //moving to this coordinate
    private final boolean isCapturing; //are we capturing?
    private final Piece attackedPiece;

    public Move() {
        this.board = null;
        this.movedPiece = null;
        this.attackedPiece = null;
        this.destination = 0;
        this.isCapturing = false;
    }
    public Move(final Board board, final Piece movedPiece,
        final int destination, final boolean isCapturing) {
            this.board = board;
            this.movedPiece = movedPiece;
            this.destination = destination;
            this.isCapturing = isCapturing;
            this.attackedPiece = null;
        }
    public Move(final Board board, final Piece movedPiece,
        final Piece attackedPiece,
        final int destination, final boolean isCapturing) {
            this.board = board;
            this.movedPiece = movedPiece;
            this.attackedPiece = attackedPiece;
            this.destination = destination;
            this.isCapturing = isCapturing;
        }
}
