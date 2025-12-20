package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.gameonjava.utlcs.backend.Game;
import com.gameonjava.utlcs.backend.Player;

public class PlayerInfoWidget extends Group {

    private Player targetPlayer;
    private Player myself;
    private Game gameBackend;

    public PlayerInfoWidget(Player targetPlayer, Player myself, Game gameBackend) {
        this.targetPlayer = targetPlayer;
        this.myself = myself;
        this.gameBackend = gameBackend;

        // 1. Ana Arka Plan (Kahverengi)
        Image bg = new Image(Assets.infoBgBrown);
        this.setSize(bg.getWidth(), bg.getHeight());
        this.addActor(bg);

        // 2. Başlık (Örnek: "Emir's Resources")
        Label.LabelStyle titleStyle = new Label.LabelStyle(Assets.skin.getFont("default"), Color.BLACK);

        Label titleLbl = new Label(targetPlayer.getName() + "'s Resources", titleStyle);
        titleLbl.setPosition((getWidth() - titleLbl.getPrefWidth()) / 2, getHeight() - 50);
        this.addActor(titleLbl);

        // 3. Kapatma Butonu (X)
        Label closeBtn = new Label("X", titleStyle);
        closeBtn.setPosition(getWidth() - 40, getHeight() - 45);
        closeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
            }
        });
        this.addActor(closeBtn);

        // 4. Sarı Bilgi Alanı (Resources Background)
        Image infoBg = new Image(Assets.infoBgYellow);

        // --- KONUM DÜZELTME ---
        // Panelin X konumu: Ortada
        float infoX = (getWidth() - infoBg.getWidth()) / 2;

        // Panelin Y konumu: En tepeden başlık kadar (80px) aşağı in, sonra panelin kendi boyu kadar daha in.
        // LibGDX'te (0,0) sol alt köşe olduğu için Y, resmin ALT kenarını temsil eder.
        float infoY = getHeight() - 80 - infoBg.getHeight();

        infoBg.setPosition(infoX, infoY);
        this.addActor(infoBg);

        // 5. Kaynak Tablosu
        Table resTable = new Table();
        resTable.setPosition(infoX, infoY);
        resTable.setSize(infoBg.getWidth(), infoBg.getHeight());
        // Tablo içindekileri hem dikey hem yatay ortala
        resTable.center();

        // İkonlar ve Değerler
        // İKON BOYUTLARI 40 -> 60 yapıldı
        resTable.add(new Image(Assets.gold)).size(60).pad(10);
        resTable.add(new Image(Assets.food)).size(60).pad(10);
        resTable.add(new Image(Assets.book)).size(60).pad(10);
        resTable.add(new Image(Assets.tech)).size(60).pad(10).row();

        // Değerler
        Label.LabelStyle valStyle = new Label.LabelStyle(Assets.skin.getFont("default"), Color.BLACK);

        // Sayı etiketlerini oluşturup büyütüyoruz
        resTable.add(createBigLabel((int)targetPlayer.getGold().getValue() + "", valStyle));
        resTable.add(createBigLabel((int)targetPlayer.getFood().getValue() + "", valStyle));
        resTable.add(createBigLabel((int)targetPlayer.getBook().getValue() + "", valStyle));
        resTable.add(createBigLabel(targetPlayer.getTechnologyPoint() + "", valStyle));

        this.addActor(resTable);

        // 6. Trade Butonu
        TextButton.TextButtonStyle tradeStyle = new TextButton.TextButtonStyle();
        tradeStyle.up = new TextureRegionDrawable(new TextureRegion(Assets.infoBtnTrade));
        tradeStyle.down = new TextureRegionDrawable(new TextureRegion(Assets.infoBtnTrade)).tint(Color.LIGHT_GRAY);
        tradeStyle.font = Assets.skin.getFont("default");
        tradeStyle.fontColor = Color.BLACK;

        TextButton tradeBtn = new TextButton("TRADE", tradeStyle);
        
        // Butonu ortala ve biraz aşağıya koy
        tradeBtn.setPosition((getWidth() - tradeBtn.getWidth()) / 2, 40);

        tradeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                TradeDialog dialog = new TradeDialog(myself, targetPlayer, gameBackend);

                // Dialog'u ekranın ortasına koy
                dialog.setPosition(
                        (getStage().getWidth() - dialog.getWidth()) / 2,
                        (getStage().getHeight() - dialog.getHeight()) / 2);

                getStage().addActor(dialog);
                PlayerInfoWidget.this.remove();
            }
        });

        this.addActor(tradeBtn);
    }

    private Label createBigLabel(String text, Label.LabelStyle style) {
        Label l = new Label(text, style);
        l.setAlignment(com.badlogic.gdx.utils.Align.center);
        return l;
    }
}
