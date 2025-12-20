package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Array;
import com.gameonjava.utlcs.backend.Game;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Trade;
import com.gameonjava.utlcs.backend.resources.*;

public class TradeDialog extends Dialog {

    private Player currentPlayer;
    private Game gameBackend;
    
    private SelectBox<String> playerSelect;
    private TextField giveAmountField, wantAmountField;
    private SelectBox<String> giveResSelect, wantResSelect;

    public TradeDialog(String title, Skin skin, Player currentPlayer, Game gameBackend) {
        super(title, skin);
        this.currentPlayer = currentPlayer;
        this.gameBackend = gameBackend;

        initializeUI();
        button("Cancel");
    }

    private void initializeUI() {
        Table content = getContentTable();

        // 1. Hedef Oyuncu Seçimi
        content.add(new Label("Trade with:", getSkin())).padRight(10);
        playerSelect = new SelectBox<>(getSkin());
        
        // Kendisi dışındaki oyuncuları listeye ekle
        Array<String> playerNames = new Array<>();

        playerNames.add("Player 2"); 
        playerNames.add("Player 3");
        playerSelect.setItems(playerNames);
        content.add(playerSelect).row();

        // 2. Verilecek Kaynak
        Table giveTable = new Table();
        giveTable.add(new Label("I Give:", getSkin())).row();
        giveResSelect = new SelectBox<>(getSkin());
        giveResSelect.setItems("Gold", "Food", "Book");
        giveAmountField = new TextField("0", getSkin());
        giveTable.add(giveResSelect).width(80);
        giveTable.add(giveAmountField).width(60);
        
        // 3. İstenen Kaynak
        Table wantTable = new Table();
        wantTable.add(new Label("I Want:", getSkin())).row();
        wantResSelect = new SelectBox<>(getSkin());
        wantResSelect.setItems("Gold", "Food", "Book");
        wantAmountField = new TextField("0", getSkin());
        wantTable.add(wantResSelect).width(80);
        wantTable.add(wantAmountField).width(60);

        content.add(giveTable).pad(10);
        content.add(wantTable).pad(10).row();

        // 4. Teklif Gönder Butonu
        TextButton sendBtn = new TextButton("Send Offer", getSkin());
        sendBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createTradeOffer();
            }
        });
        content.add(sendBtn).colspan(2).padTop(10);
    }

    private void createTradeOffer() {
     
        int giveAmt = Integer.parseInt(giveAmountField.getText());
        int wantAmt = Integer.parseInt(wantAmountField.getText());
        
       
        Resource givenRes = createResourceByType(giveResSelect.getSelected(), giveAmt);
        Resource wantedRes = createResourceByType(wantResSelect.getSelected(), wantAmt);


        
        // Backende ekle
       
        System.out.println("Ticaret teklifi gönderildi!");
        hide();
    }

    private Resource createResourceByType(String type, int amount) {
       
        if (type.equals("Gold")) return new GoldResource(amount, 0,0,0,0); 
        if (type.equals("Food")) return new FoodResource(amount, 0,0,0);
      
        return null;
    }
}