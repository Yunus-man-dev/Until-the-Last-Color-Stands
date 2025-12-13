package io.github.some_example_name.resources;
/*Food class is a subclass of Resource class, which has necessary variable
and methods for food related features */

public class FoodResource extends Resource {
    private final int RECRUIT;
    private final int TILE;
    private final int MAINTAIN;

    public FoodResource(int value, int RECRUIT, int TILE, int MAINTAIN) {
        super(value);
        this.RECRUIT = RECRUIT;
        this.TILE = TILE;
        this.MAINTAIN = MAINTAIN;
    }
    
    public int getRECRUIT() {
        return RECRUIT;
    }
    public int getTILE() {
        return TILE;
    }
    public int getMAINTAIN() {
        return MAINTAIN;
    }
    @Override
    public void initializeConstants() {

    }
}