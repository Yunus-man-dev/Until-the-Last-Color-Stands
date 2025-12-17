package com.gameonjava.utlcs.building;
import com.gameonjava.utlcs.Player;
import com.gameonjava.utlcs.Tile;
/*Port is a subclass of Building that produces a specified amount of gold and
food each turn. This amount might differ to civilization to civilization. */
public class Port extends Building {
    private final int FOOD;
    private final int GOLD;

    public Port(Tile tile, int FOOD, int GOLD) {
        super(tile);
        this.FOOD = FOOD;
        this.GOLD = GOLD;
    }

    @Override
    public void produce(Player player) {
        int foodProduced = FOOD;
        int goldProduced = GOLD;
        player.addFood(foodProduced);
        player.addGold(goldProduced);
    }
}
