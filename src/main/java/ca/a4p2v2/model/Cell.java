/** The Cell class is used to model the status of every cell individually on the Gameboard.
 * It manages whether the cell is a tank, hit, and what its coordinates on the Gameboard are.
 * It also keeps track of its parent Cell, which is the Cell that precedes it during tank generation.
 * All functions are getters and setters.
 */

package ca.a4p2v2.model;

public class Cell {
    //Cell
    private boolean isTank;
    private boolean isHit;
    private final int x;
    private final int y;
    private Cell parent;

    //there are 5 tanks, and we need to decide which tank the cell
    // is for the cheat part to
    // print the right letter
    private int whichTank;
    // Cell Constructor, sets the coordinates and booleans
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        isTank = false;
        isHit = false;
        //-1 in beginning since we haven't decided if it's a tank
        whichTank = -1;
    }
    protected void setNotTank(){
        isTank = false;
    }
    protected void setWhichTank(int tankNum){
        whichTank = tankNum;
    }

    protected int getWhichTank(){
        return whichTank;

    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    protected boolean getIsTank() {
        return isTank;
    }

    protected boolean getIsHit() {
        return isHit;
    }

    // + setTank():void
    protected void setTank() {
        isTank = true;
    }

    // +gotHit(object:cell):void
    protected void setHit() {
        isHit = true;
    }

    public Cell getParent() {
        return parent;
    }
    public void setParent(Cell cell){
        parent = cell;
    }
}
