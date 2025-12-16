package io.github.some_example_name.resources;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/*  Resource is an abstract class that has a variable that stores the value of the
resource and works as a template for more specified resource types. Every constant of
these resources is used throughout the game to compute resource costs in a civilizationdependent
way. */
public abstract class Resource {
    protected int value;

    public Resource(int value) {
        this.value = value;
    }

    public abstract void initializeConstants();

    public void addResource(int value) {
        this.value += value;
    }
    public void reduceResource(int value) {
        if(checkForResource(value)){
            this.value -= value;
        }
    }
    public boolean checkForResource(int value) {
        if(this.value >= value){
            return true;
        }
        else
            return false;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }

    public void write(Json json) {
        json.writeValue("value", value);
    }
    public void read(Json json, JsonValue jsonData) {
        value = jsonData.getInt("value");
    }
}
