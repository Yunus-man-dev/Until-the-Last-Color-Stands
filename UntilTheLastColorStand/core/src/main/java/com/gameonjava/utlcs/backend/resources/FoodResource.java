package com.gameonjava.utlcs.backend.resources;
/*Food class is a subclass of Resource class, which has necessary variable
and methods for food related features */

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class FoodResource extends Resource {
    public double RECRUIT;
    public double TILE;
    public double MAINTAIN;

    public FoodResource(int value,double RECRUIT, double TILE, double MAINTAIN) {
        super(value);
        this.RECRUIT = RECRUIT;
        this.TILE = TILE;
        this.MAINTAIN = MAINTAIN;

    }
    public FoodResource(){
        super();
    }

    @Override
    public void initializeConstants() {

    }
    @Override
    public void write(Json json) {
        super.write(json);
        json.writeValue("RECRUIT", RECRUIT);
        json.writeValue("TILE", TILE);
        json.writeValue("MAINTAIN", MAINTAIN);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
        RECRUIT = jsonData.getDouble("RECRUIT");
        TILE = jsonData.getDouble("TILE");
        MAINTAIN = jsonData.getDouble("MAINTAIN");
    }

}
