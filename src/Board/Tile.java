package Board;

import Pieces.Piece;
import util.Colour;

public class Tile {

    private final int coordinates;
    

    private final Piece occupier;
    private final Colour colour;

    public Colour getColour() {
        return colour;
    }
    public int getCoordinates() {
        return coordinates;
    }
    /**
     * Constructor with no occupier.
     * @param coordinates
     */
    public Tile(int coordinates, Piece occupier) {
        this.coordinates = coordinates; 
        this.occupier = occupier;
        if (occupier != null) {
            this.colour = occupier.getColour();
        } else {
            this.colour = null;
        }
        
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
        return occupier != null;
    }
    public Piece getPiece() {
        return occupier;
    }

    @Override 
    public String toString() {
        if (isOccupied()) {
            return (occupier.getColour() == Colour.WHITE) ? occupier.toString().toLowerCase() :
            occupier.toString();
        } else {
            return "-";
        }

    }
}