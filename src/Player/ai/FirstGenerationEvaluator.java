package Player.ai;

import Board.Board;
import Player.Player;
import util.Colour;

public class FirstGenerationEvaluator implements Evaluator {    

    @Override
    public int evaluate(final Board board, int depth) {
        
        
        return score(board, board.getPlayer(Colour.WHITE), depth) - 
            score(board, board.getPlayer(Colour.BLACK), depth);
    }

    private int score(Board board, Player player, int depth) {
        return 0;
    }

}
