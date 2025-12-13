package io.github.some_example_name.resources;
/*MovementPoint is a subclass of Resource class and represents the
Movement Point that is the limit of a player can do in one turn as terms of actions, such
as upgrading a building etc. This limit can be changed by the amount of TP, which is
calculated based on amount of books */
public class MovementPoint extends Resource {
    public final int MOVE;
    public final int UPGRADE;
    public final int CONSTRUCT;
    public final int TRADE;
    public final int RECRUIT;
    public final int ATTACK;

    public MovementPoint(int value, int MOVE, int UPGRADE, int CONSTRUCT, int TRADE, int RECRUIT, int ATTACK) {
        super(value);
        this.MOVE = MOVE;
        this.UPGRADE = UPGRADE;
        this.CONSTRUCT = CONSTRUCT;
        this.TRADE = TRADE;
        this.RECRUIT = RECRUIT;
        this.ATTACK = ATTACK;
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
