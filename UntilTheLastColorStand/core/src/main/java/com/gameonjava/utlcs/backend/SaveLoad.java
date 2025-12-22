package com.gameonjava.utlcs.backend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

public class SaveLoad {
    private Json json;

    public SaveLoad() {
        json = new Json();
        json.setOutputType(OutputType.json);
        
        // CRITICAL: This enables object ID tracking. 
        // Instead of writing the same Player 100 times, it writes an ID and stops the loop.
        json.setUsePrototypes(false); 

        // Register all your tags
        json.addClassTag("Blue", com.gameonjava.utlcs.backend.civilization.Blue.class);
        json.addClassTag("Cyan", com.gameonjava.utlcs.backend.civilization.Cyan.class);
        json.addClassTag("Red", com.gameonjava.utlcs.backend.civilization.Red.class);
        json.addClassTag("DarkRed", com.gameonjava.utlcs.backend.civilization.DarkRed.class);
        json.addClassTag("Gold", com.gameonjava.utlcs.backend.civilization.GoldCivilization.class);
        json.addClassTag("Orange", com.gameonjava.utlcs.backend.civilization.Orange.class);
        json.addClassTag("Brown", com.gameonjava.utlcs.backend.civilization.Brown.class);
        json.addClassTag("Gray", com.gameonjava.utlcs.backend.civilization.Gray.class);
        json.addClassTag("Farm", com.gameonjava.utlcs.backend.building.Farm.class);
        json.addClassTag("GoldMine", com.gameonjava.utlcs.backend.building.GoldMine.class);
        json.addClassTag("Library", com.gameonjava.utlcs.backend.building.Library.class);
        json.addClassTag("Port", com.gameonjava.utlcs.backend.building.Port.class);
    }

    public void save(Game game, String filename) {
        try {
            FileHandle file = Gdx.files.local(filename);
            // Use json.toJson to ensure the object graph is handled correctly
            String gameState = json.prettyPrint(game); 
            file.writeString(gameState, false);
            System.out.println("Game saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Game load(String filename) {
        try {
            FileHandle file = Gdx.files.local(filename);
            if (file.exists()) {
                return json.fromJson(Game.class, file.readString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}