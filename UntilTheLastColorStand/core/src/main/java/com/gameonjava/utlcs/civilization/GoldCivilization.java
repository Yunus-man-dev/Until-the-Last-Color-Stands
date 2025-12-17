package com.gameonjava.utlcs.civilization;

import com.gameonjava.utlcs.resources.BookResource;
import com.gameonjava.utlcs.resources.FoodResource;
import com.gameonjava.utlcs.resources.GoldResource;
import com.gameonjava.utlcs.resources.MovementPoint;
import com.gameonjava.utlcs.building.GoldMine;

import com.gameonjava.utlcs.Player;
import com.gameonjava.utlcs.Tile;

public class GoldCivilization extends Civilization{

    public static final int REQUIRED_GOLD = 100000;
    public static final int REQUIRED_GOLD_MINES = 10;
    public static final double GOLD_PRODUCTION_BONUS = 1.5;
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






    public GoldCivilization() {
        super("Gold Civilization", "Gold", 0.8, 0.8, 0.001);
        initializeStartingResources();
    }

    public void initializeStartingResources(){

        GRECRUIT *= RECRUITMENT_COST_INCREASE;

        PORT_GOLD *= GOLD_PRODUCTION_BONUS;
        MINE_GOLD *= GOLD_PRODUCTION_BONUS;

        startingGold = new GoldResource(START_GOLD,GRECRUIT,GCONSTRUCT,GDEVELOP,GREMOVE);
        // startingGold.addResource(150);

        startingFood = new FoodResource(START_FOOD,FRECRUIT,FTILE,FMAINTAIN);
        // startingFood.addResource(70);

        startingBook = new BookResource(START_BOOK,technologyMultiplier);
        // startingBook.addResource(15);

        startingMP = new MovementPoint(START_MOVEMENT,M_MOVE,M_UPGRADE,M_CONSTRUCT,M_TRADE,M_RECRUIT,M_ATTACK);
        // startingMP.addResource(10);
    }
    public boolean checkWinCondition(Player p){
        double goldAmount = p.getGold().getValue();
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
