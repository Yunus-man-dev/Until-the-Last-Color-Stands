package com.gameonjava.utlcs.building;
import com.gameonjava.utlcs.Player;
import com.gameonjava.utlcs.Tile;
import com.gameonjava.utlcs.civilization.Brown;
/*Farm is a subclass of Building that produces a specified amount of food
each turn. This amount might differ to civilization to civilization. */
public class Farm extends Building {
    private final int FOOD;

    public Farm(Tile tile, int FOOD) {
        super(tile);
        this.FOOD = FOOD;
    }

    @Override
    public void produce(Player player) {
        int foodProduced = FOOD;
        if(player.getCivilization() instanceof Brown){
            foodProduced = (int) (foodProduced * Brown.getFoodBonus());
        }
        player.addFood(foodProduced);
    }

}
