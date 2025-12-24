package com.gameonjava.utlcs.backend;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

// civilizations to war each other and defend their empire or expand their borders. It will
// be used in War Manager class that handles battles between civilizations.
public class Army implements com.badlogic.gdx.utils.Json.Serializable{
    Player player;
    private int numberOfSoldiers;
    Tile tile;

    private int movesMadeThisTurn = 0;

    public int getMovesMadeThisTurn() {
        return movesMadeThisTurn;
    }

    public void setMovesMadeThisTurn(int moves) {
        this.movesMadeThisTurn = moves;
    }

    public void incrementMoves() {
        this.movesMadeThisTurn++;
    }

    // Tur bittiğinde çağrılacak
    public void resetTurnData() {
        this.movesMadeThisTurn = 0;
    }


    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }


    // Player player;

    //player will be in the constructor
    public Army(int numberOfSoldiers, Player player, Tile tile){

        this.numberOfSoldiers = numberOfSoldiers;
        this.player = player;
        this.tile = tile;
    }

    public Army() {}
    public void setSoldiers(int numberOfSoldiers) {
        this.numberOfSoldiers = numberOfSoldiers;
    }

    public int getSoldiers(){
        return numberOfSoldiers;
    }
    // adds soldiers to army in
    //recruiting and merge situations
    public void addSoldiers(int amount){

        numberOfSoldiers += amount;

    }

    public void removeSoldiers(int amount){

        numberOfSoldiers -= amount;

    }

    public Player getPlayer(){
        return player;
    }
    @Override
    public void write(Json json) {
        // json.writeValue("Player", player);
        json.writeValue("NumSoldiers", numberOfSoldiers);
        // json.writeValue("Tile", tile);
        json.writeValue("Moves", movesMadeThisTurn);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        // player = json.readValue("Player", Player.class, jsonData);
        numberOfSoldiers = jsonData.getInt("NumSoldiers", 0);
        // tile = json.readValue("Tile", Tile.class, jsonData);
        numberOfSoldiers = jsonData.getInt("NumSoldiers", 0);
        movesMadeThisTurn = jsonData.getInt("Moves", 0);
    }


}
