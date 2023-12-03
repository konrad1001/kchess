package Board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import util.BoardTools;
import util.Colour;

import Pieces.*;

public class Board {

    private final List<Tile> tileBoard;
    
    private Board(Builder builder) {
        this.tileBoard = tileBoard(builder);
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
        //set white
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

    public Tile getTile(int coordinate) { return null;}

    public static class Builder {
        Map<Integer, Piece> boardConfig;
        Colour currentPlayer;

        public Builder() {}

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
    }
}


