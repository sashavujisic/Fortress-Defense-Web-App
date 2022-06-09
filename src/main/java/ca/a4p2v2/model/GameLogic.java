/** GameLogic class is responsible for processing data from the UI and making a decision afterwards.
 * Data included is the fortress health, the number of tanks, a 2D array for the UI, a 2D cheat UI array,
 * an array of tank identifiers, and a grid for game logic. The primary function of GameLogic is to generate
 * tanks and manage the user's turn.
 */

package ca.a4p2v2.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;

public class GameLogic {
    private int FortressHealth;
    private final int numOfTanks;
    protected String[][] UIGrid = new String[10][10];
    protected String[][] cheatGrid = new String[10][10];
    Grid logicGrid;

    private final String[] TankLetter = new String[10];
    List<Tank> tankArray = new ArrayList<>();



    public List<Tank> getTankArray() {
        return tankArray;
    }

    public GameLogic(int numOfTanks, Grid grid){
        setFortressHealth(2500);
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                UIGrid[i][j] = "fog";
                cheatGrid[i][j] = "field";
            }
        }
        this.numOfTanks = numOfTanks;
        logicGrid = grid;
        TankLetter[0] = "tank";
        TankLetter[1] = "tank";
        TankLetter[2] = "tank";
        TankLetter[3] = "tank";
        TankLetter[4] = "tank";
        TankLetter[5] = "tank";
        TankLetter[6] = "tank";
        TankLetter[7] = "tank";
        TankLetter[8] = "tank";
        TankLetter[9] = "tank";
        generateTanks();

    }

    private void updateGameboard(){
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                if(logicGrid.isCellTank(i,j)){
                    cheatGrid[i][j] = getTankChar(logicGrid.getCell(i,j));
                }
                if(logicGrid.isCellHit(i, j) && !logicGrid.isCellTank(i, j)){
                    cheatGrid[i][j] = "miss";
                    UIGrid[i][j] = "miss";
                }
                else if(logicGrid.isCellHit(i, j) && logicGrid.isCellTank(i, j)){
                    cheatGrid[i][j] = getTankChar(logicGrid.getCell(i,j));
                    cheatGrid[i][j] = "hit";
                    UIGrid[i][j] = "hit";
                }
            }
        }
    }
    private String getTankChar(Cell cell){
        return TankLetter[cell.getWhichTank()];
    }
    //Game Logic
    private ArrayList<Cell> getChildren(int x, int y){
        ArrayList<Cell> children = new ArrayList<>();
        if(x < 9 ){
            if(!logicGrid.getCell(x+1,y).getIsTank()){
                children.add(logicGrid.getCell(x+1,y));
            }
        }
        if( y < 9){
            if(!logicGrid.getCell(x,y+1).getIsTank()){
                children.add(logicGrid.getCell(x, y+1));
            }
        }
        if( x > 0){
            if( !logicGrid.isCellTank(x-1,y)){
                children.add(logicGrid.getCell(x-1, y));
            }
        }
        if(y > 0){
            if(!logicGrid.getCell(x,y-1).getIsTank()){
                children.add(logicGrid.getCell(x, y-1));
            }
        }
        return children;
    }

    public void generateTanks(){
        for(int i = 0; i < numOfTanks; i++){
            Random rand = new Random();
            Tank tank = new Tank();
            int x = rand.nextInt() & Integer.MAX_VALUE %10;
            int y = rand.nextInt() & Integer.MAX_VALUE %10;
            while (logicGrid.isCellTank(x, y)){
                x = rand.nextInt() & Integer.MAX_VALUE %10;
                y = rand.nextInt() & Integer.MAX_VALUE %10;

            }

            ArrayList<Cell> Children = getChildren(x, y);

            //generate random cell till the starting point has the least one child or neighbour
            while (Children.size() == 0) {
                x = abs(rand.nextInt()%10);
                y = abs(rand.nextInt()%10);
                Children = getChildren(x, y);
            }

            //make it a tank
            logicGrid.setTank(x,y);
            tank.addCell(logicGrid.getCell(x, y));
            logicGrid.getCell(x, y).setWhichTank(i);
            Cell current = addFrom(logicGrid.getCell(x, y), tank, i);

            while(tank.tankCells.size() < 5){
                //adding children from this place
                ArrayList<Cell> nextChildChildren = getChildren(current.getX(), current.getY());
                if(nextChildChildren.size() != 0){
                    //current = current->next
                    current = addFrom(current, tank, i);
                }
                else{
                    while(getChildren(current.getX(), current.getY()).size() == 0 && current.getParent() != null ){
                        current = current.getParent();
                    }
                    current = addFrom(current, tank, i);
                    if(current == null){
                        regenerateTank();
                        return;
                    }
                }
            }
            tankArray.add(tank);
        }
        updateGameboard();
    }
    //If an invalid tank generates, the tank is regenerated instead of crashing the game
    private void regenerateTank(){
        System.out.println("recalculate");
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                logicGrid.setNotTank(i,j);
            }
        }
        for(Tank tank : tankArray){
            tank.removeCells();
        }
        tankArray.clear();
        updateGameboard();
        generateTanks();
        updateGameboard();
    }

    //This reliably makes tank cells adjacent and randomly placed
    private Cell addFrom(Cell origin, Tank tank, int tankId) {
        //select which child
        Random rand = new Random();
        ArrayList<Cell> Children = getChildren(origin.getX(), origin.getY());

        int size = Children.size();
        if (size < 1) {
            return null;
            //System.out.println("size1 : " + size);
        }

        int randomChildIndex = rand.nextInt(size); // size have to be at least 1.
        //make the cell to tan

        Cell child = Children.get(randomChildIndex);

        logicGrid.setTank(child.getX(), child.getY());
        child.setWhichTank(tankId);
        tank.addCell(child);
        child.setParent(origin);

        return child;
    }


    //returns if its users turn
    public Boolean userTurn(int[] coordinates){
        int x = coordinates[0];
        int y =  coordinates[1];

        if(logicGrid.isCellTank(x, y)){
            logicGrid.setHit(x, y);
            updateGameboard();
            return true;
        }
        else{
            logicGrid.setHit(x, y);
            updateGameboard();
            return false;
        }


    }


    public int getFortressHealth() {
        return FortressHealth;
    }

    public void setFortressHealth(int fortressHealth) {
        FortressHealth = fortressHealth;
    }

    public int getNumOfTanks() {
        return numOfTanks;
    }

    public Boolean getUserTheWinner() {
        return areAllTanksDead();
    }

    public Boolean getGameFinished() {
        System.out.println("Checking if game is finished in model");
        System.out.println(areAllTanksDead() || FortressHealth <= 0);
        return areAllTanksDead() || FortressHealth <= 0;
    }

    private boolean areAllTanksDead() {
        System.out.println("There are this many tanks still alive: " + tankArray.size());
        System.out.println("Fortress health is " + getFortressHealth());
        if(tankArray.size() == 0){
            return false;
        }
        for(Tank tank : tankArray){
            if(tank.getTankHealth() != 0){
                return false;
            }
        }
        return true;
    }


    public String[][] getUIGrid() {
        return UIGrid;
    }

    public Grid getLogicGrid() {
        return logicGrid;
    }

    public String[][] getCheatGrid() {
        return cheatGrid;
    }




}
