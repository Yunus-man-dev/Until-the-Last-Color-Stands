package com.gameonjava.utlcs.backend.civilization;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.resources.BookResource;
import com.gameonjava.utlcs.backend.resources.FoodResource;
import com.gameonjava.utlcs.backend.resources.GoldResource;
import com.gameonjava.utlcs.backend.resources.MovementPoint;


public abstract class Civilization implements com.badlogic.gdx.utils.Json.Serializable{



    final int START_GOLD = 1000;
    final int START_FOOD = 200;
    final int START_MOVEMENT = 20;
    final int START_BOOK = 0;
    final int START_TILE = 7;

    // fsdjhgjokgj sdo



    public  double FRECRUIT = 3;
    public  double FTILE = 3 ;
    public  double FMAINTAIN = 0;

    public  double GRECRUIT = 50;
    public  double GCONSTRUCT = 300;
    public  double GDEVELOP = 250;
    public  double GREMOVE = 300;

    public  double M_MOVE = 1;
    public  double M_UPGRADE = 3;
    public double M_CONSTRUCT = 5;
    public  double M_TRADE = 7;
    public  double M_RECRUIT = 3;
    public  double M_ATTACK = 4;

    //Baslangic degerleri
    public  double FARM_FOOD = 50;
    public  double PORT_FOOD = 25;
    public  double PORT_GOLD = 50;
    public  double MINE_GOLD = 50;
    public  double BOOK = 2;









    protected String civilizationName;
    protected  String civilizationColor;
    protected double attackMultiplier;
    protected double defenseMultiplier;
    protected double technologyMultiplier;
    protected GoldResource startingGold;
    public FoodResource startingFood;
    protected BookResource startingBook;
    protected MovementPoint startingMP;
    public String winCondText;
    public String featuresText;

    public Civilization(String civilizationName, String civilizationColor,
                        double attackMultiplier, double defenseMultiplier,
                        double technologyMultiplier){

        this.civilizationName = civilizationName;
        this.civilizationColor = civilizationColor;
        this.attackMultiplier = attackMultiplier;
        this.defenseMultiplier = defenseMultiplier;
        this.technologyMultiplier = technologyMultiplier;
        if( civilizationColor.equals("Red") || civilizationColor.equals("Dark Red") ) {
            winCondText = "Capture minimum %40 of capturable tiles. (Plains and Forests)";
            featuresText = "Recruit 5 more soldiers in each turn and start with extra 5 soldiers.\nHas 30% attack multiplier against all civilizations except Brown and Black.\nMovement of soldiers requires 20% less movement points.\nSoldier recruitment cost is 20% higher.";
        }
        if( civilizationColor.equals("Orange") || civilizationColor.equals("Gold") ) {
            winCondText = "Must have at least 100.000 golds and 10 gold mines.";
            featuresText = "50% gold production bonus from mines.\n15% trade discount will be applied while trading.\nSoldier recruitment cost is 30% higher.\n Has %20 less attack and defense multipliers";
        }
        if( civilizationColor.equals("Gray") || civilizationColor.equals("Brown") ) {
            winCondText = "Must be alive for 250 turns.";
            featuresText = "Has 20% food production bonus from farms and ports.\nHas extra 30% defense multiplier and has extra 15% attack multiplier against Red and Dark Red\nSoldiers consume 30% more food.";
        }
        if( civilizationColor.equals("Blue") || civilizationColor.equals("Cyan") ) {
            winCondText = "Technology points must be above 5, have at least 2.000 books, have at least 10 libraries.";
            featuresText = "Books provide x2 technology points. Has %20 less attack multiplier";
        }

    }

    public Civilization() {}

    public String getCivilizationName() {
        return civilizationName;
    }

    public String getWinCondText() {
        return winCondText;
    }
    public String getFeaturesText() {
        return featuresText;
    }

    public void setCivilizationName(String civilizationName) {
        this.civilizationName = civilizationName;
    }

    public String getCivilizationColor() {
        return civilizationColor;
    }

    public void setCivilizationColor(String civilizationColor) {
        this.civilizationColor = civilizationColor;
    }

    public double getAttackMultiplier() {
        return attackMultiplier;
    }

    public void setAttackMultiplier(double attackMultiplier) {
        this.attackMultiplier = attackMultiplier;
    }

    public double getDefenseMultiplier() {
        return defenseMultiplier;
    }

    public void setDefenseMultiplier(double defenseMultiplier) {
        this.defenseMultiplier = defenseMultiplier;
    }

    public double getTechnologyMultiplier() {
        return technologyMultiplier;
    }

    public void setTechnologyMultiplier(double technologyMultiplier) {
        this.technologyMultiplier = technologyMultiplier;
    }

    public GoldResource getStartingGold() {
        return startingGold;
    }

    public void setStartingGold(GoldResource startingGold) {
        this.startingGold = startingGold;
    }

    public FoodResource getStartingFood() {
        return startingFood;
    }

    public void setStartingFood(FoodResource startingFood) {
        this.startingFood = startingFood;
    }

    public BookResource getStartingBook() {
        return startingBook;
    }

    public void setStartingBook(BookResource startingBook) {
        this.startingBook = startingBook;
    }

    public MovementPoint getStartingMP() {
        return startingMP;
    }

    public void setStartingMp(MovementPoint startingMp) {
        this.startingMP = startingMp;
    }

    // public abstract void applyUniqueFeature(Player p);
    public abstract boolean checkWinCondition(Player p);
    public abstract void initializeStartingResources();

    @Override
    public void write(Json json) {
        json.writeValue("CName", civilizationName);
        json.writeValue("CColor", civilizationColor);
        json.writeValue("CAttackMult", attackMultiplier);
        json.writeValue("CDefenseMult", defenseMultiplier);
        json.writeValue("CTechMult", technologyMultiplier);
    }
    @Override
    public void read(Json json, JsonValue jsonData) {
        civilizationName = json.readValue("CName", String.class, jsonData);
        civilizationColor = json.readValue("CColor", String.class, jsonData);
        attackMultiplier = jsonData.getFloat("CAttackMult");
        defenseMultiplier = jsonData.getFloat("CDefenseMult");
        technologyMultiplier = jsonData.getFloat("CTechMult");
    }
}
