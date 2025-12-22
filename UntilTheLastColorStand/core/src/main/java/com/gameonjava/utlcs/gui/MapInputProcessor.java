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

// public class MapInputProcessor extends InputAdapter {
//     private Viewport viewport;
//     private Map map;
//     private GameScreen screen;
//     private TileSelector selector;

//     OrthographicCamera camera;
//     MapManager harita;
//     float r;

//     public MapInputProcessor(GameScreen screen, Map map, Viewport viewport, TileSelector selector) {
//         this.screen = screen;
//         this.map = map;
//         this.viewport = viewport;
//         this.selector = selector;
//     }
//     public MapInputProcessor(MapManager harita, OrthographicCamera camera, float r){
//         this.harita = harita;
//         this.camera = camera;
//         this.r = r;
//     }
//     @Override
//     public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//         Vector2 worldCoords = viewport.unproject(new Vector2(screenX, screenY));

//         float hexWidth = 64f;
//         int q = Math.round(worldCoords.x / (hexWidth * 0.75f));
//         int r = Math.round((worldCoords.y / 48f) - (q % 2 == 0 ? 0 : 0.5f));

//         if (q >= 0 && q < 32 && r >= 0 && r < 21) {
//             Tile clickedTile = map.getTile(q, r);
//             if (clickedTile != null) {
//                 handleInteraction(clickedTile);
//                 return true;
//             }
//         }
//         return false;
//     }

//     private void handleInteraction(Tile targetTile) {
//         Tile sourceTile = selector.getSelectedTile();
//         Player currentPlayer = screen.getHud().backendGame.getCurrentPlayer();

//         // --- SALDIRI MANTIĞI ---
//         if (sourceTile != null && sourceTile.hasArmy() && targetTile.hasArmy()) {
//             if (!targetTile.getOwner().equals(currentPlayer)) {
//                 WarManager result = screen.getHud().backendGame.initiateAttack(sourceTile, targetTile);

//                 if (result != null) {
//                     boolean attackerWon = result.isAttackerWon();
//                     int attRoll = result.getAttackerDice();
//                     int defRoll = result.getDefenderDice();
//                     int attAP = result.getAttackerAP();
//                     int defAP = result.getDefenderAP();

//                     WarDialog warReport = new WarDialog(
//                             Assets.skin,
//                             sourceTile,
//                             targetTile,
//                             attackerWon,
//                             attRoll,
//                             defRoll,
//                             attAP,
//                             defAP);

//                     warReport.show(screen.getHud().stage);
//                     selector.clearSelection();
//                 } else {
//                     System.out.println("Savaş başlatılamadı.");
//                 }
//                 return;
//             }
//         }

//         // --- BİNA İNŞA ETME KONTROLÜ ---
//         if (targetTile.getOwner() != null && targetTile.getOwner().equals(currentPlayer)) {
//             if (!targetTile.hasBuilding()) {
//                 // DÜZELTME: BuildingSelectionDialog kullanımı
//                 BuildingSelectionDialog build = new BuildingSelectionDialog(
//                         Assets.skin, 
//                         targetTile, 
//                         currentPlayer,
//                         screen.getHud(), 
//                         map); // Map parametresi eklendi
                
//                 build.show(screen.getHud().stage);
//             }
//         }
//         selector.selectTile(targetTile, map);
//     }

//      @Override
//         public boolean mouseMoved(int screenX, int screenY) {
//             Vector3 worldPos = new Vector3(screenX, screenY, 0);
//             camera.unproject(worldPos);
            
//             // 1. Öncekilerin highlight'ını kapat (Basit yöntem)
//             // (Daha iyisi: MapManager'da clearHighlights() metodu yazmaktır)
//             // Şimdilik brute-force:
//              for (int q = 0; q < 32; q++) {
//                 for (int row = 0; row < 21; row++) {
//                     Tile t = harita.gameMap.getTile(q, row);
//                     if(t != null) t.highlight = false;
//                 }
//             }

//             // 2. Yeni Tile'ı bul
//             Tile t = harita.getTileAtPixel(worldPos.x, worldPos.y, r);
//             if(t != null) {
//                 t.highlight = true;
//                 // Test: Backend verisinin doğru geldiğini gör
//                 // System.out.println("Seçilen: " + t.getQ() + "," + t.getR() + " Tip: " + t.getTerrainType());
//             }
//             return true;
//         }

//          // Mouse Sürükleme (Pan)
//         @Override
//         public boolean touchDragged(int screenX, int screenY, int pointer) {
//             if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
//                 // Zoom faktörüyle çarpıyoruz ki uzaktayken hızlı kaysın
//                 float xHareket = -Gdx.input.getDeltaX() * camera.zoom;
//                 float yHareket = Gdx.input.getDeltaY() * camera.zoom;

//                 camera.translate(xHareket, yHareket);
//                 // Burada update çağırmıyoruz, render içinde zaten çağrılıyor
//             }
//             return true;
//         }

//         // Mouse Tekerleği (Zoom)
//         @Override
//         public boolean scrolled(float amountX, float amountY) {
//             float zoomSpeed = 0.1f;
//             camera.zoom += amountY * zoomSpeed;
            
//             // Zoom sınırları (0.25x ile 1.5x arası)
//             camera.zoom = MathUtils.clamp(camera.zoom, 0.25f, 1.5f);
            
//             System.out.println("Zoom Seviyesi: " + camera.zoom);
//             return true;
//         }



// }
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

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Sadece SOL tık ile işlem yap
        if (button != Input.Buttons.LEFT) return false;

        // 1. Koordinatı Çevir (Kamera -> Dünya)
        Vector3 worldPos = camera.unproject(new Vector3(screenX, screenY, 0));

        // 2. Tile'ı MapManager'dan İste
        Tile clickedTile = mapManager.getTileAtPixel(worldPos.x, worldPos.y, r);

        if (clickedTile != null) {
            handleInteraction(clickedTile);
            return true;
        } else {
            // Boşa tıklandıysa seçimi ve barı kaldır
            clearSelection();
            if (hud.getInteractionBar() != null) {
                hud.getInteractionBar().setVisible(false);
            }
            // Sürükleme (Pan) yapabilmek için true dönmeliyiz
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