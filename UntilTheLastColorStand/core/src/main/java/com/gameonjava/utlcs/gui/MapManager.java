package com.gameonjava.utlcs.gui;

// import com.badlogic.gdx.maps.Map;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gameonjava.utlcs.backend.Map;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Tile; // Senin backend Tile sınıfın
import com.gameonjava.utlcs.backend.Enum.TerrainType;

// public class MapManager {

//     // Backend Haritası
//     public Map gameMap;

//     // Görsel Ayarlar
//     private float hexWidth;
//     private float verticalDist;
//     private float startX = 20f;
//     private float startY = 800f; // Haritanın başlayacağı yükseklik
    
//     // Çizim Boyutları
//     private float drawWidth, drawHeight;
//     private Texture imgGrass, imgMountain, imgWater, imgForest; // Diğer resimler...

//     // Sınırlar (Kamera için)
//     public float mapLeft, mapRight, mapTop, mapBottom;

//     public MapManager(float r) {
//         // 1. Textureları Yükle
//         imgGrass = new Texture("tileGrass_full.png");
//         // Diğerlerini de şimdilik grass yapabilirsin test için:
//         imgMountain = new Texture("tileGrass_full.png"); // İlerde mountain.png yap
//         imgWater = new Texture("tileGrass_full.png");
//         imgForest = new Texture("tileGrass_full.png");

//         // 2. Matematiksel Boyutları Hesapla
//         hexWidth = (float) (Math.sqrt(3) * r);
//         verticalDist = 1.5f * r;

//         // Resim çizim boyutu (Orantılı)
//         float scaleFactor = hexWidth / imgGrass.getWidth();
//         drawWidth = hexWidth;
//         drawHeight = imgGrass.getHeight() * scaleFactor;

//         // 3. Backend Haritasını Başlat
//         gameMap = new Map();
//         gameMap.initializeMap(1); // ID: 1 olan haritayı yükle (Senin kodundaki getMap1Data)

//         // 4. KRİTİK NOKTA: Koordinatları Hesapla (q,r -> x,y)
//         calculatePixelCoordinates();
        
//         // 5. Sınırları Hesapla
//         calculateBounds();
//     }

//     // Backend'deki (q,r) verisini alıp Frontend için (x,y) hesaplar
//     private void calculatePixelCoordinates() {
//         // Backend Map sınıfına erişim metodları (getWidth/Height) eklemen gerekebilir 
//         // veya sabit 32/21 kullanabiliriz.
//         int width = 32; 
//         int height = 21;

//         for (int q = 0; q < width; q++) {
//             for (int r = 0; r < height; r++) {
//                 Tile t = gameMap.getTile(q, r);
                
//                 if (t != null) {
//                     // X Hesabı: q sütunu * genişlik
//                     float x = startX + q * hexWidth;
                    
//                     // Tek satırlarda (r % 2 == 1) yarım genişlik sağa kaydır
//                     // DİKKAT: Senin backend Map mantığında 'r' satır mı sütun mu?
//                     // Genelde (q=col, r=row) kullanılır.
//                     if (r % 2 == 1) {
//                         x += hexWidth / 2f;
//                     }

//                     // Y Hesabı: Başlangıçtan aşağı in
//                     float y = startY - r * verticalDist;

//                     // Tile içine kaydet
//                     t.setX(x);
//                     t.setY(y);
//                 }
//             }
//         }
//     }

//     // Sınırları bulur
//     private void calculateBounds() {
//         mapLeft = Float.MAX_VALUE;
//         mapRight = Float.MIN_VALUE;
//         mapBottom = Float.MAX_VALUE;
//         mapTop = Float.MIN_VALUE;

//         int width = 32;
//         int height = 21;

//         for (int q = 0; q < width; q++) {
//             for (int r = 0; r < height; r++) {
//                 Tile t = gameMap.getTile(q, r);
//                 if (t != null) {
//                     if (t.getPixelX() < mapLeft) mapLeft = t.getPixelX();
//                     if (t.getPixelX() > mapRight) mapRight = t.getPixelX();
//                     if (t.getPixelY() < mapBottom) mapBottom = t.getPixelY();
//                     if (t.getPixelY() > mapTop) mapTop = t.getPixelY();
//                 }
//             }
//         }
//         // Padding
//         float padding = 30f;
//         mapLeft -= padding;
//         mapRight += padding;
//         mapBottom -= padding;
//         mapTop += padding;
//     }

//     public void render(SpriteBatch batch, float textureYOffset) {
//         int width = 32;
//         int height = 21;

//         for (int q = 0; q < width; q++) {
//             for (int r = 0; r < height; r++) {
//                 Tile t = gameMap.getTile(q, r);
                
//                 if (t != null) {
//                     // Tipe göre resim seç
//                     Texture img = imgGrass;
//                     if (t.getTerrainType() == TerrainType.MOUNTAIN) img = imgMountain;
//                     else if (t.getTerrainType() == TerrainType.WATER) img = imgWater;
                    
//                     // Highlight kontrolü
//                     if (t.highlight) batch.setColor(Color.GOLD);
//                     else batch.setColor(Color.WHITE);

//                     // Çizim
//                     batch.draw(img, 
//                                t.getPixelX() - drawWidth/2f, 
//                                t.getPixelY() - drawHeight/2f + textureYOffset, 
//                                drawWidth, drawHeight);
//                 }
//             }
//         }
//         batch.setColor(Color.WHITE); // Reset
//     }
    
//     // Pikselden Tile bulma (Tıklama için)
//     public Tile getTileAtPixel(float worldX, float worldY, float r) {
//         float hitRadiusSq = (r * 1.15f) * (r * 1.15f); // Toleranslı yarıçap karesi
        
//         int width = 32;
//         int height = 21;
        
//         for (int q = 0; q < width; q++) {
//             for (int rIdx = 0; rIdx < height; rIdx++) {
//                 Tile t = gameMap.getTile(q, rIdx);
//                 if (t != null) {
//                     float dx = worldX - t.getPixelX();
//                     float dy = worldY - t.getPixelY();
                    
//                     if (dx*dx + dy*dy < hitRadiusSq) {
//                         return t;
//                     }
//                 }
//             }
//         }
//         return null;
//     }

//     public void dispose() {
//         imgGrass.dispose();
//         imgMountain.dispose();
//         imgWater.dispose();
//         imgForest.dispose();
//     }
// }

public class MapManager {

    public Map gameMap; // Backend Map

    // Ayarlar
    private float hexWidth;
    private float verticalDist;
    
    // --- DÜZELTME 1: Başlangıç Koordinatları ---
    // startX: Soldan biraz boşluk bırak
    // startY: Harita YUKARIDAN aşağı çizildiği için yüksek bir değer olmalı (1800f)
    private float startX = 60f;
    private float startY = 1800f;
    
    // Çizim
    private float drawWidth, drawHeight;
    
    // Sınırlar
    public float mapLeft, mapRight, mapTop, mapBottom;

    public MapManager(Map map,float r) {
        // 1. Backend Haritasını Başlat
        gameMap = map;
       
       
        // 2. Boyutları Hesapla
        hexWidth = (float) (Math.sqrt(3) * r);
        verticalDist = 1.5f * r;

        // Assets.terrainPlain YÜKLENDİKTEN SONRA burası çalışmalı
        if (Assets.terrainPlain != null) {
            float scaleFactor = hexWidth / Assets.terrainPlain.getWidth();
            drawWidth = hexWidth;
            drawHeight = Assets.terrainPlain.getHeight() * scaleFactor;
        } else {
            // Asset yüklenmediyse fallback (Hata almamak için)
            drawWidth = hexWidth;
            drawHeight = hexWidth; 
        }

        // 3. Koordinatları Hesapla
        calculatePixelCoordinates();
        
        // 4. Sınırları Hesapla
        calculateBounds();
    }

    private void calculatePixelCoordinates() {
        int width = 32; 
        int height = 21;

        for (int q = 0; q < width; q++) {
            for (int r = 0; r < height; r++) {
                Tile t = gameMap.getTile(q, r);
                if (t != null) {
                    float x = startX + q * hexWidth;
                    // Tek satırlarda kaydırma yap
                    if (r % 2 == 1) x += hexWidth / 2f;
                    
                    // YUKARIDAN AŞAĞIYA İN
                    float y = startY - r * verticalDist;
                    
                    t.setX(x); // setPixelX kullandığına emin ol (Tile sınıfına göre değişebilir)
                    t.setY(y);
                }
            }
        }
    }

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
        float padding = 60f;
        mapLeft -= padding;
        mapRight += padding;
        mapBottom -= padding;
        mapTop += padding;
    }

    public void render(SpriteBatch batch, float textureYOffset, boolean showSoldiers, boolean showBuildings) {
        int width = 32;
        int height = 21;

        for (int q = 0; q < width; q++) {
            for (int r = 0; r < height; r++) {
                Tile t = gameMap.getTile(q, r);
                
                if (t != null) {
                    // 1. ZEMİN KAPLAMASI
                    Texture terrainImg = Assets.terrainPlain;
                    if (t.getTerrainType() == TerrainType.FOREST) terrainImg = Assets.terrainForest;
                    else if (t.getTerrainType() == TerrainType.MOUNTAIN) terrainImg = Assets.terrainMountain;
                    else if (t.getTerrainType() == TerrainType.WATER) terrainImg = Assets.terrainWater;
                    else if (t.getTerrainType() == TerrainType.DEEP_WATER) terrainImg = Assets.terrainDeepWater;

                    // Seçili ise parlat (GOLD)
                    if (t.highlight) batch.setColor(Color.GOLD);
                    else batch.setColor(Color.WHITE);

                    batch.draw(terrainImg, t.getPixelX() - drawWidth/2f, t.getPixelY() - drawHeight/2f + textureYOffset, drawWidth, drawHeight);

                    // 2. SAHİPLİK ÇERÇEVESİ (OUTLINE)
                    // if (t.getOwner() != null) {
                    //     Player owner = t.getOwner();
                    //     if(owner.getCivilization() != null) {
                    //         String c = owner.getCivilization().getCivilizationColor();
                            
                    //         // Renk koduna göre çerçeveyi boya
                    //         if(c.contains("Red")) batch.setColor(Color.RED);
                    //         else if(c.contains("Blue")) batch.setColor(Color.BLUE);
                    //         else if(c.contains("Gold")) batch.setColor(Color.GOLD);
                    //         else if(c.contains("Brown")) batch.setColor(Color.BROWN);
                    //         else batch.setColor(Color.GRAY);
                            
                    //         batch.draw(Assets.hexOutline, t.getPixelX() - drawWidth/2f, t.getPixelY() - drawHeight/2f + textureYOffset, drawWidth, drawHeight);
                    //     }
                    // }
                    // batch.setColor(Color.WHITE); // Rengi sıfırla
                    if (t.getOwner() != null) {
                        Player owner = t.getOwner();
                        if (owner.getCivilization() != null) {
                            String c = owner.getCivilization().getCivilizationColor();
                            
                            // --- RENK SEÇİMİ ---
                            // Assets sınıfındaki özel renklerini kullanırsan daha uyumlu olur
                            Color drawColor = Color.GRAY; 
                            
                            if (c.contains("Red")) drawColor = Color.RED; 
                            else if (c.contains("Blue")) drawColor = Color.BLUE;
                            else if (c.contains("Gold")) drawColor = Color.GOLD;
                            else if (c.contains("Brown")) drawColor = Color.BROWN;
                            else if (c.contains("Orange")) drawColor = Color.ORANGE;
                            else if (c.contains("Cyan")) drawColor = Color.CYAN;
                            else if (c.contains("Dark Red")) drawColor = Color.MAROON; // Veya Assets.COL_DARK_RED

                            // 1. Rengi Ayarla (Kırmızı, Mavi vs.)
                            batch.setColor(drawColor);
                            //drawColor.r,drawColor.g,drawColor.b,0.6f

                            // 2. TARAMA DESENİNİ ÇİZ (YENİ KISIM)
                            // Assets.tilePattern -> Assets classında yeni tanımladığın değişken
                            batch.draw(Assets.pattern, 
                                    t.getPixelX() - drawWidth / 2f, 
                                    t.getPixelY() - drawHeight / 2f + textureYOffset, 
                                    drawWidth, drawHeight);

                            // 3. KENAR ÇİZGİSİNİ ÇİZ (Opsiyonel - Daha net görünür)
                            // Eğer çok kalabalık gelirse bu kısmı silebilirsin.
                            batch.draw(Assets.hexOutline, 
                                    t.getPixelX() - drawWidth / 2f, 
                                    t.getPixelY() - drawHeight / 2f + textureYOffset, 
                                    drawWidth, drawHeight);
                        }
    }

                    // ÖNEMLİ: Rengi beyaza sıfırla ki sonraki çizilenler (askerler) boyalı çıkmasın.
                    batch.setColor(Color.WHITE);

                        float iconSize = drawWidth * 0.6f; 
                        float iconOffset = iconSize / 2f;  // Tam ortaya gelmesi için kaydırma miktarı

                        // Rengi tamamen resetle
                        batch.setColor(Color.WHITE); 

                        // A) ASKER ÇİZİMİ (Filtre Kontrolü ile)
                        if (showSoldiers && t.hasArmy()) {
                            batch.draw(Assets.soldier, 
                                    t.getPixelX() - iconOffset, 
                                    t.getPixelY() - iconOffset + textureYOffset, 
                                    iconSize, iconSize);
                        } 
                        // B) BİNA ÇİZİMİ (Filtre Kontrolü ile)
                        // else if kullanarak asker varsa binayı gizleyebiliriz veya üst üste bindirebiliriz. 
                        // Genelde asker binanın üstünde durur.
                        else if (showBuildings && t.hasBuilding()) {
                            // Binanın tipine göre ikon seçimi (İlerde eklersin, şimdilik Farm)
                            Texture buildingIcon;
                            String s = t.getBuilding().getName();
                            if(s.equals("Farm")){
                                buildingIcon  = Assets.farm; 
                            }
                            else if(s.equals("Mine")){
                                buildingIcon = Assets.mine;
                            }
                             else if(s.equals("Library")){
                                buildingIcon = Assets.library;
                            }
                            else{
                                buildingIcon = Assets.port;
                            }
                            
                            
                            batch.draw(buildingIcon, 
                                    t.getPixelX() - iconOffset, 
                                    t.getPixelY() - iconOffset + textureYOffset, 
                                    iconSize, iconSize);
                        }
                    }
                }
            }
            
            // batch.setColor(Color.WHITE); // Render bitiminde rengi temizle
    }
    
    public Tile getTileAtPixel(float worldX, float worldY, float r) {
        float hitRadiusSq = (r * 1.15f) * (r * 1.15f);
        int width = 32;
        int height = 21;
        for (int q = 0; q < width; q++) {
            for (int rIdx = 0; rIdx < height; rIdx++) {
                Tile t = gameMap.getTile(q, rIdx);
                if (t != null) {
                    float dx = worldX - t.getPixelX();
                    float dy = worldY - t.getPixelY();
                    if (dx*dx + dy*dy < hitRadiusSq) return t;
                }
            }
        }
        return null;
    }
    
    public void dispose() {
        // Texture'lar Assets'ten geldiği için burada dispose etmeye gerek yok.
        // Assets.dispose() Main.java'da çağrılıyor.
    }
}
