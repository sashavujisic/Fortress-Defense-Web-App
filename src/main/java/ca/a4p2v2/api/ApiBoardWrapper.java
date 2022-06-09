package ca.a4p2v2.api;
import static ca.a4p2v2.api.ApiGameWrapper.*;
import static ca.a4p2v2.api.ApiGameWrapper.models;

/**
 * Wrapper class for the REST API to define object structures required by the front-end.
 * HINT: Create static factory methods (or constructors) which help create this object
 *       from the data stored in the model, or required by the model.
 *
 *       The purpose of this class is to manage the board, taking requests from the controller via gameNumber to
 *       determine its dimensions, and then taking the backend cell states and giving them an
 *       appropriate icon. The board is updated every turn by assessing the cell states by checking the
 *       cheat value given by the front end, and then displaying the board in accordance to the backend.
 */
public class ApiBoardWrapper {
    public int dimension;
    public int boardWidth;
    public int boardHeight;
    public int gameNumber;
    public boolean cheat;
    public String[][] cellStates;

    public ApiBoardWrapper(int gameNumber, int inputDimension) {
        setModel(gameNumber);
        setCheat(false);
        setDimension(inputDimension);
        this.gameNumber = gameNumber;
        setBoardHeight(dimension);
        setBoardWidth(dimension);
        cellStates = model.getUIGrid();
        System.out.println("Created game board instance.");
    }

    public void setCheat(boolean inputCheat) {
        this.cheat = inputCheat;
    }

    public void updateBoard() {
        if (getCheat()){
            setModel(gameNumber);
            this.cellStates = models.get(gameNumber).getCheatGrid();
        }
        else {
            setModel(gameNumber);
            this.cellStates = models.get(gameNumber).getUIGrid();
        }
        System.out.println("Updated game board with cheat status: " + getCheat());
    }

    public boolean getCheat() {
        return cheat;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public void setBoardWidth(int boardWidth) {
        this.boardWidth = boardWidth;
    }

    public void setBoardHeight(int boardHeight) {
        this.boardHeight = boardHeight;
    }
}
