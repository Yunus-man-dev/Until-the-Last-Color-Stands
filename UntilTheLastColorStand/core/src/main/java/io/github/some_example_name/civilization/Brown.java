package io.github.some_example_name.civilization;

import com.badlogic.gdx.graphics.Color;
import io.github.some_example_name.resources.*;
import io.github.some_example_name.Player;


public class Brown extends Civilization{

    private static final int REQUIRED_TURNS = 50;
    private final double FOOD_BONUS = 1.4;
    private static final double FOOD_CONSUMPTION_INCREASE = 1.3;
    private static final double DEFENSE_BONUS = 1.2;
    private static final double RED_DEFENSE_BONUS = 1.3;
    private static final double RED_ATTACK_BONUS = 1.15;


    public final int FRECRUIT1 = 0;
    public final int FTILE1 = 0 ;
    public final int FMAINTAIN1 = 0;

    public final int GRECRUIT1 = 0;
    public final int GCONSTRUCT1 = 0;
    public final int GDEVELOP1 = 0;
    public final int GREMOVE1 = 0;

    public final int M_MOVE1 = 0;
    public final int M_UPGRADE1 = 0;
    public final int M_CONSTRUCT1 = 0;
    public final int M_TRADE1 = 0;
    public final int M_RECRUIT1 = 0;
    public final int M_ATTACK1 = 0;

    public  final int FARM_FOOD1 = 0;
    public  final int PORT_FOOD1 = 0;
    public  final int PORT_GOLD1 = 0;
    public  final int MINE_GOLD1 = 0;
    public  final int BOOK1 = 0;


    private int currentTurnNumber;

    public Brown(){
        super("Brown Civilization", "Brown", 0.95, 1.2, 1.0); 
         initializeStartingResources();
    }

    public void initializeStartingResources() {


        FRECRUIT = FRECRUIT1;
        FTILE = FTILE1 ;
        FMAINTAIN = FMAINTAIN1;

        GRECRUIT = GRECRUIT1;
        GCONSTRUCT = GCONSTRUCT1;
        GDEVELOP = GDEVELOP1;
        GREMOVE = GREMOVE1;

        M_MOVE = M_MOVE1;
        M_UPGRADE = M_UPGRADE1;
        M_CONSTRUCT = M_CONSTRUCT1;
        M_TRADE = M_TRADE1;
        M_RECRUIT = M_RECRUIT1;
        M_ATTACK = M_ATTACK1;

        FARM_FOOD = FARM_FOOD1;
        PORT_FOOD = PORT_FOOD1;
        PORT_GOLD = PORT_GOLD1;
        MINE_GOLD = MINE_GOLD1;
        BOOK = BOOK1;

        startingGold = new GoldResource(START_GOLD,GRECRUIT,GCONSTRUCT,GDEVELOP,GREMOVE);
        // startingGold.addResource(80);
        
        startingFood = new FoodResource(START_FOOD,FRECRUIT,FTILE,FMAINTAIN);
        // startingFood.addResource(120); 
        
        startingBook = new BookResource(START_BOOK);
        // startingBook.addResource(15);
        
        startingMP = new MovementPoint(START_MOVEMENT,M_MOVE,M_UPGRADE,M_CONSTRUCT,M_TRADE,M_RECRUIT,M_ATTACK);
        // startingMP.addResource(10);
    }
    public boolean checkWinCondition(Player p){
        //int currentTurn = p.getTurnNumber(); // Requires turn number
        if (!p.isActive()) {
            return false;
        }
        if (p.getTileCount() == 0) {
            return false;
        }
        if(currentTurnNumber >= REQUIRED_TURNS){
            return true;
        }
        return false;
    }

    // public int setCurrentTurnNumber(Game game){
    //     currentTurnNumber = game.getTurnNumber();
    // }
    public double getFoodBonus() {
        return FOOD_BONUS;
    }
    
    public static double getFoodConsumptionIncrease() {
        return FOOD_CONSUMPTION_INCREASE;
    }
    
    public static double getDefenseBonus() {
        return DEFENSE_BONUS;
    }
    
    public static double getRedDefenseBonus() {
        return RED_DEFENSE_BONUS;
    }
    
    public static double getRedAttackBonus() {
        return RED_ATTACK_BONUS;
    }
    
    public static int getRequiredTurns() {
        return REQUIRED_TURNS;
    }

   
}
