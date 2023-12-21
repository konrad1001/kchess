package Player.ai;

import Board.Board;
import Board.Move;
import Pieces.Piece;
import Pieces.Queen;
import Player.Player;
import util.Colour;

public class FirstGenerationEvaluator implements Evaluator { 
    
    private static final int CHECK_BONUS = 50;
    private static final int CHECK_MATE_BONUS = 10000;
    private static final int CASTLE_BONUS = 30;
    private static final int PAWN_VALUE = 100;
    private static int gameProgression;


    @Override
    public int evaluate(final Board board, int depth) {

        gameProgression = (32 - (board.getActivePieces(Colour.WHITE).size() + board.getActivePieces(Colour.BLACK).size()));
        System.out.println(gameProgression);
        
        int score = score(board, board.getPlayer(Colour.WHITE), depth) - 
            score(board, board.getPlayer(Colour.BLACK), depth);

        System.out.println("Score: " + score);
        return score;
    }

    private int score(Board board, Player player, int depth) {
        System.out.println("Player: " + player.colour());
        System.out.println("Piece value: " + pieceValue(player));
        System.out.println("Mobility: " + mobility(player));
        System.out.println("King safety: " + kingSafety(player));
        System.out.println("Checkmate: " + checkmate(player, depth));
        System.out.println("Check: " + check(player));
        System.out.println("Castle: " + castle(player));
        System.out.println("Queen safety: " + queenSafety(player));
        System.out.println(board);
        return pieceValue(player) + mobility(player) + kingSafety(player)
         + checkmate(player, depth) + check(player) + castle(player) + queenSafety(player);
    }

    private int pieceValue(Player player) {
        int pieceValueScore = 0;
        for (final Piece piece : player.getActivePieces()) {
            pieceValueScore += piece.getValue();
        }
        return pieceValueScore;
    }

    private int mobility(Player player) {
        int mobilityScore = 0;
        mobilityScore += player.getLegalMoves().size() * 5;
        if (player.getQueen() != null) { 
            mobilityScore += player.getQueen().getLegalMoves().size() * 10;
        }
        for (final Piece piece : player.getActivePieces()) {
            switch (piece.getName()) {
                case PAWN:
                    mobilityScore += piece.getLegalMoves().size() * 5;
                    break;
                case KNIGHT:
                    mobilityScore += piece.getLegalMoves().size() * 20;
                    break;
                case BISHOP:
                    mobilityScore += piece.getLegalMoves().size() * 20;
                    break;
                case ROOK:
                    mobilityScore += piece.getLegalMoves().size() * 10 * (gameProgression / 12);
                    break;
                case QUEEN:
                    mobilityScore += piece.getLegalMoves().size() * 10 * (gameProgression / 12);
                    break;
                case KING:
                    mobilityScore += piece.getLegalMoves().size() * 5;
                    break;
                default:
                    break;
            }
        }
       
        return mobilityScore;
    }

    private int kingSafety(Player player) {
        int kingSafetyScore = 0;
        kingSafetyScore = 50 * (player.isCastled() ? 50 : 1) * 1 / (player.getKing().getLegalMoves().size() + 1);
        
        System.out.println("King safety score: " + kingSafetyScore);
        return kingSafetyScore;
    }

    private static int checkmate(Player player, int depth) {
        return player.getOpponent().isInCheckmate() ? CHECK_MATE_BONUS * depthBonus(depth) : 0;
    }

    private static int depthBonus(int depth) {
        return depth == 0 ? 1 : PAWN_VALUE * depth;
    }

    private static int check(Player player) {
        return player.getOpponent().isInCheck() ? CHECK_BONUS : 0;
    }

    private static int castle(Player player) {
        return player.isCastled() ? CASTLE_BONUS : 0;
    }

    //unsafe queen penalty
    private static int queenSafety(Player player) {
        Queen queen = player.getQueen();
        int queenSafetyScore = 0;
        if (queen != null) {
            for (Move move : player.getOpponent().getLegalMoves()) {
                if (move.getDestinationCoordinate() == queen.getCoordinates()) {
                    
                    queenSafetyScore -= queen.getValue();
                    System.out.println("Queen is in danger! " + move);
                }
            }
            
        }
        return queenSafetyScore;
    }

    

}
