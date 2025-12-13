package io.github.some_example_name.resources;
/*Gold class is a subclass of Resource class, which has necessary variable
and methods for Gold related actions, such as amount of gold that is necessary for
constructing a building */

public class GoldResource extends Resource {
    private final int RECRUIT;
    private final int CONSTRUCT;
    private final int DEVELOP;
    private final int REMOVE;

    public GoldResource(int value , int RECRUIT, int CONSTRUCT, int DEVELOP, int REMOVE) {
        super(value);
        this.RECRUIT = RECRUIT;
        this.CONSTRUCT = CONSTRUCT;
        this.DEVELOP = DEVELOP;
        this.REMOVE = REMOVE;
    }

    @Override
    public void initializeConstants() {
        
    }

    public int getRECRUIT() {
        return RECRUIT;
    }
    public int getCONSTRUCT() {
        return CONSTRUCT;
    }
    public int getDEVELOP() {
        return DEVELOP;
    }
    public int getREMOVE() {
        return REMOVE;
    }
}
