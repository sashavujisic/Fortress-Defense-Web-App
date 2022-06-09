package ca.a4p2v2.api;
import java.util.Arrays;

/**
 * Wrapper class for the REST API to define object structures required by the front-end.
 * HINT: Create static factory methods (or constructors) which help create this object
 *       from the data stored in the model, or required by the model.
 *
 * The purpose of this class is to manage the user's selected coordinantes. It accepts input for the
 * row and column. The use of this class is mostly for the frontend making requests to post a move that requires
 * a userTurn, which gets the coordinates it needs from this class. I wanted to make an inner class to store these
 * coordinates, but the GameLogic is hardcoded to use an array of length two.
 */

public class ApiLocationWrapper {
    public int row;
    public int col;
    public int[] coords = new int[2];

    public ApiLocationWrapper (int inputRow, int inputCol) {
        this.row = inputRow;
        this.col = inputCol;
        coords[0] = row;
        coords[1] = col;
        System.out.println("Created location instance.");
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int[] getCoords() {
        System.out.println("Got coords: " + Arrays.toString(coords));
        return coords;
    }
}