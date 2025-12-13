package io.github.some_example_name;
import java.util.ArrayList;

import io.github.some_example_name.building.Building;
import io.github.some_example_name.building.Farm;
import io.github.some_example_name.building.GoldMine;
import io.github.some_example_name.building.Library;
import io.github.some_example_name.building.Port;
import io.github.some_example_name.resources.FoodResource;
import io.github.some_example_name.resources.GoldResource;
import io.github.some_example_name.resources.MovementPoint;

public class Player {

    private String name;
    private Civilization civilization;
    private FoodResource food;
    private GoldResource gold;
    private MovementPoint movementPoint;
    private ArrayList<Tile> ownedTiles;
    private boolean isActive;
    private int technologyPoint;
    private static final int SOLDIERS_PER_TILE_LIMIT = 10;

    public Player (String name, Civilization civilization){
        this.name = name;
        this.civilization = civilization;
        this.isActive = true;
        this.technologyPoint = 0;

    }

    public void addTile(Tile t){
        if(t != null && !ownedTiles.contains(t)){
            ownedTiles.add(t);
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
        int foodProduction = 0;
        int goldProduction = 0;
        int bookProduction = 0;
        int foodConsumption = 0;

        for(Tile tile : ownedTiles) {
            if(tile.hasBuilding()){
                Building building = tile.getBuilding();
                building.produce(this);
            }

            // Calculate food consumption for every tile. ==> foodConsumption += tile.calculateFoodConsumption()
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

        if(!t.canConstruct(bt)){
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
        if(bt instanceof Farm){
            building = new Farm();
        }else if(bt instanceof GoldMine){
            building = new GoldMine();
        }else if(bt instanceof Port){
            building = new Port();
        }else if(bt instanceof Library){
            building = new Library();
        }
        t.setBuilding(building);
        return true;
    }
}
