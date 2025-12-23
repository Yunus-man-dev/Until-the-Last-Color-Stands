package com.gameonjava.utlcs.gui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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

    private Table filterMenuTable;
    private ButtonGroup<CheckBox> filterGroup;
    public static TextButton.TextButtonStyle beigeStyle;

    private PlayerInfoWidget currentInfoWidget;

    public GameHUD(SpriteBatch batch, Game backendGame) {
        viewport = new FitViewport(1280, 720);
        stage = new Stage(viewport, batch);

        this.backendGame = backendGame;
        beigeStyle = createBeigeStyle();

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.setTouchable(Touchable.childrenOnly);
        stage.addActor(rootTable);

        // stage.addListener(new ClickListener() {
        //     @Override
        //     public void clicked(InputEvent event, float x, float y) {
        //         if (event.getTarget() == stage.getRoot() || event.getTarget() == rootTable) {
        //             if (currentInfoWidget != null) {
        //                 currentInfoWidget.remove();
        //                 currentInfoWidget = null;
        //             }
        //         }
        //     }
        // });

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

        topTable.add(settingsBtn).size(50, 50).padLeft(10).padRight(10);

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
            String civColor = backendGame.getPlayers().get(i).getCivilization().getCivilizationColor();
            TextButton pBtn = new TextButton("P" + (i + 1), createPlayerStyle(civColor) );

            pBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Player me = backendGame.getCurrentPlayer();
                    Player target = backendGame.getPlayers().get(pIndex);
                    if (target.getName().equals(me.getName())) {
                         return;
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

        if (Assets.mail != null) {
            ImageButton.ImageButtonStyle mailStyle = new ImageButton.ImageButtonStyle();
            mailStyle.up = new TextureRegionDrawable(new TextureRegion(Assets.mail));
            mailStyle.down = new TextureRegionDrawable(new TextureRegion(Assets.mail)).tint(Color.LIGHT_GRAY);

            mailBtn = new ImageButton(mailStyle);
            mailBtn.setVisible(false);

            mailBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Player current = backendGame.getCurrentPlayer();

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

        currentPlayerLabel = new Label(backendGame.getPlayers().get(0).getName(), Assets.skin);
        currentPlayerLabel.setAlignment(Align.center);

        Label winCondTitle = new Label("Win Condition:", Assets.skin);
        winCondTitle.setAlignment(Align.center);
        winCondDesc = new Label(backendGame.getPlayers().get(0).getCivilization().winCondText, Assets.skin);
        winCondDesc.setWrap(true);

        rightTable.add(currentPlayerLabel).pad(20).row();

        winTable = new Table();
        TextureRegionDrawable firstplayersColor = civNameToColor(backendGame.getPlayers().get(0).getCivilization().getCivilizationColor());
        winTable.setBackground(firstplayersColor);
        winTable.add(winCondTitle).padTop(10).row();
        winTable.add(winCondDesc).width(180).pad(10).row();

        rightTable.add(winTable).growX().pad(10).row();
        rightTable.add().expandY();

        // --- D. BOTTOM BAR (END TURN) ---
        Table bottomTable = new Table();
        endTurnBtn = new TextButton("End Turn", beigeStyle);
        bottomTable.add(endTurnBtn).width(200).height(60).padBottom(10).padRight(20);
        bottomTable.right();

        interactionBar = new InteractionBar(Assets.skin, backendGame, this,backendGame.getMap());
        interactionBar.setSize(viewport.getWorldWidth(), 120);
        interactionBar.setPosition(0, 0);
        interactionBar.setVisible(false);

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
            else {
                mailBtn.setVisible(false);
                mailBtn.clearActions();
                mailBtn.clearActions();
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
        style.down = Assets.skin.newDrawable(Assets.filterbarbg, Color.LIGHT_GRAY);
        style.fontColor = Color.BLACK;
        return style;
    }
    private TextButton.TextButtonStyle createPlayerStyle(String civcolor) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = Assets.skin.getFont("default");
        if (civcolor.equals("Red")) {
            style.up = Assets.pred;
            style.down = Assets.skin.newDrawable(Assets.pred, Color.LIGHT_GRAY);
        }
        if (civcolor.equals("Dark Red")) {
            style.up = Assets.pdarkred;
            style.down = Assets.skin.newDrawable(Assets.pdarkred, Color.LIGHT_GRAY);
        }if (civcolor.equals("Orange")) {
            style.up = Assets.porange;
            style.down = Assets.skin.newDrawable(Assets.porange, Color.LIGHT_GRAY);
        }if (civcolor.equals("Blue")) {
            style.up = Assets.pblue;
            style.down = Assets.skin.newDrawable(Assets.pblue, Color.LIGHT_GRAY);
        }if (civcolor.equals("Cyan")) {
            style.up = Assets.pcyan;
            style.down = Assets.skin.newDrawable(Assets.pcyan, Color.LIGHT_GRAY);
        }if (civcolor.equals("Gray")) {
            style.up = Assets.pgray;
            style.down = Assets.skin.newDrawable(Assets.pgray, Color.LIGHT_GRAY);
        }if (civcolor.equals("Brown")) {
            style.up = Assets.pbrown;
            style.down = Assets.skin.newDrawable(Assets.pbrown, Color.LIGHT_GRAY);
        }if (civcolor.equals("Gold")) {
            style.up = Assets.pgold;
            style.down = Assets.skin.newDrawable(Assets.pgold, Color.LIGHT_GRAY);
        }
        style.fontColor = Color.BLACK;
        return style;
    }

    // GameHUD.java içine ekle:

    // Parametre olarak 'Main game' alıyoruz ki ekran değiştirebilelim
    public void showGameOver(Player winner, com.gameonjava.utlcs.Main game) {
        GameOverDialog dialog = new GameOverDialog("Game Over", Assets.skin, game, winner);

        // Diyaloğu sahneye ekle
        dialog.show(stage);

        // Ekranın tam ortasına yerleştir
        dialog.setPosition(
            Math.round((stage.getWidth() - dialog.getWidth()) / 2),
            Math.round((stage.getHeight() - dialog.getHeight()) / 2)
        );
    }

    public void createFilterMenu(final com.gameonjava.utlcs.gui.GameScreen screen) {
        filterMenuTable = new Table();

        filterMenuTable.setBackground(Assets.skin.newDrawable("white", Color.BLACK));

        filterMenuTable.setVisible(false);

        Table innerTable = new Table();
        innerTable.setBackground(Assets.skin.newDrawable("white", new Color(0.9f, 0.7f, 0.3f, 1f)));
        innerTable.top().pad(5);

        final CheckBox checkBuildings = new CheckBox(" Buildings Only", Assets.skin);
        final CheckBox checkSoldiers = new CheckBox(" Soldiers Only", Assets.skin);
        final CheckBox checkNone = new CheckBox(" Show None", Assets.skin);

        checkBuildings.getLabel().setColor(Color.BLACK);
        checkSoldiers.getLabel().setColor(Color.BLACK);
        checkNone.getLabel().setColor(Color.BLACK);

        filterGroup = new ButtonGroup<>();
        filterGroup.add(checkBuildings);
        filterGroup.add(checkSoldiers);
        filterGroup.add(checkNone);
        filterGroup.setMaxCheckCount(1);
        filterGroup.setMinCheckCount(1);
        filterGroup.setUncheckLast(true);

        if (screen.isShowBuildings()) checkBuildings.setChecked(true);
        else if (screen.isShowSoldiers()) checkSoldiers.setChecked(true);
        else checkNone.setChecked(true);

        ChangeListener listener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.setShowBuildings(false);
                screen.setShowSoldiers(false);

                if (checkBuildings.isChecked()) screen.setShowBuildings(true);
                else if (checkSoldiers.isChecked()) screen.setShowSoldiers(true);
            }
        };

        checkBuildings.addListener(listener);
        checkSoldiers.addListener(listener);
        checkNone.addListener(listener);

        innerTable.add(checkBuildings).left().padBottom(5).expandX().fillX().row();
        innerTable.add(checkSoldiers).left().padBottom(5).expandX().fillX().row();
        innerTable.add(checkNone).left().expandX().fillX().row();

        filterMenuTable.add(innerTable).grow().pad(2);

        stage.addActor(filterMenuTable);
    }
    public void toggleFilterMenu() {
        if (filterMenuTable != null) {
            boolean isVisible = filterMenuTable.isVisible();

            if (!isVisible) {

                Vector2 coords = new Vector2(0, 0);

                if(getFilterBtn() != null) {
                    getFilterBtn().localToStageCoordinates(coords);
                }

                float width = Math.max(getFilterBtn().getWidth(), 150);

                filterMenuTable.setWidth(width);
                filterMenuTable.pack();

                filterMenuTable.setPosition(coords.x, coords.y - filterMenuTable.getHeight());

                filterMenuTable.toFront();
                filterMenuTable.setVisible(true);
            } else {
                filterMenuTable.setVisible(false);
            }
        }
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

    public TextButton getFilterBtn() {
        return filterBtn;
    }
    public TextureRegionDrawable civNameToColor(String civName) {
        if (civName.equals("Gold")) {
            return Assets.bgGold;
        }
        if (civName.equals("Orange")) {
            return Assets.bgOrange;
        }
        if (civName.equals("Brown")) {
            return Assets.bgBrown;
        }
        if (civName.equals("Gray")) {
            return Assets.bgGray;
        }
        if (civName.equals("Blue")) {
            return Assets.bgBlue;
        }
        if (civName.equals("Cyan")) {
            return Assets.bgCyan;
        }
        if (civName.equals("Red")) {
            return Assets.bgRed;
        }
        if (civName.equals("Dark Red")) {
            return Assets.bgDarkred;
        }
        return null;
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
