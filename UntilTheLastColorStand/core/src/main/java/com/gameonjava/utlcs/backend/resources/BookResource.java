package com.gameonjava.utlcs.backend.resources;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/*Book is a subclass of Resource class. Calculation of Technology Point has
been calculated in Player class. */
public class BookResource extends Resource {
    public double TECHNOLOGY_MULTIPLIER;
    
    public BookResource(int value, double tech) {
        super(value);
        TECHNOLOGY_MULTIPLIER = tech;
    }
    public BookResource() {
        super();
    }

    @Override
    public void initializeConstants() {

    }

    public double calculateTP(){
        return 1+value * TECHNOLOGY_MULTIPLIER;
    }
    @Override
    public void write(Json json) {
        super.write(json);
        json.writeValue("TECH_MULT", TECHNOLOGY_MULTIPLIER);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
        TECHNOLOGY_MULTIPLIER = jsonData.getDouble("TECH_MULT");
    }

}
