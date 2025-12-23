package com.gameonjava.utlcs.gui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.gameonjava.utlcs.backend.Game;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Trade;
import com.gameonjava.utlcs.backend.resources.*;

public class IncomingTradesDialog extends Dialog {

    private Game gameBackend;
    private Player currentPlayer;
    private GameHUD hud;

    private static final float CARD_WIDTH = 450;
    private static final float CARD_HEIGHT = 300;

    public IncomingTradesDialog(Skin skin, Game gameBackend, Player currentPlayer, GameHUD hud) {
        super("", skin);
        this.gameBackend = gameBackend;
        this.currentPlayer = currentPlayer;
        this.hud = hud;

        setBackground((TextureRegionDrawable) null);

        setSize(500, 450);
        setModal(true);
        setMovable(false);
        setResizable(false);

        getContentTable().top();


        showCurrentTrade();
    }

    private void showCurrentTrade() {
        ArrayList<Trade> pendingTrades = gameBackend.getPendingTradesFor(currentPlayer);

        if (pendingTrades.isEmpty()) {
            hide();
            return;
        }

        Trade firstTrade = pendingTrades.get(0);

        // Kartı oluştur
        Group tradeCard = createTradeCard(firstTrade);

        // Ekrana ekle (Döngü yok, sadece 1 tane)
        getContentTable().add(tradeCard).width(CARD_WIDTH).height(CARD_HEIGHT).padTop(20);
    }

    private Group createTradeCard(final Trade trade) {
        Group card = new Group();
        card.setSize(CARD_WIDTH, CARD_HEIGHT);

        // A. KART ARKA PLANI
        if (Assets.incomingBg != null) {
            Image bg = new Image(Assets.incomingBg);
            bg.setSize(CARD_WIDTH, CARD_HEIGHT);
            card.addActor(bg);
        }

        // B. SARI BAŞLIK
        float headerH = 50;
        float headerW = CARD_WIDTH - 60;
        float headerX = (CARD_WIDTH - headerW) / 2;
        float headerY = CARD_HEIGHT - headerH - 15;

        if (Assets.incomingHeader != null) {
            Image header = new Image(Assets.incomingHeader);
            header.setSize(headerW, headerH);
            header.setPosition(headerX, headerY);
            card.addActor(header);
        }

        // C. GÖNDEREN İSMİ
        Label.LabelStyle textStyle = new Label.LabelStyle(getSkin().getFont("default"), Color.BLACK);
        Label nameLbl = new Label("Offer From: " + trade.getCreator().getName(), textStyle);
        nameLbl.setAlignment(Align.center);
        nameLbl.setPosition(
                headerX + (headerW - nameLbl.getPrefWidth()) / 2,
                headerY + (headerH - nameLbl.getPrefHeight()) / 2);
        card.addActor(nameLbl);

        // --- KAYNAKLAR ---
        float contentY = 130;

        // SOL (You Get)
        Resource getRes = trade.getGivenResource();
        int getAmt = trade.getGivenResourceAmount();
        Texture getIcon = getIconForResource(getRes);

        Image imgGet = new Image(getIcon);
        imgGet.setSize(48, 48);
        imgGet.setPosition(50, contentY);
        card.addActor(imgGet);

        if (Assets.incomingCircle != null) {
            Image circleGet = new Image(Assets.incomingCircle);
            circleGet.setSize(60, 60);
            circleGet.setPosition(100, contentY - 5);
            card.addActor(circleGet);
        }

        Label amtGet = new Label("+" + getAmt, textStyle);
        amtGet.setAlignment(Align.center);
        amtGet.setPosition(100 + (60 - amtGet.getPrefWidth()) / 2, contentY + 15);
        card.addActor(amtGet);

        // OK
        Label arrow = new Label(">>>", textStyle);
        arrow.setFontScale(1.5f);
        arrow.setPosition((CARD_WIDTH - arrow.getPrefWidth()) / 2, contentY + 15);
        card.addActor(arrow);

        // SAĞ (You Pay)
        Resource payRes = trade.getWantedResource();
        int payAmt = trade.getWantedResourceAmount();
        Texture payIcon = getIconForResource(payRes);

        Image imgPay = new Image(payIcon);
        imgPay.setSize(48, 48);
        imgPay.setPosition(CARD_WIDTH - 100, contentY);
        card.addActor(imgPay);

        if (Assets.incomingCircle != null) {
            Image circlePay = new Image(Assets.incomingCircle);
            circlePay.setSize(60, 60);
            circlePay.setPosition(CARD_WIDTH - 160, contentY - 5);
            card.addActor(circlePay);
        }

        Label amtPay = new Label("-" + payAmt, textStyle);
        amtPay.setAlignment(Align.center);
        amtPay.setPosition(CARD_WIDTH - 160 + (60 - amtPay.getPrefWidth()) / 2, contentY + 15);
        card.addActor(amtPay);

        // D. BUTONLAR
        float btnY = 30;
        float btnW = 120;
        float btnH = 50;

        TextButton.TextButtonStyle acceptStyle = new TextButton.TextButtonStyle();
        if (Assets.btnAccept != null) {
            acceptStyle.up = new TextureRegionDrawable(new TextureRegion(Assets.btnAccept));
            acceptStyle.down = new TextureRegionDrawable(new TextureRegion(Assets.btnAccept)).tint(Color.GRAY);
        }
        acceptStyle.font = getSkin().getFont("default");
        acceptStyle.fontColor = Color.BLACK;

        TextButton acceptBtn = new TextButton("Accept", acceptStyle);
        acceptBtn.setSize(btnW, btnH);
        acceptBtn.setPosition(60, btnY);

        acceptBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameBackend.acceptTrade(trade);

                // HUD güncelle (Böylece mektup butonu hala teklif varsa yanıp sönmeye devam eder)
                hud.updateStats(currentPlayer, Game.getCurrentTurn());

                // --- DEĞİŞİKLİK: Bir işlem yapınca pencereyi direkt kapat ---
                // Kullanıcı sıradaki için tekrar mektuba basmalı.
                hide();
            }
        });
        card.addActor(acceptBtn);

        TextButton.TextButtonStyle refuseStyle = new TextButton.TextButtonStyle();
        if (Assets.btnRefuse != null) {
            refuseStyle.up = new TextureRegionDrawable(new TextureRegion(Assets.btnRefuse));
            refuseStyle.down = new TextureRegionDrawable(new TextureRegion(Assets.btnRefuse)).tint(Color.GRAY);
        }
        refuseStyle.font = getSkin().getFont("default");
        refuseStyle.fontColor = Color.BLACK;

        TextButton refuseBtn = new TextButton("Refuse", refuseStyle);
        refuseBtn.setSize(btnW, btnH);
        refuseBtn.setPosition(CARD_WIDTH - 60 - btnW, btnY);

        refuseBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameBackend.refuseTrade(trade);

                // HUD güncelle
                hud.updateStats(currentPlayer, Game.getCurrentTurn());

                // Pencereyi kapat
                hide();
            }
        });
        card.addActor(refuseBtn);

        return card;
    }

    private Texture getIconForResource(Resource r) {
        if (r instanceof GoldResource)
            return Assets.gold;
        if (r instanceof FoodResource)
            return Assets.food;
        if (r instanceof BookResource)
            return Assets.book;
        return Assets.gold;
    }
}
