package com.gameonjava.utlcs.civilization;

import com.gameonjava.utlcs.resources.BookResource;
import com.gameonjava.utlcs.resources.FoodResource;
import com.gameonjava.utlcs.resources.GoldResource;
import com.gameonjava.utlcs.resources.MovementPoint;
import com.gameonjava.utlcs.Player;


public abstract class Civilization {



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

    public Civilization(String civilizationName, String civilizationColor,
                        double attackMultiplier, double defenseMultiplier,
                        double technologyMultiplier){

        this.civilizationName = civilizationName;
        this.civilizationColor = civilizationColor;
        this.attackMultiplier = attackMultiplier;
        this.technologyMultiplier = technologyMultiplier;


    }

    public String getCivilizationName() {
        return civilizationName;
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
}
