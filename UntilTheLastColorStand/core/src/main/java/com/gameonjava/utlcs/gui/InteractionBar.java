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
import com.gameonjava.utlcs.backend.Map;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Tile;
import com.gameonjava.utlcs.backend.Enum.TerrainType;
import com.gameonjava.utlcs.backend.building.Building;

public class InteractionBar extends Table {

    private Skin skin;
    private Game gameBackend;
    private GameHUD hud;
    private Map map; 

    // UI Bileşenleri
    private Image slotBackground;
    private Table buttonTable;

    public InteractionBar(Skin skin, Game gameBackend, GameHUD hud, Map map) {
        this.skin = skin;
        this.gameBackend = gameBackend;
        this.hud = hud;
        this.map = map;

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

        this.add(stack).height(60).expandX().center();
    }

    public void updateContent(final Tile t) {
        buttonTable.clear();
        
        if (t == null) {
            setVisible(false);
            return;
        }
        Player me = gameBackend.getCurrentPlayer();
        
        // 1. GÜVENLİK KONTROLÜ: Eğer seçili kare benim değilse GİZLE.
        // Bu sayede End Turn yapıldığında, sıra başkasına geçtiği an bar güncellenirse kapanır.
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
                // DEĞİŞİKLİK: Artık dialog açmıyor, direkt develop yapmayı deniyor.
                addTextButton("Develop", () -> developBuilding(t));
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
        
        // Ekstra: Bina var (Max değil) ve Asker Yok -> 2 Buton
        else if (hasBuilding && !isMaxLevel && !hasArmy) {
            setSlotImage(Assets.ibSlot2);
            // DEĞİŞİKLİK: Direkt develop.
            addTextButton("Develop", () -> developBuilding(t));
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

    // İnşaat menüsünü açar (Sadece bina yoksa kullanılır)
    private void openBuildingDialog(Tile t) {
        if (t.getOwner() != null) {
            BuildingSelectionDialog build = new BuildingSelectionDialog(Assets.skin, t, t.getOwner(), hud, map);
            build.show(hud.stage);
        }
    }

    // DEĞİŞİKLİK: Binayı direkt geliştirir
    private void developBuilding(Tile t) {
        Player player = gameBackend.getCurrentPlayer();
        
        if (t.hasBuilding()) {
            Building b = t.getBuilding();
            
            // Maliyet Kontrolü: GoldResource içindeki DEVELOP sabiti kullanılır
            double cost = player.getGold().DEVELOP;
            
            if (player.getGold().checkForResource(cost)) {
                // 1. Parayı düş
                player.getGold().reduceResource(cost);
                
                // 2. Binayı yükselt
                b.upgrade();
                
                System.out.println("Building upgraded to Level " + b.getLevel());
                
                // 3. UI Güncelle (Para azaldı, göstergeyi yenile)
                hud.updateStats(player, Game.getCurrentTurn());
                
                // 4. Barı güncelle (Belki bina Max Level oldu, butonlar değişmeli)
                updateContent(t);
                
            } else {
                System.out.println("Yetersiz Altın! Gerekli: " + cost);
                // İstersen buraya bir pop-up uyarısı ekleyebilirsin
            }
        }
    }

    private void openRecruitDialog(Tile t) {
        System.out.println("Recruit Dialog should open here.");
    }

    private void enableMoveMode(Tile t) {
        System.out.println("Move Mode Active.");
    }
}