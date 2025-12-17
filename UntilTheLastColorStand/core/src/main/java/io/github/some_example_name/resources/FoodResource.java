package io.github.some_example_name.resources;
/*Food class is a subclass of Resource class, which has necessary variable
and methods for food related features */

public class FoodResource extends Resource {
    public final double RECRUIT = 10.0;
    public final double TILE = 5.0;
    public final double MAINTAIN = 2.0;

    public FoodResource(int value) {
        super(value);        
    }
    
    @Override
    public void initializeConstants() {
        
    }
    
}