package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.gameonjava.utlcs.backend.Game;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Trade;
import com.gameonjava.utlcs.backend.resources.*;

public class TradeDialog extends Group {

    private Player creator;
    private Player receiver;
    private Game gameBackend;

    // Seçim durumları
    private String selectedGiveType = "Gold"; 
    private String selectedWantType = "Food"; 
    
    private TextField giveAmountField;
    private TextField wantAmountField;

    private Image giveGoldImg, giveFoodImg, giveBookImg;
    private Image wantGoldImg, wantFoodImg, wantBookImg;

    public TradeDialog(Player creator, Player receiver, Game gameBackend) {
        this.creator = creator;
        this.receiver = receiver;
        this.gameBackend = gameBackend;

        // 1. Ana Arka Plan (Kahverengi)
        Image bg = new Image(Assets.tradeBgBrown);
        this.setSize(bg.getWidth(), bg.getHeight());
        this.addActor(bg);

        // -- BAŞLIK (Title) --
        Label.LabelStyle titleStyle = new Label.LabelStyle(Assets.skin.getFont("default"), Color.BLACK);
        Label titleLbl = new Label("Trade with " + receiver.getName(), titleStyle);
        titleLbl.setPosition((getWidth() - titleLbl.getPrefWidth()) / 2, getHeight() - 55);
        this.addActor(titleLbl);
        
        // -- KAPAT BUTONU (X) --
        Label closeBtn = new Label("X", titleStyle);
        closeBtn.setFontScale(1.5f);
        closeBtn.setPosition(getWidth() - 40, getHeight() - 45);
        closeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
            }
        });
        this.addActor(closeBtn);

        // ==========================================
        // DİNAMİK YÜKSEKLİK VE KONUM AYARLARI
        // ==========================================
        
        float panelHeight = Assets.tradeBgYellow.getHeight();
        float panelWidth = Assets.tradeBgYellow.getWidth();
        
        // İki panel arasındaki dikey boşluk
        float gap = 20f; 
        
        // Üst panelin Y konumu
        float topPanelY = getHeight() - 80 - panelHeight;

        // Alt panelin Y konumu
        float botPanelY = topPanelY - gap - panelHeight;

        // Yatay ortalama
        float panelX = (getWidth() - panelWidth) / 2;


        // ==========================================
        // PANEL 1: YOU GIVE (ÜST SARI PANEL)
        // ==========================================
        
        Image topPanelBg = new Image(Assets.tradeBgYellow);
        topPanelBg.setPosition(panelX, topPanelY);
        this.addActor(topPanelBg);

        Table topTable = new Table();
        topTable.setSize(panelWidth, panelHeight);
        topTable.setPosition(panelX, topPanelY);
        
        // İkonlar
        giveGoldImg = createSelectableIcon(Assets.gold, "Gold", true);
        giveFoodImg = createSelectableIcon(Assets.food, "Food", true);
        giveBookImg = createSelectableIcon(Assets.book, "Book", true);

        // --- DEĞİŞİKLİK BURADA ---
        // size(50) yapıldı ve padding ayarlandı.
        // padLeft ve padRight kullanarak aralarındaki boşluğu eşitledik.
        topTable.add(giveGoldImg).size(50).padLeft(5).padRight(5);
        topTable.add(giveFoodImg).size(50).padLeft(5).padRight(5);
        topTable.add(giveBookImg).size(50).padLeft(30).padRight(5).row();

        // Label ve Input
        topTable.add(new Label("You Give:", titleStyle)).right().padRight(10).padTop(5);
        giveAmountField = new TextField("0", Assets.skin);
        giveAmountField.setAlignment(Align.center);
        topTable.add(giveAmountField).width(70).colspan(2).left().padTop(5);
        
        this.addActor(topTable);
        updateIconVisuals(true); 


        // ==========================================
        // PANEL 2: YOU TAKE (ALT SARI PANEL)
        // ==========================================

        Image botPanelBg = new Image(Assets.tradeBgYellow);
        botPanelBg.setPosition(panelX, botPanelY);
        this.addActor(botPanelBg);

        Table botTable = new Table();
        botTable.setSize(panelWidth, panelHeight);
        botTable.setPosition(panelX, botPanelY);

        // İkonlar
        wantGoldImg = createSelectableIcon(Assets.gold, "Gold", false);
        wantFoodImg = createSelectableIcon(Assets.food, "Food", false);
        wantBookImg = createSelectableIcon(Assets.book, "Book", false);

        // --- DEĞİŞİKLİK BURADA ---
        botTable.add(wantGoldImg).size(50).padLeft(1).padRight(5);
        botTable.add(wantFoodImg).size(50).padLeft(1).padRight(5);
        botTable.add(wantBookImg).size(50).padLeft(30).padRight(5).row();

        // Label ve Input
        botTable.add(new Label("You Take:", titleStyle)).right().padRight(10).padTop(5);
        wantAmountField = new TextField("0", Assets.skin);
        wantAmountField.setAlignment(Align.center);
        botTable.add(wantAmountField).width(70).colspan(2).left().padTop(5);

        this.addActor(botTable);
        updateIconVisuals(false);


        // ==========================================
        // APPROVE BUTONU (EN ALTTA)
        // ==========================================
        
        TextButton.TextButtonStyle approveStyle = new TextButton.TextButtonStyle();
        approveStyle.up = new TextureRegionDrawable(new TextureRegion(Assets.tradeBtnApprove));
        approveStyle.down = new TextureRegionDrawable(new TextureRegion(Assets.tradeBtnApprove)).tint(Color.LIGHT_GRAY);
        approveStyle.font = Assets.skin.getFont("default");
        approveStyle.fontColor = Color.BLACK;

        TextButton approveBtn = new TextButton("APPROVE", approveStyle);
        
        // Butonu biraz daha aşağı çekmek için -15 yerine -20 yaptım
        float btnY = botPanelY - approveBtn.getHeight() - 20;
        if (btnY < 10) btnY = 15; 

        approveBtn.setPosition((getWidth() - approveBtn.getWidth()) / 2, btnY);
        
        approveBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sendTradeOffer();
            }
        });
        this.addActor(approveBtn);
    }

    // --- YARDIMCI METODLAR ---

    private Image createSelectableIcon(Texture texture, final String typeName, final boolean isGiveSection) {
        final Image img = new Image(texture);
        // Tıklama alanını iyileştirmek için image scale edilebilir ama şimdilik size(50) yetecektir.
        img.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isGiveSection) {
                    selectedGiveType = typeName;
                    updateIconVisuals(true);
                } else {
                    selectedWantType = typeName;
                    updateIconVisuals(false);
                }
            }
        });
        return img;
    }

    private void updateIconVisuals(boolean isGiveSection) {
        if (isGiveSection) {
            giveGoldImg.setColor(selectedGiveType.equals("Gold") ? Color.WHITE : Color.DARK_GRAY);
            giveFoodImg.setColor(selectedGiveType.equals("Food") ? Color.WHITE : Color.DARK_GRAY);
            giveBookImg.setColor(selectedGiveType.equals("Book") ? Color.WHITE : Color.DARK_GRAY);
        } else {
            wantGoldImg.setColor(selectedWantType.equals("Gold") ? Color.WHITE : Color.DARK_GRAY);
            wantFoodImg.setColor(selectedWantType.equals("Food") ? Color.WHITE : Color.DARK_GRAY);
            wantBookImg.setColor(selectedWantType.equals("Book") ? Color.WHITE : Color.DARK_GRAY);
        }
    }

    private void sendTradeOffer() {
        try {
            String giveText = giveAmountField.getText().trim();
            String wantText = wantAmountField.getText().trim();
            
            int giveAmt = giveText.isEmpty() ? 0 : Integer.parseInt(giveText);
            int wantAmt = wantText.isEmpty() ? 0 : Integer.parseInt(wantText);

            if (giveAmt <= 0 && wantAmt <= 0) {
                System.out.println("En az bir miktar girilmeli!");
                return;
            }

            Resource givenRes = createResourceByType(selectedGiveType, giveAmt);
            Resource wantedRes = createResourceByType(selectedWantType, wantAmt);

            Trade newTrade = new Trade(creator, receiver, givenRes, giveAmt, wantedRes, wantAmt);
            gameBackend.addTrade(newTrade);
            
            System.out.println("Trade offer sent: " + giveAmt + " " + selectedGiveType + " <-> " + wantAmt + " " + selectedWantType);
            remove(); 
            
        } catch (NumberFormatException e) {
            System.out.println("Lütfen geçerli bir sayı girin!");
        }
    }

    private Resource createResourceByType(String type, int amount) {
        if (type.equals("Gold")) return new GoldResource(amount, 0,0,0,0);
        if (type.equals("Food")) return new FoodResource(amount, 0,0,0);
        if (type.equals("Book")) return new BookResource(amount, 0);
        return new GoldResource(0,0,0,0,0);
    }
}