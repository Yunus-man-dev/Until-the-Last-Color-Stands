package com.gameonjava.utlcs.backend.resources;

public class MovementPoint extends Resource {

    public final double MOVE;
    public final double UPGRADE;
    public final double CONSTRUCT;
    public final double TRADE;
    public final double RECRUIT;
    public final double ATTACK;
    private final double baseLimit;

    public MovementPoint(int value, double MOVE, double UPGRADE, double CONSTRUCT, double TRADE, double RECRUIT, double ATTACK) {
        super(value);
        this.baseLimit = value;
        this.MOVE = MOVE;
        this.UPGRADE = UPGRADE;
        this.CONSTRUCT = CONSTRUCT;
        this.TRADE = TRADE;
        this.RECRUIT = RECRUIT;
        this.ATTACK = ATTACK;
    }
    public int updateMovementPoint(double techPoints){
        this.setValue(this.baseLimit);

        return (int) this.value;
    }
    @Override
    public void initializeConstants() {

    }
}
