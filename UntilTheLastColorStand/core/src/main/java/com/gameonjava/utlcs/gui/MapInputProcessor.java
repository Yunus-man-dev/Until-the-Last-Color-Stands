package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gameonjava.utlcs.backend.Map;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Tile;

public class MapInputProcessor extends InputAdapter {
    private Viewport viewport;
    private Map map;
    private GameScreen screen;
    private TileSelector selector;

    public MapInputProcessor(GameScreen screen, Map map, Viewport viewport, TileSelector selector) {
        this.screen = screen;
        this.map = map;
        this.viewport = viewport;
        this.selector = selector;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Ekran piksellerini oyun dünyası koordinatlarına çeviriyoruz
        Vector2 worldCoords = viewport.unproject(new Vector2(screenX, screenY));
        
        // Altıgen harita dönüşüm matematiği
        float hexWidth = 64f; 
        int q = Math.round(worldCoords.x / (hexWidth * 0.75f));
        int r = Math.round((worldCoords.y / 48f) - (q % 2 == 0 ? 0 : 0.5f));

        if (q >= 0 && q < 32 && r >= 0 && r < 21) {
            Tile clickedTile = map.getTile(q, r);
            if (clickedTile != null) {
                handleInteraction(clickedTile);
                return true;
            }
        }
        return false;
    }

    private void handleInteraction(Tile targetTile) {
        Tile sourceTile = selector.getSelectedTile();
        Player currentPlayer = screen.getHud().backendGame.getCurrentPlayer();

        //Seçili ordu düşmana saldırıyor mu?
        if (sourceTile != null && sourceTile.hasArmy() && targetTile.hasArmy()) {
            if (!targetTile.getOwner().equals(currentPlayer)) {
                // Backend'de savaşı başlat
                screen.getHud().backendGame.initiateAttack(sourceTile, targetTile);
                
                // GUI'de savaş sonucunu göster
                WarDialog warReport = new WarDialog("Battle Result", Assets.skin, 
                sourceTile, targetTile, true, 10); 
                warReport.show(screen.getHud().stage);
                
                selector.clearSelection();
                return;
            }
        }

        // bina inşa etme kontrolü
        if (targetTile.getOwner() != null && targetTile.getOwner().equals(currentPlayer)) {
            if (!targetTile.hasBuilding()) {
                BuildingDialog build = new BuildingDialog("Construct", Assets.skin, targetTile, currentPlayer, screen.getHud());
                build.show(screen.getHud().stage);
            }
        }
        selector.selectTile(targetTile, map);
    }
}