package com.gameonjava.utlcs.backend.civilization;

// import com.badlogic.gdx.graphics.Color;
import com.gameonjava.utlcs.backend.resources.BookResource;
import com.gameonjava.utlcs.backend.resources.FoodResource;
import com.gameonjava.utlcs.backend.resources.GoldResource;
import com.gameonjava.utlcs.backend.resources.MovementPoint;

import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Tile;
import com.gameonjava.utlcs.backend.building.Library;

public class Blue extends Civilization{

    private static final int REQUIRED_TECHNOLOGY_POINTS = 50;
    private static final int REQUIRED_LIBRARIES = 8;
    // private static final double LIBRARY_PRODUCTION_BONUS = 1.5;


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



    public Blue() {
        super("Blue Civilization", "Blue", 0.8, 1.0, 0.002 );
        initializeStartingResources();
    }

    public void initializeStartingResources() {


        startingGold = new GoldResource(START_GOLD,GRECRUIT,GCONSTRUCT,GDEVELOP,GREMOVE);
        // startingGold.addResource(80);

        startingFood = new FoodResource(START_FOOD,FRECRUIT,FTILE,FMAINTAIN);
        // startingFood.addResource(70);

        startingBook = new BookResource(START_BOOK,technologyMultiplier);
        // startingBook.addResource(30);

        startingMP = new MovementPoint(START_MOVEMENT,M_MOVE,M_UPGRADE,M_CONSTRUCT,M_TRADE,M_RECRUIT,M_ATTACK);
        // startingMP.addResource(10);
    }

    public boolean checkWinCondition(Player p){
        int technologyPoints = p.getTechnologyPoint();
        int libraryCount = countLibraries(p);

        boolean hasTechPoints = technologyPoints >= REQUIRED_TECHNOLOGY_POINTS;
        boolean hasLibraries = libraryCount >= REQUIRED_LIBRARIES;

        if(hasTechPoints && hasLibraries){
            return true;
        }
        return false;
    }

    public int countLibraries(Player p){
        int count = 0;
        for (Tile tile : p.getOwnedTiles()){
            if(tile.hasBuilding() && tile.getBuilding() instanceof Library){
                count++;
            }
        }
        return count;
    }

    // public static double getLibraryProductionBonus() {
    //     return LIBRARY_PRODUCTION_BONUS;
    // }

    public static int getRequiredTechnologyPoints() {
        return REQUIRED_TECHNOLOGY_POINTS;
    }

    public static int getRequiredLibraries() {
        return REQUIRED_LIBRARIES;
    }

}
