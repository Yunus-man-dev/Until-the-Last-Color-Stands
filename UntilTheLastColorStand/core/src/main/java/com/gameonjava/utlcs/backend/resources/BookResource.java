package com.gameonjava.utlcs.backend.resources;

/*Book is a subclass of Resource class. Calculation of Technology Point has
been calculated in Player class. */
public class BookResource extends Resource {
    public double TECHNOLOGY_MULTIPLIER;
    
    public BookResource(int value, double tech) {
        super(value);
        TECHNOLOGY_MULTIPLIER = tech;
    }
    public BookResource() {
        super();
    }

    @Override
    public void initializeConstants() {

    }

    public double calculateTP(){
        return 1+value * TECHNOLOGY_MULTIPLIER;
    }

}
