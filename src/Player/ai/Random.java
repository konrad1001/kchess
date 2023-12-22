package Player.ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Board.Board;
import Board.Move;
import Board.MoveTransition;

public class Random implements MoveStrategy {

    @Override
    public Move execute(Board board, int depth) {
        
        Collection<Move> legalMoves = board.getCurrentPlayer().getLegalMoves();
        
        List<Move> moveList = new ArrayList<>(legalMoves);
        List<Move> safeMoves = new ArrayList<>();
        for (Move move : moveList) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getBoard().getCurrentPlayer().isInCheck()) {
                continue;
            } else {
                safeMoves.add(move);
            }
        }
    //    System.out.println(moveList);
        System.out.println(safeMoves);
        
        int randomIndex = (int) (Math.random() * safeMoves.size());
        
        return safeMoves.get(randomIndex);
    }
    
}
