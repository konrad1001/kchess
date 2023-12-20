import Board.Board;
import gui.ChessBoardGUI;
import util.Colour;

public class kchess {
    
    public static void main(String[] args) {
        Board board = Board.create();

        ChessBoardGUI table = new ChessBoardGUI(board);
    }

}
