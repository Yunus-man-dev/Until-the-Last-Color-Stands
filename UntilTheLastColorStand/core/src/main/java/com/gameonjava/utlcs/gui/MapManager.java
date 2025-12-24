package com.gameonjava.utlcs.gui;

// import com.badlogic.gdx.maps.Map;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gameonjava.utlcs.backend.Map;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Tile; // Senin backend Tile sınıfın
import com.gameonjava.utlcs.backend.Enum.BuildingType;
import com.gameonjava.utlcs.backend.Enum.TerrainType;


public class MapManager {

    public Map gameMap; // Backend Map

    // Ayarlar
    private float hexWidth;
    private float verticalDist;

    private float startX = 60f;
    private float startY = 1800f;

    // Çizim
    private float drawWidth, drawHeight;

    // Sınırlar
    public float mapLeft, mapRight, mapTop, mapBottom;

    public MapManager(Map map,float r) {
        gameMap = map;


        hexWidth = (float) (Math.sqrt(3) * r);
        verticalDist = 1.5f * r;

        if (Assets.terrainPlain != null) {
            float scaleFactor = hexWidth / Assets.terrainPlain.getWidth();
            drawWidth = hexWidth;
            drawHeight = Assets.terrainPlain.getHeight() * scaleFactor;
        } else {
            drawWidth = hexWidth;
            drawHeight = hexWidth;
        }

        calculatePixelCoordinates();

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
                    if (r % 2 == 1) x += hexWidth / 2f;

                    float y = startY - r * verticalDist;

                    t.setX(x);
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

    public void render(SpriteBatch batch, float textureYOffset, boolean showSoldiers, boolean showBuildings, Tile moveSource) {
        int width = 32;
        int height = 21;

        for (int q = 0; q < width; q++) {
            for (int r = 0; r < height; r++) {
                Tile t = gameMap.getTile(q, r);

                if (t != null) {
                    Texture terrainImg = Assets.terrainPlain;
                    if (t.getTerrainType() == TerrainType.FOREST) terrainImg = Assets.terrainForest;
                    else if (t.getTerrainType() == TerrainType.MOUNTAIN) terrainImg = Assets.terrainMountain;
                    else if (t.getTerrainType() == TerrainType.WATER) terrainImg = Assets.terrainWater;
                    else if (t.getTerrainType() == TerrainType.DEEP_WATER) terrainImg = Assets.terrainDeepWater;

                    if (t.highlight) batch.setColor(Color.GOLD);
                    else batch.setColor(Color.WHITE);

                    batch.draw(terrainImg, t.getPixelX() - drawWidth/2f, t.getPixelY() - drawHeight/2f + textureYOffset, drawWidth, drawHeight);
                    if (t.getOwner() != null) {
                        Player owner = t.getOwner();
                        if (owner.getCivilization() != null) {
                            String c = owner.getCivilization().getCivilizationColor();

                            Color drawColor = Color.GRAY;

                            if (c.contains("Red")) drawColor = Color.RED;
                            else if (c.contains("Blue")) drawColor = Color.BLUE;
                            else if (c.contains("Gold")) drawColor = Color.GOLD;
                            else if (c.contains("Brown")) drawColor = Color.BROWN;
                            else if (c.contains("Orange")) drawColor = Color.ORANGE;
                            else if (c.contains("Cyan")) drawColor = Color.CYAN;
                            else if (c.contains("Dark Red")) drawColor = Color.MAROON;


                            batch.setColor(drawColor);

                            batch.draw(Assets.pattern,
                                    t.getPixelX() - drawWidth / 2f,
                                    t.getPixelY() - drawHeight / 2f + textureYOffset,
                                    drawWidth, drawHeight);


                            batch.draw(Assets.hexOutline,
                                    t.getPixelX() - drawWidth / 2f,
                                    t.getPixelY() - drawHeight / 2f + textureYOffset,
                                    drawWidth, drawHeight);
                        }
                    }

                    batch.setColor(Color.WHITE);

                        float iconSize = drawWidth * 0.6f;
                        float iconOffset = iconSize / 2f;

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
                        else if (showBuildings && t.hasBuilding()) {
                            Texture buildingIcon = getBuildingTexture(t);
                            batch.draw(buildingIcon,
                                    t.getPixelX() - iconOffset,
                                    t.getPixelY() - iconOffset + textureYOffset,
                                    iconSize, iconSize);
                        }
                    }
                }
            }

            if (moveSource != null) {
                java.util.ArrayList<Tile> neighbors = gameMap.getNeighbors(moveSource);

                batch.setColor(Color.BLACK);
                float dotSize = drawWidth * 0.3f;
                float dotOffset = dotSize / 2f;

                for (Tile n : neighbors) {
                    if (n.canUnitPass(moveSource)) {

                        boolean drawDot = true;

                        boolean isTargetWater = (n.getTerrainType() == TerrainType.WATER);
                        boolean isSourceLand = (moveSource.getTerrainType() != TerrainType.WATER &&
                                                moveSource.getTerrainType() != TerrainType.DEEP_WATER);

                        if (isTargetWater && isSourceLand) {
                            boolean hasPort = false;
                            if (moveSource.hasBuilding()) {
                                String bName = moveSource.getBuilding().getName();
                                if (bName != null && bName.equals("Port")) {
                                    hasPort = true;
                                }
                            }

                            if (!hasPort) {
                                drawDot = false;
                            }
                        }

                        if (drawDot) {
                            batch.draw(Assets.moveDot,
                                    n.getPixelX() - dotOffset,
                                    n.getPixelY() - dotOffset + textureYOffset,
                                    dotSize, dotSize);
                        }
                    }
                }
                batch.setColor(Color.WHITE);
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
    }

    private Texture getBuildingTexture(Tile t) {
        if (!t.hasBuilding()) return null;

        int level = t.getBuilding().getLevel();
        String s = t.getBuilding().getName();

        if(s.equals("Farm")){
            if (level == 1) return Assets.farm1;
            if (level == 2) return Assets.farm2;
            if (level >= 3) return Assets.farm3;
        }
        if(s.equals("Mine")){
            if (level == 1) return Assets.mine1;
            if (level == 2) return Assets.mine2;
            if (level >= 3) return Assets.mine3;
        }
        if(s.equals("Library")){
            if (level == 1) return Assets.library1;
            if (level == 2) return Assets.library2;
            if (level >= 3) return Assets.library3;
        }
        if(s.equals("Port")){
            if (level == 1) return Assets.port1;
            if (level == 2) return Assets.port2;
            if (level >= 3) return Assets.port3;
        }
        return Assets.farm;
    }
}
