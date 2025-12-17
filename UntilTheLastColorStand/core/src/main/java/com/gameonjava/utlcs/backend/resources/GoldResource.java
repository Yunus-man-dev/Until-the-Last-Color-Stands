package com.gameonjava.utlcs.backend.resources;
/*Gold class is a subclass of Resource class, which has necessary variable
and methods for Gold related actions, such as amount of gold that is necessary for
constructing a building */

public class GoldResource extends Resource {
    public final double RECRUIT;
    public final double CONSTRUCT;
    public final double DEVELOP;
    public final double REMOVE;

    public GoldResource(int value , double RECRUIT, double CONSTRUCT,double DEVELOP, double REMOVE) {
        super(value);
        this.RECRUIT = RECRUIT;
        this.CONSTRUCT = CONSTRUCT;
        this.DEVELOP = DEVELOP;
        this.REMOVE = REMOVE;
    }

    @Override
    public void initializeConstants() {

    }

}
