package io.github.some_example_name.civilization;

import io.github.some_example_name.civilization.*;
import io.github.some_example_name.building.GoldMine;

import com.badlogic.gdx.graphics.Color;

import io.github.some_example_name.Player;
import io.github.some_example_name.resources.*;
import io.github.some_example_name.Tile;

public class GoldCivilization extends Civilization{

    public static final int REQUIRED_GOLD = 1000; 
    public static final int REQUIRED_GOLD_MINES = 8;
    public static final double GOLD_PRODUCTION_BONUS = 1.4;
    public static final double RECRUITMENT_COST_INCREASE = 1.3;
    public final double TRADE_DISCOUNT = 0.85;


    // public final int FRECRUIT = 0;
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






    public GoldCivilization() {
        super("Gold Civilization", "Gold", 0.9, 0.9, 1.0);
        initializeStartingResources();
    }

    public void initializeStartingResources(){



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
        // startingGold.addResource(150);
        
        startingFood = new FoodResource(START_FOOD,FRECRUIT,FTILE,FMAINTAIN);
        // startingFood.addResource(70);
        
        startingBook = new BookResource(START_BOOK);
        // startingBook.addResource(15);
        
        startingMP = new MovementPoint(START_MOVEMENT,M_MOVE,M_UPGRADE,M_CONSTRUCT,M_TRADE,M_RECRUIT,M_ATTACK);
        // startingMP.addResource(10);
    }
    public boolean checkWinCondition(Player p){
        int goldAmount = p.getGold().getValue();
        int goldMineCount = countGoldMines(p);
        
        boolean hasGold = goldAmount >= REQUIRED_GOLD;
        boolean hasGoldMines = goldMineCount >= REQUIRED_GOLD_MINES;

        if(hasGold && hasGoldMines) {
            return true;
        }
        return false;
    }
    public int countGoldMines(Player p){
        int count = 0;
        for(Tile tile : p.getOwnedTiles()) {
            if(tile.hasBuilding() && tile.getBuilding() instanceof GoldMine){
                count++;
            }
        }
        return count;
    }
    public double getTradeDiscount() {
        return TRADE_DISCOUNT;
    }
    
    public static double getGoldProductionBonus() {
        return GOLD_PRODUCTION_BONUS;
    }
    
    public static double getRecruitmentCostIncrease() {
        return RECRUITMENT_COST_INCREASE;
    }
    
    public static int getRequiredGold() {
        return REQUIRED_GOLD;
    }
    
    public static int getRequiredGoldMines() {
        return REQUIRED_GOLD_MINES;
    }
}
