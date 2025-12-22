package com.gameonjava.utlcs.backend.civilization;

import com.gameonjava.utlcs.backend.Game;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.resources.BookResource;
import com.gameonjava.utlcs.backend.resources.FoodResource;
import com.gameonjava.utlcs.backend.resources.GoldResource;
import com.gameonjava.utlcs.backend.resources.MovementPoint;


public class Brown extends Civilization{

    private static final int REQUIRED_TURNS = 200;
    private final double FOOD_BONUS = 1.2;
    private static final double FOOD_CONSUMPTION_INCREASE = 1.3;
    // private static final double DEFENSE_BONUS = 1.2;
    private static final double RED_DEFENSE_BONUS = 1.3;
    private static final double RED_ATTACK_BONUS = 1.15;


    // public final int FRECRUIT1 = FRECRUIT *;
    public final int FTILE1 = 0 ;
    // public final int FMAINTAIN1 = FRECRUIT * 1.3;

    private int currentTurnNumber;

    public Brown(String color) {
        super("Brown Civilization", color, 1, 1.3, 0.001);
        initializeStartingResources();
        currentTurnNumber = 0;
    }
    public Brown(){
        super();
    }

    public void initializeStartingResources() {



        FMAINTAIN *= FOOD_CONSUMPTION_INCREASE;
            // 1.2
        FARM_FOOD *= FOOD_BONUS;
             // 1.2
        PORT_FOOD *= FOOD_BONUS;


        startingGold = new GoldResource(START_GOLD,GRECRUIT,GCONSTRUCT,GDEVELOP,GREMOVE);
        // startingGold.addResource(80);

        startingFood = new FoodResource(START_FOOD,FRECRUIT,FTILE,FMAINTAIN);
        // startingFood.addResource(120);

        startingBook = new BookResource(START_BOOK,technologyMultiplier);
        // startingBook.addResource(15);

        startingMP = new MovementPoint(START_MOVEMENT,M_MOVE,M_UPGRADE,M_CONSTRUCT,M_TRADE,M_RECRUIT,M_ATTACK);
        // startingMP.addResource(10);
    }
    public boolean checkWinCondition(Player p){
        setCurrentTurnNumber(Game.getCurrentTurn()); // Requires turn number
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

    public void setCurrentTurnNumber(int turn){
        currentTurnNumber = turn;
}
    public double getFoodBonus() {
        return FOOD_BONUS;
    }

    public static double getFoodConsumptionIncrease() {
        return FOOD_CONSUMPTION_INCREASE;
    }

    public  double getDefenseBonus() {
        return defenseMultiplier;
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
