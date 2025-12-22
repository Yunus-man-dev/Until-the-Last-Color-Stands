package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.gameonjava.utlcs.backend.Game;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Tile;
import com.gameonjava.utlcs.backend.Trade;
import com.gameonjava.utlcs.backend.Army;
import com.gameonjava.utlcs.backend.building.Farm;
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

        // --- P: PAUSE MENU ---
        if (keycode == Input.Keys.P) {
            PauseDialog pause = new PauseDialog("Game Paused", Assets.skin, screen.getMainGame(),
                    screen.getHud().getBackendGame());
            pause.show(screen.getHud().stage);
            return true;
        }

        // --- B: BUILDING DIALOG TEST ---
        if (keycode == Input.Keys.B) {
            Player currentPlayer = screen.getHud().getBackendGame().getCurrentPlayer();
            ArrayList<Tile> tiles = currentPlayer.getOwnedTiles();

            if (tiles != null && !tiles.isEmpty()) {
                Tile testTile = tiles.get(0);

                // DÜZELTME: BuildingSelectionDialog kullanımı
                BuildingSelectionDialog build = new BuildingSelectionDialog(
                        Assets.skin,
                        testTile,
                        currentPlayer,
                        screen.getHud(),
                        screen.getGameHud().getBackendGame().getMap()); // Screen'den Map'i alıyoruz

                build.show(screen.getHud().stage);
            }
            return true;
        }

        // --- T: TRADE DIALOG TEST ---
        if (keycode == Input.Keys.T) {
            Player me = screen.getHud().getBackendGame().getCurrentPlayer();
            // Kendisiyle ticaret yapamaz, listeden başkasını bulmalı ama test için:
            // Dummy bir oyuncu oluşturuyoruz
            Player other = new Player("Other Empire", new Red("Red"));
            TradeDialog trade = new TradeDialog(me, other, screen.getHud().getBackendGame());

            // Ekranın ortasına koy
            trade.setPosition((screen.getHud().stage.getWidth() - trade.getWidth()) / 2,
                    (screen.getHud().stage.getHeight() - trade.getHeight()) / 2);

            screen.getHud().stage.addActor(trade);
            return true;
        }

        // --- M: MAIL / INCOMING TRADE TEST ---
        if (keycode == Input.Keys.M) {
            Player me = screen.getHud().getBackendGame().getCurrentPlayer();
            Player sender = new Player("Rich Empire", new Red("Red"));

            GoldResource goldOffer = new GoldResource(500, 0, 0, 0, 0);
            FoodResource foodRequest = new FoodResource(50, 0, 0, 0);

            Trade fakeTrade = new Trade(sender, me, goldOffer, 500, foodRequest, 50);
            screen.getHud().getBackendGame().addTrade(fakeTrade);
            screen.getHud().updateStats(me, Game.getCurrentTurn());

            System.out.println("DEBUG: Sahte ticaret teklifi oluşturuldu! Mektup ikonunu kontrol et.");
            return true;
        }

        // --- W: WAR RESULT TEST ---
        if (keycode == Input.Keys.W) {
            Tile t1 = new Tile(0, 0, TerrainType.PLAIN);
            t1.setOwner(new Player("Player 1", new com.gameonjava.utlcs.backend.civilization.Blue("Blue")));

            Tile t2 = new Tile(1, 0, TerrainType.PLAIN);
            t2.setOwner(new Player("Player 2", new com.gameonjava.utlcs.backend.civilization.Red("Red")));

            // Dummy War Dialog
            WarDialog warReport = new WarDialog(
                    Assets.skin, t1, t2, true, 6, 3, 150, 100);

            warReport.show(screen.getHud().stage);
            return true;
        }

        // ====================================================================
        // --- INTERACTION BAR TESTLERİ (NUM 1, 2, 3, 0) ---
        // ====================================================================

        // Buraya gelmeden önce kodda "return" olmamalı.

        Player me = screen.getHud().getBackendGame().getCurrentPlayer();

        // TUŞ 1: (Bina Yok, Asker Yok) -> 2 Buton (Construct, Move)
        if (keycode == Input.Keys.NUM_1) {
            Tile t = new Tile(0, 0, TerrainType.PLAIN);
            t.setOwner(me);
            screen.getHud().getInteractionBar().updateContent(t);
            return true;
        }

        // TUŞ 2: (Bina Var Lvl 1, Asker Var) -> 3 Buton
        if (keycode == Input.Keys.NUM_2) {
            Tile t = new Tile(0, 0, TerrainType.PLAIN);
            t.setOwner(me);
            t.setBuilding(new Farm(t, 10)); // Bina ekle
            t.setArmy(new Army(5, me, t)); // Asker ekle
            screen.getHud().getInteractionBar().updateContent(t);
            return true;
        }

        // TUŞ 3: (Bina Max, Asker Yok) -> 1 Buton (Recruit)
        if (keycode == Input.Keys.NUM_3) {
            Tile t = new Tile(0, 0, TerrainType.PLAIN);
            t.setOwner(me);
            Farm f = new Farm(t, 10);
            f.upgrade();
            f.upgrade();
            f.upgrade(); // Max level yap
            t.setBuilding(f);
            screen.getHud().getInteractionBar().updateContent(t);
            return true;
        }

        // TUŞ 0: Gizle
        if (keycode == Input.Keys.NUM_0) {
            screen.getHud().getInteractionBar().setVisible(false);
            return true;
        }

        return false; // Hiçbir tuşa basılmadıysa false dön
    }
}