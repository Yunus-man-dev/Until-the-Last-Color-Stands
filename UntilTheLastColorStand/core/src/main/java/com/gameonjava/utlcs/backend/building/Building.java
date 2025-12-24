package com.gameonjava.utlcs.backend.building;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Tile;

public abstract class Building implements com.badlogic.gdx.utils.Json.Serializable{
    protected int level;
    protected Tile tile;
    protected String name;

    public Building(Tile tile) {
        this.tile = tile;
        this.level = 1;
    }

    public Building() {
        
    }
    
    public int getLevel() {
        return level;
    }
    public Tile getTile() {
        return tile;
    }
    public void setTile(Tile tile) {
        this.tile = tile;
    }
    public void upgrade(){
        
        if(this.level < 4){
            this.level++;
            System.out.println(level);
        }

    }
    public String getName(){
        return name;
    }
    public abstract void produce(Player player);

    @Override
    public void write(Json json) {
        json.writeValue("level", level);
        
    }
    @Override
    public void read(Json json, JsonValue jsonData) {
        this.level = jsonData.getInt("level");
        
    }
}
