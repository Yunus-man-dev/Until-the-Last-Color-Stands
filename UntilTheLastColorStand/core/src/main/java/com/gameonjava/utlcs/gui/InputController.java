package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Tile;
import com.gameonjava.utlcs.backend.Enum.TerrainType;

public class InputController extends InputAdapter {
    private GameScreen screen;

    public InputController(GameScreen screen) {
        this.screen = screen;
    }

    @Override
    public boolean keyDown(int keycode) {
        // P tuşuna basınca PAUSE menüsü gelsin
        if (keycode == Input.Keys.P) {
            PauseDialog pause = new PauseDialog("Game Paused", Assets.skin, screen.getMainGame(), screen.getHud().backendGame);
            pause.show(screen.getHud().stage);
            return true;
        }

        // B tuşuna basınca BUILDING menüsü gelsin (Test için rastgele bir tile gönderiyoruz)
        if (keycode == Input.Keys.B) {
            // Örnek bir Tile ve Oyuncu alalım
            Tile testTile = screen.getHud().backendGame.getCurrentPlayer().getOwnedTiles().get(0); 
            Player currentPlayer = screen.getHud().backendGame.getCurrentPlayer();
            
            BuildingDialog build = new BuildingDialog("Construct", Assets.skin, testTile, currentPlayer, screen.getHud());
            build.show(screen.getHud().stage);
            return true;
        }

        // T tuşuna basınca TRADE menüsü gelsin
        if (keycode == Input.Keys.T) {
            Player currentPlayer = screen.getHud().backendGame.getCurrentPlayer();
            TradeDialog trade = new TradeDialog("Diplomacy", Assets.skin, currentPlayer, screen.getHud().backendGame);
            trade.show(screen.getHud().stage);
            return true;
        }

        // W tuşuna basınca WAR sonucu gelsin (Örnek verilerle)
        if (keycode == Input.Keys.W) {
            Tile t1 = new Tile(0,0, TerrainType.PLAIN);
            t1.setOwner(new Player("Player 1", new com.gameonjava.utlcs.backend.civilization.Blue()));
            
            Tile t2 = new Tile(1,0, TerrainType.PLAIN);
            t2.setOwner(new Player("Player 2", new com.gameonjava.utlcs.backend.civilization.Red()));

            // Saldıran kazanmış gibi gösterelim, 50 kayıp verelim
            WarDialog war = new WarDialog("Battle Result", Assets.skin, t1, t2, true, 50);
            war.show(screen.getHud().stage);
            return true;
        }

        return false;
    }
}