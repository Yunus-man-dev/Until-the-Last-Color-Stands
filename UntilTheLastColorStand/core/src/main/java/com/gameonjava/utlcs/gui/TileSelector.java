package com.gameonjava.utlcs.gui;

import java.util.ArrayList;

import com.gameonjava.utlcs.backend.Map;
import com.gameonjava.utlcs.backend.Tile;

public class TileSelector {
    private Tile selectedTile;
    private ArrayList<Tile> highlightedTiles;

    public TileSelector() {
        this.highlightedTiles = new ArrayList<>();
    }

    public void selectTile(Tile tile, Map map) {
        this.selectedTile = tile;
        this.highlightedTiles.clear();
        if (tile != null) {
            // Seçilen tile'ın komşularını Map sınıfından alıyoruz
            this.highlightedTiles = map.getNeighbors(tile);
        }
    }

    public void clearSelection() {
        this.selectedTile = null;
        this.highlightedTiles.clear();
    }

    public Tile getSelectedTile() { return selectedTile; }
}