package com.gameonjava.utlcs.backend;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.gameonjava.utlcs.backend.Enum.TerrainType;
import com.gameonjava.utlcs.backend.resources.MovementPoint;

//Description: Controls the game flow, player turns, and global actions.
public class Game implements com.badlogic.gdx.utils.Json.Serializable {

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

    public void addPlayer(Player p) {
        this.players.add(p);
    }

    public void startGame(int mapID) {
        gameMap.initializeMap(mapID);

        // SET UP FOR PLAYERS MUST BE DONE WITH GUI METHODS, ASSUME PLAYERS ARE
        // INITIATED

        // initialize resources & Assign starting tiles
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);

            gameMap.assignStartingTiles(p, i);

        }
        getCurrentPlayer().updateResources();
    }

    // Moves onto the next player's turn and updates resources.

    public void nextTurn() {
        // 1. Oyun bitti mi kontrolü (Mevcut kodun)
        if (checkGameOver()) {
            endGame();
            return; // Oyun bittiyse turu ilerletme
        }

        // 2. --- DÜZELTME: SIRA ATLAMA DÖNGÜSÜ ---
        // Sıradaki oyuncu "Aktif" olana kadar indeksi artır.
        do {
            currentPlayerIndex++;

            // Liste sonuna geldiyse başa dön ve tur sayısını artır
            if (currentPlayerIndex >= players.size()) {
                currentPlayerIndex = 0;
                gameMap.resetAllTilesTurnData();
            }
            currentTurn++;

        } while (!getCurrentPlayer().isActive()); // Oyuncu aktif DEĞİLSE döngü devam eder

        // 3. Yeni aktif oyuncuyu al ve kaynaklarını güncelle
        Player currentPlayer = getCurrentPlayer();
        System.out.println("Turn passed to: " + currentPlayer.getName()); // Debug için

        currentPlayer.updateResources();

        // Tekrar oyun bitti mi kontrolü (updateResources veya tur başı olaylar oyunu
        // bitirebilir)
        if (checkGameOver()) {
            endGame();
        }
    }

    // helper: returns the current player object.
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    // Returns pending trades for the specified player.
    public ArrayList<Trade> getPendingTradesFor(Player p) {
        ArrayList<Trade> playerTrades = new ArrayList<>();
        for (Trade t : activeTrades) {
            if (t.getReceiver().equals(p)) {
                playerTrades.add(t);
            }
        }
        return playerTrades;
    }

    public void endGame() {
        // GUI METHOD, WINNERS UNIQUE FRAME WILL BE DISPLAYED

    }

    // checks if the game should end due to victory conditions.

    public boolean checkGameOver() {
        int activePlayerCount = 0;
        Player lastActivePlayer = null;

        for (Player p : players) {
            if (p.isActive()) {
                activePlayerCount++;
                lastActivePlayer = p;
            }
            if (p.hasWon()) {
                this.winner = p;
                return true;
            }
        }

        if (activePlayerCount == 1 && lastActivePlayer != null) {
            this.winner = lastActivePlayer;
            return true;
        }

        return false;
    }

    public Player getWinner() {
        return winner;
    }

    // Handles all army movements. Checks rules, costs, and triggers attacks.
    // ITS ASSSUMED THAT WHEN AN ARMY MOVES, ALL TROOPS HAVE BEEN MOVED.
    // IF WE WILL SELECT THE AMOUNT OF SOLDIERS TO MOVE THERE MUST BE AN int amount
    // PARAMETER IN THE ÖETHOD
    // AND MOVING LOGIC MUST BE MODIFIED ACCORDINGLY
    // Game.java

    public com.gameonjava.utlcs.backend.WarManager moveArmy(Tile owned, Tile target, int amount) {
        if (owned == null || target == null)
            return null;
        if (!owned.hasArmy())
            return null;

        com.gameonjava.utlcs.backend.Army sourceArmy = owned.getArmy();

        // --- 1. HAREKET HAKKI KONTROLÜ ---
        Player player = owned.getOwner();
        int allowedMoves = 1;

        // Kırmızı Irk (RED) ise 2 hareket hakkı
        if (player.getCivilization() instanceof com.gameonjava.utlcs.backend.civilization.Red) {
            allowedMoves = 2;
        }

        if (sourceArmy.getMovesMadeThisTurn() >= allowedMoves) {
            System.out.println("MOVE FAILED: Hareket hakkı doldu!");
            return null;
        }

        int currentSoldiers = sourceArmy.getSoldiers();
        if (amount > currentSoldiers || amount <= 0)
            return null;

        MovementPoint mp = player.getMp();

        // --- SAVAŞ KONTROLÜ ---
        if (target.hasArmy() && !target.getOwner().equals(player)) {
            return initiateAttack(owned, target, amount);
        }

        // --- NORMAL HAREKET KONTROLLERİ ---
        if (!gameMap.getNeighbors(owned).contains(target))
            return null;

        // B. GEÇİLEBİLİR ARAZİ Mİ? (EKSİK OLAN KISIM EKLENDİ)
        // Tile sınıfındaki 'canUnitPass' metodu Mountain ve Deep Water için false
        // döner.
        if (!target.canUnitPass(owned)) {
            System.out.println("MOVE FAILED: Arazi (Dağ/Derin Su) geçişe uygun değil.");
            return null;
        }
        if (!mp.checkForResource(mp.MOVE))
            return null;

        // --- LİMAN (PORT) KONTROLÜ (Burası değişmedi, kural aynen duruyor) ---
        boolean isTargetWater = (target.getTerrainType() == com.gameonjava.utlcs.backend.Enum.TerrainType.WATER ||
                target.getTerrainType() == com.gameonjava.utlcs.backend.Enum.TerrainType.DEEP_WATER);
        boolean isSourceWater = (owned.getTerrainType() == com.gameonjava.utlcs.backend.Enum.TerrainType.WATER ||
                owned.getTerrainType() == com.gameonjava.utlcs.backend.Enum.TerrainType.DEEP_WATER);

        // Karadan -> Suya geçişte LİMAN ŞARTI
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

        // Hareket sayacını artır
        int currentMoveCount = sourceArmy.getMovesMadeThisTurn() + 1;

        // A. Kaynak Kareden Düş
        int remainingSoldiers = currentSoldiers - amount;
        if (remainingSoldiers > 0) {
            owned.getArmy().setSoldiers(remainingSoldiers);
        } else {
            owned.setArmy(null);
        }

        // B. Hedef Kareye Ekle
        if (target.hasArmy()) {
            // Zaten askerim varsa ekle
            target.getArmy().addSoldiers(amount);
        } else {
            // Boşsa yeni ordu koy
            com.gameonjava.utlcs.backend.Army newArmy = new com.gameonjava.utlcs.backend.Army(amount, player, target);
            newArmy.setMovesMadeThisTurn(currentMoveCount); // Yorgunluğu taşı
            target.setArmy(newArmy);

            // --- SAHİPLİK DEĞİŞTİRME (REVİZE EDİLDİ) ---
            // ARTIK SU OLSA BİLE ELE GEÇİRİLEBİLİR.
            // Sadece "Sahibi ben değilsem" (Boşsa veya Düşmansa) ele geçir.
            if (!player.equals(target.getOwner())) {

                com.gameonjava.utlcs.backend.Player oldOwner = target.getOwner();

                // Eski sahibin listesinden sil
                if (oldOwner != null) {
                    oldOwner.getOwnedTiles().remove(target);
                    oldOwner.checkElimination(); // Elendi mi kontrol et
                }

                // Yeni sahip benim
                target.setOwner(player);
                player.addTile(target);
            }
        }

        System.out.println("MOVE SUCCESS: Hareket " + currentMoveCount + "/" + allowedMoves);
        return null;
    }

    // Game.java içindeki initiateAttack metodunu bununla değiştir:

    private com.gameonjava.utlcs.backend.WarManager initiateAttack(Tile attackerTile, Tile defenderTile, int amount) {
        System.out.println("WAR STARTED!");

        Player attacker = attackerTile.getOwner();
        Player defender = defenderTile.getOwner();

        // --- 1. SAVAŞ ÖNCESİ VERİLERİ SAKLA (SNAPSHOT) ---
        // Savaş sonucunda askerler öleceği için, başlangıç değerlerini şimdiden alıyoruz.
        String startAttName = attacker.getName();
        String startDefName = (defender != null) ? defender.getName() : "Neutral";
        // Saldıranın o anki toplam askeri (veya sadece amount, senaryona göre değişir ama genelde o karedeki ordu + amount savaşa girer)
        // Ancak moveArmy metodunda amount kadar asker yeni bir ordu gibi düşünüldüğü için:
        int startAttSoldiers = amount;
        int startDefSoldiers = (defenderTile.hasArmy()) ? defenderTile.getArmy().getSoldiers() : 0;
        // ------------------------------------------------

        // 2. Zar Atma
        int attRoll = com.badlogic.gdx.math.MathUtils.random(1, 6);
        int defRoll = com.badlogic.gdx.math.MathUtils.random(1, 6);

        int defPower = defRoll + startDefSoldiers + (int)(defender != null ? defender.getTechnologyPoint() : 0);
        int attPower = attRoll + amount + (int)attacker.getTechnologyPoint();
        // 3. Güç Hesaplama

        // Orman Bonusu
        if (defenderTile.getTerrainType() == com.gameonjava.utlcs.backend.Enum.TerrainType.FOREST) {
            defPower += 2;
        }

        boolean attackerWon = attPower > defPower;

        // 4. SONUÇLARI UYGULA
        if (attackerWon) {
            System.out.println("ATTACKER WON!");

            // Savunan ölür
            if(defender != null) defender.getOwnedTiles().remove(defenderTile);
            defenderTile.setArmy(null);

            // Saldıranın kaynak karesindeki askerleri azalt
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

        // 5. GUI İÇİN VERİ PAKETLEME
        com.gameonjava.utlcs.backend.WarManager result = new com.gameonjava.utlcs.backend.WarManager();

        // --- EKSİK OLAN KISIM BURASIYDI, EKLENDİ ---
        result.guiAttName = startAttName;
        result.guiDefName = startDefName;
        result.guiAttSoldierCount = startAttSoldiers;
        result.guiDefSoldierCount = startDefSoldiers;
        // -------------------------------------------

        result.guiAttackerWon = attackerWon;
        result.guiAttRoll = attRoll;
        result.guiDefRoll = defRoll;
        result.guiAttAP = attPower;
        result.guiDefAP = defPower;
        result.guiAttTile = attackerTile;
        result.guiDefTile = defenderTile;

        return result;
    }

    // Creates the trade and adds to active trades.
    public boolean addTrade(Trade t) {
        // Trade sınıfındaki checkForCreation metodu hem kaynak hem de MP kontrolü
        // yapar.
        if (t.checkForCreation()) {
            activeTrades.add(t);
            return true; // Başarılı
        }
        return false; // Başarısız (Yetersiz kaynak veya MP)
    }
    // ------------------------

    // Executes trade and removes it from active list.
    public void acceptTrade(Trade t) {
        t.trade();
        activeTrades.remove(t);
    }

    // Refuses trade, returns resources, and removes from list.
    public void refuseTrade(Trade t) {
        t.returnResources();
        activeTrades.remove(t);
    }

    public static int getCurrentTurn() {
        return currentTurn;
    }

    public Map getMap() {
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
        // players = json.readValue("Players", java.util.ArrayList.class, Player.class,
        // jsonData);
        // gameMap = json.readValue("Map", Map.class, jsonData);

        // // Standard primitive reads
        // currentTurn = jsonData.getInt("Turn", 1);
        // currentPlayerIndex = jsonData.getInt("CurPlayerIndex", 0);

        // activeTrades = json.readValue("ActiveTrades", java.util.ArrayList.class,
        // Trade.class, jsonData);

        // if (players != null) {
        // for (Player p : players) {
        // p.relinkTiles();
        // }
        // }
        // 1. Önce Haritayı Yükle (Sıralama Önemli!)
        gameMap = json.readValue("Map", Map.class, jsonData);

        // 2. Sonra Oyuncuları Yükle (Oyuncular kendi "Kopya" Tile'larıyla gelir)
        players = json.readValue("Players", java.util.ArrayList.class, Player.class, jsonData);

        // 3. Diğer Veriler
        currentTurn = jsonData.getInt("Turn", 1);
        currentPlayerIndex = jsonData.getInt("CurPlayerIndex", 0);
        activeTrades = json.readValue("ActiveTrades", java.util.ArrayList.class, Trade.class, jsonData);

        // 4. BAĞLANTILARI ONAR (YENİ METODU ÇAĞIRIYORUZ)
        relinkTilesWithMap();
    }

    private void relinkTilesWithMap() {
        if (players == null || gameMap == null)
            return;

        for (Player p : players) {
            // Oyuncunun elindeki (Json'dan gelen kopya) listeyi al
            ArrayList<Tile> loadedTiles = new ArrayList<>(p.getOwnedTiles());

            // Oyuncunun listesini temizle (Doğrularını ekleyeceğiz)
            p.getOwnedTiles().clear();

            for (Tile fakeTile : loadedTiles) {
                // Koordinatları al
                int q = fakeTile.getQ();
                int r = fakeTile.getR();

                // Haritadan GERÇEK tile'ı çek
                Tile realTile = gameMap.getTile(q, r);

                if (realTile != null) {
                    // 1. Oyuncuya gerçek tile'ı ver
                    p.addTile(realTile);

                    // 2. Gerçek tile'a sahibini tanıt
                    realTile.setOwner(p);

                    // 3. Eğer tile üstünde ordu varsa, ordunun sahibini de güncelle
                    if (realTile.hasArmy()) {
                        realTile.getArmy().setPlayer(p);
                    }
                }
            }
        }
    }
}
