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





    public final int FRECRUIT = 0;
    public final int FTILE = 3;
    public final int FMAINTAIN = 0;

    public int gRecruit = 50;
    public final int GCONSTRUCT = 300;
    public final int GDEVELOP = 250;
    public final int GREMOVE = 300;

    public final int M_MOVE = 1;
    public final int M_UPGRADE = 3;
    public final int M_CONSTRUCT = 5;
    public final int M_TRADE = 7;
    public final int M_RECRUIT = 3;
    public int mAttack = 4;




    public Red() {
        super("Red Civilization", "Red", 1.3, 1, 0.001);
         initializeStartingResources();
    }

    public void initializeStartingResources() {

       
      
       
        mAttack -= 1;


        
        startingGold = new GoldResource(START_GOLD,gRecruit,GCONSTRUCT,GDEVELOP,GREMOVE);
        // startingGold.addResource(100);
        
        startingFood = new FoodResource(START_FOOD,FRECRUIT,FTILE,FMAINTAIN);
        // startingFood.addResource(80);
        
        startingBook = new BookResource(START_BOOK,technologyMultiplier);
        // startingBook.addResource(10);
        
        startingMP = new MovementPoint(START_MOVEMENT,M_MOVE,M_UPGRADE,M_CONSTRUCT,M_TRADE,M_RECRUIT,mAttack);
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
