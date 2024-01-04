package Board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.BoardTools;
import util.Colour;

import Pieces.*;
import Player.*;
import Player.ai.MiniMax;
import Player.ai.Random;


public class Board {

    private final List<Tile> tileBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;

    private final Player whitePlayer;
    private final Player blackPlayer;
    private final Player currentPlayer;

    private final Collection<Move> allLegalMoves;

    private final Pawn enPassantPawn;
    
    
    private Board(final Builder builder) {
        this.tileBoard = tileBoard(builder);
        this.whitePieces = getActivePieces(this.tileBoard, Colour.WHITE);
        this.blackPieces = getActivePieces(this.tileBoard, Colour.BLACK);
        this.enPassantPawn = builder.enPassantPawn;

        final Collection<Move> whiteLegalMoves = calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackLegalMoves = calculateLegalMoves(this.blackPieces);

        

        allLegalMoves = new ArrayList<>(whiteLegalMoves);
        allLegalMoves.addAll(blackLegalMoves);
        

     //   whitePlayer = new HumanPlayer(this, whiteLegalMoves, blackLegalMoves, Colour.WHITE);
        blackPlayer = new AIPlayer(this, whiteLegalMoves, blackLegalMoves, Colour.BLACK, new Random());
        whitePlayer = new AIPlayer(this, whiteLegalMoves, blackLegalMoves,  Colour.WHITE, new MiniMax());

        final Collection<Move> whiteCastleMoves = getPlayer(Colour.WHITE).getKingCastles();
        final Collection<Move> blackCastleMoves = getPlayer(Colour.BLACK).getKingCastles();

        allLegalMoves.addAll(whiteCastleMoves);
        allLegalMoves.addAll(blackCastleMoves);
        currentPlayer = builder.currentPlayer.choosePlayer(whitePlayer, blackPlayer);

        
        

        
    }

    public Piece getEnpassantPawn() {
        return enPassantPawn;
    }
    public boolean isGameOver() {
        return currentPlayer.isInCheckmate() || currentPlayer.isInStalemate();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        for (int i=0; i < BoardTools.NUM_TILES; i++) {
            Tile T = this.tileBoard.get(i);
            final String asString = T.toString();
            builder.append(asString);
            if ((i+1) % BoardTools.ROW_LENGTH == 0) {
                builder.append("\n");
            }
        }

        return builder.toString();

    }
    private Collection<Move> calculateLegalMoves(Collection<Piece> pieces) {
        
        final List<Move> legalMoves = new ArrayList<>();
       
        for (final Piece piece : pieces) {
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return Collections.unmodifiableList(legalMoves);
    }

    public Collection<Piece> getActivePieces(Colour colour) {
        if (colour == Colour.WHITE) {
            return whitePieces;
        } else {
            return blackPieces;
        }
    }
    private static Collection<Piece> getActivePieces(final List<Tile> tileBoard, final Colour colour) {
        
        final List<Piece> activePieces = new ArrayList<>();

        for (final Tile T : tileBoard) {
            if (T.isOccupied()) {
                final Piece piece = T.getPiece();
                if (piece.getColour() == colour) {
                    activePieces.add(piece);
                }
            }
        }
        
        return Collections.unmodifiableList(activePieces);
    }

    /**
     * Create and populate list of tiles. 
     * @param builder
     * @return
     */
    private List<Tile> tileBoard(Builder builder) {
        final List<Tile> tiles = new ArrayList<>(BoardTools.NUM_TILES);
        for (int i = 0; i < BoardTools.NUM_TILES; i++) {
            
            tiles.add(new Tile(i, builder.boardConfig.get(i)));
        }

        return Collections.unmodifiableList(tiles);
    }

    /**
     * Create and fill a board with standard piece positioning
     * @return initialised and placed board
     */
    public static Board create() {
        final Builder builder = new Builder();

        //set black
        builder.set(new Rook(0, Colour.BLACK));
        builder.set(new Knight(1, Colour.BLACK));
        builder.set(new Bishop(2, Colour.BLACK));
        builder.set(new Queen(3, Colour.BLACK));
        builder.set(new King(4, Colour.BLACK));
        builder.set(new Bishop(5, Colour.BLACK));
        builder.set(new Knight(6, Colour.BLACK));
        builder.set(new Rook(7, Colour.BLACK));
        for (int i=8; i<16; i++) { 
            builder.set(new Pawn(i, Colour.BLACK));
        }
        // set white
        builder.set(new Rook(56, Colour.WHITE));
        builder.set(new Knight(57, Colour.WHITE));
        builder.set(new Bishop(58, Colour.WHITE));
        builder.set(new Queen(59, Colour.WHITE));
        builder.set(new King(60, Colour.WHITE));
        builder.set(new Bishop(61, Colour.WHITE));
        builder.set(new Knight(62, Colour.WHITE));
        builder.set(new Rook(63, Colour.WHITE));
        for (int i=48; i<56; i++) { 
            builder.set(new Pawn(i, Colour.WHITE));
        }
        //initialise player

        builder.setCurrentPlayer(Colour.WHITE);

        return builder.build();
    }

    public static Board clear() {
        final Builder builder = new Builder();
        builder.setCurrentPlayer(Colour.WHITE);
        return builder.build();
    }

    public Tile getTile(int coordinate) { 
        return tileBoard.get(coordinate);
    }
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    public Piece getPiece(String name, Colour colour) {
        if (colour == Colour.WHITE) {
            for (Piece piece : whitePieces) {
                if (piece.toString() == name) {
                    return piece;
                }
            }
        } else {
            for (Piece piece : blackPieces) {
                if (piece.toString() == name) {
                    return piece;
                }
            }
        }
        System.out.println("No active piece found with name '" + name + "'");
        return null;
    }
    public Collection<Piece> getPieces(Colour colour) {
        if (colour == Colour.WHITE) {
            return whitePieces;
        } else {
            return blackPieces;
        }
    }
    public Collection<Move> getAllLegalMoves() {
        return allLegalMoves;
    }

    public Player getPlayer(Colour colour) {
        if (colour == Colour.WHITE) {
            return whitePlayer;
        } else {
            return blackPlayer;
        }
    }


    public static class Builder {
        Map<Integer, Piece> boardConfig;
        Colour currentPlayer;
        private Pawn enPassantPawn;


        public Builder() {
            this.boardConfig = new HashMap<>();
        }

        public Builder set(final Piece piece) {
            boardConfig.put(piece.getCoordinates(), piece);
            return this;
        }
        public Builder setCurrentPlayer(final Colour colour) {
            this.currentPlayer = colour;
            return this;
        }
    
        public Board build() {
            return new Board(this);
        }

        public void setEnPassantPawn(Pawn pawn) {
            this.enPassantPawn = pawn;
        }
    }


    


    
}


