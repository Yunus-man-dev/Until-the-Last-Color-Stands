package com.gameonjava.utlcs.backend.building;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Tile;

/*Farm is a subclass of Building that produces a specified amount of food
each turn. This amount might differ to civilization to civilization. */
public class Farm extends Building {

    private final double FOOD;

    public Farm(Tile tile, double FOOD) {
        super(tile);
        this.FOOD = FOOD;
        name = "Farm";
    }

    @Override
    public void produce(Player player) {
        double foodProduced = FOOD;
        if(level == 2){
            foodProduced *=2;
        }
        if(level == 3){
            foodProduced *= 3;
        }
        // if(player.getCivilization() instanceof Brown){
        //     foodProduced = (int) (foodProduced * Brown.getFoodBonus());
        // }
        player.addFood(foodProduced);
    }


}
