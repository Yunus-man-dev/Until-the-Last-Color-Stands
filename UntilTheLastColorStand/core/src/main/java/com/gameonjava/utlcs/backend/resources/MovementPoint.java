package com.gameonjava.utlcs.backend.resources;

public class MovementPoint extends Resource {

    public double MOVE;
    public double UPGRADE;
    public double CONSTRUCT;
    public double TRADE;
    public double RECRUIT;
    public double ATTACK;
    private double baseLimit;

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
    public MovementPoint() {
        super();
    }
    
    public int updateMovementPoint(double techPoints){
        this.setValue(this.baseLimit);

        return (int) this.value;
    }
    @Override
    public void initializeConstants() {

    }
}
