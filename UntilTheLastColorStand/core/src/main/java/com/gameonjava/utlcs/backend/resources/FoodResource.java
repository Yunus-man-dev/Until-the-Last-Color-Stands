package com.gameonjava.utlcs.backend.resources;
/*Food class is a subclass of Resource class, which has necessary variable
and methods for food related features */

public class FoodResource extends Resource {
    public double RECRUIT;
    public double TILE;
    public double MAINTAIN;

    public FoodResource(int value,double RECRUIT, double TILE, double MAINTAIN) {
        super(value);
        this.RECRUIT = RECRUIT;
        this.TILE = TILE;
        this.MAINTAIN = MAINTAIN;

    }
    public FoodResource(){
        super();
    }

    @Override
    public void initializeConstants() {

    }

}
