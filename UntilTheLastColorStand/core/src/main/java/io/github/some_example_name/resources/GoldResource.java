package io.github.some_example_name.resources;
/*Gold class is a subclass of Resource class, which has necessary variable
and methods for Gold related actions, such as amount of gold that is necessary for
constructing a building */

public class GoldResource extends Resource {
    public final int RECRUIT;
    public final int CONSTRUCT;
    public final int DEVELOP;
    public final int REMOVE;

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

}
