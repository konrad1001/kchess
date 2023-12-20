package Player.ai;

import Board.Board;
import Board.Move;

public interface MoveStrategy {
    
    Move execute(Board board, int depth);
    
}