package io.github.some_example_name.civilization;

import com.badlogic.gdx.graphics.Color;
import io.github.some_example_name.civilization.Civilization;
import io.github.some_example_name.resources.*;
import io.github.some_example_name.Player;

public class Red extends Civilization{

    private static final int REQUIRED_TILES = 30;
    private static final int RECRUITMENT_LIMIT_BONUS = 5;
    private static final double GOLD_COST_MULTIPLIER = 1.2;
    private static final double MOVEMENT_COST_REDUCTIION = 0.8;
    private static final double ATTACK_BONUS = 1.15;





    //  public final int FRECRUIT = 0;
    // public final int FTILE = 0 ;
    // public final int FMAINTAIN = 0;

    // public final int GRECRUIT = 0;
    // public final int GCONSTRUCT = 0;
    // public final int GDEVELOP = 0;
    // public final int GREMOVE = 0;

    // public final int M_MOVE = 0;
    // public final int M_UPGRADE = 0;
    // public final int M_CONSTRUCT = 0;
    // public final int M_TRADE = 0;
    // public final int M_RECRUIT = 0;
    // public final int M_ATTACK = 0;



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


    public Red() {
        super("Red Civilization", "Red", 1.1, 0.9, 0.9);
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
        // startingGold.addResource(100);
        
        startingFood = new FoodResource(START_FOOD,FRECRUIT,FTILE,FMAINTAIN);
        // startingFood.addResource(80);
        
        startingBook = new BookResource(START_BOOK);
        // startingBook.addResource(10);
        
        startingMP = new MovementPoint(START_MOVEMENT,M_MOVE,M_UPGRADE,M_CONSTRUCT,M_TRADE,M_RECRUIT,M_ATTACK);
        // startingMP.addResource(10);
    }
    public boolean checkWinCondition(Player p){
        int ownedTiles = p.getTileCount();

        if(ownedTiles >= REQUIRED_TILES){
            return true;
        }

        return false;
    }

    public static int getRequiredTiles() {
        return REQUIRED_TILES;
    }

    public static int getRecruitmentLimitBonus() {
        return RECRUITMENT_LIMIT_BONUS;
    }

    public static double getGoldCostMultiplier() {
        return GOLD_COST_MULTIPLIER;
    }

    public static double getMovementCostReductiion() {
        return MOVEMENT_COST_REDUCTIION;
    }

    public static double getAttackBonus() {
        return ATTACK_BONUS;
    }

}
