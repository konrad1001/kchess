package Board;

import Pieces.Piece;
import util.Colour;

public class Tile {

    private int coordinates;
    private Piece occupier;
    private final Colour colour;


    /**
     * Constructor with no occupier.
     * @param coordinates
     */
    public Tile(int coordinates, Colour colour) {
        this.coordinates = coordinates; 
        this.colour = colour;
        occupier = null;
    }
    /**
     * Constructor with occupier. 
     * @param coordinates
     * @param piece
     */
    public Tile(int coordinates, Colour colour, Piece piece) {
        this.coordinates = coordinates;
        this.colour = colour;
        occupier = piece;
    }

    public boolean isOccupied() {
        return false;
    }
    public Piece getPiece() {
        return occupier;
    }
}