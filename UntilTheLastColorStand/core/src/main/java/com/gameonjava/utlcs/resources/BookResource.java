package com.gameonjava.utlcs.resources;
/*Book is a subclass of Resource class. Calculation of Technology Point has
been calculated in Player class. */
public class BookResource extends Resource {
    public final double TECHNOLOGY_MULTIPLIER;
    public BookResource(int value, double tech) {
        super(value);
        TECHNOLOGY_MULTIPLIER = tech;
    }
    @Override
    public void initializeConstants() {

    }
    public double calculateTP(){
        return value * TECHNOLOGY_MULTIPLIER;
    }

}
