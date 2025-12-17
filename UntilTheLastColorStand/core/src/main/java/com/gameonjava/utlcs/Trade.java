package com.gameonjava.utlcs;

import com.gameonjava.utlcs.civilization.GoldCivilization;
import com.gameonjava.utlcs.resources.*;
import io.github.some_example_name.building.*;
import io.github.some_example_name.resources.*;
import io.github.some_example_name.civilization.*;

/**
 * Manages trade offers, validation, and execution between players
 */
public class Trade {

    private Player creator;
    private Player receiver;
    private Resource givenResource;
    private int givenResourceAmount;
    private Resource wantedResource;
    private int wantedResourceAmount;

    public Trade(Player creator, Player receiver, Resource givenResource, int givenResourceAmount, Resource wantedResource, int wantedResourceAmount) {
        this.creator = creator;
        this.receiver = receiver;
        this.givenResource = givenResource;
        this.givenResourceAmount = givenResourceAmount;
        this.wantedResource = wantedResource;
        this.wantedResourceAmount = wantedResourceAmount;
    }

     //Checks validation
     //Reduces MP and the given resource  upon success.

    public boolean checkForCreation() {
        MovementPoint creatorMP = creator.getMp();
        // checks whether the MP is sufficent
        if (!creatorMP.checkForResource(creatorMP.TRADE)) {
            return false;
        }

        if (givenResource != null) {
          //checks whether the resource is sufficent
            if (!givenResource.checkForResource(givenResourceAmount)) {
                return false;
            }
            givenResource.reduceResource(givenResourceAmount);
        }

        creatorMP.reduceResource(creatorMP.TRADE);

        return true;
    }
    //helper method for Game/getPendingTradesFor()
    public Player getReciever(){
        return receiver;
    }

     // checks whether receiver  has enough wantedResource.

    public boolean checkForApplication() {
        if (wantedResource != null) {
            return wantedResource.checkForResource(wantedResourceAmount);
        }
        return false;
    }

    /**
     * Called in case of refused trade, decreased resources are given back to creator.
     * MP are not refunded.
     */
    public void returnResources() {
        if (givenResource != null) {
            givenResource.addResource(givenResourceAmount);
        }
      }


     //Increases the amount of resource that Gold player receives.
     //Leaves values unchanged if neither is Gold civilization or trade doesn't involve gold.

    public void applyDiscount() {
        if (creator.getCivilization() instanceof GoldCivilization && wantedResource instanceof GoldResource) {
            GoldCivilization goldCiv = (GoldCivilization) creator.getCivilization();
            this.wantedResourceAmount += (int) (this.wantedResourceAmount * goldCiv.TRADE_DISCOUNT);
        }

        if (receiver.getCivilization() instanceof GoldCivilization && givenResource instanceof GoldResource) {
            GoldCivilization goldCiv = (GoldCivilization) receiver.getCivilization();
            this.givenResourceAmount += (int) (this.givenResourceAmount * goldCiv.TRADE_DISCOUNT);
        }
    }


    //Executes the trade if valid and accepted.
    //Updates player resources accordingly.

    public void trade() {
        applyDiscount();


        // reduce from Receiver,add to Creator.
        Resource receiverResource = getPlayerResource(receiver, wantedResource);
        Resource creatorResource = getPlayerResource(creator, wantedResource);

        if (receiverResource != null) {// null check to avoid null pointer exception in case of one-way trades
            receiverResource.reduceResource(wantedResourceAmount);
        }
        if (creatorResource != null) {// null check to avoid null pointer exception in case of one-way trades
            creatorResource.addResource(wantedResourceAmount);
        }

        // receiver gets the given resource (Creator already paid in checkForCreation)
        Resource receiverGainResource = getPlayerResource(receiver, givenResource);
        if (receiverGainResource != null && givenResource != null) {
            receiverGainResource.addResource(givenResourceAmount);
        }
    }

      //helper method to find the matching resource instance for a specific player.
    // same as player.getResource() at spesific resource type

    private Resource getPlayerResource(Player p, Resource type) {
        if (type instanceof GoldResource) return p.getGold();
        if (type instanceof FoodResource) return p.getFood();
        if (type instanceof BookResource) return p.getBook();

        return null;
    }
}
