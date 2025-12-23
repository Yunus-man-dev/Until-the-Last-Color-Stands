package com.gameonjava.utlcs.backend.building;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Tile;
/*GoldMine is a subclass of Building that produces a specified amount of
gold each turn. This amount might differ to civilization to civilization */
public class GoldMine extends Building {
    private double GOLD;

    public GoldMine(Tile tile, double GOLD) {
        super(tile);
        this.GOLD = GOLD;
        name = "Mine";
    }
    public GoldMine(){
        super();
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
