/** The tank class models which cells on the Gameboard are grouped together as individual tanks.
 * Every tank has health, attack damage, and an arrayList of cells from the Grid.
 * Functions provided calculate the health of the tank, and how much damage it will do
 * based on its own health.
 */

package ca.a4p2v2.model;
import java.util.ArrayList;
import java.util.List;

public class Tank {
    private int tankHealth;
    private int tankAttackDMG;
    List<Cell> tankCells = new ArrayList<>();



    public Tank() {
        tankHealth = 5;
        tankAttackDMG = 20;
    }
    public void addCell(Cell cell){
        if(!tankCells.contains(cell)){
            tankCells.add(cell);
        }
    }
    public void removeCells(){
        tankCells.clear();

    }

    public void calcTankAttackDMG() {
        switch (tankHealth) {
            case 0:
                tankAttackDMG = 0;
                break;

            case 1:
                tankAttackDMG = 1;
                break;

            case 2:
                tankAttackDMG = 2;
                break;

            case 3:
                tankAttackDMG = 3;
                break;

            case 4:
                tankAttackDMG = 5;
                break;

            case 5:
                tankAttackDMG = 20;
                break;

            default:
                assert tankCells.size() < 6;
        }
    }

    public void calcTankHP() {
        int aliveCells = 0;
        for (Cell cell : tankCells) {
            if (!cell.getIsHit()) {
                aliveCells++;
            }
        }
        tankHealth = aliveCells;
    }

    public int getTankHealth() {
        return tankHealth;
    }

    public int getTankAttackDMG() {
        return tankAttackDMG;
    }
}
