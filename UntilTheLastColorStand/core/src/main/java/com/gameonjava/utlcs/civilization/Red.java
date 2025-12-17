package com.gameonjava.utlcs.civilization;

import com.gameonjava.utlcs.resources.BookResource;
import com.gameonjava.utlcs.resources.FoodResource;
import com.gameonjava.utlcs.resources.GoldResource;
import com.gameonjava.utlcs.resources.MovementPoint;
import com.gameonjava.utlcs.Player;

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




    public Red() {
        super("Red Civilization", "Red", 1.3, 1, 0.001);
         initializeStartingResources();
    }

    public void initializeStartingResources() {




        M_ATTACK  -= 1;



        startingGold = new GoldResource(START_GOLD,GRECRUIT,GCONSTRUCT,GDEVELOP,GREMOVE);
        // startingGold.addResource(100);

        startingFood = new FoodResource(START_FOOD,FRECRUIT,FTILE,FMAINTAIN);
        // startingFood.addResource(80);

        startingBook = new BookResource(START_BOOK,technologyMultiplier);
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
