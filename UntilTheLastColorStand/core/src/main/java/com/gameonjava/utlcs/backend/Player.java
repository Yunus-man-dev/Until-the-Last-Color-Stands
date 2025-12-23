package com.gameonjava.utlcs.backend;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.gameonjava.utlcs.backend.Enum.BuildingType;
import com.gameonjava.utlcs.backend.Enum.TerrainType;
import com.gameonjava.utlcs.backend.building.Building;
import com.gameonjava.utlcs.backend.building.Farm;
import com.gameonjava.utlcs.backend.building.GoldMine;
import com.gameonjava.utlcs.backend.building.Library;
import com.gameonjava.utlcs.backend.building.Port;
import com.gameonjava.utlcs.backend.civilization.Civilization;
import com.gameonjava.utlcs.backend.resources.BookResource;
import com.gameonjava.utlcs.backend.resources.FoodResource;
import com.gameonjava.utlcs.backend.resources.GoldResource;
import com.gameonjava.utlcs.backend.resources.MovementPoint;


public class Player implements com.badlogic.gdx.utils.Json.Serializable{

    private String name;
    private Civilization civilization;
    private FoodResource food;
    private GoldResource gold;
    private MovementPoint movementPoint;
    private BookResource book;
    private ArrayList<Tile> ownedTiles = new ArrayList<>();
    private boolean isActive;
    private double technologyPoint;

    private int soldiersPerTileLimit;

    public Player(String name, Civilization civilization) {
        this.name = name;
        this.civilization = civilization;
        this.isActive = true;
        this.technologyPoint = 1;

        this.gold = civilization.getStartingGold();
        this.food = civilization.getStartingFood();
        this.movementPoint = civilization.getStartingMP();
        this.book = civilization.getStartingBook();

        if (civilization instanceof com.gameonjava.utlcs.backend.civilization.Red) {
            this.soldiersPerTileLimit = 10;
        } else {
            this.soldiersPerTileLimit = 5;
        }
    }

    public Player() {}

    public FoodResource getFood() {
        return food;
    }

    public GoldResource getGold() {
        return gold;
    }

    public MovementPoint getMp() {
        return movementPoint;
    }

    public BookResource getBook() {
        return book;
    }

    public double getTechnologyPoint() {
        return technologyPoint;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return isActive;
    }

    public ArrayList<Tile> getOwnedTiles() {
        return ownedTiles;
    }

    public Civilization getCivilization() {
        return civilization;
    }

    public void addTile(Tile t) {
        if (t != null && !ownedTiles.contains(t)) {
            ownedTiles.add(t);
            t.setOwner(this);
        }
    }

    public void removeTile(Tile t) {
        if (t != null) {
            ownedTiles.remove(t);

            if (ownedTiles.isEmpty()) {
                eliminate();
            }
        }
    }

    public void updateResources() {

        int foodConsumption = 0;

        for (Tile tile : ownedTiles) {
            if (tile.hasBuilding()) {
                Building building = tile.getBuilding();
                building.produce(this);
            }

            foodConsumption += tile.calculateFoodConsumption(); // Tile classında bu metod yoksa hata verebilir
        }
        // Buildinglerden gelen sourceleri hesapla



        food.reduceResource(foodConsumption);

        technologyPoint =  this.book.calculateTP();
        movementPoint.setValue(movementPoint.updateMovementPoint(technologyPoint))  ;
    }

    public int getTileCount() {
        return ownedTiles.size();
    }

    public boolean constructBuilding(Tile t, BuildingType bt) {
        if (!ownedTiles.contains(t)) {
            return false;
        }

        if (t.getTerrainType() == TerrainType.MOUNTAIN || t.getTerrainType() == TerrainType.DEEP_WATER) {
            return false;
        }

        if (t.hasBuilding()) {
            return false;
        }

        if (!gold.checkForResource(gold.CONSTRUCT)) {
            return false;
        }

        if (!movementPoint.checkForResource(movementPoint.CONSTRUCT)) {
            return false;
        }
        gold.reduceResource(gold.CONSTRUCT);
        movementPoint.reduceResource(movementPoint.CONSTRUCT);

        Building building = null;
        if(bt == BuildingType.FARM){
            building = new Farm(t,this.getCivilization().FARM_FOOD);
        }else if(bt == BuildingType.GOLD_MINE){
            building = new GoldMine(t,this.getCivilization().MINE_GOLD);
        }else if(bt == BuildingType.PORT){
            building = new Port(t,this.getCivilization().PORT_FOOD,this.getCivilization().PORT_GOLD);
        }else if(bt == BuildingType.LIBRARY){
            building = new Library(t,this.getCivilization().BOOK);
        }
        t.setBuilding(building);
        return true;
    }

    public void developBuilding(Tile t) {
        if (!ownedTiles.contains(t)) {
            return;
        }

        if (!t.hasBuilding()) {
            return;
        }

        Building building = t.getBuilding();

        if (!gold.checkForResource(gold.DEVELOP)) {
            return;
        }

        if (!movementPoint.checkForResource(movementPoint.UPGRADE)) {
            return;
        }

        gold.reduceResource(gold.DEVELOP);
        movementPoint.reduceResource(movementPoint.UPGRADE);

        building.upgrade();
    }

    public void recruitSoldiers(Tile t, int amount) {
        if (!ownedTiles.contains(t)) {
            return;
        }

        if (amount <= 0) {
            return;
        }
        if (t.getRecruitedThisTurn() + amount > this.soldiersPerTileLimit) {
            return;
        }


        int currentSoldiers = t.hasArmy() ? t.getArmy().getSoldiers() : 0;

        if (currentSoldiers + amount > soldiersPerTileLimit) {
            return;
        }

        int totalFoodCost = (int) food.RECRUIT * amount;
        int totalGoldCost = (int) gold.RECRUIT * amount;
        int totalMPCost = (int) movementPoint.RECRUIT * amount;

        if (!food.checkForResource(totalFoodCost)) {
            return;
        }

        if (!gold.checkForResource(totalGoldCost)) {
            return;
        }

        if (!movementPoint.checkForResource(totalMPCost)) {
            return;
        }

        food.reduceResource(totalFoodCost);
        gold.reduceResource(totalGoldCost);
        movementPoint.reduceResource(totalMPCost);

        if (t.hasArmy()) {
            t.getArmy().addSoldiers(amount);
        } else {
            Army newArmy = new Army(amount, this, t);
            t.setArmy(newArmy);
        }
        t.addRecruitedCount(amount);
    }

    public void eliminate() {
        isActive = false;

        for (Tile tile : new ArrayList<>(ownedTiles)) {
            tile.setOwner(null);
            tile.removeArmy();
            tile.removeBuilding();
        }
        ownedTiles.clear();
    }

    public boolean hasWon() {
        return civilization.checkWinCondition(this);
    }

    public void addFood(double amount) {
        food.addResource(amount);
    }

    public void addGold(double amount) {
        gold.addResource(amount);
    }

    public void addScience(double amount) {
        book.addResource(amount);
    }

    @Override
    public void write(Json json) {
        json.writeValue("Name", name);
        json.writeValue("Civilization", civilization, civilization.getClass());

        /*if (civilization != null) {
            json.writeValue("Civilization", civilization, civilization.getClass());
        } else {
            json.writeValue("Civilization", (Civilization) null);
        }*/
        json.writeValue("Food", food);
        json.writeValue("Gold", gold);
        json.writeValue("Book", book);
        json.writeValue("MP", movementPoint);
        json.writeValue("OwnedTiles", ownedTiles);
        json.writeValue("Activity", isActive);
        json.writeValue("TechPoint", technologyPoint);
    }

    // Add this new helper method
    public void relinkTiles() {
        if (ownedTiles != null) {
            for (Tile t : ownedTiles) {
                t.setOwner(this);
            }
        }
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        name = json.readValue("Name", String.class, jsonData);
        civilization = json.readValue("Civilization", com.gameonjava.utlcs.backend.civilization.Civilization.class, jsonData);
        food = json.readValue("Food", com.gameonjava.utlcs.backend.resources.FoodResource.class, jsonData);
        gold = json.readValue("Gold", com.gameonjava.utlcs.backend.resources.GoldResource.class, jsonData);
        book = json.readValue("Book", com.gameonjava.utlcs.backend.resources.BookResource.class, jsonData);
        movementPoint = json.readValue("MP", com.gameonjava.utlcs.backend.resources.MovementPoint.class, jsonData);
        ownedTiles = json.readValue("OwnedTiles", java.util.ArrayList.class, Tile.class, jsonData);
        isActive = json.readValue("Activity", boolean.class, jsonData);
        technologyPoint = jsonData.getInt("TechPoint", 0);
        relinkTiles();
    }
}
