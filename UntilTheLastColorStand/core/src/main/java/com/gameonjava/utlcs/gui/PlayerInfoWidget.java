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

    private Label lblGold, lblFood, lblBook, lblTech;

    public PlayerInfoWidget(Player targetPlayer, Player myself, Game gameBackend) {
        this.targetPlayer = targetPlayer;
        this.myself = myself;
        this.gameBackend = gameBackend;

        Image bg = new Image(Assets.infoBgBrown);
        this.setSize(bg.getWidth(), bg.getHeight());
        this.addActor(bg);

        Label.LabelStyle titleStyle = new Label.LabelStyle(Assets.skin.getFont("default"), Color.BLACK);
        Label titleLbl = new Label(targetPlayer.getName() + "'s Resources", titleStyle);
        titleLbl.setPosition((getWidth() - titleLbl.getPrefWidth()) / 2, getHeight() - 50);
        this.addActor(titleLbl);

        Label closeBtn = new Label("X", titleStyle);
        closeBtn.setPosition(getWidth() - 40, getHeight() - 45);
        closeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
            }
        });
        this.addActor(closeBtn);

        Image infoBg = new Image(Assets.infoBgYellow);
        float infoX = (getWidth() - infoBg.getWidth()) / 2;
        float infoY = getHeight() - 80 - infoBg.getHeight();
        infoBg.setPosition(infoX, infoY);
        this.addActor(infoBg);

        Table resTable = new Table();
        resTable.setPosition(infoX, infoY);
        resTable.setSize(infoBg.getWidth(), infoBg.getHeight());
        resTable.center();


        resTable.add(new Image(Assets.gold)).size(60).pad(10);
        resTable.add(new Image(Assets.food)).size(60).pad(10);
        resTable.add(new Image(Assets.book)).size(60).pad(10);
        resTable.add(new Image(Assets.tech)).size(60).pad(10).row();

        Label.LabelStyle valStyle = new Label.LabelStyle(Assets.skin.getFont("default"), Color.BLACK);

        lblGold = createBigLabel("0", valStyle);
        lblFood = createBigLabel("0", valStyle);
        lblBook = createBigLabel("0", valStyle);
        lblTech = createBigLabel("0", valStyle);

        resTable.add(lblGold);
        resTable.add(lblFood);
        resTable.add(lblBook);
        resTable.add(lblTech);

        this.addActor(resTable);

        TextButton.TextButtonStyle tradeStyle = new TextButton.TextButtonStyle();
        tradeStyle.up = new TextureRegionDrawable(new TextureRegion(Assets.infoBtnTrade));
        tradeStyle.down = tradeStyle.up;
        tradeStyle.font = Assets.skin.getFont("default");
        tradeStyle.fontColor = Color.BLACK;

        TextButton tradeBtn = new TextButton("Trade", tradeStyle);
        tradeBtn.setPosition((getWidth() - tradeBtn.getWidth()) / 2, 40);

        tradeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                TradeDialog dialog = new TradeDialog(myself, targetPlayer, gameBackend);
                dialog.setPosition(
                    (getStage().getWidth() - dialog.getWidth()) / 2,
                    (getStage().getHeight() - dialog.getHeight()) / 2);
                getStage().addActor(dialog);
                PlayerInfoWidget.this.remove();
            }
        });

        this.addActor(tradeBtn);
    }
    public void act(float delta) {
        super.act(delta);

        if (targetPlayer != null) {
            lblGold.setText(String.valueOf((int) targetPlayer.getGold().getValue()));
            lblFood.setText(String.valueOf((int) targetPlayer.getFood().getValue()));
            lblBook.setText(String.valueOf((int) targetPlayer.getBook().getValue()));
            lblTech.setText(String.valueOf(targetPlayer.getTechnologyPoint()));
        }
    }

    private Label createBigLabel(String text, Label.LabelStyle style) {
        Label l = new Label(text, style);
        l.setAlignment(com.badlogic.gdx.utils.Align.center);
        return l;
    }
}
