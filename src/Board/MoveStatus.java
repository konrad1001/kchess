package Board;

public enum MoveStatus {
    ILLEGAL_MOVE {
        @Override
        public boolean isDone() {
            return false;
        }
    },
    DONE {
        @Override
        public boolean isDone() {
            return false;
        }
    }, LEAVES_PLAYER_IN_CHECK {
        @Override
        public boolean isDone() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'isDone'");
        }
    };

    public abstract boolean isDone();

}
