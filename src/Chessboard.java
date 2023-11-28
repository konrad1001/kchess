import Pieces.Piece;

public class Chessboard {
    //Board array made up of pieces
    private Piece[][] board;

    public Chessboard() {
        setUp();
    }
    private void setUp() {
        board = new Piece[8][8];

        for (int row=0; row<8; row++) {
            for (int col=0; col<8; col++) {
                board[row][col] = null;
            }
        }
    }
}


