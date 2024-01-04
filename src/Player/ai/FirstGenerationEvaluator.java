package Player.ai;

import Board.Board;
import Board.Move;
import Pieces.Piece;
import Pieces.Queen;
import Pieces.Piece.Name;
import Player.Player;
import util.Colour;

public class FirstGenerationEvaluator implements Evaluator { 
    
    private static final int CHECK_BONUS = 30;
    private static final int CHECK_MATE_BONUS = 10000;
    private static final int CASTLE_BONUS = 30;
    private static final int PAWN_VALUE = 100;
    private static final int ATTACK_MULTIPLIER = 30;
    private static int gameProgression;


    @Override
    public int evaluate(final Board board, int depth) {

        gameProgression = (32 - (board.getActivePieces(Colour.WHITE).size() + board.getActivePieces(Colour.BLACK).size()));
   //     System.out.println(gameProgression);
        
        int score = score(board, board.getPlayer(Colour.WHITE), depth) - 
            score(board, board.getPlayer(Colour.BLACK), depth);

  //      System.out.println("Score: " + score);
        return score;
    }

    private int score(Board board, Player player, int depth) {

        return pieceValue(player) + mobilityRatio(player) + kingSafety(player)
         + checkmate(player, depth) + check(player) + castle(player)
         + attacks(player);
    }

    private int pieceValue(Player player) {
        int pieceValueScore = 0;
        for (final Piece piece : player.getActivePieces()) {
            pieceValueScore += piece.getValue();
            if (gameProgression < 2) {
                pieceValueScore += piece.onFavouriteTile() * piece.getValue() / 10;
            }
                
        }
        return pieceValueScore;
    }

    private int mobilityRatio(Player player) {
        return (int) Math.floor(player.getLegalMoves().size() * 10 / player.getOpponent().getLegalMoves().size());
    }

    private static int attacks(Player player) {
        int attackScore = 0;
        for (final Move move : player.getLegalMoves()) {
            if (move.isAttack()) {
                Piece movedPiece = move.getMovedPiece();
                Piece attackedPiece = move.getInteractedPiece();
                if (movedPiece.getValue() <= attackedPiece.getValue()) {
                    attackScore ++;
                    if(attackedPiece.getName() == Name.QUEEN) {
                        attackScore += 2;
                    }
                }
            }
        }
        return attackScore * ATTACK_MULTIPLIER;
    }

    private int kingSafety(Player player) {
        int kingSafetyScore = 0;
        kingSafetyScore = 50 * (player.isCastled() ? 50 : 1) * 1 / (player.getKing().getLegalMoves().size() + 1);
        return kingSafetyScore;
    }

    private static int checkmate(Player player, int depth) {
        return player.getOpponent().isInCheckmate() ? CHECK_MATE_BONUS * depthBonus(depth) : 0;
    }

    private static int depthBonus(int depth) {
        return 1;
    }

    private static int check(Player player) {
        return player.getOpponent().isInCheck() ? CHECK_BONUS : 0;
    }

    private static int castle(Player player) {
        return player.isCastled() ? CASTLE_BONUS : 0;
    }


    

}
