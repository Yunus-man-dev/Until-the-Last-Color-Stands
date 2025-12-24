package com.gameonjava.utlcs.backend.resources;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class MovementPoint extends Resource {

    public double MOVE;
    public double UPGRADE;
    public double CONSTRUCT;
    public double TRADE;
    public double RECRUIT;
    public double ATTACK;
    private double baseLimit;

    public MovementPoint(int value, double MOVE, double UPGRADE, double CONSTRUCT, double TRADE, double RECRUIT, double ATTACK) {
        super(value);
        this.baseLimit = value;
        this.MOVE = MOVE;
        this.UPGRADE = UPGRADE;
        this.CONSTRUCT = CONSTRUCT;
        this.TRADE = TRADE;
        this.RECRUIT = RECRUIT;
        this.ATTACK = ATTACK;
    }
    public MovementPoint() {
        super();
    }

    public int updateMovementPoint(double techPoints){
        this.setValue(this.baseLimit);

        return (int) this.value;
    }
    @Override
    public void initializeConstants() {

    }
    @Override
    public void write(Json json) {
        super.write(json);

        json.writeValue("baseLimit", baseLimit);
        json.writeValue("MOVE", MOVE);
        json.writeValue("UPGRADE", UPGRADE);
        json.writeValue("CONSTRUCT", CONSTRUCT);
        json.writeValue("TRADE", TRADE);
        json.writeValue("RECRUIT", RECRUIT);
        json.writeValue("ATTACK", ATTACK);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);

        // MovementPoint ayarlarını geri yükle
        baseLimit = jsonData.getDouble("baseLimit");
        MOVE = jsonData.getDouble("MOVE");
        UPGRADE = jsonData.getDouble("UPGRADE");
        CONSTRUCT = jsonData.getDouble("CONSTRUCT");
        TRADE = jsonData.getDouble("TRADE");
        RECRUIT = jsonData.getDouble("RECRUIT");
        ATTACK = jsonData.getDouble("ATTACK");
    }

}
