package io.github.some_example_name.civilization;

import com.badlogic.gdx.graphics.Color;
import io.github.some_example_name.resources.BookResource;
import io.github.some_example_name.resources.FoodResource;
import io.github.some_example_name.resources.GoldResource;
import io.github.some_example_name.resources.MovementPoint;

public abstract class Civilization {

    protected String civilizationName;
    protected Color civilizationColor;
    protected double attackMultiplier;
    protected double defenseMultiplier;
    protected double technologyMultiplier;
    protected GoldResource startingGold;
    public FoodResource startingFood;
    protected BookResource startingBook;
    protected MovementPoint startingMp;

    public Civilization(String civilizationName, Color civilizationColor,
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

    public Color getCivilizationColor() {
        return civilizationColor;
    }

    public void setCivilizationColor(Color civilizationColor) {
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

    public void setStartingGold(Gold startingGold) {
        this.startingGold = startingGold;
    }

    public FoodResource getStartingFood() {
        return startingFood;
    }

    public void setStartingFood(Food startingFood) {
        this.startingFood = startingFood;
    }

    public BookResource getStartingBook() {
        return startingBook;
    }

    public void setStartingBook(Book startingBook) {
        this.startingBook = startingBook;
    }

    public MovementPoint getStartingMp() {
        return startingMp;
    }

    public void setStartingMp(MovementPoint startingMp) {
        this.startingMp = startingMp;
    }

    // public abstract void applyUniqueFeature(Player p);
    public abstract boolean checkWinCondition(Player p);
    public abstract void initializeStartingResources();
}
