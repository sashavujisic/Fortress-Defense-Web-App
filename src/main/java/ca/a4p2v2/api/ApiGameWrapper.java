package ca.a4p2v2.api;
import ca.a4p2v2.model.GameLogic;
import ca.a4p2v2.model.Tank;
import java.util.ArrayList;
import java.util.List;
import static ca.a4p2v2.controller.SpringRestController.boards;

/**
 * Wrapper class for the REST API to define object structures required by the front-end.
 * HINT: Create static factory methods (or constructors) which help create this object
 *       from the data stored in the model, or required by the model.
 *
 * This class manages the state of the user, the tanks, the game number, game logic, and fortress health.
 * This class takes requests from the controller and gets data generated from the model GameLogic, Grid, and Tank
 * It first processes the user's turn, then updates the Tank damage wave, followed by damaging the fortress,
 * and finally checking to see if the state of the game changed. Process is similar to GameLogic backend and
 * UI class (not included) from Assignment 3. Key feature is the setModel function, which dynamically changes
 * the backend model in accordance to each game number.
 */
public class ApiGameWrapper {
    static ArrayList<GameLogic> models = new ArrayList<>(10);
    static GameLogic model;
    public boolean cheat;
    public boolean isGameWon;
    public boolean isGameLost;
    public int fortressHealth;
    public final int TANKS_ALIVE_MAX = 5;
    public int numTanksAlive;
    public int gameNumber;
    public int[] lastTankDamages = new int[5];

    public ApiGameWrapper(int gameNumber, boolean isGameWon, boolean isGameLost, GameLogic inputModel) {
        int TANK_DMG = 20;
        this.gameNumber = gameNumber;
        this.isGameWon = isGameWon;
        this.isGameLost = isGameLost;
        this.fortressHealth = inputModel.getFortressHealth();
        this.numTanksAlive = inputModel.getNumOfTanks();
        this.cheat = false;
        for (int i = 0; i < numTanksAlive; i++) {
            lastTankDamages[i] = TANK_DMG;
        }
        models.add(inputModel);
        setModel(gameNumber);
        System.out.println("Created game instance.");
    }

    public void setCheat(boolean inputCheat) {
        this.cheat = inputCheat;
        boards.get(gameNumber).setCheat(inputCheat);
        System.out.println("Set cheat to: " + inputCheat);
    }

    public static void setModel(int gameNumber) {
        model = models.get(gameNumber);
        System.out.println("Updated model to match game and board");
    }

    public void turn(int[] coordinates){
        int y = coordinates[0];
        int x = coordinates[1];
        model.userTurn(coordinates);
        System.out.println("Executed user turn, updating last tank attack.");
        updateLastAttack();
    }

    public void updateLastAttack(){
        int aliveTanks = TANKS_ALIVE_MAX;
        List<Tank> tanks = model.getTankArray();
        int i = aliveTanks-1;
        for(Tank tank : tanks){
            tank.calcTankHP();
            if (tank.getTankHealth() <= 0){
                aliveTanks--;
            }
            tank.calcTankAttackDMG();
            if (i > 0) {
                int tankDamage = tank.getTankAttackDMG();
                this.lastTankDamages[i] = tankDamage;
                i--;
            }
        }
        numTanksAlive = aliveTanks;
        System.out.println("Updated last tank attack, now damaging fortress.");
        damageFortress();
    }

    public void damageFortress(){
        int fortressHP = fortressHealth;
        for(int i = 0; i < numTanksAlive; i++){
            fortressHP -= lastTankDamages[i];
        }
        this.fortressHealth = fortressHP;
        model.setFortressHealth(fortressHealth);
        System.out.println("Fortress damage taken, now checking for win/lose conditions.");
        endGameCheck();
    }

    public void endGameCheck(){
        if (model.getGameFinished()){
            if (model.getUserTheWinner()){
                isGameWon = true;
            }
            else {
                isGameLost = true;
            }
        }
        System.out.println("Game conditions evaluated.");
    }
}



