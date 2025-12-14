package io.github.some_example_name.building;
import io.github.some_example_name.Player;
import io.github.some_example_name.Tile;
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
        player.addFood(foodProduced);
    }
    
}
