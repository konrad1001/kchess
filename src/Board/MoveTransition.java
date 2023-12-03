package Board;

public class MoveTransition {

    private final Board transition;
    private final Move move;
    private final MoveStatus moveStatus;
    
    public MoveTransition(Board transition, Move move, MoveStatus moveStatus) {
        this.transition = transition;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    

}
