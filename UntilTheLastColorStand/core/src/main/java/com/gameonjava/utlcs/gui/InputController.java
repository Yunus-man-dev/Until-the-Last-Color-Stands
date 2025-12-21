package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.gameonjava.utlcs.backend.Game;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Tile;
import com.gameonjava.utlcs.backend.Trade;
import com.gameonjava.utlcs.backend.Enum.TerrainType;
import com.gameonjava.utlcs.backend.civilization.Red;
import com.gameonjava.utlcs.backend.resources.FoodResource;
import com.gameonjava.utlcs.backend.resources.GoldResource;

import java.util.ArrayList;

public class InputController extends InputAdapter {
    private GameScreen screen;

    public InputController(GameScreen screen) {
        this.screen = screen;
    }

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Input.Keys.P) {
            PauseDialog pause = new PauseDialog("Game Paused", Assets.skin, screen.getMainGame(),
                    screen.getHud().backendGame);
            pause.show(screen.getHud().stage);
            return true;
        }

        if (keycode == Input.Keys.B) {
            Player currentPlayer = screen.getHud().backendGame.getCurrentPlayer();
            ArrayList<Tile> tiles = currentPlayer.getOwnedTiles();

            if (tiles != null && !tiles.isEmpty()) {
                Tile testTile = tiles.get(0);
                BuildingDialog build = new BuildingDialog("Construct", Assets.skin, testTile, currentPlayer,
                        screen.getHud());
                build.show(screen.getHud().stage);
            }
            return true;
        }

        if (keycode == Input.Keys.T) {
            Player current = screen.getHud().backendGame.getCurrentPlayer();
            Player dummyTarget = new Player("Test Rival", new Red()); 

            TradeDialog trade = new TradeDialog(current, dummyTarget, screen.getHud().backendGame);
            trade.setPosition(
                    (screen.getHud().stage.getWidth() - trade.getWidth()) / 2,
                    (screen.getHud().stage.getHeight() - trade.getHeight()) / 2);

            screen.getHud().stage.addActor(trade);
            return true;
        }

        if (keycode == Input.Keys.M) {
            Player me = screen.getHud().backendGame.getCurrentPlayer();
            Player sender = new Player("Rich Empire", new Red());

            GoldResource goldOffer = new GoldResource(500, 0, 0, 0, 0);
            FoodResource foodRequest = new FoodResource(50, 0, 0, 0);

            Trade fakeTrade = new Trade(sender, me, goldOffer, 500, foodRequest, 50);
            screen.getHud().backendGame.addTrade(fakeTrade);
            screen.getHud().updateStats(me, Game.getCurrentTurn());

            System.out.println("DEBUG: Sahte ticaret teklifi oluşturuldu! Mektup ikonunu kontrol et.");
            return true;
        }

        // --- W: WAR RESULT (Savaş Testi) ---
        if (keycode == Input.Keys.W) {
            Tile t1 = new Tile(0, 0, TerrainType.PLAIN);
            t1.setOwner(new Player("Player 1", new com.gameonjava.utlcs.backend.civilization.Blue()));

            Tile t2 = new Tile(1, 0, TerrainType.PLAIN);
            t2.setOwner(new Player("Player 2", new com.gameonjava.utlcs.backend.civilization.Red()));

            // DÜZELTME: Yeni Constructor'a uygun Dummy Veriler
            // Saldıran (t1), Savunan (t2), Kazanan (true), AttRoll, DefRoll, AttAP, DefAP
            WarDialog war = new WarDialog(Assets.skin, t1, t2, true, 5, 2, 120, 80);
            war.show(screen.getHud().stage);
            return true;
        }

        return false;
    }
}