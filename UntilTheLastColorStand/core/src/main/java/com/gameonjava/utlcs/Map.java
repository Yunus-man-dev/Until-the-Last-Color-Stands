package com.gameonjava.utlcs;

import java.util.ArrayList;

import com.gameonjava.utlcs.Enum.BuildingType;
import com.gameonjava.utlcs.Enum.TerrainType;

public class Map {

    private Tile[][] tiles;
    private int width = 32;
    private int height = 21;
    private int mapID;

    // Constants
    private static final int PLAIN = 0;
    private static final int FOREST = 1;
    private static final int MOUNTAIN = 2;
    private static final int WATER = 3;
    private static final int DEEP_WATER = 4;

    public Map() {
        tiles = new Tile[width][height];
    }

    public void initializeMap(int mapID) {
        this.mapID = mapID;
        int[][] mapData = new int[width][height];

        // Load the dataset based on the map ID.
        if (mapID == 1) {
            mapData = getMap1Data();
        } else if (mapID == 2) {
            mapData = getMap2Data();
        } else if (mapID == 3) {
            mapData = getMap3Data();
        }
        // Convert array into Tile objects.
        for (int q = 0; q < width; q++) {
            for (int r = 0; r < height; r++) {
                // Check if the map size matches the array.
                int terrainCode = PLAIN;
                if (q < mapData.length && r < mapData[0].length) {
                    terrainCode = mapData[q][r];
                }

                TerrainType type = TerrainType.PLAIN;
                if (terrainCode == PLAIN) {
                    type = TerrainType.PLAIN;
                } else if (terrainCode == FOREST) {
                    type = TerrainType.FOREST;
                } else if (terrainCode == MOUNTAIN) {
                    type = TerrainType.MOUNTAIN;
                } else if (terrainCode == WATER) {
                    type = TerrainType.WATER;
                } else if (terrainCode == DEEP_WATER) {
                    type = TerrainType.DEEP_WATER;
                }

                // Create tile
                tiles[q][r] = new Tile(q, r, type);
            }
        }
        for (int r = 0; r < height; r++) {
            for (int q = 0; q < width; q++) {

                int terrainCode = PLAIN;

                // Check if the map size matches the array.
                if (r < mapData.length && q < mapData[0].length) {
                    terrainCode = mapData[r][q];
                }

                TerrainType type = TerrainType.PLAIN;
                switch (terrainCode) {
                    case 0:
                        type = TerrainType.PLAIN;
                        break;
                    case 1:
                        type = TerrainType.FOREST;
                        break;
                    case 2:
                        type = TerrainType.MOUNTAIN;
                        break;
                    case 3:
                        type = TerrainType.WATER;
                        break;
                    case 4:
                        type = TerrainType.DEEP_WATER;
                        break;
                }

                // Create tiles
                tiles[q][r] = new Tile(q, r, type);
            }
        }
    }

    // Map data
    // 0:Plain, 1:Forest, 2:Mountain, 3:Water, 4:DeepWater

    private int[][] getMap1Data() {
        return new int[][] {
                 { 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 3, 3, 4, 4, 4, 3, 3, 3, 3, 3, 3 },
                { 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 3, 3, 3, 3, 4, 3, 3, 0, 3, 3, 3, 3 },
                { 4, 4, 3, 4, 3, 4, 4, 4, 3, 3, 3, 3, 3, 3, 0, 0, 0, 3, 0, 0, 0, 0, 0, 3, 3, 3, 0, 0, 0, 0, 3, 3, 3 },
                { 3, 3, 3, 3, 3, 3, 3, 4, 3, 3, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3 },
                { 0, 3, 0, 3, 3, 3, 3, 3, 0, 3, 1, 1, 1, 0, 0, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 3, 3, 3, 3 },
                { 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 3, 3, 4, 4 },
                { 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 3, 3, 4, 4 },
                { 3, 3, 3, 3, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 4, 4 },
                { 3, 3, 0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 1, 0, 3, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3 },
                { 3, 3, 3, 0, 0, 0, 2, 0, 3, 0, 0, 0, 0, 1, 3, 3, 4, 3, 3, 1, 1, 0, 2, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3 },
                { 3, 3, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 1, 3, 4, 4, 4, 3, 1, 1, 2, 2, 0, 0, 1, 1, 0, 0, 0, 0, 3, 3 },
                { 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 3, 3, 3, 3, 3, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 2, 3, 3 },
                { 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2, 2, 2, 0, 3 },
                { 3, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 2, 0, 0, 3 },
                { 3, 3, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 3, 0, 0, 0, 1, 1, 0, 0, 0, 3, 3, 1, 0, 0, 0, 0, 0, 0, 3, 3 },
                { 4, 3, 3, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 3, 3 },
                { 4, 4, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 3, 3, 3 },
                { 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 1, 1, 0, 3, 3, 3 },
                { 3, 3, 3, 0, 0, 0, 0, 0, 1, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 4 },
                { 3, 3, 3, 0, 0, 0, 0, 0, 1, 1, 1, 2, 0, 0, 3, 0, 3, 3, 3, 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4 },
                { 3, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 0, 3, 3, 3, 3, 4, 4, 4, 3, 3, 3, 4, 4, 4, 3, 3, 3, 4, 4, 4, 4, 4 }
        };
    }

    private int[][] getMap2Data() {

        return new int[][] {
                { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 },
                { 3, 0, 0, 0, 3, 1, 3, 0, 3, 0, 3, 3, 3, 0, 3, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 0, 3, 0, 3, 0, 3, 0, 3 },
                { 3, 3, 0, 0, 1, 1, 0, 0, 0, 0, 3, 3, 0, 0, 3, 3, 3, 3, 3, 0, 3, 3, 3, 3, 1, 0, 0, 0, 0, 0, 0, 0, 3 },
                { 3, 0, 0, 0, 1, 0, 0, 0, 3, 3, 3, 4, 3, 3, 3, 3, 3, 3, 0, 0, 3, 3, 4, 4, 3, 0, 0, 0, 0, 0, 0, 0, 3 },
                { 3, 0, 0, 0, 0, 0, 0, 3, 3, 3, 4, 4, 4, 3, 3, 3, 0, 0, 0, 0, 3, 3, 4, 4, 3, 0, 0, 0, 1, 0, 0, 0, 3 },
                { 3, 0, 0, 0, 0, 0, 3, 3, 3, 4, 4, 3, 3, 3, 3, 3, 0, 0, 0, 1, 1, 1, 3, 3, 0, 0, 0, 1, 1, 1, 0, 3, 3 },
                { 3, 3, 0, 1, 1, 3, 3, 3, 3, 3, 3, 0, 3, 3, 3, 3, 3, 0, 0, 0, 1, 0, 3, 3, 0, 3, 1, 1, 1, 3, 3, 3, 3 },
                { 3, 3, 0, 3, 1, 3, 3, 3, 3, 0, 0, 0, 0, 0, 3, 3, 3, 3, 0, 0, 0, 3, 3, 3, 3, 3, 3, 1, 3, 3, 3, 3, 3 },
                { 3, 0, 3, 3, 3, 0, 3, 0, 0, 0, 1, 2, 1, 0, 0, 0, 3, 3, 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 },
                { 3, 0, 3, 3, 3, 3, 0, 0, 0, 2, 2, 1, 1, 0, 0, 3, 3, 4, 3, 3, 0, 3, 3, 1, 3, 1, 3, 3, 3, 3, 0, 0, 3 },
                { 3, 0, 0, 3, 3, 3, 3, 0, 1, 0, 2, 0, 0, 3, 0, 3, 3, 4, 4, 3, 3, 3, 3, 1, 1, 1, 0, 0, 3, 3, 0, 0, 0 },
                { 3, 0, 3, 3, 3, 4, 3, 0, 0, 0, 0, 3, 3, 4, 3, 3, 4, 3, 3, 3, 3, 3, 3, 3, 1, 0, 0, 0, 0, 3, 3, 0, 0 },
                { 3, 3, 3, 3, 4, 4, 3, 3, 0, 0, 3, 3, 4, 4, 3, 3, 3, 0, 3, 3, 3, 3, 3, 3, 0, 0, 0, 3, 3, 3, 3, 3, 3 },
                { 3, 0, 3, 1, 3, 3, 4, 0, 3, 3, 3, 3, 4, 3, 3, 0, 0, 2, 0, 1, 3, 3, 4, 4, 3, 0, 0, 3, 3, 3, 4, 3, 3 },
                { 3, 1, 1, 1, 1, 0, 0, 0, 3, 3, 3, 3, 3, 0, 3, 0, 1, 2, 2, 1, 0, 3, 4, 3, 3, 1, 1, 3, 3, 4, 4, 3, 3 },
                { 3, 3, 1, 0, 1, 0, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 1, 3, 3, 3, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 },
                { 3, 0, 1, 0, 0, 0, 0, 0, 3, 3, 3, 0, 0, 0, 0, 0, 3, 3, 0, 3, 3, 3, 3, 0, 3, 0, 0, 0, 1, 1, 3, 3, 3 },
                { 3, 3, 0, 0, 0, 0, 0, 1, 3, 3, 3, 3, 0, 3, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 3 },
                { 3, 3, 0, 0, 0, 0, 0, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 3, 3 },
                { 3, 3, 3, 0, 0, 0, 0, 3, 3, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 1, 0, 0, 0, 0, 3, 0, 0, 0, 3, 3, 3, 3 },
                { 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 }
        };
    }

    private int[][] getMap3Data() {
        return new int[][] {

        };
    }

    // Checks if coordinates exist.
    public boolean isValidCoordinate(int q, int r) {
        return q >= 0 && q < width && r >= 0 && r < height;
    }

    public Tile getTile(int q, int r) {
        if (isValidCoordinate(q, r)) {
            return tiles[q][r];
        }
        return null;
    }

    // This method returns the neighboring hexagons of a given hexagon.
    public ArrayList<Tile> getNeighbors(Tile t) {
        ArrayList<Tile> neighbors = new ArrayList<>();
        int q = t.getQ();
        int r = t.getR();

        int[][] directions = {
                { 1, 0 }, { 1, -1 }, { 0, -1 },
                { -1, 0 }, { -1, 1 }, { 0, 1 }
        };

        for (int[] dir : directions) {
            int nQ = q + dir[0];
            int nR = r + dir[1];

            if (isValidCoordinate(nQ, nR)) {
                neighbors.add(tiles[nQ][nR]);
            }
        }
        return neighbors;
    }

    // Assigns the starting points of the players.
    public void assignStartingTiles(Player p, int startPositionIndex) {

        int startQ = 0;
        int startR = 0;

        if (startPositionIndex == 0) {
            startQ = 1;
            startR = 1;
        } else if (startPositionIndex == 1) {
            startQ = width - 4;
            startR = height - 4;
        } else if (startPositionIndex == 2) {
            startQ = width - 4;
            startR = 1;
        } else if (startPositionIndex == 3) {
            startQ = 1;
            startR = height - 4;
        } else {
            startQ = 1;
            startR = 1;
        }

        Tile centerTile = getTile(startQ, startR);
        if (centerTile != null) {
            centerTile.setOwner(p);
            p.addTile(centerTile);

            ArrayList<Tile> neighbors = getNeighbors(centerTile);
            for (Tile n : neighbors) {
                if (n != null) { // Null check
                    n.setOwner(p);
                    p.addTile(n);
                }
            }
        }
    }

    public boolean canConstruct(Tile tile, BuildingType bt) {
        // Check if tile is null or already occupied by a building
        if (tile == null || tile.hasBuilding()) {
            return false;
        }

        TerrainType type = tile.getTerrainType();

        // Construction is not allowed on MOUNTAIN, WATER, or DEEP_WATER.
        if (type != TerrainType.PLAIN && type != TerrainType.FOREST) {
            return false;
        }

        // Special Rule for PORT:Although built on land (Plain/Forest), it must be
        // adjacent to a water source.
        if (bt == BuildingType.PORT) {
            ArrayList<Tile> neighbors = getNeighbors(tile);
            boolean hasWaterNeighbor = false;

            for (Tile n : neighbors) {
                // Check if any neighbor is WATER or DEEP_WATER
                if (n.getTerrainType() == TerrainType.WATER || n.getTerrainType() == TerrainType.DEEP_WATER) {
                    hasWaterNeighbor = true;
                    break;
                }
            }

            // If no water neighbor is found, Port cannot be constructed.
            if (!hasWaterNeighbor) {
                return false;
            }
        }

        // For other building types (FARM, GOLD_MINE, LIBRARY),
        return true;
    }
}
