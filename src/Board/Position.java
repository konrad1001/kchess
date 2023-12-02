package Board;

public class Position {
    int x, y;
    int pos;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        
        this.pos = y * 8 + x;
    }
}
