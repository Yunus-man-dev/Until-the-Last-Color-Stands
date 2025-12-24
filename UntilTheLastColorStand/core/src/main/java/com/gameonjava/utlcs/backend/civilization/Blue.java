package com.gameonjava.utlcs.backend.civilization;

// import com.badlogic.gdx.graphics.Color;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Tile;
import com.gameonjava.utlcs.backend.building.Library;
import com.gameonjava.utlcs.backend.resources.BookResource;
import com.gameonjava.utlcs.backend.resources.FoodResource;
import com.gameonjava.utlcs.backend.resources.GoldResource;
import com.gameonjava.utlcs.backend.resources.MovementPoint;

public class Blue extends Civilization{

    private static final int REQUIRED_TECHNOLOGY_POINTS = 50;
    private static final int REQUIRED_LIBRARIES = 8;



    public Blue(String color) {
        super("Blue Civilization", color, 0.8, 1.0, 0.002 );
        initializeStartingResources();
    }
    public Blue() {
        super();
    }

    public void initializeStartingResources() {


        startingGold = new GoldResource(START_GOLD,GRECRUIT,GCONSTRUCT,GDEVELOP,GREMOVE);


        startingFood = new FoodResource(START_FOOD,FRECRUIT,FTILE,FMAINTAIN);

        startingBook = new BookResource(START_BOOK,technologyMultiplier);

        startingMP = new MovementPoint(START_MOVEMENT,M_MOVE,M_UPGRADE,M_CONSTRUCT,M_TRADE,M_RECRUIT,M_ATTACK);
    }

    public boolean checkWinCondition(Player p){
        double technologyPoints = p.getTechnologyPoint();
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


    public static int getRequiredTechnologyPoints() {
        return REQUIRED_TECHNOLOGY_POINTS;
    }

    public static int getRequiredLibraries() {
        return REQUIRED_LIBRARIES;
    }

}
