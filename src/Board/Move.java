package Board;

import Board.Board.Builder;
import Pieces.Pawn;
import Pieces.Piece;
import Pieces.Queen;
import Pieces.Rook;
import util.Colour;

public class Move {
    
    private final Board board;  //this board
    private final Piece movedPiece; //the piece to be moved
    private final int destination; //moving to this coordinate
    private final Piece interactedPiece; //if another piece is involved
    private final int interactedPieceDestination;
    private final Colour colour; //what colour is making this move.
    private final MoveType moveType;
    private final Boolean isFirstMove;

    

    public Move() {
        this.board = null;
        this.movedPiece = null;
        this.interactedPiece = null;
        this.destination = 0;
        this.interactedPieceDestination = 0;
        this.colour = null;
        this.moveType = MoveType.NULL;
        this.isFirstMove = null;
    }

    public Move(final Board board, final Piece movedPiece,
        final int destination, final MoveType moveType) {
            this.board = board;
            this.movedPiece = movedPiece;
            this.destination = destination;
            this.interactedPieceDestination = 0;
            this.moveType = moveType;
            this.interactedPiece = null;
            this.colour = movedPiece.getColour();
            this.isFirstMove = movedPiece.isFirstMove();
        }
    public Move(final Board board, final Piece movedPiece,
        final Piece interactedPiece,
        final int destination, final MoveType moveType) {
            this.board = board;
            this.movedPiece = movedPiece;
            this.interactedPiece = interactedPiece;
            this.interactedPieceDestination = 0;
            this.destination = destination;
            this.colour = movedPiece.getColour();
            this.moveType = moveType;
            this.isFirstMove = movedPiece.isFirstMove();
        }
    public Move(final Board board, final Piece movedPiece,
        final Piece interactedPiece, final int destination, 
        final int interactedPieceDestination, final MoveType moveType) {
            this.board = board;
            this.movedPiece = movedPiece;
            this.interactedPiece = interactedPiece;
            this.interactedPieceDestination = interactedPieceDestination;
            this.destination = destination;
            this.colour = movedPiece.getColour();
            this.moveType = moveType;
            this.isFirstMove = movedPiece.isFirstMove();
        }
    
        @Override
        public String toString() {
            Colour player = movedPiece.getColour();
            int position = movedPiece.getCoordinates();

            return player + " moves " + movedPiece.getFullName() + " from " +
                     String.valueOf(position) + " to " + String.valueOf(destination) + " type " + moveType;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;

            result = prime * result + this.destination;
            result = prime * result + this.movedPiece.hashCode();
            result = prime * result + this.movedPiece.getCoordinates();
            if (moveType == MoveType.ATTACK) {
                result = result + interactedPiece.hashCode();
            }
            return result;
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Move)) {
                return false;
            }
            final Move otherMove = (Move) other;
            return getCurrentCoordinate() == otherMove.getCurrentCoordinate() &&
                    getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
                    getMovedPiece().equals(getMovedPiece()) && getType() == otherMove.getType();

        }

        public int getDestinationCoordinate() {
            return destination;
        }
        public int getCurrentCoordinate() {
            return movedPiece.getCoordinates();
        }
        public Colour getMoveColour() {
            return colour;
        }

        public MoveType getType() {
            return moveType;
        }
        public Piece getinteractedPiece() {
            return interactedPiece;
        }
        public Piece getMovedPiece() {
            return movedPiece;
        }


        public boolean isAttack() {
            return moveType == MoveType.ATTACK || moveType == MoveType.PAWN_ATTACK || moveType == MoveType.PAWN_ENPASSANT_ATTACK; 
        }

        public Piece getInteractedPiece() {
            return interactedPiece;
        }

        public Board execute() {
            //executing a move returns a new board
            final Builder builder = new Builder();
            switch (moveType) {
                case STANDARD:
                    for (final Piece piece : board.getPieces(colour)) {
                        if(!movedPiece.equals(piece)) {
                            builder.set(piece);
                        }
                    }
                    //set all of their unmoved.
                    for (final Piece piece : board.getPieces(colour.Opposite())) {                       
                        builder.set(piece);
                    }
                    builder.set(movedPiece.movePiece(this));
                    break;
                case ATTACK:
                    for (final Piece piece : board.getPieces(colour)) {
                        if(!this.movedPiece.equals(piece)) {
                            builder.set(piece);
                        }
                    }
                    //set all of their unmoved unless it is interacted.
                    for (final Piece piece : board.getPieces(colour.Opposite())) {
                        if(!this.interactedPiece.equals(piece)) {
                            builder.set(piece);
                        }
                    }
                    builder.set(movedPiece.movePiece(this));
                    break;
                    
                case PAWN_ATTACK:
                    for (final Piece piece : board.getPieces(colour)) {
                        if(!this.movedPiece.equals(piece)) {
                            builder.set(piece);
                        }
                    }
                    //set all of their unmoved unless it is interacted.
                    for (final Piece piece : board.getPieces(colour.Opposite())) {
                        if(!this.interactedPiece.equals(piece)) {
                            builder.set(piece);
                        }
                    }
                    builder.set(movedPiece.movePiece(this));
                    break;
                case PAWN_ENPASSANT_ATTACK:
                    for (final Piece piece : board.getPieces(colour)) {
                        if(!this.movedPiece.equals(piece)) {
                            builder.set(piece);
                        }
                    }
                    //set all of their unmoved unless it is interacted.
                    for (final Piece piece : board.getPieces(colour.Opposite())) {
                        if(!this.interactedPiece.equals(piece)) {
                            builder.set(piece);
                        }
                    }
                    builder.set(movedPiece.movePiece(this));
                    break;
                case PAWN_JUMP:
                    for (final Piece piece : board.getPieces(colour)) {
                        if(!this.movedPiece.equals(piece)) {
                            builder.set(piece);
                        }
                    }
                    for (final Piece piece : board.getPieces(colour.Opposite())) {                       
                        builder.set(piece);
                    }
                    final Pawn pawn = (Pawn) this.movedPiece.movePiece(this);
                    builder.set(pawn);
                    builder.setEnPassantPawn(pawn);
                    break;
                case PAWN_MOVE:
                    for (final Piece piece : board.getPieces(colour)) {
                        if(!movedPiece.equals(piece)) {
                            builder.set(piece);
                        }
                    }
                    //set all of their unmoved.
                    for (final Piece piece : board.getPieces(colour.Opposite())) {                       
                        builder.set(piece);
                    }
                    builder.set(movedPiece.movePiece(this));
                    break;
                case CASTLE: 
                    final Rook castleRook = (Rook) interactedPiece;
                    for (final Piece piece : board.getPieces(colour)) {
                        if(!movedPiece.equals(piece) && !castleRook.equals(piece)) {
                            builder.set(piece);
                        }
                    }
                    //set all of their unmoved.
                    for (final Piece piece : board.getPieces(colour.Opposite())) {                       
                        builder.set(piece);
                    }
                    builder.set(movedPiece.movePiece(this));
                    builder.set(new Rook(interactedPieceDestination, colour, false));
                    break;
                case PAWN_PROMOTION:
                    final Queen promotedQueen = new Queen(destination, colour);
                    for (final Piece piece : board.getPieces(colour)) {
                        if(!movedPiece.equals(piece)) {
                            builder.set(piece);
                        }
                    }
                    //set all of their unmoved.
                    for (final Piece piece : board.getPieces(colour.Opposite())) {                       
                        builder.set(piece);
                    }
                    builder.set(promotedQueen);
                    break;
                case PAWN_PROMOTION_ATTACK:
                    final Queen promotedQueenAttack = new Queen(destination, colour);
                    for (final Piece piece : board.getPieces(colour)) {
                        if(!movedPiece.equals(piece)) {
                            builder.set(piece);
                        }
                    }
                    //set all of their unmoved.
                    for (final Piece piece : board.getPieces(colour.Opposite())) {                       
                        if (!interactedPiece.equals(piece)) {
                            builder.set(piece);
                        }
                    }
                    builder.set(promotedQueenAttack);
                    break;

                default:
                    break;
                
            }
                       
            //switch teams
            builder.setCurrentPlayer(colour.Opposite());
            //build board
            return builder.build();
        }

        public static class Constructor {
            public static Move createMove(final Board board, 
                                            final int currentCoordinate,
                                            final int destinationCoordinate) {
                
                for (Move move : board.getAllLegalMoves()) {
                    
                    if (move.getCurrentCoordinate() == currentCoordinate &&
                        move.getDestinationCoordinate() == destinationCoordinate) {
                            return move;
                        }
        
                }
                return null;
            }
        }

}

