package com.gameonjava.utlcs.backend.building;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Tile;

public class Port extends Building {
    private double FOOD;
    private double GOLD;

    public Port(Tile tile, double FOOD, double GOLD) {
        super(tile);
        this.FOOD = FOOD;
        this.GOLD = GOLD;
        name = "Port";
    }
    public Port(){
        super();
        this.name = "Port";
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
    @Override
    public void write(Json json) {
        super.write(json);
        json.writeValue("FOOD", FOOD);
        json.writeValue("GOLD", GOLD);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
        this.FOOD = jsonData.getDouble("FOOD");
        this.GOLD = jsonData.getDouble("GOLD");
    }
}
