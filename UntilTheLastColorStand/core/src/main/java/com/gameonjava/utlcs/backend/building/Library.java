package com.gameonjava.utlcs.backend.building;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Tile;


public class Library extends Building {
    private double BOOK;

    public Library(Tile tile, double BOOK) {
        super(tile);
        this.BOOK = BOOK;
        name = "Library";
    }
    public Library(){
        super();
        this.name = "Library";
    }

    @Override
    public void produce(Player player) {
        double bookProduced = BOOK;
        if(level == 2){
            bookProduced *= 2;
        }
        if(level == 3){
            bookProduced *= 3;
        }
        player.addScience(bookProduced);
    }
    @Override
    public void write(Json json) {
        super.write(json);
        json.writeValue("BOOK", BOOK);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
        this.BOOK = jsonData.getDouble("BOOK");
    }

}
