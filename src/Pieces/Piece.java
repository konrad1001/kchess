package Pieces;

import java.awt.Color;

public abstract class Piece {
    
    public int x, y;
    private Color colour;

    public Piece(Color colour) {
        this.colour = colour;
    }
    public Color getColor() {
        return colour;
    }

}
