package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.gameonjava.utlcs.backend.Game;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Tile;
import com.gameonjava.utlcs.backend.Enum.TerrainType;

public class InteractionBar extends Table {

    private Skin skin;
    private Game gameBackend;
    private GameHUD hud; 

    // UI Bileşenleri
    private Image slotBackground; 
    private Table buttonTable;    

    public InteractionBar(Skin skin, Game gameBackend, GameHUD hud) {
        this.skin = skin;
        this.gameBackend = gameBackend;
        this.hud = hud;

        this.bottom(); 
        this.padBottom(20); 

        Stack stack = new Stack();
        
        slotBackground = new Image();
        slotBackground.setScaling(Scaling.none); 
        slotBackground.setAlign(Align.center);

        buttonTable = new Table();
        buttonTable.setFillParent(true); 

        stack.add(slotBackground);
        stack.add(buttonTable);

        this.add(stack).height(50).expandX().center(); 
    }

    public void updateContent(final Tile t) {
        buttonTable.clear(); 
        
        if (t == null) {
            setVisible(false);
            return;
        }
        Player me = gameBackend.getCurrentPlayer();
        
        if (t.getOwner() == null || !t.getOwner().equals(me)) {
            setVisible(false);
            return;
        }

        setVisible(true);

        boolean hasBuilding = t.hasBuilding();
        boolean isMaxLevel = hasBuilding && t.getBuilding().getLevel() >= 3; 
        boolean hasArmy = t.hasArmy() && t.getArmy().getSoldiers() > 0;
        
        boolean canConstruct = (t.getTerrainType() == TerrainType.PLAIN || 
                                t.getTerrainType() == TerrainType.FOREST);

        // --- SENARYOLAR ---

        // 1. (Bina Yok/Yükseltilebilir) VE (Asker Var) -> 3 BUTON
        if ( (!hasBuilding || !isMaxLevel) && hasArmy ) {
            setSlotImage(Assets.ibSlot3); 
            
            if (!hasBuilding) {
                if (canConstruct) addTextButton("Construct", () -> openBuildingDialog(t));
                else addEmptySlot(); 
            } else {
                addTextButton("Develop", () -> openBuildingDialog(t));
            }

            addTextButton("Recruit", () -> openRecruitDialog(t));
            addTextButton("Move", () -> enableMoveMode(t));
        }
        
        // 2. (Bina Tam Seviye) VE (Asker Var) -> 2 BUTON
        else if (isMaxLevel && hasArmy) {
            setSlotImage(Assets.ibSlot2); 
            
            addTextButton("Recruit", () -> openRecruitDialog(t));
            addTextButton("Move", () -> enableMoveMode(t));
        }

        // 3. (Bina Yok) VE (Asker Yok) -> 2 BUTON
        else if (!hasBuilding && !hasArmy) {
            
            if (canConstruct) {
                setSlotImage(Assets.ibSlot2); 
                addTextButton("Construct", () -> openBuildingDialog(t));
                addTextButton("Move", () -> System.out.println("No soldiers!")); 
            } else {
                setVisible(false);
            }
        }

        // 4. (Bina Var/Tam) VE (Asker Yok) -> 1 BUTON
        else if (hasBuilding && isMaxLevel && !hasArmy) {
            setSlotImage(Assets.ibSlot1); 
            addTextButton("Recruit", () -> openRecruitDialog(t));
        }
        
        // Ekstra Durum: Bina var (Max değil) ve Asker Yok -> 2 Buton
        else if (hasBuilding && !isMaxLevel && !hasArmy) {
            setSlotImage(Assets.ibSlot2);
            addTextButton("Develop", () -> openBuildingDialog(t));
            addTextButton("Recruit", () -> openRecruitDialog(t));
        }
        
        else {
            setVisible(false);
        }
    }

    private void setSlotImage(Texture tex) {
        if (tex != null) {
            slotBackground.setDrawable(new TextureRegionDrawable(tex));
        }
    }

    private void addTextButton(String text, Runnable action) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = skin.getFont("default"); 
        style.fontColor = Color.BLACK;        
        style.up = Assets.btnGenericDr;       
        
        if(Assets.btnGenericDr != null)
             style.down = Assets.btnGenericDr.tint(Color.LIGHT_GRAY);

        TextButton btn = new TextButton(text, style);
        
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                action.run();
            }
        });

       
        buttonTable.add(btn).width(120).height(50).expandX().center(); 
    }
    
    private void addEmptySlot() {
        buttonTable.add().expandX(); 
    }

    // --- AKSİYONLAR ---

    private void openBuildingDialog(Tile t) {
        System.out.println("Building Dialog Açılıyor...");
         if (t.getOwner() != null) {
            BuildingDialog build = new BuildingDialog("Construct", Assets.skin, t, t.getOwner(), hud);
            build.show(hud.stage);
        }
    }

    private void openRecruitDialog(Tile t) {
        System.out.println("Recruit Dialog Açılıyor...");
        // BURADA: Yeni RecruitDialog classını çağıracağız
    }

    private void enableMoveMode(Tile t) {
        System.out.println("Move Mode Aktif.");
        
    }
}