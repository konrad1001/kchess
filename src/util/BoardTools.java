package util;

import Board.Tile;
import Pieces.Piece;

public class BoardTools {

    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(7);
    public static final boolean[] EIGTH_COLUMN = initColumn(8);

    public static final boolean[] FIRST_ROW = initRow(0);
    public static final boolean[] SECOND_ROW = initRow(1);
    public static final boolean[] THIRD_ROW = initRow(2);
    public static final boolean[] FOURTH_ROW = initRow(3);
    public static final boolean[] FIFTH_ROW = initRow(4);
    public static final boolean[] SIXTH_ROW = initRow(5);
    public static final boolean[] SEVENTH_ROW = initRow(6);
    public static final boolean[] EIGTH_ROW = initRow(7);

    public static final int NUM_TILES = 64;
    public static final int ROW_LENGTH = 8;

    public static boolean isValid(int coordinate) {
        return coordinate >= 0 && coordinate < NUM_TILES;
    }


    private static boolean[] initColumn(int N) {

        final boolean[] grid = new boolean[64];

        do {
            grid[N] = true;
            N += ROW_LENGTH;
        } while (N < NUM_TILES);

        return grid;
    }

    private static boolean[] initRow(int N) {
        int TileID = N * 8;

        final boolean[] grid = new boolean[64];

        do {
            grid[TileID] = true;
            TileID++;
        } while (TileID % ROW_LENGTH != 0);

        return grid;
    }


    public static String printTile(Tile tile) {
        if (tile.isOccupied()) {
            Piece piece = tile.getPiece();
            return (piece.getColour() == Colour.WHITE) ? tile.toString().toLowerCase() :
                    tile.toString();
        }
        return tile.toString();
    }

    
}
