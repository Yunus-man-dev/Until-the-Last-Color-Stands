package com.gameonjava.utlcs.backend.building;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Tile;

public class Farm extends Building {

    private double FOOD;

    public Farm(Tile tile, double FOOD) {
        super(tile);
        this.FOOD = FOOD;
        name = "Farm";
    }
    public Farm(){
        super();
        this.name = "Farm";
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
      
        player.addFood(foodProduced);
    }
    @Override
    public void write(Json json) {
        super.write(json); 
        json.writeValue("FOOD", FOOD); 
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
        this.FOOD = jsonData.getDouble("FOOD");
    }

}
