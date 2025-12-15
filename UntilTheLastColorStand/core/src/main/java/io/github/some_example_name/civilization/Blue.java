package io.github.some_example_name.civilization;

// import com.badlogic.gdx.graphics.Color;
import io.github.some_example_name.civilization.Civilization;
import io.github.some_example_name.resources.*;
import io.github.some_example_name.Player;
import io.github.some_example_name.Tile;
import io.github.some_example_name.building.Library;

public class Blue extends Civilization{

    private static final int REQUIRED_TECHNOLOGY_POINTS = 50;
    private static final int REQUIRED_LIBRARIES = 8; 
    private static final double LIBRARY_PRODUCTION_BONUS = 1.5;

    public Blue() {
        super("Blue Civilization", "Blue", 0.85, 1.0, 1.3);
    }

    public void initializeStartingResources() {

        startingGold = new GoldResource();
        startingGold.addResource(80);
        
        startingFood = new FoodResource();
        startingFood.addResource(70);
        
        startingBook = new BookResource();
        startingBook.addResource(30);
        
        startingMP = new MovementPoint();
        startingMP.addResource(10);
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

    public static double getLibraryProductionBonus() {
        return LIBRARY_PRODUCTION_BONUS;
    }
    
    public static int getRequiredTechnologyPoints() {
        return REQUIRED_TECHNOLOGY_POINTS;
    }
    
    public static int getRequiredLibraries() {
        return REQUIRED_LIBRARIES;
    }

}
