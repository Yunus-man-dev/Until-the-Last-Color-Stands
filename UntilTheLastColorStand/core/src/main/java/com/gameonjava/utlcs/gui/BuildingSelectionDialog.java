package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.gameonjava.utlcs.backend.Enum.BuildingType;
import com.gameonjava.utlcs.backend.Enum.TerrainType;
import com.gameonjava.utlcs.backend.Game;
import com.gameonjava.utlcs.backend.Map;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Tile;

public class BuildingSelectionDialog extends Dialog {

    private Tile tile;
    private Player player;
    private GameHUD hud;
    private Map map;

    public BuildingSelectionDialog(Skin skin, Tile tile, Player player, GameHUD hud, Map map) {
        super("", skin);
        this.tile = tile;
        this.player = player;
        this.hud = hud;
        this.map = map;

        // Dialogun kendi arka planını şeffaf yap, sadece slot resmi görünsün
        setBackground((TextureRegionDrawable) null);
        
        // Varsayılan dialog boşluklarını sıfırla ki resim tam otursun
        pad(0);

        setModal(true);
        setMovable(false);
        setResizable(false);

        // İçeriği oluştur
        createContent();
    }

    private void createContent() {
        getContentTable().clearChildren();
        
        boolean canBuildPort = checkPortAvailability();

        // InteractionBar yapısının aynısı: Stack(Image + Table)
        Stack stack = new Stack();
        
        // 1. Arka Plan Resmini Belirle
        Image slotBackground = new Image();
        slotBackground.setScaling(Scaling.none);
        slotBackground.setAlign(Align.center);

        // Port varsa Slot3 (Genelde 4'lü/Geniş), yoksa Slot2 (3'lü/Dar) mantığı
        if (canBuildPort) {
            if (Assets.ibSlot3 != null) {
                slotBackground.setDrawable(new TextureRegionDrawable(Assets.ibSlot3));
            }
        } else {
            if (Assets.ibSlot2 != null) {
                slotBackground.setDrawable(new TextureRegionDrawable(Assets.ibSlot2));
            }
        }

        // 2. Buton Tablosu
        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        buttonTable.center();

        // Ortak Buton Stili
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = getSkin().getFont("default");
        buttonStyle.fontColor = Color.BLACK;
        buttonStyle.up = Assets.btnGenericDr;
        if(Assets.btnGenericDr != null) {
             buttonStyle.down = Assets.btnGenericDr.tint(Color.LIGHT_GRAY);
        }

        // 3. Bina Butonlarını Ekle
        addBuildingButton(buttonTable, "Farm", BuildingType.FARM, buttonStyle);
        addBuildingButton(buttonTable, "Mine", BuildingType.GOLD_MINE, buttonStyle);
        addBuildingButton(buttonTable, "Library", BuildingType.LIBRARY, buttonStyle);

        if (canBuildPort) {
            addBuildingButton(buttonTable, "Port", BuildingType.PORT, buttonStyle);
        }

        // 4. Cancel Butonunu Ekle (En sağa gelecek şekilde sona ekliyoruz)
        TextButton cancelBtn = new TextButton("Cancel", buttonStyle);
        cancelBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cancelAndClose();
            }
        });
        buttonTable.add(cancelBtn).width(120).height(50).expandX().center();

        // Stack'i birleştir
        stack.add(slotBackground);
        stack.add(buttonTable);

        // Dialog içeriğine ekle. Yüksekliği InteractionBar ile aynı (120px)
        getContentTable().add(stack).height(120).expandX().center();
    }

    private void addBuildingButton(Table table, String text, final BuildingType type, TextButton.TextButtonStyle style) {
        TextButton btn = new TextButton(text, style);
        
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                constructBuilding(type);
            }
        });

        table.add(btn).width(120).height(50).expandX().center();
    }

    private void constructBuilding(BuildingType type) {
        boolean success = player.constructBuilding(tile, type);
        if (success) {
            hud.updateStats(player, Game.getCurrentTurn());
            // İnşaat başarılı: Barı güncelle
            if (hud.getInteractionBar() != null) {
                hud.getInteractionBar().updateContent(tile);
            }
            cancelAndClose();
        } else {
            System.out.println("Yetersiz kaynak veya şartlar sağlanmıyor!");
        }
    }

    private boolean checkPortAvailability() {
        if (map == null) return false;
        java.util.ArrayList<Tile> neighbors = map.getNeighbors(tile);
        for (Tile n : neighbors) {
            if (n.getTerrainType() == TerrainType.WATER || n.getTerrainType() == TerrainType.DEEP_WATER) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Dialog show(Stage stage) {
        // Alttaki InteractionBar'ı gizle
        if (hud.getInteractionBar() != null) {
            hud.getInteractionBar().setVisible(false);
        }

        super.show(stage);
        
        // Konumu ekranın EN ALTINA ayarla.
        // -10 vererek biraz daha aşağı (ekran sınırına veya dışına doğru) itiyoruz.
        setPosition(Math.round((stage.getWidth() - getWidth()) / 2), -10);
        
        return this;
    }

    private void cancelAndClose() {
        // InteractionBar'ı geri getir
        if (hud.getInteractionBar() != null) {
            hud.getInteractionBar().setVisible(true);
        }
        hide();
    }
}