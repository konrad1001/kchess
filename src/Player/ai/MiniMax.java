package Player.ai;

import Board.Board;
import Board.Move;
import Board.MoveStatus;
import Board.MoveTransition;
import util.Colour;

public class MiniMax implements MoveStrategy{

    private final Evaluator evaluator;

    public MiniMax() {
        this.evaluator = new FirstGenerationEvaluator();
    }

    /**
     * Executes the MiniMax algorithm to find the best move for the current player.
     * 
     * @param board The current game board.
     * @param depth The depth of the search tree.
     * @return The best move found by the MiniMax algorithm.
     */
    @Override
    public Move execute(Board board, int depth) {
        final long startTime = System.currentTimeMillis();
        Move bestMove = null;
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;

        System.out.println("Running Minimax at depth: " + depth);

        for (final Move move : board.getCurrentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus() == MoveStatus.DONE) {
                currentValue = miniMax(moveTransition.getBoard(), depth, false, lowestSeenValue, highestSeenValue);
                if (board.getCurrentPlayer().colour() == Colour.WHITE && currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                    bestMove = move;
                } else if (board.getCurrentPlayer().colour() == Colour.BLACK && currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
        }
        final long endTime = System.currentTimeMillis();
        final long executionTime = endTime- startTime;
        System.out.println("Execution time: " + executionTime + "ms");
        
            
        
        return bestMove;
    }

    /**
     * Recursive helper method for the MiniMax algorithm.
     * Uses basic alpha-beta pruning. 
     *
     * @param board The current game board.
     * @param depth The depth of the search tree.
     * @param isMaximizingPlayer Indicates whether the current player is maximizing or minimizing.
     * @param alpha The alpha value for alpha-beta pruning.
     * @param beta The beta value for alpha-beta pruning.
     * @return The evaluation score for the current game state.
     */
    private int miniMax(Board board, int depth, boolean isMaximizingPlayer, int alpha, int beta) {
        if (depth == 0 || board.isGameOver()) {
            return evaluator.evaluate(board, depth);
        }
        if (isMaximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for(Move move : board.getCurrentPlayer().getLegalMoves()) {
                MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
                if (moveTransition.getMoveStatus() == MoveStatus.DONE) {
                    int eval = miniMax(board, depth - 1, false, alpha, beta);
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, maxEval);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for(Move move : board.getCurrentPlayer().getLegalMoves()) {
                MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
                if (moveTransition.getMoveStatus() == MoveStatus.DONE) {
                    int eval = miniMax(board, depth - 1, true, alpha, beta);
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, minEval);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return minEval;
        }
    }
     
}
