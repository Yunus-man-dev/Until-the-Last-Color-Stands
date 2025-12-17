package io.github.some_example_name.resources;
/*Gold class is a subclass of Resource class, which has necessary variable
and methods for Gold related actions, such as amount of gold that is necessary for
constructing a building */

public class GoldResource extends Resource {
    public final double RECRUIT = 10.0;
    public final double CONSTRUCT = 50.0;
    public final double DEVELOP = 100.0;
    public final double REMOVE = 10.0;

    public GoldResource(int value) {
        super(value);
    }

    @Override
    public void initializeConstants() {

    }

}
