package com.gameonjava.utlcs.building;
import com.gameonjava.utlcs.Player;
import com.gameonjava.utlcs.Tile;
/*GoldMine is a subclass of Building that produces a specified amount of
gold each turn. This amount might differ to civilization to civilization */
public class GoldMine extends Building {
    private  final double GOLD;

    public GoldMine(Tile tile, double GOLD) {
        super(tile);
        this.GOLD = GOLD;
    }

    @Override
    public void produce(Player player) {
       double goldProduced = GOLD;
        if(level == 2){
            goldProduced *= 2;
        }
        if(level == 3){
            goldProduced *= 3;
        }

        player.addGold(goldProduced);
    }
}
