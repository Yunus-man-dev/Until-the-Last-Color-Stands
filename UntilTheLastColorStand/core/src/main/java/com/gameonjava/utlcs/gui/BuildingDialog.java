package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.graphics.Texture;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Tile;
import com.gameonjava.utlcs.backend.Enum.BuildingType;

public class BuildingDialog extends Dialog {

    private Tile tile;
    private Player player;
    private GameHUD hud; // Kaynakları güncellemek için HUD referansı

    public BuildingDialog(String title, Skin skin, Tile tile, Player player, GameHUD hud) {
        super(title, skin);
        this.tile = tile;
        this.player = player;
        this.hud = hud;

        text("Select a building to construct:");
        initializeBuildings();
        button("Cancel", false);
    }

    private void initializeBuildings() {
        Table contentTable = getContentTable();
        contentTable.row();

        // FARM SEÇENEĞİ
        addBuildingOption(contentTable, "Farm", Assets.farm, BuildingType.FARM, "Cost: 50 Gold");
        
        // MINE SEÇENEĞİ
        addBuildingOption(contentTable, "Gold Mine", Assets.mine, BuildingType.GOLD_MINE, "Cost: 100 Gold");

        // LIBRARY SEÇENEĞİ
        addBuildingOption(contentTable, "Library", Assets.library, BuildingType.LIBRARY, "Cost: 75 Gold");

        // PORT SEÇENEĞİ 
        addBuildingOption(contentTable, "Port", Assets.port, BuildingType.PORT, "Cost: 150 Gold");
    }

    private void addBuildingOption(Table table, String name, Texture icon, final BuildingType type, String costInfo) {
        Table itemTable = new Table();
        
        Image img = new Image(icon);
        Label nameLbl = new Label(name, getSkin());
        Label costLbl = new Label(costInfo, getSkin());
        
        TextButton buildBtn = new TextButton("Build", getSkin());
        buildBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                
                boolean success = player.constructBuilding(tile, type);
                
                if (success) {
                    
                    hud.updateStats(player, com.gameonjava.utlcs.backend.Game.getCurrentTurn());
                    hide();
                } else {
                    
                    
                }
            }
        });

        itemTable.add(img).size(48).padBottom(5).row();
        itemTable.add(nameLbl).row();
        itemTable.add(costLbl).padBottom(5).row();
        itemTable.add(buildBtn).width(80).height(30);

        table.add(itemTable).pad(10);
    }
}