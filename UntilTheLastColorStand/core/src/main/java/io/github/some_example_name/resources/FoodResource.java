package io.github.some_example_name.resources;
/*Food class is a subclass of Resource class, which has necessary variable
and methods for food related features */

public class FoodResource extends Resource {
    public final int RECRUIT;
    public final int TILE;
    public final int MAINTAIN;

    public FoodResource(int value, int RECRUIT, int TILE, int MAINTAIN) {
        super(value);
        this.RECRUIT = RECRUIT;
        this.TILE = TILE;
        this.MAINTAIN = MAINTAIN;
    }
    
    @Override
    public void initializeConstants() {

    }
}