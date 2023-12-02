package util;

public class BoardTools {

    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(7);
    public static final boolean[] EIGTH_COLUMN = initColumn(8);

    public static final int NUM_TILES = 64;
    public static final int ROW_LENGTH = 8;


    private static boolean[] initColumn(int N) {

        final boolean[] grid = new boolean[64];

        // while (N < 64) {
        //     grid[N] = true;
        //     N += 8;
        // }
        do {
            grid[N] = true;
            N += ROW_LENGTH;
        } while (N < NUM_TILES);

        return grid;
    }
    
}
