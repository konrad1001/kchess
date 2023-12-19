package util;

import Board.Tile;
import Pieces.Piece;

public class BoardTools {

    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGTH_COLUMN = initColumn(7);

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
    public static final String[] ALGEBRAIC_MAP = initialiseAlgebraicMap();
  //  public static final Map<String, Integer> POSITION_TO_COORDINATE = null;

    public static boolean isValid(int coordinate) {
        return coordinate >= 0 && coordinate < NUM_TILES;
    }


    private static String[] initialiseAlgebraicMap() {
        String[] algerbriacMap = new String[NUM_TILES];
        for (int i = 0; i < NUM_TILES; i++) {
            algerbriacMap[i] = toAlgerbraic(i);
        }
        return algerbriacMap;
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

    public static Boolean isPromotionalTile(int coordinates, Colour colour) {
        return colour == Colour.WHITE ? FIRST_ROW[coordinates] : EIGTH_ROW[coordinates];
    }

    public static String toAlgerbraic(int coordinate) {
        int row = coordinate / ROW_LENGTH + 1;
        int column = coordinate % (ROW_LENGTH) + 1;
        
        String[] files = {"a", "b", "c", "d", "e", "f", "g", "h"};
        int rank = 8 - row + 1;

        return files[column - 1] + rank;
        
    }

    public static String printTile(Tile tile) {
        if (tile.isOccupied()) {
            Piece piece = tile.getPiece();
            return (piece.getColour() == Colour.WHITE) ? tile.toString().toLowerCase() :
                    tile.toString();
        }
        return tile.toString();
    }

    public static String getPiecePath(final Piece piece) {
        String colourPrefix = (piece.getColour() == Colour.WHITE) ? "w" : "b";
        return "graphics\\" + colourPrefix + "_" + piece.getFullName().toLowerCase() + ".png";
    }

    public static String getAlgerbraMapped(final int coordinate) {
        return ALGEBRAIC_MAP[coordinate];
    }
    
}
