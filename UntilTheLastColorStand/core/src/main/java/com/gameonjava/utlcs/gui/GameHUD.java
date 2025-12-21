package com.gameonjava.utlcs.gui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions; // EKLENDİ (Animasyon için gerekli)
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gameonjava.utlcs.backend.Game;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Trade;

public class GameHUD implements Disposable {

    public Stage stage;
    private Viewport viewport;
    public Game backendGame;

    private Label goldLabel, foodLabel, bookLabel, techLabel, moveLabel;
    private Label turnCount, currentPlayerLabel, winCondDesc;
    private TextButton endTurnBtn, settingsBtn, filterBtn;
    private Table winTable;
    private ImageButton mailBtn;
    private InteractionBar interactionBar;

    private PlayerInfoWidget currentInfoWidget;

    public GameHUD(SpriteBatch batch, Game backendGame) {
        viewport = new FitViewport(1280, 720);
        stage = new Stage(viewport, batch);

        this.backendGame = backendGame;
        // this.backendGame.startGame(1);
        TextButton.TextButtonStyle beigeStyle = createBeigeStyle();

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        // Pencere dışına tıklayınca widget kapatma
        stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getTarget() == stage.getRoot() || event.getTarget() == rootTable) {
                    if (currentInfoWidget != null) {
                        currentInfoWidget.remove();
                        currentInfoWidget = null;
                    }
                }
            }
        });

        // --- A. TOP BAR (KAYNAKLAR) ---
        Table topTable = new Table();
        topTable.setBackground(Assets.topbarbg);

        goldLabel = createStatLabel("134");
        foodLabel = createStatLabel("72");
        bookLabel = createStatLabel("89");
        techLabel = createStatLabel("12");
        moveLabel = createStatLabel("9");

        settingsBtn = new TextButton("", beigeStyle);
        settingsBtn.clearChildren();
        settingsBtn.add(new Image(Assets.settings)).size(32).expand().center();

        topTable.add(settingsBtn).size(60, 50).padLeft(10).padRight(10);

        Table resourcesTable = new Table();
        resourcesTable.setBackground(Assets.rbarbg);

        addResourceToTable(resourcesTable, Assets.gold, goldLabel);
        addResourceToTable(resourcesTable, Assets.food, foodLabel);
        addResourceToTable(resourcesTable, Assets.book, bookLabel);
        addResourceToTable(resourcesTable, Assets.tech, techLabel);
        addResourceToTable(resourcesTable, Assets.dash, moveLabel);

        topTable.add(resourcesTable).height(50).pad(5).expandX().fillX();

        filterBtn = new TextButton("Filters", beigeStyle);
        topTable.add(filterBtn).height(50).width(120).padLeft(10).padRight(5);

        Table turnPanel = new Table();
        turnPanel.setBackground(Assets.tfbg);

        turnCount = new Label("Turn 1", Assets.skin);
        turnCount.setAlignment(Align.center);
        turnCount.setColor(Color.BLACK);

        turnPanel.add(turnCount).expand().fill();
        topTable.add(turnPanel).height(50).width(120).padRight(10);

        // --- B. LEFT PANEL (P1, P2, P3, P4 ve Mail) ---
        Table leftTable = new Table();
        leftTable.top();

        // P1..P4 Butonları
        for (int i = 0; i < 4; i++) {
            final int pIndex = i;
            TextButton pBtn = new TextButton("P" + (i + 1), beigeStyle);

            pBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Player me = backendGame.getCurrentPlayer();
                    // Test için hedef oyuncu
                    Player target = new Player("Player " + (pIndex + 1),
                            new com.gameonjava.utlcs.backend.civilization.Blue());

                    if (target.getName().equals(me.getName())) {
                        // return;
                    }
                    if (currentInfoWidget != null)
                        currentInfoWidget.remove();

                    currentInfoWidget = new PlayerInfoWidget(target, me, backendGame);
                    float xPos = 90;
                    float yPos = 0;
                    currentInfoWidget.setPosition(xPos, yPos);
                    stage.addActor(currentInfoWidget);
                }
            });
            leftTable.add(pBtn).size(50).padBottom(10).row();
        }

        // Mail Butonu (Teklif Gelince Görünür)
        if (Assets.mail != null) {
            ImageButton.ImageButtonStyle mailStyle = new ImageButton.ImageButtonStyle();
            mailStyle.up = new TextureRegionDrawable(new TextureRegion(Assets.mail));
            mailStyle.down = new TextureRegionDrawable(new TextureRegion(Assets.mail)).tint(Color.LIGHT_GRAY);

            mailBtn = new ImageButton(mailStyle);
            mailBtn.setVisible(false); // Başlangıçta mutlaka gizli

            mailBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Player current = backendGame.getCurrentPlayer();

                    // DÜZELTME: Constructor'a 'GameHUD.this' gönderiliyor
                    IncomingTradesDialog mailDialog = new IncomingTradesDialog(Assets.skin, backendGame, current,
                            GameHUD.this);
                    mailDialog.show(stage);

                    float currentX = mailDialog.getX();
                    mailDialog.setPosition(currentX - 200, mailDialog.getY());
                }
            });

            leftTable.add(mailBtn).size(60).padTop(20).row();
        }

        // --- C. RIGHT PANEL (BİLGİ EKRANI) ---
        Table rightTable = new Table();
        rightTable.setBackground(Assets.infobg);

        currentPlayerLabel = new Label("", Assets.skin);
        currentPlayerLabel.setAlignment(Align.center);

        Label winCondTitle = new Label("Win Condition:", Assets.skin);
        winCondTitle.setAlignment(Align.center);
        winCondDesc = new Label("", Assets.skin);
        winCondDesc.setWrap(true);

        rightTable.add(currentPlayerLabel).pad(20).row();

        winTable = new Table();
        winTable.setBackground(Assets.bgRed);
        winTable.add(winCondTitle).padTop(10).row();
        winTable.add(winCondDesc).width(180).pad(10).row();

        rightTable.add(winTable).growX().pad(10).row();
        rightTable.add().expandY();

        // --- D. BOTTOM BAR (END TURN) ---
        Table bottomTable = new Table();
        endTurnBtn = new TextButton("End Turn", beigeStyle);
        bottomTable.add(endTurnBtn).width(200).height(60).padBottom(10).padRight(20);
        bottomTable.right();

        interactionBar = new InteractionBar(Assets.skin, backendGame, this);
        interactionBar.setSize(viewport.getWorldWidth(), 120); // Yüksekliği ayarla
        interactionBar.setPosition(0, 0); // En alta sabitle
        interactionBar.setVisible(false); // Başlangıçta gizli
        
        stage.addActor(interactionBar);

        // ANA YERLEŞİM
        rootTable.add(topTable).growX().height(60).colspan(3).top();
        rootTable.row();
        rootTable.add(leftTable).width(70).top().padTop(20).left();
        rootTable.add().expand();
        rootTable.add(rightTable).width(250).growY().right().padTop(20).padBottom(20).padRight(10);
        rootTable.row();
        rootTable.add(bottomTable).growX().height(80).colspan(3).bottom();
    }

    private void addResourceToTable(Table table, Texture icon, Label label) {
        table.add(new Image(icon)).size(32).padLeft(15).padRight(5);
        table.add(label);
    }

    private Label createStatLabel(String text) {
        Label l = new Label(text, Assets.skin);
        l.setColor(Color.BLACK);
        return l;
    }

    public TextButton getEndTurnBtn() {
        return endTurnBtn;
    }

    public void updateStats(Player player, int turnNumber) {
        goldLabel.setText(String.valueOf((int) player.getGold().getValue()));
        foodLabel.setText(String.valueOf((int) player.getFood().getValue()));
        bookLabel.setText(String.valueOf((int) player.getBook().getValue()));
        techLabel.setText(String.valueOf(player.getTechnologyPoint()));
        moveLabel.setText(String.valueOf((int) player.getMp().getValue()));

        turnCount.setText("Turn " + turnNumber);
        currentPlayerLabel.setText(player.getName() + "'s Turn");

        // Mail Butonu Güncelleme
        if (mailBtn != null) {
            ArrayList<Trade> pending = backendGame.getPendingTradesFor(player);

            if (pending != null && !pending.isEmpty()) {
                if (!mailBtn.isVisible()) {
                    mailBtn.setVisible(true);
                    mailBtn.addAction(Actions.forever(Actions.sequence(
                            Actions.fadeOut(0.5f),
                            Actions.fadeIn(0.5f))));
                }
            }
            // Eğer liste boşsa (Teklif yoksa) -> GİZLE
            else {
                mailBtn.setVisible(false);
                mailBtn.clearActions(); // Animasyonu temizle
                mailBtn.setColor(1, 1, 1, 1);
            }
        }
    }

    public void updateTurnInfo(String winConditionText, TextureRegionDrawable background) {
        if (winCondDesc != null) {
            winCondDesc.setText(winConditionText);
        }
        if (winTable != null) {
            winTable.setBackground(background);
        }
    }

    private TextButton.TextButtonStyle createBeigeStyle() {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = Assets.skin.getFont("default");
        style.up = Assets.tfbg;
        style.down = Assets.skin.newDrawable(Assets.tfbg, Color.LIGHT_GRAY);
        style.fontColor = Color.BLACK;
        return style;
    }

    public Game getBackendGame() {
        return backendGame;
    }

    public InteractionBar getInteractionBar() {
        return interactionBar;
    }

    public TextButton getSettingsBtn() {
        return settingsBtn;
    }

    public void render() {
        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}