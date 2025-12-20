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

        // --- P: PAUSE MENU ---
        if (keycode == Input.Keys.P) {
            PauseDialog pause = new PauseDialog("Game Paused", Assets.skin, screen.getMainGame(),
                    screen.getHud().backendGame);
            pause.show(screen.getHud().stage);
            return true;
        }

        // --- B: BUILDING MENU ---
        if (keycode == Input.Keys.B) {
            Player currentPlayer = screen.getHud().backendGame.getCurrentPlayer();
            ArrayList<Tile> tiles = currentPlayer.getOwnedTiles();

            if (tiles != null && !tiles.isEmpty()) {
                // Test için oyuncunun ilk karesini alıyoruz
                Tile testTile = tiles.get(0);
                BuildingDialog build = new BuildingDialog("Construct", Assets.skin, testTile, currentPlayer,
                        screen.getHud());
                build.show(screen.getHud().stage);
            } else {
                System.out.println("DEBUG: Oyuncunun hiç toprağı yok, bina yapılamaz.");
            }
            return true;
        }

        // --- T: TRADE MENU (Manuel Test) ---
        if (keycode == Input.Keys.T) {
            Player current = screen.getHud().backendGame.getCurrentPlayer();
            Player dummyTarget = new Player("Test Rival", new Red()); // Test oyuncusu

            TradeDialog trade = new TradeDialog(current, dummyTarget, screen.getHud().backendGame);

            // Konumlandırma
            trade.setPosition(
                    (screen.getHud().stage.getWidth() - trade.getWidth()) / 2,
                    (screen.getHud().stage.getHeight() - trade.getHeight()) / 2);

            screen.getHud().stage.addActor(trade);
            return true;
        }

        // --- M: MAIL / INCOMING TRADE (Gelen Teklif Testi) ---
        // Buna basınca sana bir ticaret teklifi gelmiş gibi simüle eder.
        // Mektup ikonunun çıkıp çıkmadığını test etmek için kullanabilirsin.
        if (keycode == Input.Keys.M) {
            Player me = screen.getHud().backendGame.getCurrentPlayer();
            Player sender = new Player("Rich Empire", new Red());

            // Basit kaynaklar (Constructor parametrelerin Resources sınıflarında farklı
            // olabilir,
            // burada basitçe 0 veya varsayılan değerler geçiyorum)
            GoldResource goldOffer = new GoldResource(500, 0, 0, 0, 0);
            FoodResource foodRequest = new FoodResource(50, 0, 0, 0);

            // Teklif: Sana 500 Altın verip 50 Yemek istiyor
            Trade fakeTrade = new Trade(sender, me, goldOffer, 500, foodRequest, 50);

            // Oyuna teklifi ekle
            screen.getHud().backendGame.addTrade(fakeTrade);

            // HUD'u güncelle ki mektup ikonu görünsün
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

            // Saldıran kazanmış gibi gösterelim, 50 kayıp verelim
            WarDialog war = new WarDialog("Battle Result", Assets.skin, t1, t2, true, 50);
            war.show(screen.getHud().stage);
            return true;
        }

        return false;
    }
}