package com.gameonjava.utlcs.backend.resources;
/*Food class is a subclass of Resource class, which has necessary variable
and methods for food related features */

public class FoodResource extends Resource {
    public final double RECRUIT;
    public final double TILE;
    public final double MAINTAIN;

    public FoodResource(int value,double RECRUIT, double TILE, double MAINTAIN) {
        super(value);
        this.RECRUIT = RECRUIT;
        this.TILE = TILE;
        this.MAINTAIN = MAINTAIN;

    }

    @Override
    public void initializeConstants() {

    }

}
