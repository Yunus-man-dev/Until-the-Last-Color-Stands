package com.gameonjava.utlcs.backend.resources;
/*Gold class is a subclass of Resource class, which has necessary variable
and methods for Gold related actions, such as amount of gold that is necessary for
constructing a building */

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class GoldResource extends Resource {
    public double RECRUIT;
    public double CONSTRUCT;
    public double DEVELOP;
    public double REMOVE;

    public GoldResource(int value , double RECRUIT, double CONSTRUCT,double DEVELOP, double REMOVE) {
        super(value);
        this.RECRUIT = RECRUIT;
        this.CONSTRUCT = CONSTRUCT;
        this.DEVELOP = DEVELOP;
        this.REMOVE = REMOVE;
    }
    public GoldResource(){
        super();
    }

    @Override
    public void initializeConstants() {

    }
    @Override
    public void write(Json json) {
        super.write(json); // 'value' kaydedilir
        json.writeValue("RECRUIT", RECRUIT);
        json.writeValue("CONSTRUCT", CONSTRUCT);
        json.writeValue("DEVELOP", DEVELOP);
        json.writeValue("REMOVE", REMOVE);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData); // 'value' okunur
        RECRUIT = jsonData.getDouble("RECRUIT");
        CONSTRUCT = jsonData.getDouble("CONSTRUCT");
        DEVELOP = jsonData.getDouble("DEVELOP");
        REMOVE = jsonData.getDouble("REMOVE");
    }

}
