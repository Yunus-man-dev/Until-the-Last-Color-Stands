package com.gameonjava.utlcs.backend;

import com.gameonjava.utlcs.backend.resources.*;

public class Trade implements com.badlogic.gdx.utils.Json.Serializable {

    private Player creator;
    private Player receiver;

    private Resource givenResource;
    private int givenResourceAmount;
    private Resource wantedResource;
    private int wantedResourceAmount;

    public Trade(Player creator, Player receiver, Resource givenResource, int givenResourceAmount,
            Resource wantedResource, int wantedResourceAmount) {
        this.creator = creator;
        this.receiver = receiver;
        this.givenResource = givenResource;
        this.givenResourceAmount = givenResourceAmount;
        this.wantedResource = wantedResource;
        this.wantedResourceAmount = wantedResourceAmount;
    }


    public boolean checkForCreation() {
        MovementPoint creatorMP = creator.getMp();
        if (!creatorMP.checkForResource(creatorMP.TRADE)) {
            System.out.println("Trade Error: Yetersiz MP");
            return false;
        }

        Resource realCreatorResource = getPlayerResource(creator, givenResource);
        if (realCreatorResource == null || !realCreatorResource.checkForResource(givenResourceAmount)) {
            System.out.println("Trade Error: Gönderenin kaynağı yetersiz.");
            return false;
        }

        Resource realReceiverResource = getPlayerResource(receiver, wantedResource);
        if (realReceiverResource == null || !realReceiverResource.checkForResource(wantedResourceAmount)) {
            System.out.println("Trade Error: Alıcının (" + receiver.getName() + ") kaynağı yetersiz.");
            return false;
        }

        realCreatorResource.reduceResource(givenResourceAmount);
        creatorMP.reduceResource(creatorMP.TRADE);

        return true;
    }

    public void returnResources() {
        Resource realPlayerResource = getPlayerResource(creator, givenResource);
        if (realPlayerResource != null) {
            realPlayerResource.addResource(givenResourceAmount);
        }
    }

    public void trade() {
        applyDiscount();

        Resource receiverGain = getPlayerResource(receiver, givenResource);
        if (receiverGain != null) {
            receiverGain.addResource(givenResourceAmount);
        }

        Resource receiverPay = getPlayerResource(receiver, wantedResource);
        Resource creatorGain = getPlayerResource(creator, wantedResource);

        if (receiverPay != null && creatorGain != null) {
            receiverPay.reduceResource(wantedResourceAmount);
            creatorGain.addResource(wantedResourceAmount);
        }
    }

    private void applyDiscount() {
    }

    private Resource getPlayerResource(Player p, Resource typeTemplate) {
        if (typeTemplate instanceof GoldResource)
            return p.getGold();
        if (typeTemplate instanceof FoodResource)
            return p.getFood();
        if (typeTemplate instanceof BookResource)
            return p.getBook();
        return null;
    }

    public Player getReceiver() {
        return receiver;
    }

    public Player getCreator() {
        return creator;
    }

    public Resource getGivenResource() {
        return givenResource;
    }

    public int getGivenResourceAmount() {
        return givenResourceAmount;
    }

    public Resource getWantedResource() {
        return wantedResource;
    }

    public int getWantedResourceAmount() {
        return wantedResourceAmount;
    }

    @Override
    public void write(com.badlogic.gdx.utils.Json json) {
        json.writeValue("creator", creator);
        json.writeValue("receiver", receiver);
        json.writeValue("givenRes", givenResource);
        json.writeValue("givenAmt", givenResourceAmount);
        json.writeValue("wantedRes", wantedResource);
        json.writeValue("wantedAmt", wantedResourceAmount);
    }

    @Override
    public void read(com.badlogic.gdx.utils.Json json, com.badlogic.gdx.utils.JsonValue jsonData) {
        creator = json.readValue("creator", Player.class, jsonData);
        receiver = json.readValue("receiver", Player.class, jsonData);
        givenResource = json.readValue("givenRes", Resource.class, jsonData);
        givenResourceAmount = jsonData.getInt("givenAmt");
        wantedResource = json.readValue("wantedRes", Resource.class, jsonData);
        wantedResourceAmount = jsonData.getInt("wantedAmt");
    }
}
