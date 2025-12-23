package com.gameonjava.utlcs.backend;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.gameonjava.utlcs.backend.Enum.TerrainType;
import com.gameonjava.utlcs.backend.resources.MovementPoint;


//Description: Controls the game flow, player turns, and global actions.
public class Game implements com.badlogic.gdx.utils.Json.Serializable{


    // holds all the players
    private ArrayList<Player> players;

    // holds the game map
    private Map gameMap;


    // holds the current turn index
    public static int currentTurn = 1;

    // holds the players list’s index to track whose turn it is.

    private int currentPlayerIndex;

    // holds the pending trade offers.
    private ArrayList<Trade> activeTrades;

    private Player winner = null;




    public Game() {
        this.players = new ArrayList<>();
        this.gameMap = new Map();
        this.activeTrades = new ArrayList<>();
        this.currentPlayerIndex = 0;
        // players.add(new Player("x", new Black()));
        // players.add(new Player("y", new GoldCivilization()));
        // players.add(new Player("z", new Blue()));
        // players.add(new Player("d", new Red()));
    }

    public void addPlayer(Player p){
        this.players.add(p);
    }
    public void startGame(int mapID) {
        gameMap.initializeMap(mapID);

        // SET UP FOR PLAYERS MUST BE DONE WITH GUI METHODS, ASSUME PLAYERS ARE INITIATED

        // initialize resources & Assign starting tiles
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);

            gameMap.assignStartingTiles(p, i);

        }
    }


    //Moves onto the next player's turn and updates resources.

    public void nextTurn() {

        // --- DÜZELTME: Tur mantığı düzenlendi ---
        // Oyuncu sırasını ilerlet
        currentPlayerIndex++;

        if (currentPlayerIndex >= players.size()) {
            currentPlayerIndex = 0;
            gameMap.resetAllTilesTurnData();
        }
        currentTurn++;

        // Get the new current player
        Player currentPlayer = getCurrentPlayer();

        currentPlayer.updateResources();

        if (checkGameOver()) {
            endGame();
        }
    }

    // helper: returns the current player object.
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    //Returns pending trades for the specified player.
    public ArrayList<Trade> getPendingTradesFor(Player p) {
        ArrayList<Trade> playerTrades = new ArrayList<>();
        for (Trade t : activeTrades) {
            if (t.getReciever().equals(p)) {
                playerTrades.add(t);
            }
        }
        return playerTrades;
    }


    public void endGame() {
        //GUI METHOD, WINNERS UNIQUE FRAME WILL BE DISPLAYED

    }

    //checks if the game should end due to victory conditions.

    public boolean checkGameOver() {
        int activePlayerCount = 0;
        Player lastActivePlayer = null;

        for (Player p : players) {
            if (p.isActive()) {
                activePlayerCount++;
                lastActivePlayer = p;
            }
            // 1. Durum: Oyuncu kendi Win Condition'ını sağladıysa
            if (p.hasWon()) {
                this.winner = p;
                return true;
            }
        }

        // 2. Durum: Sadece 1 kişi hayatta kaldıysa (Elimination Victory)
        if (activePlayerCount == 1 && lastActivePlayer != null) {
            this.winner = lastActivePlayer;
            return true;
        }

        return false;
    }

    // Bu metodu sınıfın en altına ekle (Getter):
    public Player getWinner() {
        return winner;
    }


    //Handles all army movements. Checks rules, costs, and triggers attacks.
    // ITS ASSSUMED THAT WHEN AN ARMY MOVES, ALL TROOPS HAVE BEEN MOVED.
    // IF WE WILL SELECT THE AMOUNT OF SOLDIERS TO MOVE THERE MUST BE AN int amount PARAMETER IN THE ÖETHOD
    // AND MOVING LOGIC MUST BE MODIFIED ACCORDINGLY
    // Game.java

    // İmzayı değiştirdik: int amount eklendi
//    public void moveArmy(Tile owned, Tile target, int amount) {
//        if (owned == null || target == null) return;
//
//        if (!owned.hasArmy()) {
//            System.out.println("MOVE FAILED: Kaynak karede asker yok.");
//            return;
//        }
//
//        int currentSoldiers = owned.getArmy().getSoldiers();
//
//        // --- GÜVENLİK KONTROLÜ ---
//        // Eğer istenen miktar mevcuttan fazlaysa veya 0'sa işlemi durdur
//        if (amount > currentSoldiers || amount <= 0) {
//            System.out.println("MOVE FAILED: Geçersiz asker miktarı: " + amount);
//            return;
//        }
//
//        Player player = owned.getOwner();
//        MovementPoint mp = player.getMp();
//
//        // Zemin ve Liman Kontrolleri (Önceki kodların aynısı)
//        boolean isTargetWater = (target.getTerrainType() == TerrainType.WATER ||
//            target.getTerrainType() == TerrainType.DEEP_WATER);
//        boolean isSourceWater = (owned.getTerrainType() == TerrainType.WATER ||
//            owned.getTerrainType() == TerrainType.DEEP_WATER);
//
//        if (isTargetWater && !isSourceWater) {
//            boolean hasPort = false;
//            if (owned.hasBuilding() && owned.getBuilding() instanceof com.gameonjava.utlcs.backend.building.Port) {
//                hasPort = true;
//            }
//            if (!hasPort) {
//                System.out.println("MOVE FAILED: Denize açılmak için Liman (Port) gerekli!");
//                return;
//            }
//        }
//
//        if (!gameMap.getNeighbors(owned).contains(target)) {
//            System.out.println("MOVE FAILED: Hedef komşu değil.");
//            return;
//        }
//
//        if (!mp.checkForResource(mp.MOVE)) {
//            System.out.println("MOVE FAILED: Yetersiz MP.");
//            return;
//        }
//
//        if (target.hasArmy() && !target.getOwner().equals(player)) {
//            // Saldırıda tüm orduyla mı yoksa bir kısmıyla mı saldırılacak?
//            // Şimdilik sadece tüm orduyla saldırıya izin veriyorsan:
//            initiateAttack(owned, target);
//            return;
//        }
//
//        // --- HAREKET İŞLEMİ (MİKTARA GÖRE) ---
//
//        mp.reduceResource(mp.MOVE);
//
//        // A. Kaynak Kareden DÜŞ (Tamamını silme!)
//        int remainingSoldiers = currentSoldiers - amount;
//
//        if (remainingSoldiers > 0) {
//            // Eğer geriye asker kalıyorsa sadece sayıyı güncelle
//            owned.getArmy().setSoldiers(remainingSoldiers);
//        } else {
//            // Eğer hepsi gittiyse orduyu tamamen kaldır
//            owned.setArmy(null);
//        }
//
//        // B. Hedef Kareye EKLE
//        if (target.hasArmy()) {
//            target.getArmy().addSoldiers(amount);
//        } else {
//            Army newArmy = new Army(amount, player, target);
//            target.setArmy(newArmy);
//
//            if (target.getOwner() == null && !isTargetWater) {
//                target.setOwner(player);
//                player.addTile(target);
//            }
//        }
//
//        System.out.println("MOVE SUCCESS: " + amount + " asker taşındı.");
//    }
//
//    //WarManager to resolve a battle.
//    // --- DÜZELTME: Dönüş tipi WarManager yapıldı ---
//    public WarManager initiateAttack(Tile from, Tile target) {
//        Army attacker = from.getArmy();
//        Army defender = target.getArmy();
//
//        MovementPoint mp = from.getOwner().getMp();
//        if (mp.checkForResource(mp.ATTACK)) {
//            mp.reduceResource(mp.ATTACK);
//
//            WarManager war = new WarManager(attacker, defender, target);
//            return war; // Return the result
//        }
//        return null;
//    }
    // Game.java içine yapıştır:

    public com.gameonjava.utlcs.backend.WarManager moveArmy(Tile owned, Tile target, int amount) {
        if (owned == null || target == null) return null;
        if (!owned.hasArmy()) return null;

        int currentSoldiers = owned.getArmy().getSoldiers();

        // Miktar kontrolü
        if (amount > currentSoldiers || amount <= 0) {
            System.out.println("MOVE FAILED: Geçersiz asker miktarı.");
            return null;
        }

        Player player = owned.getOwner();
        MovementPoint mp = player.getMp();

        // --- SAVAŞ KONTROLÜ (Sadece düşman askeri varsa girer) ---
        if (target.hasArmy() && !target.getOwner().equals(player)) {
            // Savaşı başlat ve sonucu döndür (GUI pencere açacak)
            return initiateAttack(owned, target, amount);
        }

        // --- BURADAN SONRASI NORMAL HAREKET (Savaş Yok) ---

        // 1. Komşuluk Kontrolü
        if (!gameMap.getNeighbors(owned).contains(target)) return null;

        // 2. MP Kontrolü
        if (!mp.checkForResource(mp.MOVE)) return null;

        // 3. Liman Kontrolü (Karadan -> Suya)
        boolean isTargetWater = (target.getTerrainType() == com.gameonjava.utlcs.backend.Enum.TerrainType.WATER ||
            target.getTerrainType() == com.gameonjava.utlcs.backend.Enum.TerrainType.DEEP_WATER);
        boolean isSourceWater = (owned.getTerrainType() == com.gameonjava.utlcs.backend.Enum.TerrainType.WATER ||
            owned.getTerrainType() == com.gameonjava.utlcs.backend.Enum.TerrainType.DEEP_WATER);

        if (isTargetWater && !isSourceWater) {
            boolean hasPort = false;
            if (owned.hasBuilding() && owned.getBuilding() instanceof com.gameonjava.utlcs.backend.building.Port) {
                hasPort = true;
            }
            if (!hasPort) {
                System.out.println("Denize girmek için Liman gerekli!");
                return null;
            }
        }

        // --- İŞLEM ---
        mp.reduceResource(mp.MOVE);

        // A. Kaynak Kareden Askeri Azalt
        int remainingSoldiers = currentSoldiers - amount;
        if (remainingSoldiers > 0) {
            owned.getArmy().setSoldiers(remainingSoldiers);
        } else {
            owned.setArmy(null);
        }

        // B. Hedef Kareye Ekle
        if (target.hasArmy()) {
            // Zaten asker varsa ekle (Benim toprağım)
            target.getArmy().addSoldiers(amount);
        } else {
            // Hedef boşsa yeni ordu koy
            com.gameonjava.utlcs.backend.Army newArmy = new com.gameonjava.utlcs.backend.Army(amount, player, target);
            target.setArmy(newArmy);

            // --- SAHİPLİK DEĞİŞTİRME MANTIĞI (DÜZELTİLDİ) ---
            // Hedef su değilse VE sahibi ben değilsem (Boşsa veya Düşmansa)
            if (!isTargetWater && !player.equals(target.getOwner())) {

                Player oldOwner = target.getOwner();

                // Eski sahibin listesinden sil (Eğer varsa)
                if (oldOwner != null) {
                    oldOwner.getOwnedTiles().remove(target);
                }

                // Yeni sahip olarak beni ata
                target.setOwner(player);
                player.addTile(target);
            }
        }

        System.out.println("MOVE SUCCESS: " + amount + " asker taşındı.");
        return null; // Savaş olmadı, null dönüyoruz
    }

    private com.gameonjava.utlcs.backend.WarManager initiateAttack(Tile attackerTile, Tile defenderTile, int amount) {
        System.out.println("WAR STARTED!");

        Player attacker = attackerTile.getOwner();
        Player defender = defenderTile.getOwner();

        // 1. Zar Atma
        int attRoll = com.badlogic.gdx.math.MathUtils.random(1, 6);
        int defRoll = com.badlogic.gdx.math.MathUtils.random(1, 6);

        // 2. Güç Hesaplama (Basit Formül)
        int attPower = attRoll + amount + (int)attacker.getTechnologyPoint();
        int defPower = defRoll + defenderTile.getArmy().getSoldiers() + (int)defender.getTechnologyPoint();

        // Orman Bonusu
        if(defenderTile.getTerrainType() == com.gameonjava.utlcs.backend.Enum.TerrainType.FOREST) {
            defPower += 2;
        }

        boolean attackerWon = attPower > defPower;

        // 3. SONUÇLARI UYGULA
        if (attackerWon) {
            System.out.println("ATTACKER WON!");

            // Savunan ölür
            if(defender != null) defender.getOwnedTiles().remove(defenderTile);
            defenderTile.setArmy(null);

            // Saldıranın askerleri ilerler
            int remainingSource = attackerTile.getArmy().getSoldiers() - amount;
            if (remainingSource > 0) {
                attackerTile.getArmy().setSoldiers(remainingSource);
            } else {
                attackerTile.setArmy(null);
            }

            // Yeni orduyu hedefe koy
            com.gameonjava.utlcs.backend.Army winningArmy = new com.gameonjava.utlcs.backend.Army(amount, attacker, defenderTile);
            defenderTile.setArmy(winningArmy);

            // Toprağı al
            defenderTile.setOwner(attacker);
            attacker.addTile(defenderTile);

        } else {
            System.out.println("DEFENDER WON!");
            // Saldıranın gönderdiği askerler ölür
            int remainingSource = attackerTile.getArmy().getSoldiers() - amount;
            if (remainingSource > 0) {
                attackerTile.getArmy().setSoldiers(remainingSource);
            } else {
                attackerTile.setArmy(null);
            }
        }

        // 4. GUI İÇİN VERİ PAKETLEME
        com.gameonjava.utlcs.backend.WarManager result = new com.gameonjava.utlcs.backend.WarManager();
        result.guiAttackerWon = attackerWon;
        result.guiAttRoll = attRoll;
        result.guiDefRoll = defRoll;
        result.guiAttAP = attPower;
        result.guiDefAP = defPower;
        result.guiAttTile = attackerTile;
        result.guiDefTile = defenderTile;

        return result;
    }

    //Creates the trade and adds to active trades.
    public void addTrade(Trade t) {
        if (t.checkForCreation()) {
            activeTrades.add(t);
        }
    }

    //Executes trade and removes it from active list.
    public void acceptTrade(Trade t) {
        t.trade();
        activeTrades.remove(t);
    }

    //Refuses trade, returns resources, and removes from list.

    public void refuseTrade(Trade t) {
        t.returnResources();
        activeTrades.remove(t);
    }

    public static int getCurrentTurn() {
        return currentTurn;
    }
    public Map getMap(){
        return gameMap;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

@Override
    public void write(Json json) {
        json.writeValue("Players", players);
        json.writeValue("Map", gameMap);
        json.writeValue("Turn", currentTurn); // Saves the static value
        json.writeValue("CurPlayerIndex", currentPlayerIndex);
        json.writeValue("ActiveTrades", activeTrades);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        players = json.readValue("Players", java.util.ArrayList.class, Player.class, jsonData);
        gameMap = json.readValue("Map", Map.class, jsonData);

        // Standard primitive reads
        currentTurn = jsonData.getInt("Turn", 1);
        currentPlayerIndex = jsonData.getInt("CurPlayerIndex", 0);

        activeTrades = json.readValue("ActiveTrades", java.util.ArrayList.class, Trade.class, jsonData);

        if (players != null) {
            for (Player p : players) {
                p.relinkTiles();
            }
        }
    }

}
