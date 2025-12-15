package io.github.some_example_name.civilization;

import com.badlogic.gdx.graphics.Color;

import io.github.some_example_name.resources.*;
import io.github.some_example_name.Player;


public abstract class Civilization {



    final int START_GOLD = 100;
    final int START_FOOD = 100;
    final int START_MOVEMENT = 10;
    final int START_BOOK = 10;




    protected String civilizationName;
    protected String civilizationColor;
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
