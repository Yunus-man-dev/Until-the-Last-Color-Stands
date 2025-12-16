package io.github.some_example_name;
import java.util.ArrayList;

import io.github.some_example_name.Enum.*;
import io.github.some_example_name.building.*;
import io.github.some_example_name.resources.*;
import io.github.some_example_name.civilization.*;
import io.github.some_example_name.Tile.*;

public class Player {

    private String name;
    private Civilization civilization;
    private FoodResource food;
    private GoldResource gold;
    private MovementPoint movementPoint;
    private BookResource book;
    private ArrayList<Tile> ownedTiles;
    private boolean isActive;
    private int technologyPoint;

    private int soldiersPerTileLimit;

    public Player (String name, Civilization civilization){
        this.name = name;
        this.civilization = civilization;
        this.isActive = true;
        this.technologyPoint = 0;

        this.gold =  civilization.getStartingGold();
        this.food = civilization.getStartingFood();
        this.movementPoint = civilization.getStartingMP();
        this.book = civilization.getStartingBook();

        soldiersPerTileLimit = civilization.getCivilizationName().equals("Red Civilization") || civilization.getCivilizationName().equals("Dark Red Civilization") ? 15 : 10;
    }
    public FoodResource getFood() {
        return food;
    }

    public GoldResource getGold() {
        return gold;
    }

    public MovementPoint getMp() {
        return movementPoint;
    }

    public BookResource getBook(){
        return book;
    }

    public int getTechnologyPoint(){
        return technologyPoint;
    }
    
    public String getName(){
        return name;
    }

    public boolean isActive(){
        return isActive;
    }

    public ArrayList<Tile> getOwnedTiles(){
        return ownedTiles;
    }

    public Civilization getCivilization() {
        return civilization;
    }
    public void addTile(Tile t){
        if(t != null && !ownedTiles.contains(t)){
            ownedTiles.add(t);
            t.setOwner(this);
        }
    }
    public void removeTile(Tile t) {
        if(t != null) {
            ownedTiles.remove(t);

            if(ownedTiles.isEmpty()){
                eliminate();
            }
        }
    }
    public void updateResources(){

        int foodConsumption = 0;

        for(Tile tile : ownedTiles) {
            if(tile.hasBuilding()){
                Building building = tile.getBuilding();
                building.produce(this);
            }

            foodConsumption += tile.calculateFoodConsumption();
        }

        food.reduceResource(foodConsumption);

        technologyPoint = (int) (book.getValue() * civilization.getTechnologyMultiplier());
        movementPoint.updateMovementPoint(technologyPoint);
    }

    public int getTileCount(){
        return ownedTiles.size();
    }

    public boolean constructBuilding (Tile t, BuildingType bt) {
        if (!ownedTiles.contains(t)){
            return false;
        }

        if(t.getTerrainType() == TerrainType.MOUNTAIN || t.getTerrainType() == TerrainType.DEEP_WATER){
            return false;
        }

        if(t.hasBuilding()){
            return false;
        }

        if(!gold.checkForResource(gold.CONSTRUCT)) {
            return false;
        }

        if(!movementPoint.checkForResource(movementPoint.CONSTRUCT)){
            return false;
        }
        gold.reduceResource(gold.CONSTRUCT);
        movementPoint.reduceResource(movementPoint.CONSTRUCT);

        Building building = null;
        if(bt == BuildingType.FARM){
            building = new Farm();
        }else if(bt == BuildingType.GOLD_MINE){
            building = new GoldMine();
        }else if(bt == BuildingType.PORT){
            building = new Port();
        }else if(bt == BuildingType.LIBRARY){
            building = new Library();
        }
        t.setBuilding(building);
        return true;
    }

    public void developBuilding(Tile t){
        if(!ownedTiles.contains(t)) {
            return;
        }
        
        if(!t.hasBuilding()){
            return;
        }

        Building building = t.getBuilding();

        if(!gold.checkForResource(gold.DEVELOP)){
            return;
        }

        if(!movementPoint.checkForResource(movementPoint.UPGRADE)){
            return;
        }

        gold.reduceResource(gold.DEVELOP);
        movementPoint.reduceResource(movementPoint.UPGRADE);

        building.upgrade();
    }

    public void recruitSoldiers(Tile t, int amount){
        if(!ownedTiles.contains(t)){
            return;
        }

        if(amount <= 0){
            return;
        }

        int currentSoldiers = t.getSoldierCount();
        if(currentSoldiers + amount > soldiersPerTileLimit){
            return;
        }

        int totalFoodCost = food.RECRUIT * amount;
        int totalGoldCost = gold.RECRUIT * amount;
        int totalMPCost = movementPoint.RECRUIT * amount;

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

        if(t.hasArmy()) {
            t.getArmy().addSoldiers(amount);
        }else{
            Army newArmy = new Army(amount, this, t);
            t.setArmy(newArmy);
        }
    }

    public void eliminate(){
        isActive = false;

        for(Tile tile : new ArrayList<>(ownedTiles)){
            tile.setOwner(null);
            tile.removeArmy();
            tile.removeBuilding();
        }
        ownedTiles.clear();
    }
    public boolean hasWon(){
        return civilization.checkWinCondition(this);
    }

    public void addFood(int amount){
        food.setValue(amount);
    }

    public void addGold(int amount){
        gold.setValue(amount);
    }

    public void addScience(int amount){
        book.setValue(amount);
    }
    
}
