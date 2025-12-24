package com.gameonjava.utlcs.backend.building;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Tile;

public class GoldMine extends Building {
    private double GOLD;

    public GoldMine(Tile tile, double GOLD) {
        super(tile);
        this.GOLD = GOLD;
        name = "Mine";
    }
    public GoldMine(){
        super();
        this.name = "Mine";
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
    @Override
    public void write(Json json) {
        super.write(json);
        json.writeValue("GOLD", GOLD);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
        this.GOLD = jsonData.getDouble("GOLD");
    }
}
