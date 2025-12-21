package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.gameonjava.utlcs.backend.Army;
import com.gameonjava.utlcs.backend.Tile;

public class WarDialog extends Dialog {

    private static final float DIALOG_WIDTH = 350;
    private static final float DIALOG_HEIGHT = 450;
    private static final float ANIM_DELAY_STEP = 0.4f; 

    public WarDialog(Skin skin, Tile attackerTile, Tile defenderTile, boolean attackerWon, 
                     int attRoll, int defRoll, int attFinalAP, int defFinalAP) {
        super("", skin);

        if (Assets.warBgBrownDr != null) {
            setBackground(Assets.warBgBrownDr);
        }
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setModal(true);
        setMovable(false);
        setResizable(false);
        
        getContentTable().top().pad(20);

        Label.LabelStyle titleStyle = new Label.LabelStyle(skin.getFont("default"), Color.BLACK);
        Label.LabelStyle defaultStyle = new Label.LabelStyle(skin.getFont("default"), Color.BLACK);

        Label warTitle = new Label("War", titleStyle);
        
        warTitle.setFontScale(0.4f);
        getContentTable().add(warTitle).padBottom(200).row();
        getContentTable().add(warTitle).padBottom(2).row();

        Stack contentStack = new Stack();
        Image yellowBg = new Image(Assets.warBgYellowDr);
        contentStack.add(yellowBg);

        Table dataTable = new Table();
        dataTable.pad(50);
        
        Army attArmy = attackerTile.getArmy();
        Army defArmy = defenderTile.getArmy();

        // --- İSİMLER ---
        Label attNameLbl = new Label("     "+ attackerTile.getOwner().getName(), titleStyle);
        Label defNameLbl = new Label(defenderTile.getOwner() != null ? defenderTile.getOwner().getName() : "Neutral"+"        ", titleStyle);
        attNameLbl.setFontScale(0.5f); defNameLbl.setFontScale(0.5f);

        float currentDelay = ANIM_DELAY_STEP;
        dataTable.add(animateAppearance(attNameLbl, currentDelay)).expandX();
        dataTable.add(new Label("   VS        ", titleStyle)).pad(10);
        dataTable.add(animateAppearance(defNameLbl, currentDelay)).expandX().row();
        
        currentDelay += ANIM_DELAY_STEP;

        // --- ASKER SAYISI (SOLDIER) ---
        int attCount = (attArmy != null) ? attArmy.getSoldiers() : 0;
        int defCount = (defArmy != null) ? defArmy.getSoldiers() : 0;
        
        addStatRow(dataTable, Assets.soldier, Assets.soldier, 
                   attCount + "", defCount + "", "     Soldier Amount        ", defaultStyle, currentDelay);
        currentDelay += ANIM_DELAY_STEP;

        // --- TEKNOLOJİ PUANI (TECH) ---
        int attTech = attackerTile.getOwner().getTechnologyPoint();
        int defTech = (defenderTile.getOwner() != null) ? defenderTile.getOwner().getTechnologyPoint() : 0;
        
        addStatRow(dataTable, Assets.tech, Assets.tech, 
                   attTech + "  Tp", defTech + " Tp ", "     Research Point        ", defaultStyle, currentDelay);
        currentDelay += ANIM_DELAY_STEP;

        // --- ZAR SONUCU (DICE) - DİNAMİK SEÇİM ---
        Texture attDiceTex = getDiceTexture(attRoll);
        Texture defDiceTex = getDiceTexture(defRoll);

        addStatRow(dataTable, attDiceTex, defDiceTex, 
                   "    Roll: " + attRoll, "Roll: " + defRoll+"    ", "     Dice Point        ", defaultStyle, currentDelay);
        currentDelay += ANIM_DELAY_STEP;

        // --- SALDIRI GÜCÜ (AP/DP - KILIÇ) ---
        addStatRow(dataTable, Assets.war, Assets.war, 
                   "    Ap: " + attFinalAP, "Dp: " + defFinalAP+"    ", "     Attack/Defense        ", defaultStyle, currentDelay);
        currentDelay += ANIM_DELAY_STEP;
        
        contentStack.add(dataTable);
        getContentTable().add(contentStack).growX().height(350).row();

        // --- KAZANAN YAZISI ---
        String winnerText = attackerWon ? attackerTile.getOwner().getName() + " Wins!" : 
                                          (defenderTile.getOwner() != null ? defenderTile.getOwner().getName() : "Neutral") + " WINS!";
        
        Label winnerLbl = new Label(winnerText, titleStyle);
        winnerLbl.setFontScale(0.5f);
        winnerLbl.setColor(attackerWon ? Color.BLACK : Color.FIREBRICK);
        winnerLbl.setAlignment(Align.center);
        
        getContentTable().add(animateAppearance(winnerLbl, currentDelay)).padTop(20).row();

        // --- DONE BUTONU ---
        Table buttonTable = new Table();
        
        TextButton.TextButtonStyle doneStyle = new TextButton.TextButtonStyle();
        doneStyle.font = skin.getFont("default");
        doneStyle.fontColor = Color.BLACK;
        
        if (Assets.warBtnDoneDr != null) {
            doneStyle.up = Assets.warBtnDoneDr;
            if (Assets.warBtnDoneDr instanceof TextureRegionDrawable) {
                doneStyle.down = ((TextureRegionDrawable)Assets.warBtnDoneDr).tint(Color.LIGHT_GRAY);
            } else {
                doneStyle.down = Assets.warBtnDoneDr;
            }
        }

        TextButton doneBtn = new TextButton("", doneStyle);
        Label btnLabel = new Label("Done    ", defaultStyle);
        doneBtn.add(btnLabel).expand().center();

        doneBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });

        buttonTable.add().expandX();
        buttonTable.add(doneBtn).size(140, 60).right();

        getContentTable().add(buttonTable).growX().padTop(30).bottom();
    }

    /**
     * Sol ve Sağ için ayrı ikonlar + ortada metin + YANLARDA TIRE (-----)
     */
    private void addStatRow(Table t, Texture leftIcon, Texture rightIcon, String leftText, String rightText, String centerText, Label.LabelStyle style, float delay) {
        // --- SOL GRUP ---
        Table leftGroup = new Table();
        // 1. Dash
        Label lDash = new Label("                            ", style);
        leftGroup.add(lDash).padRight(2);

        // 2. İkon
        if (leftIcon != null) leftGroup.add(new Image(leftIcon)).size(41).padRight(5);
        
        // 3. Değer (SABİT GENİŞLİK VEREREK HİZALADIK)
        Label leftLbl = new Label(leftText, style);
        leftLbl.setAlignment(Align.center);
        leftGroup.add(leftLbl).width(50); // <-- Hizalama için genişlik


        // --- ORTA METİN ---
        Label centerLbl = new Label(centerText, style);
        centerLbl.setAlignment(Align.center);


        // --- SAĞ GRUP ---
        Table rightGroup = new Table();
        
        // 1. Değer (SABİT GENİŞLİK VEREREK HİZALADIK)
        Label rightLbl = new Label(rightText, style);
        rightLbl.setAlignment(Align.center);
        rightGroup.add(rightLbl).width(50).padRight(5); // <-- Hizalama için genişlik
        
        // 2. İkon
        if (rightIcon != null) rightGroup.add(new Image(rightIcon)).size(41).padRight(5);

        // 3. Dash
        Label rDash = new Label("                  ", style);
        rightGroup.add(rDash).padLeft(2);


        // --- TABLOYA EKLEME ---
        t.add(animateAppearance(leftGroup, delay)).left().padBottom(15);
        t.add(animateAppearance(centerLbl, delay)).center().expandX().padBottom(15);
        t.add(animateAppearance(rightGroup, delay)).right().padBottom(15).row();
    }

    /**
     * Gelen zar sayısına göre Assets'ten doğru resmi döndürür.
     */
    private Texture getDiceTexture(int roll) {
        if (roll == 1) return Assets.dice1;
        if (roll == 2) return Assets.dice2;
        if (roll == 3) return Assets.dice3;
        if (roll == 4) return Assets.dice4;
        if (roll == 5) return Assets.dice5;
        if (roll == 6) return Assets.dice6;
        
        return Assets.dice1;
    }

    private Actor animateAppearance(Actor actor, float delay) {
        actor.getColor().a = 0f;
        actor.addAction(Actions.sequence(
            Actions.delay(delay),
            Actions.fadeIn(0.5f)
        ));
        return actor;
    }
}