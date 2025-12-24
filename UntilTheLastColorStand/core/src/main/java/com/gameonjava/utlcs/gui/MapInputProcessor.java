package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gameonjava.utlcs.backend.Map;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Tile;
import com.gameonjava.utlcs.backend.WarManager;


public class MapInputProcessor extends InputAdapter {

    // Gerekli Bileşenler
    private MapManager mapManager;
    private OrthographicCamera camera;
    private GameHUD hud;
    private float r; // Hex Yarıçapı

    // Seçim Mantığı İçin
    private Tile selectedTile = null;

    // TEK VE TEMİZ CONSTRUCTOR
    public MapInputProcessor(MapManager mapManager, OrthographicCamera camera, float r, GameHUD hud) {
        this.mapManager = mapManager;
        this.camera = camera;
        this.r = r;
        this.hud = hud;
    }


    private boolean isMoveMode = false;
    private Tile moveSourceTile = null;
    private int amountToMove = 0;

    public void startMoveMode(Tile source, int amount) {
        this.moveSourceTile = source;
        this.amountToMove = amount;
        this.isMoveMode = true;
        System.out.println("Move Mode ON: Moving " + amount + " soldiers.");
    }

    // touchDown metodunu tamamen bununla güncelle:
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.LEFT) return false;

        Vector3 worldPos = camera.unproject(new Vector3(screenX, screenY, 0));
        Tile clickedTile = mapManager.getTileAtPixel(worldPos.x, worldPos.y, r);

        if (isMoveMode) {
            if (clickedTile != null && moveSourceTile != null) {

                com.gameonjava.utlcs.backend.WarManager warResult =
                    hud.getBackendGame().moveArmy(moveSourceTile, clickedTile, amountToMove);

                hud.updateStats(hud.getBackendGame().getCurrentPlayer(), com.gameonjava.utlcs.backend.Game.getCurrentTurn());

                if (warResult != null) {
                    hud.showWarResult(warResult);
                }

                System.out.println("Move/Attack processed.");
            }

            isMoveMode = false;
            moveSourceTile = null;
            clearSelection();
            if (hud.getInteractionBar() != null) hud.getInteractionBar().setVisible(false);
            return true;
        }
        // -------------------------------------------------

        if (clickedTile != null) {
            handleInteraction(clickedTile);
            return true;
        } else {
            clearSelection();
            if (hud.getInteractionBar() != null) {
                hud.getInteractionBar().setVisible(false);
            }
            return true;
        }
    }


    private void handleInteraction(Tile targetTile) {
        Player currentPlayer = hud.getBackendGame().getCurrentPlayer();

        if (targetTile.getOwner() != null && targetTile.getOwner().equals(currentPlayer)) {

            // Eğer bina yoksa -> İnşaat menüsünü açma ihtimali var
            if (!targetTile.hasBuilding() && hud.getInteractionBar().isVisible()) {
                // Eğer zaten seçiliyse ve TEKRAR tıkladıysa Dialog aç
                if (selectedTile == targetTile) {
                    BuildingSelectionDialog build = new BuildingSelectionDialog(
                        Assets.skin,
                        targetTile,
                        currentPlayer,
                        hud,
                        mapManager.gameMap);
                    build.show(hud.stage);

                    clearSelection(); // Dialog açılınca seçimi kaldır
                    return;
                }
            }
        }

        selectTile(targetTile);
    }

    private void selectTile(Tile t) {
        clearSelection();

        selectedTile = t;
        t.highlight = true;

        System.out.println("Tile Seçildi: Q=" + t.getQ() + ", R=" + t.getR());

        // Interaction Bar'ı güncelle
        if (hud.getInteractionBar() != null) {
            hud.getInteractionBar().updateContent(t);
            hud.getInteractionBar().setVisible(true);
        }
    }

    private void clearSelection() {
        if (selectedTile != null) {
            selectedTile.highlight = false;
            selectedTile = null;
        }
        // Garanti olsun diye tüm haritayı temizle
        for (int q = 0; q < 32; q++) {
            for (int row = 0; row < 21; row++) {
                Tile tile = mapManager.gameMap.getTile(q, row);
                if (tile != null) tile.highlight = false;
            }
        }
    }


    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            // Kamera Hareketi (Pan)
            float xMovement = -Gdx.input.getDeltaX() * camera.zoom;
            float yMovement = Gdx.input.getDeltaY() * camera.zoom;
            camera.translate(xMovement, yMovement);
        }
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        // Zoom
        float zoomSpeed = 0.1f;
        camera.zoom += amountY * zoomSpeed;
        camera.zoom = MathUtils.clamp(camera.zoom, 0.25f, 2.0f);
        return true;
    }
    public boolean isMoveMode() {
        return isMoveMode;
    }

    public Tile getMoveSourceTile() {
        return moveSourceTile;
    }


}
