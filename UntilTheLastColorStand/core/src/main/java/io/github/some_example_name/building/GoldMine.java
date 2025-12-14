package io.github.some_example_name.building;
import io.github.some_example_name.Player;
import io.github.some_example_name.Tile;
/*GoldMine is a subclass of Building that produces a specified amount of
gold each turn. This amount might differ to civilization to civilization */
public class GoldMine extends Building {
    private final int GOLD;

    public GoldMine(Tile tile, int GOLD) {
        super(tile);
        this.GOLD = GOLD;
    }

    @Override
    public void produce(Player player) {
        int goldProduced = GOLD;
        player.addGold(goldProduced);
    }
}