package io.github.some_example_name.civilization;

import com.badlogic.gdx.graphics.Color;
import io.github.some_example_name.Civilization;
import io.github.some_example_name.resources.BookResource;
import io.github.some_example_name.resources.FoodResource;
import io.github.some_example_name.resources.GoldResource;
import io.github.some_example_name.resources.MovementPoint;
import main.java.io.github.some_example_name.Player;

public class Red extends Civilization{

    private static final int REQUIRED_TILES = 30;
    private static final int RECRUITMENT_LIMIT_BONUS = 5;
    private static final double GOLD_COST_MULTIPLIER = 1.2;
    private static final double MOVEMENT_COST_REDUCTIION = 0.8;
    private static final double ATTACK_BONUS = 1.15;

    public Red() {
        super("Red", Color.RED, 1.1, 0.9, 0.9);
    }

    protected void initializeStartingResources() {
        
        startingGold = new GoldResource();
        startingGold.addResource(100);
        
        startingFood = new FoodResource();
        startingFood.addResource(80);
        
        startingBook = new BookResource();
        startingBook.addResource(10);
        
        startingMP = new MovementPoint();
        startingMP.addResource(10);
    }
    public boolean checkWinCondition(Player p){
        int ownedTiles = p.getTileCount();

        if(ownedTiles >= REQUIRED_TILES){
            return true;
        }

        return false;
    }
}
