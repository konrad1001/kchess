package Player;

import java.util.Collection;

import Board.Board;
import Board.Move;
import Player.ai.MiniMax;
import Player.ai.MoveStrategy;
import util.Colour;

public class AIPlayer extends Player {

    final MoveStrategy brain;
    final int SEARCH_DEPTH = 8;

    public AIPlayer(Board board, Collection<Move> whiteLegalMoves, Collection<Move> blackLegalMoves, Colour colour, MoveStrategy brain) {
        super(board, whiteLegalMoves, blackLegalMoves, colour, false);
        this.brain = brain;
    }

    @Override
    public Move promptMove() {
        
        
        return brain.execute(this.board, SEARCH_DEPTH);
        
    }
    
}
