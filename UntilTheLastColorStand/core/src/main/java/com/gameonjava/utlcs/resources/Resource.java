package com.gameonjava.utlcs.resources;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/*  Resource is an abstract class that has a variable that stores the value of the
resource and works as a template for more specified resource types. Every constant of
these resources is used throughout the game to compute resource costs in a civilizationdependent
way. */
public abstract class Resource {
    protected double value;

    public Resource(int value) {
        this.value = value;
    }

    public abstract void initializeConstants();

    public void addResource(double value) {
        this.value += value;
    }
    public void reduceResource(double value) {
        if(checkForResource(value)){
            this.value -= value;
        }
    }
    public boolean checkForResource(double value) {
        if(this.value >= value){
            return true;
        }
        else
            return false;
    }
    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }

    public void write(Json json) {
        json.writeValue("value", value);
    }
    public void read(Json json, JsonValue jsonData) {
        value = jsonData.getInt("value");
    }
}
