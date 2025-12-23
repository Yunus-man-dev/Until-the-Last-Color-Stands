package com.gameonjava.utlcs.backend.resources;
/*MovementPoint is a subclass of Resource class and represents the
Movement Point that is the limit of a player can do in one turn as terms of actions, such
as upgrading a building etc. This limit can be changed by the amount of TP, which is
calculated based on amount of books */
public class MovementPoint extends Resource {
    public final double MOVE;
    public final double UPGRADE;
    public final double CONSTRUCT;
    public final double TRADE;
    public final double RECRUIT;
    public final double ATTACK;

    public MovementPoint(int value, double MOVE, double UPGRADE,double CONSTRUCT, double TRADE, double RECRUIT, double ATTACK) {
        super(value);
        this.MOVE = MOVE;
        this.UPGRADE = UPGRADE;
        this.CONSTRUCT = CONSTRUCT;
        this.TRADE = TRADE;
        this.RECRUIT = RECRUIT;
        this.ATTACK = ATTACK;
    }


    public void updateMovementPoint(double techPoints){
        /* Updates the movement point based on tech points, techPoints/10 is just an constant you can edit
        that 10 whatever you want it to be */
        double additionalMovementPoint = techPoints / 10;
        this.addResource(additionalMovementPoint);
    }

    @Override
    public void initializeConstants() {

    }
}
