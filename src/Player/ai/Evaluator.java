package Player.ai;

import Board.Board;

public interface Evaluator {

    int evaluate(Board board, int depth);
    
}
 