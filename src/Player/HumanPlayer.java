package Player;

import java.util.Collection;

import Board.Board;
import Board.Move;
import util.Colour;

public class HumanPlayer extends Player {

    public HumanPlayer(Board board, Collection<Move> whiteLegalMoves, Collection<Move> blackLegalMoves, Colour colour) {
        super(board, whiteLegalMoves, blackLegalMoves, colour, true);
        
    }
    
}
