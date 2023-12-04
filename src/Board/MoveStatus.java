package Board;

public enum MoveStatus {
    ILLEGAL_MOVE {
        @Override
        boolean isDone() {
            return false;
        }
    },
    DONE {
        @Override
        boolean isDone() {
            return false;
        }
    }, LEAVES_PLAYER_IN_CHECK;

    public abstract boolean isDone();

}
