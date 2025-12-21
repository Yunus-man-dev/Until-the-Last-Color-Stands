package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gameonjava.utlcs.backend.Map;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Tile;
import com.gameonjava.utlcs.backend.WarManager;

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
        Vector2 worldCoords = viewport.unproject(new Vector2(screenX, screenY));

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

        // --- SALDIRI MANTIĞI ---
        if (sourceTile != null && sourceTile.hasArmy() && targetTile.hasArmy()) {
            if (!targetTile.getOwner().equals(currentPlayer)) {
                WarManager result = screen.getHud().backendGame.initiateAttack(sourceTile, targetTile);

                if (result != null) {
                    boolean attackerWon = result.isAttackerWon();
                    int attRoll = result.getAttackerDice();
                    int defRoll = result.getDefenderDice();
                    int attAP = result.getAttackerAP();
                    int defAP = result.getDefenderAP();

                    WarDialog warReport = new WarDialog(
                            Assets.skin,
                            sourceTile,
                            targetTile,
                            attackerWon,
                            attRoll,
                            defRoll,
                            attAP,
                            defAP);

                    warReport.show(screen.getHud().stage);
                    selector.clearSelection();
                } else {
                    System.out.println("Savaş başlatılamadı.");
                }
                return;
            }
        }

        // --- BİNA İNŞA ETME KONTROLÜ ---
        if (targetTile.getOwner() != null && targetTile.getOwner().equals(currentPlayer)) {
            if (!targetTile.hasBuilding()) {
                // DÜZELTME: BuildingSelectionDialog kullanımı
                BuildingSelectionDialog build = new BuildingSelectionDialog(
                        Assets.skin, 
                        targetTile, 
                        currentPlayer,
                        screen.getHud(), 
                        map); // Map parametresi eklendi
                
                build.show(screen.getHud().stage);
            }
        }
        selector.selectTile(targetTile, map);
    }
}