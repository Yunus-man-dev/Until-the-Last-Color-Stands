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

    public GoldCivilization() {
        super("Gold Civilization", "Gold", 0.9, 0.9, 1.0);
    }

    public void initializeStartingResources(){
        startingGold = new GoldResoource();
        startingGold.addResource(150);
        
        startingFood = new FoodResource();
        startingFood.addResource(70);
        
        startingBook = new BookResource();
        startingBook.addResource(15);
        
        startingMP = new MovementPoint();
        startingMP.addResource(10);
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
