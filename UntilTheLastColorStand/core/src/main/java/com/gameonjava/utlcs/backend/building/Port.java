package com.gameonjava.utlcs.backend.building;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Tile;
/*Port is a subclass of Building that produces a specified amount of gold and
food each turn. This amount might differ to civilization to civilization. */
public class Port extends Building {
    private final double FOOD;
    private final double GOLD;

    public Port(Tile tile, double FOOD, double GOLD) {
        super(tile);
        this.FOOD = FOOD;
        this.GOLD = GOLD;
        name = "Port";
    }

    @Override
    public void produce(Player player) {
        double foodProduced = FOOD;
        double goldProduced = GOLD;
        if(level == 2){
            foodProduced *= 2;
            goldProduced *= 2;
        }
        if(level == 2){
            foodProduced *= 3;
            goldProduced *= 3;
        }

        player.addFood(foodProduced);
        player.addGold(goldProduced);
    }
}
