package com.gameonjava.utlcs.backend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.gameonjava.utlcs.backend.building.Farm;
import com.gameonjava.utlcs.backend.building.GoldMine;
import com.gameonjava.utlcs.backend.building.Library;
import com.gameonjava.utlcs.backend.building.Port;
import com.gameonjava.utlcs.backend.civilization.Black;
import com.gameonjava.utlcs.backend.civilization.Blue;
import com.gameonjava.utlcs.backend.civilization.Brown;

public class SaveLoad {

    private Json json;

    public SaveLoad() {
        json = new Json();
        json.setOutputType(OutputType.json);

        json.addClassTag("Blue", Blue.class);
        json.addClassTag("Brown", Brown.class);
        json.addClassTag("Black", Black.class);
        json.addClassTag("Farm", Farm.class);
        json.addClassTag("GoldMine", GoldMine.class);
        json.addClassTag("Library", Library.class);
        json.addClassTag("Port", Port.class);
    }

    public void save(Game game, String filename) {
        try {
            FileHandle file = Gdx.files.local(filename);
            String gameState = json.toJson(game);
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
