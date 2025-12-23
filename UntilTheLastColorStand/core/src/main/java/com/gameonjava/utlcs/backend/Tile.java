package com.gameonjava.utlcs.backend;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.gameonjava.utlcs.backend.Enum.TerrainType;
import com.gameonjava.utlcs.backend.building.Building;
import com.gameonjava.utlcs.backend.civilization.Brown;

public class Tile implements com.badlogic.gdx.utils.Json.Serializable{
    private int q;
    private int r;

    private float x;
    private float y;
    public boolean highlight;
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

    public Tile() {}

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

        if(terrainName == TerrainType.DEEP_WATER && terrainName == TerrainType.MOUNTAIN && terrainName == TerrainType.WATER)
            return;
        // Eklenemeyecegi veya hareket edemeyecegi ile alakali bir sey diyeblirz

        this.owner = owner;
    }
    // Tile.java -> resetTurnData metodu:

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

    public void setRecruitedThisTurn(int recruitedThisTurn) {
        this.recruitedThisTurn = recruitedThisTurn;
    }

    public void addRecruitedCount(int amount) {
        this.recruitedThisTurn += amount;
    }

    public void resetTurnData() {
        this.recruitedThisTurn = 0;
        if (this.army != null) {
            this.army.resetTurnData();
        }
    }


    public float getPixelX() { return x; }
    public void setX(float pixelX) { this.x = pixelX; }

    public float getPixelY() { return y; }
    public void setY(float pixelY) { this.y = pixelY; }

    @Override
    public void write(Json json) {
        json.writeValue("q", q);
        json.writeValue("r", r);
        json.writeValue("Terrain", terrainName);

        json.writeValue("Building", building, null);
        json.writeValue("Army", army);
        json.writeValue("ConsumptionRate", soldierConsumptionRate);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
       
        // q = jsonData.getInt("q", 0);
        // r = jsonData.getInt("r", 0);
        // terrainName = json.readValue("Terrain", com.gameonjava.utlcs.backend.Enum.TerrainType.class, jsonData);
        
        // // 1. Bina Bağlantısı (Bunu zaten yapmıştık)
        // building = json.readValue("Building", null, jsonData); 
        // if (building != null) {
        //     building.setTile(this);
        // }
        
        // // 2. ORDU BAĞLANTISI (YENİ KISIM)
        // army = json.readValue("Army", com.gameonjava.utlcs.backend.Army.class, jsonData);
        // if (army != null) {
        //     army.setTile(this); // Orduya "Senin tile'ın benim" diyoruz.
        //     // Not: Player bağlantısını Player.java'da yapacağız.
        // }

        // soldierConsumptionRate = jsonData.getDouble("ConsumptionRate", 1.0);
        q = jsonData.getInt("q", 0);
        r = jsonData.getInt("r", 0);
        terrainName = json.readValue("Terrain", com.gameonjava.utlcs.backend.Enum.TerrainType.class, jsonData);
        
        // 1. Bina Bağlantısı
        building = json.readValue("Building", null, jsonData); 
        if (building != null) {
            building.setTile(this);
        }
        
        // 2. ORDU BAĞLANTISI VE TEMİZLİĞİ (DÜZELTME BURADA)
        army = json.readValue("Army", com.gameonjava.utlcs.backend.Army.class, jsonData);
        
        // EĞER ASKER SAYISI 0 İSE ORDUYU YOK ET (GHOST ARMY FIX)
        if (army != null) {
            if (army.getSoldiers() <= 0) {
                army = null; // Boş orduyu sil
            } else {
                army.setTile(this); // Doluysa tile'ını ata
            }
        }

        soldierConsumptionRate = jsonData.getDouble("ConsumptionRate", 1.0);
    }
}

