package io.github.some_example_name.building;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import io.github.some_example_name.Player;
import io.github.some_example_name.Tile;
/*Building is an abstract class that is super class of other building types and
stores necessary and common features and variables, such as level and the Tile that
building is constructed on. */
public abstract class Building {
    private int level;
    private Tile tile;

    public Building(Tile tile) {
        this.tile = tile;
        this.level = 1;
    }

    public int getLevel() {
        return level;
    }
    public Tile getTile() {
        return tile;
    }
    public void upgrade(){
        // 4 is just a random constant here. you can edit it
        if(this.level < 4){
            this.level++;
        }
    }

    public abstract void produce(Player player);

    public void write(Json json) {
        json.writeValue("level", level);
        json.writeValue("tile", tile);
    }
    public void read(Json json, JsonValue jsonData) {
        this.level = jsonData.getInt("level");
        this.tile = json.readValue("tile", Tile.class, jsonData);
    }
}
