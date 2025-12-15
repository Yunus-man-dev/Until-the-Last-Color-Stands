package io.github.some_example_name;

import io.github.some_example_name.civilization.Civilization;

// The Army class represents the civilizations soldiers on tiles. It enables
// civilizations to war each other and defend their empire or expand their borders. It will
// be used in War Manager class that handles battles between civilizations.
public class Army {
    Player player;
    private int numberOfSoldiers;
    Tile tile;
    
    // Player player;

    //player will be in the constructor
    public Army(int numberOfSoldiers, Player player, Tile tile){

        this.numberOfSoldiers = numberOfSoldiers;
        this.player = player;
        this.tile = tile;
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
    

}
