package com.gameonjava.utlcs.backend;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.gameonjava.utlcs.backend.Enum.TerrainType;
import com.gameonjava.utlcs.backend.building.Building;
import com.gameonjava.utlcs.backend.civilization.Brown;

public class Tile {
    private int q;
    private int r;
    private TerrainType terrainName;
    private double defenseBonus;
    private Player owner;
    private Building building;
    private Army army;
    private int recruitedThisTurn;

    private double soldierConsumptionRate;
    // Constants

    // Tile's food consumption per round
    // private static final int FOOD_CONSUMPTION = ;

    public Tile(int q, int r, TerrainType terrainName) {
        this.q = q;
        this.r = r;
        this.terrainName = terrainName;
        this.owner = null;
        this.building = null;
        this.army = null;
        this.soldierConsumptionRate = 1;
        this.recruitedThisTurn=0;

        // Extra defence in forrest
        if (this.terrainName == TerrainType.FOREST) {
            this.defenseBonus = 1.5;
        } else {
            this.defenseBonus = 1.0;
        }
    }

    // Getter and Setter Methods
    public int getQ() {
        return q;
    }

    public int getR() {
        return r;
    }

    public TerrainType getTerrainType() {
        return terrainName;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Building getBuilding() {
        return building;
    }

    public int getSoldierCount() {
        if (army != null) {
            return army.getSoldiers();
        }
        return 0;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Army getArmy() {
        return army;
    }

    public void setArmy(Army army) {
        this.army = army;
    }

    public double getDefenseBonus() {
        return defenseBonus;
    }

    // This method checks if there is a building on that tile.
    public boolean hasBuilding() {
        return building != null;
    }

    // This method checks if there is a army on that tile.
    public boolean hasArmy() {
        return army != null;
    }

    // This method checks if the soldier can enter that tile.
    public boolean canUnitPass(Tile fromTile) {

        if (this.terrainName == TerrainType.MOUNTAIN || this.terrainName == TerrainType.DEEP_WATER) {
            return false;
        }

        return true;
    }

    // This method calculates food consumption per tile. [cite: 299]
    public double calculateFoodConsumption() {
        double consumption = owner.getCivilization().FTILE;
        if (hasArmy()) {
            // The amount of food consumed by the soldiers + the amount consumed by the tile
            // soldierConsumptionRate must be added
            if (this.getOwner().getCivilization() instanceof Brown) {
                soldierConsumptionRate = (int) (soldierConsumptionRate * Brown.getFoodConsumptionIncrease());
            }
            consumption = (int) (army.getSoldiers() * soldierConsumptionRate);
            return consumption;
        }
        return consumption;
    }

    // This method removes building
    public void removeBuilding() {
        this.building = null;
    }

    // This method will delete the army if the number of soldiers is 0.
    public void removeArmy() {
        if (army != null && army.getSoldiers() <= 0) {
            this.army = null;
        }
    }

    public boolean isNeighboor(Tile t) {

        boolean isNeighboor = false;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {

                if (q + i == t.q && r + j == t.r) {
                    return isNeighboor = true;

                }

            }
        }
        return isNeighboor;
    }

    public int getRecruitedThisTurn() {
        return recruitedThisTurn;
    }

    public void addRecruitedCount(int amount) {
        this.recruitedThisTurn += amount;
    }

    public void resetTurnData() {
        this.recruitedThisTurn = 0;
    }

    public void write(Json json) {
        json.writeValue("Terrain", terrainName);
        json.writeValue("Owner", owner);
        json.writeValue("Building", building);
        json.writeValue("Army", army);
        json.writeValue("ConsumptionRate", soldierConsumptionRate);
    }
    public void read(Json json, JsonValue jsonData) {
        terrainName = json.readValue("Terrain", TerrainType.class, jsonData);
        owner = json.readValue("Owner", Player.class, jsonData);
        building = json.readValue("Building", Building.class, jsonData);
        army = json.readValue("Army", Army.class, jsonData);
        soldierConsumptionRate = jsonData.getFloat("ConsumptionRate");
    }
}
