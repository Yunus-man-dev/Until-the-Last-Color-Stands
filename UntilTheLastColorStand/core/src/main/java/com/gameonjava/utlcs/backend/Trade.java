package com.gameonjava.utlcs.backend;

import com.gameonjava.utlcs.backend.resources.*;

public class Trade implements com.badlogic.gdx.utils.Json.Serializable {

    private Player creator;
    private Player receiver;

    // Bu değişkenler sadece MİKTAR ve TİP bilgisini taşır (Dummy objects).
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

    public Trade() {
    }

    // --- KRİTİK DÜZELTME: Hem Gönderen Hem Alıcı Kontrolü ---
    public boolean checkForCreation() {
        // 1. GÖNDERENİN HAREKET PUANI (MP) KONTROLÜ
        MovementPoint creatorMP = creator.getMp();
        if (!creatorMP.checkForResource(creatorMP.TRADE)) {
            System.out.println("Trade Error: Yetersiz MP");
            return false;
        }

        // 2. GÖNDERENİN KAYNAK KONTROLÜ (Senin 1000 varken 1100 yollamanı engeller)
        Resource realCreatorResource = getPlayerResource(creator, givenResource);
        if (realCreatorResource == null || !realCreatorResource.checkForResource(givenResourceAmount)) {
            System.out.println("Trade Error: Gönderenin kaynağı yetersiz.");
            return false;
        }

        // 3. ALICININ KAYNAK KONTROLÜ (Adamın 200'ü varken 250 istemeni engeller)
        Resource realReceiverResource = getPlayerResource(receiver, wantedResource);
        if (realReceiverResource == null || !realReceiverResource.checkForResource(wantedResourceAmount)) {
            System.out.println("Trade Error: Alıcının (" + receiver.getName() + ") kaynağı yetersiz.");
            return false;
        }

        // 4. HER ŞEY TAMAMSA: Senden kaynağı ve MP'yi düş
        realCreatorResource.reduceResource(givenResourceAmount);
        creatorMP.reduceResource(creatorMP.TRADE);

        return true;
    }

    public void returnResources() {
        // Reddedilirse kaynağı geri ver
        Resource realPlayerResource = getPlayerResource(creator, givenResource);
        if (realPlayerResource != null) {
            realPlayerResource.addResource(givenResourceAmount);
        }
    }

    public void trade() {
        applyDiscount();

        // Alıcıya gelen kaynak (Gönderen zaten baştan ödemişti)
        Resource receiverGain = getPlayerResource(receiver, givenResource);
        if (receiverGain != null) {
            receiverGain.addResource(givenResourceAmount);
        }

        // Alıcıdan giden kaynak
        Resource receiverPay = getPlayerResource(receiver, wantedResource);
        // Gönderene gelen kaynak
        Resource creatorGain = getPlayerResource(creator, wantedResource);

        if (receiverPay != null && creatorGain != null) {
            // Son bir güvenlik kontrolü (Eğer arada harcadıysa eksiye düşebilir, şimdilik
            // düşsün)
            receiverPay.reduceResource(wantedResourceAmount);
            creatorGain.addResource(wantedResourceAmount);
        }
    }

    private void applyDiscount() {
        // İleride eklenecek bonuslar
    }

    // Yardımcı Metot: Oyuncunun gerçek kaynağını bulur
    private Resource getPlayerResource(Player p, Resource typeTemplate) {
        if (typeTemplate instanceof GoldResource)
            return p.getGold();
        if (typeTemplate instanceof FoodResource)
            return p.getFood();
        if (typeTemplate instanceof BookResource)
            return p.getBook();
        return null;
    }

    // Getterlar
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