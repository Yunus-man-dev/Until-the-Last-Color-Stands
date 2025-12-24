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
        if (checkGameOver()) {
            endGame();
            return;
        }

        do {
            currentPlayerIndex++;

            if (currentPlayerIndex >= players.size()) {
                currentPlayerIndex = 0;
                gameMap.resetAllTilesTurnData();
            }
            currentTurn++;

        } while (!getCurrentPlayer().isActive());

        Player currentPlayer = getCurrentPlayer();
        System.out.println("Turn passed to: " + currentPlayer.getName());

        currentPlayer.updateResources();

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

        Player player = owned.getOwner();
        int allowedMoves = 1;

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

        if (target.hasArmy() && !target.getOwner().equals(player)) {
            return initiateAttack(owned, target, amount);
        }

        if (!gameMap.getNeighbors(owned).contains(target))
            return null;

        if (!target.canUnitPass(owned)) {
            System.out.println("MOVE FAILED: Arazi (Dağ/Derin Su) geçişe uygun değil.");
            return null;
        }
        if (!mp.checkForResource(mp.MOVE))
            return null;

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

        mp.reduceResource(mp.MOVE);

        int currentMoveCount = sourceArmy.getMovesMadeThisTurn() + 1;

        int remainingSoldiers = currentSoldiers - amount;
        if (remainingSoldiers > 0) {
            owned.getArmy().setSoldiers(remainingSoldiers);
        } else {
            owned.setArmy(null);
        }

        if (target.hasArmy()) {
            target.getArmy().addSoldiers(amount);
        } else {
            com.gameonjava.utlcs.backend.Army newArmy = new com.gameonjava.utlcs.backend.Army(amount, player, target);
            newArmy.setMovesMadeThisTurn(currentMoveCount);
            target.setArmy(newArmy);

            if (!player.equals(target.getOwner())) {

                com.gameonjava.utlcs.backend.Player oldOwner = target.getOwner();

                if (oldOwner != null) {
                    oldOwner.getOwnedTiles().remove(target);
                    oldOwner.checkElimination();
                }

                target.setOwner(player);
                player.addTile(target);
            }
        }

        System.out.println("MOVE SUCCESS: Hareket " + currentMoveCount + "/" + allowedMoves);
        return null;
    }


    private com.gameonjava.utlcs.backend.WarManager initiateAttack(Tile attackerTile, Tile defenderTile, int amount) {
        System.out.println("WAR STARTED!");

        Player attacker = attackerTile.getOwner();
        Player defender = defenderTile.getOwner();

        String startAttName = attacker.getName();
        String startDefName = (defender != null) ? defender.getName() : "Neutral";
        int startAttSoldiers = amount;
        int startDefSoldiers = (defenderTile.hasArmy()) ? defenderTile.getArmy().getSoldiers() : 0;

        int attRoll = com.badlogic.gdx.math.MathUtils.random(1, 6);
        int defRoll = com.badlogic.gdx.math.MathUtils.random(1, 6);

        int defPower = defRoll + startDefSoldiers + (int)(defender != null ? defender.getTechnologyPoint() : 0);
        int attPower = attRoll + amount + (int)attacker.getTechnologyPoint();

        if (defenderTile.getTerrainType() == com.gameonjava.utlcs.backend.Enum.TerrainType.FOREST) {
            defPower += 2;
        }

        boolean attackerWon = attPower > defPower;

        if (attackerWon) {
            System.out.println("ATTACKER WON!");

            if(defender != null) defender.getOwnedTiles().remove(defenderTile);
            defenderTile.setArmy(null);

            int remainingSource = attackerTile.getArmy().getSoldiers() - amount;
            if (remainingSource > 0) {
                attackerTile.getArmy().setSoldiers(remainingSource);
            } else {
                attackerTile.setArmy(null);
            }

            com.gameonjava.utlcs.backend.Army winningArmy = new com.gameonjava.utlcs.backend.Army(amount, attacker, defenderTile);
            defenderTile.setArmy(winningArmy);

            defenderTile.setOwner(attacker);
            attacker.addTile(defenderTile);

        } else {
            System.out.println("DEFENDER WON!");
            int remainingSource = attackerTile.getArmy().getSoldiers() - amount;
            if (remainingSource > 0) {
                attackerTile.getArmy().setSoldiers(remainingSource);
            } else {
                attackerTile.setArmy(null);
            }
        }

        com.gameonjava.utlcs.backend.WarManager result = new com.gameonjava.utlcs.backend.WarManager();

        result.guiAttName = startAttName;
        result.guiDefName = startDefName;
        result.guiAttSoldierCount = startAttSoldiers;
        result.guiDefSoldierCount = startDefSoldiers;

        result.guiAttackerWon = attackerWon;
        result.guiAttRoll = attRoll;
        result.guiDefRoll = defRoll;
        result.guiAttAP = attPower;
        result.guiDefAP = defPower;
        result.guiAttTile = attackerTile;
        result.guiDefTile = defenderTile;

        return result;
    }
    public boolean addTrade(Trade t) {
        // Trade sınıfındaki checkForCreation metodu hem kaynak hem de MP kontrolü
        // yapar.
        if (t.checkForCreation()) {
            activeTrades.add(t);
            return true;
        }
        return false; // Başarısız (Yetersiz kaynak veya MP)
    }

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
        gameMap = json.readValue("Map", Map.class, jsonData);

        players = json.readValue("Players", java.util.ArrayList.class, Player.class, jsonData);

        currentTurn = jsonData.getInt("Turn", 1);
        currentPlayerIndex = jsonData.getInt("CurPlayerIndex", 0);
        activeTrades = json.readValue("ActiveTrades", java.util.ArrayList.class, Trade.class, jsonData);

        relinkTilesWithMap();
    }

    private void relinkTilesWithMap() {
        if (players == null || gameMap == null)
            return;

        for (Player p : players) {
            ArrayList<Tile> loadedTiles = new ArrayList<>(p.getOwnedTiles());

            p.getOwnedTiles().clear();

            for (Tile fakeTile : loadedTiles) {
                int q = fakeTile.getQ();
                int r = fakeTile.getR();

                Tile realTile = gameMap.getTile(q, r);

                if (realTile != null) {
                    p.addTile(realTile);

                    realTile.setOwner(p);

                    if (realTile.hasArmy()) {
                        realTile.getArmy().setPlayer(p);
                    }
                }
            }
        }
    }
}
