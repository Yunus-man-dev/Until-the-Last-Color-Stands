package io.github.some_example_name.resources;
/*MovementPoint is a subclass of Resource class and represents the
Movement Point that is the limit of a player can do in one turn as terms of actions, such
as upgrading a building etc. This limit can be changed by the amount of TP, which is
calculated based on amount of books */
public class MovementPoint extends Resource {
    private final int MOVE;
    private final int UPGRADE;
    private final int CONSTRUCT;
    private final int TRADE;
    private final int RECRUIT;
    private final int ATTACK;

    public MovementPoint(int value, int MOVE, int UPGRADE, int CONSTRUCT, int TRADE, int RECRUIT, int ATTACK) {
        super(value);
        this.MOVE = MOVE;
        this.UPGRADE = UPGRADE;
        this.CONSTRUCT = CONSTRUCT;
        this.TRADE = TRADE;
        this.RECRUIT = RECRUIT;
        this.ATTACK = ATTACK;
    }

    public int getMOVE() {
        return MOVE;
    }
    public int getUPGRADE() {
        return UPGRADE;
    }
    public int getCONSTRUCT() {
        return CONSTRUCT;
    }
    public int getTRADE() {
        return TRADE;
    }
    public int getRECRUIT() {
        return RECRUIT;
    }
    public int getATTACK() {
        return ATTACK;
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
