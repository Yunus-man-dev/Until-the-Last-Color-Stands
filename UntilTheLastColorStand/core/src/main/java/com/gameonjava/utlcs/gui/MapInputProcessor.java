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
    private Tile selectedTile = null; // İlk tıklanan tile (Örn: Saldıran asker)

    // TEK VE TEMİZ CONSTRUCTOR
    public MapInputProcessor(MapManager mapManager, OrthographicCamera camera, float r, GameHUD hud) {
        this.mapManager = mapManager;
        this.camera = camera;
        this.r = r;
        this.hud = hud;
    }


    // Sınıfın en başına bu değişkenleri ekle:
    private boolean isMoveMode = false;
    private Tile moveSourceTile = null;

    // Bu metodu sınıfın içine ekle (InteractionBar buradan çağıracak):
    public void startMoveMode(Tile source) {
        this.moveSourceTile = source;
        this.isMoveMode = true;
        System.out.println("Move Mode ON: Select a target tile.");
    }

    // touchDown metodunu tamamen bununla güncelle:
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.LEFT) return false;

        Vector3 worldPos = camera.unproject(new Vector3(screenX, screenY, 0));
        Tile clickedTile = mapManager.getTileAtPixel(worldPos.x, worldPos.y, r);

        // --- YENİ EKLENEN KISIM: HAREKET MODU KONTROLÜ ---
        if (isMoveMode) {
            if (clickedTile != null && moveSourceTile != null) {
                // 1. Backend Hareketini Çağır
                // moveArmy metodu zaten kuralları (komşu mu? MP yeterli mi?) kontrol ediyor.
                hud.getBackendGame().moveArmy(moveSourceTile, clickedTile);

                // 2. Ekranı ve İstatistikleri Güncelle
                hud.updateStats(hud.getBackendGame().getCurrentPlayer(), com.gameonjava.utlcs.backend.Game.getCurrentTurn());

                System.out.println("Move command sent to backend.");
            }

            // İşlem bitince (başarılı veya başarısız) moddan çık
            isMoveMode = false;
            moveSourceTile = null;
            clearSelection(); // Seçim ışığını söndür
            if(hud.getInteractionBar() != null) hud.getInteractionBar().setVisible(false);
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

    // Oyun Mantığı (Savaş, İnşaat, Seçim)
    private void handleInteraction(Tile targetTile) {
        Player currentPlayer = hud.getBackendGame().getCurrentPlayer();

        // A. SALDIRI SENARYOSU (Eğer daha önce bir asker seçtiysek ve şimdi düşmana tıkladıysak)
        if (selectedTile != null && selectedTile.hasArmy() && targetTile.hasArmy()) {
            // Seçili taş benimse VE Hedef taş benim DEĞİLSE -> SALDIR
            if (selectedTile.getOwner().equals(currentPlayer) && !targetTile.getOwner().equals(currentPlayer)) {

                System.out.println("Savaş Başlatılıyor: " + selectedTile.getOwner().getName() + " vs " + targetTile.getOwner().getName());

                WarManager result = hud.getBackendGame().initiateAttack(selectedTile, targetTile);

                if (result != null) {
                    boolean attackerWon = result.isAttackerWon();

                    WarDialog warReport = new WarDialog(
                            Assets.skin,
                            selectedTile,
                            targetTile,
                            attackerWon,
                            result.getAttackerDice(),
                            result.getDefenderDice(),
                            result.getAttackerAP(),
                            result.getDefenderAP());

                    warReport.show(hud.stage);

                    // Savaştan sonra seçimi temizle
                    clearSelection();
                    // Haritayı güncelle (Ölen askerler vs. için)
                    hud.updateStats(currentPlayer, com.gameonjava.utlcs.backend.Game.getCurrentTurn());
                } else {
                    System.out.println("Savaş geçersiz (Mesafe uzak veya AP yetersiz).");
                }
                return; // İşlem tamam, çık.
            }
        }

        // B. İNŞAAT VE SEÇİM SENARYOSU
        // Eğer kendi taşımıza tıkladıysak
        if (targetTile.getOwner() != null && targetTile.getOwner().equals(currentPlayer)) {

            // Eğer bina yoksa -> İnşaat menüsünü aç
            if (!targetTile.hasBuilding() && hud.getInteractionBar().isVisible()) {
                 // Eğer zaten seçiliyse ve tekrar tıkladıysa Dialog aç
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

        // C. STANDART SEÇİM (SELECT)
        selectTile(targetTile);
    }

    private void selectTile(Tile t) {
        // Eski seçimi temizle
        clearSelection();

        // Yeni taşı seç
        selectedTile = t;
        t.highlight = true; // MapManager render ederken bunu sarı yapacak

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

    // --- MOUSE HAREKETLERİ (HOVER & CAMERA) ---

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // Hover efektini şimdilik kapattım, çünkü tıklama seçimiyle (highlight) çakışıyor.
        // İstersen burayı açabilirsin ama selectedTile'ı söndürmediğinden emin olmalısın.
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
}
