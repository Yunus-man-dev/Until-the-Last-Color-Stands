package io.github.some_example_name.resources;
/*MovementPoint is a subclass of Resource class and represents the
Movement Point that is the limit of a player can do in one turn as terms of actions, such
as upgrading a building etc. This limit can be changed by the amount of TP, which is
calculated based on amount of books */
public class MovementPoint extends Resource {
    public final double MOVE = 1.0;
    public final double UPGRADE = 1.0;
    public final double CONSTRUCT = 1.0;
    public final double TRADE = 1.0;
    public final double RECRUIT = 1.0;
    public final double ATTACK = 1.0;

    public MovementPoint(int value) {
        super(value);
    }


    public void updateMovementPoint(int techPoints){
        /* Updates the movement point based on tech points, techPoints/10 is just an constant you can edit
        that 10 whatever you want it to be */
        int additionalMovementPoint = techPoints / 10;
        this.addResource(additionalMovementPoint);
    }
    
    @Override
    public void initializeConstants() {

    }
}
