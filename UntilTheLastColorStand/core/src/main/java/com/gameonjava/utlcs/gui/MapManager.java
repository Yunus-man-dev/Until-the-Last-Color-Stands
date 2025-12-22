package com.gameonjava.utlcs.gui;

// import com.badlogic.gdx.maps.Map;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gameonjava.utlcs.backend.Map;
import com.gameonjava.utlcs.backend.Tile; // Senin backend Tile sınıfın
import com.gameonjava.utlcs.backend.Enum.TerrainType;

public class MapManager {

    // Backend Haritası
    public Map gameMap;

    // Görsel Ayarlar
    private float hexWidth;
    private float verticalDist;
    private float startX = 20f;
    private float startY = 800f; // Haritanın başlayacağı yükseklik
    
    // Çizim Boyutları
    private float drawWidth, drawHeight;
    private Texture imgGrass, imgMountain, imgWater, imgForest; // Diğer resimler...

    // Sınırlar (Kamera için)
    public float mapLeft, mapRight, mapTop, mapBottom;

    public MapManager(float r) {
        // 1. Textureları Yükle
        imgGrass = new Texture("tileGrass_full.png");
        // Diğerlerini de şimdilik grass yapabilirsin test için:
        imgMountain = new Texture("tileGrass_full.png"); // İlerde mountain.png yap
        imgWater = new Texture("tileGrass_full.png");
        imgForest = new Texture("tileGrass_full.png");

        // 2. Matematiksel Boyutları Hesapla
        hexWidth = (float) (Math.sqrt(3) * r);
        verticalDist = 1.5f * r;

        // Resim çizim boyutu (Orantılı)
        float scaleFactor = hexWidth / imgGrass.getWidth();
        drawWidth = hexWidth;
        drawHeight = imgGrass.getHeight() * scaleFactor;

        // 3. Backend Haritasını Başlat
        gameMap = new Map();
        gameMap.initializeMap(1); // ID: 1 olan haritayı yükle (Senin kodundaki getMap1Data)

        // 4. KRİTİK NOKTA: Koordinatları Hesapla (q,r -> x,y)
        calculatePixelCoordinates();
        
        // 5. Sınırları Hesapla
        calculateBounds();
    }

    // Backend'deki (q,r) verisini alıp Frontend için (x,y) hesaplar
    private void calculatePixelCoordinates() {
        // Backend Map sınıfına erişim metodları (getWidth/Height) eklemen gerekebilir 
        // veya sabit 32/21 kullanabiliriz.
        int width = 32; 
        int height = 21;

        for (int q = 0; q < width; q++) {
            for (int r = 0; r < height; r++) {
                Tile t = gameMap.getTile(q, r);
                
                if (t != null) {
                    // X Hesabı: q sütunu * genişlik
                    float x = startX + q * hexWidth;
                    
                    // Tek satırlarda (r % 2 == 1) yarım genişlik sağa kaydır
                    // DİKKAT: Senin backend Map mantığında 'r' satır mı sütun mu?
                    // Genelde (q=col, r=row) kullanılır.
                    if (r % 2 == 1) {
                        x += hexWidth / 2f;
                    }

                    // Y Hesabı: Başlangıçtan aşağı in
                    float y = startY - r * verticalDist;

                    // Tile içine kaydet
                    t.setX(x);
                    t.setY(y);
                }
            }
        }
    }

    // Sınırları bulur
    private void calculateBounds() {
        mapLeft = Float.MAX_VALUE;
        mapRight = Float.MIN_VALUE;
        mapBottom = Float.MAX_VALUE;
        mapTop = Float.MIN_VALUE;

        int width = 32;
        int height = 21;

        for (int q = 0; q < width; q++) {
            for (int r = 0; r < height; r++) {
                Tile t = gameMap.getTile(q, r);
                if (t != null) {
                    if (t.getPixelX() < mapLeft) mapLeft = t.getPixelX();
                    if (t.getPixelX() > mapRight) mapRight = t.getPixelX();
                    if (t.getPixelY() < mapBottom) mapBottom = t.getPixelY();
                    if (t.getPixelY() > mapTop) mapTop = t.getPixelY();
                }
            }
        }
        // Padding
        float padding = 30f;
        mapLeft -= padding;
        mapRight += padding;
        mapBottom -= padding;
        mapTop += padding;
    }

    public void render(SpriteBatch batch, float textureYOffset) {
        int width = 32;
        int height = 21;

        for (int q = 0; q < width; q++) {
            for (int r = 0; r < height; r++) {
                Tile t = gameMap.getTile(q, r);
                
                if (t != null) {
                    // Tipe göre resim seç
                    Texture img = imgGrass;
                    if (t.getTerrainType() == TerrainType.MOUNTAIN) img = imgMountain;
                    else if (t.getTerrainType() == TerrainType.WATER) img = imgWater;
                    
                    // Highlight kontrolü
                    if (t.highlight) batch.setColor(Color.GOLD);
                    else batch.setColor(Color.WHITE);

                    // Çizim
                    batch.draw(img, 
                               t.getPixelX() - drawWidth/2f, 
                               t.getPixelY() - drawHeight/2f + textureYOffset, 
                               drawWidth, drawHeight);
                }
            }
        }
        batch.setColor(Color.WHITE); // Reset
    }
    
    // Pikselden Tile bulma (Tıklama için)
    public Tile getTileAtPixel(float worldX, float worldY, float r) {
        float hitRadiusSq = (r * 1.15f) * (r * 1.15f); // Toleranslı yarıçap karesi
        
        int width = 32;
        int height = 21;
        
        for (int q = 0; q < width; q++) {
            for (int rIdx = 0; rIdx < height; rIdx++) {
                Tile t = gameMap.getTile(q, rIdx);
                if (t != null) {
                    float dx = worldX - t.getPixelX();
                    float dy = worldY - t.getPixelY();
                    
                    if (dx*dx + dy*dy < hitRadiusSq) {
                        return t;
                    }
                }
            }
        }
        return null;
    }

    public void dispose() {
        imgGrass.dispose();
        imgMountain.dispose();
        imgWater.dispose();
        imgForest.dispose();
    }
}
