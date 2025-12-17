package com.gameonjava.utlcs.backend;

import java.util.ArrayList;

import com.gameonjava.utlcs.backend.resources.MovementPoint;

//Description: Controls the game flow, player turns, and global actions.
public class Game {


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



    public Game() {
        this.players = new ArrayList<>();
        this.gameMap = new Map();
        this.activeTrades = new ArrayList<>();
        this.currentPlayerIndex = 0;
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
        //Tilelardaki asker alım sınırını resetler
        gameMap.resetAllTilesTurnData();

        currentPlayerIndex++;

        // Check if full round is completed
        if (currentPlayerIndex >= players.size()) {
            currentPlayerIndex = 0;
            currentTurn++;
        }

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
        int countForRemainingColor = 0;
        for (Player p : players) {
            if(p.isActive()){
                countForRemainingColor++;
            }
            if (p.hasWon()) {
                return true;
            }
        }
        if( countForRemainingColor == 1){
            return true;
        }
        return false;
    }


    //Handles all army movements. Checks rules, costs, and triggers attacks.
    // ITS ASSSUMED THAT WHEN AN ARMY MOVES, ALL TROOPS HAVE BEEN MOVED.
    // IF WE WILL SELECT THE AMOUNT OF SOLDIERS TO MOVE THERE MUST BE AN int amount PARAMETER IN THE ÖETHOD
    // AND MOVING LOGIC MUST BE MODIFIED ACCORDINGLY
    public void moveArmy(Tile owned, Tile target) {
        int ownedsSoldiers = owned.getArmy().getSoldiers();
        Player player = owned.getOwner();
        MovementPoint mp = player.getMp();

        // check movement rules (Passable, Neighbors)
        if (!gameMap.getNeighbors(owned).contains(target)) {
            return; // Not a neighbor
        }
        if (!target.canUnitPass(target)) {
            return; // Impassable terrain (Deep Water/Mountain)
        }

        // check MP cost
        if (!mp.checkForResource(mp.MOVE)) {
            return;
        }

        // check for 3nemy
        if (target.hasArmy() && !target.getOwner().equals(player)) {

            initiateAttack(owned, target);
            return;
        }

        // Deduct MP
        mp.reduceResource(mp.MOVE);

        Army sourceArmy = owned.getArmy();

        owned.removeArmy(); // sets the army variable on the tile to null, READ METHOD DESCRIPTION


        if (target.hasArmy()) {
            // if target has own soldiers
            target.getArmy().addSoldiers(ownedsSoldiers);
        } else {
            Army newArmy = new Army(ownedsSoldiers, player, target);
            target.setArmy(newArmy);

            if (target.getOwner() == null) {
                target.setOwner(player);
                player.addTile(target);
            }
        }

    }

    //WarManager to resolve a battle.
    public void initiateAttack(Tile from, Tile target) {
        Army attacker = from.getArmy();
        Army defender = target.getArmy();

        MovementPoint mp = from.getOwner().getMp();
        if (mp.checkForResource(mp.ATTACK)) {
            mp.reduceResource(mp.ATTACK);

            WarManager war = new WarManager(attacker, defender, target);
        }
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
}
