package com.gameonjava.utlcs.backend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class SaveLoad {
    private Json json;

    public SaveLoad() {
        json = new Json();
        json.setOutputType(com.badlogic.gdx.utils.JsonWriter.OutputType.json);
        json.setUsePrototypes(false);
        json.setTypeName("class");

        /*json.setSerializer(Player.class, new Json.Serializer<Player>() {
            @Override
            public void write(Json json, Player player, Class knownType) {
                json.writeObjectStart();
                player.write(json);
                json.writeObjectEnd();
            }

            @Override
            public Player read(Json json, JsonValue jsonData, Class type) {
                Player p = new Player();
                p.read(json, jsonData);
                return p;
            }
        });*/

        json.addClassTag("Player", com.gameonjava.utlcs.backend.Player.class);
        json.addClassTag("Tile", com.gameonjava.utlcs.backend.Tile.class);
        json.addClassTag("Game", com.gameonjava.utlcs.backend.Game.class);
        json.addClassTag("Army", com.gameonjava.utlcs.backend.Army.class);
        json.addClassTag("Map", com.gameonjava.utlcs.backend.Map.class);
        json.addClassTag("Trade", com.gameonjava.utlcs.backend.Trade.class);

        json.addClassTag("Civilization", com.gameonjava.utlcs.backend.civilization.Civilization.class);
        json.addClassTag("Blue", com.gameonjava.utlcs.backend.civilization.Blue.class);
        json.addClassTag("Red", com.gameonjava.utlcs.backend.civilization.Red.class);
        json.addClassTag("GoldCivilization", com.gameonjava.utlcs.backend.civilization.GoldCivilization.class);
        json.addClassTag("Gray", com.gameonjava.utlcs.backend.civilization.Gray.class);
        json.addClassTag("Brown", com.gameonjava.utlcs.backend.civilization.Brown.class);
        json.addClassTag("Cyan", com.gameonjava.utlcs.backend.civilization.Cyan.class);
        json.addClassTag("DarkRed", com.gameonjava.utlcs.backend.civilization.DarkRed.class);
        json.addClassTag("Orange", com.gameonjava.utlcs.backend.civilization.Orange.class);

        json.addClassTag("Building", com.gameonjava.utlcs.backend.building.Building.class);
        json.addClassTag("Farm", com.gameonjava.utlcs.backend.building.Farm.class);
        json.addClassTag("Port", com.gameonjava.utlcs.backend.building.Port.class);
        json.addClassTag("Library", com.gameonjava.utlcs.backend.building.Library.class);
        json.addClassTag("GoldMine", com.gameonjava.utlcs.backend.building.GoldMine.class);

        json.addClassTag("Resource", com.gameonjava.utlcs.backend.resources.Resource.class);
        json.addClassTag("FoodResource", com.gameonjava.utlcs.backend.resources.FoodResource.class);
        json.addClassTag("GoldResource", com.gameonjava.utlcs.backend.resources.GoldResource.class);
        json.addClassTag("BookResource", com.gameonjava.utlcs.backend.resources.BookResource.class);
        json.addClassTag("MovementPoint", com.gameonjava.utlcs.backend.resources.MovementPoint.class);
    }

    public void save(Game game, String filename) {
        try {
            FileHandle file = Gdx.files.local(filename);
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
