package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent; // EKLENDİ
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener; // EKLENDİ
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gameonjava.utlcs.backend.Game;
import com.gameonjava.utlcs.backend.Player;
// PlayerInfoWidget import edilmeli (aynı paketteyse gerekmez)

public class GameHUD implements Disposable {

    public Stage stage;
    private Viewport viewport;
    public Game backendGame;

    private Label goldLabel, foodLabel, bookLabel, techLabel, moveLabel;
    private Label turnCount, currentPlayerLabel;
    private TextButton endTurnBtn;

    // --- EKLENDİ: Açık olan pencereyi takip etmek için ---
    private PlayerInfoWidget currentInfoWidget;

    // Tasarımdaki Kahverengi Renkler
    private final Color TOP_BAR_COLOR = new Color(0.8f, 0.6f, 0.3f, 1f);
    private final Color PANEL_COLOR = new Color(0.6f, 0.4f, 0.2f, 1f);

    public GameHUD(SpriteBatch batch) {
        viewport = new FitViewport(1280, 720);
        stage = new Stage(viewport, batch);
        TextButton.TextButtonStyle woodenStyle = createWoodenStyle();

        // Backend başlatma
        backendGame = new Game();
        backendGame.startGame(1);

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        // --- EKLENDİ: Ekranda boş bir yere tıklayınca pencereyi kapat ---
        stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Eğer tıklanan yer rootTable veya stage ise (yani buton değilse)
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
        topTable.setBackground(getColoredDrawable(TOP_BAR_COLOR));

        goldLabel = createStatLabel("134");
        foodLabel = createStatLabel("72");
        bookLabel = createStatLabel("89");
        techLabel = createStatLabel("12");
        moveLabel = createStatLabel("9");

        topTable.add(new Image(Assets.settings)).size(40).padLeft(10).padRight(20);

        addResourceToTable(topTable, Assets.gold, goldLabel);
        addResourceToTable(topTable, Assets.food, foodLabel);
        addResourceToTable(topTable, Assets.book, bookLabel);
        addResourceToTable(topTable, Assets.tech, techLabel);
        addResourceToTable(topTable, Assets.dash, moveLabel);

        topTable.add().expandX();
        TextButton filterBtn = new TextButton("Filters", woodenStyle);
        turnCount = new Label("Turn 1", Assets.skin);
        topTable.add(filterBtn).padRight(10);
        topTable.add(turnCount).padRight(20);

        // --- B. LEFT PANEL (P1, P2, P3, P4) ---
        // BURASI DÜZELTİLDİ: Tek tek eklemek yerine döngü ile ekleyip listener
        // tanımladık.
        Table leftTable = new Table();
        leftTable.top();

        leftTable.add(new TextButton("P1", woodenStyle)).size(70).padBottom(10).row();
        leftTable.add(new TextButton("P2", woodenStyle)).size(70).padBottom(10).row();
        leftTable.add(new TextButton("P3", woodenStyle)).size(70).padBottom(10).row();
        leftTable.add(new TextButton("P4", woodenStyle)).size(70).padBottom(10).row();
        for (int i = 0; i < 4; i++) {
            final int pIndex = i;
            TextButton pBtn = new TextButton("P" + (i + 1), woodenStyle);

            pBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Player me = backendGame.getCurrentPlayer();

                    // Test için hedef oyuncu
                    Player target = new Player("Player " + (pIndex + 1),
                            new com.gameonjava.utlcs.backend.civilization.Blue());

                    if (target.getName().equals(me.getName())) {
                        // return; // İstersen açabilirsin
                    }

                    if (currentInfoWidget != null)
                        currentInfoWidget.remove();

                    currentInfoWidget = new PlayerInfoWidget(target, me, backendGame);

                    // --- DEĞİŞİKLİK BURADA ---
                    // xPos = 90 -> Sol paneldeki butonların (genişliği 70-80px) üzerine binmemesi
                    // için hemen sağı.
                    // yPos = 0 -> Ekranın tam dibi (piksel olarak en alt).

                    float xPos = 90;
                    float yPos = 0; // "Dip" istiyorsan 0 yap. Biraz boşluk kalsın dersen 20 yapabilirsin.

                    currentInfoWidget.setPosition(xPos, yPos);

                    stage.addActor(currentInfoWidget);
                }
            });

            leftTable.add(pBtn).size(50).padBottom(10).row();
        }

        // --- C. RIGHT PANEL (BİLGİ EKRANI) ---
        Table rightTable = new Table();
        rightTable.setBackground(getColoredDrawable(TOP_BAR_COLOR));

        currentPlayerLabel = new Label("Player 2\nZeki's Turn", Assets.skin);
        currentPlayerLabel.setAlignment(Align.center);

        Label winCondTitle = new Label("Win Condition:", Assets.skin);
        Label winCondDesc = new Label("Have 200 Tech and 10 Libraries", Assets.skin);
        winCondDesc.setAlignment(Align.center);
        winCondDesc.setWrap(true);

        rightTable.add(currentPlayerLabel).pad(20).row();

        Table winTable = new Table();
        winTable.setBackground(getColoredDrawable(new Color(0.3f, 0.5f, 0.4f, 1f)));
        winTable.add(winCondTitle).padTop(10).row();
        winTable.add(winCondDesc).width(180).pad(10).row();

        rightTable.add(winTable).growX().pad(10).row();
        rightTable.add().expandY();

        // --- D. BOTTOM BAR (END TURN) ---
        Table bottomTable = new Table();
        endTurnBtn = new TextButton("End Turn", woodenStyle);
        endTurnBtn.getLabel().setFontScale(1.2f);
        bottomTable.add(endTurnBtn).width(200).height(60).padBottom(10).padRight(20);
        bottomTable.right();

        // Layout Yerleşimi
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

    private TextureRegionDrawable getColoredDrawable(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegionDrawable(new TextureRegion(texture));
    }

    private TextButton.TextButtonStyle createWoodenStyle() {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = Assets.skin.getFont("default");
        style.up = Assets.skin.newDrawable("white", new Color(0.9f, 0.7f, 0.3f, 1f));
        style.down = Assets.skin.newDrawable("white", new Color(0.7f, 0.5f, 0.1f, 1f));
        style.fontColor = Color.BLACK;
        return style;
    }

    public void updateStats(Player player, int turnNumber) {
        goldLabel.setText(String.valueOf((int) player.getGold().getValue()));
        foodLabel.setText(String.valueOf((int) player.getFood().getValue()));
        bookLabel.setText(String.valueOf((int) player.getBook().getValue()));
        techLabel.setText(String.valueOf(player.getTechnologyPoint()));
        moveLabel.setText(String.valueOf((int) player.getMp().getValue()));

        turnCount.setText("Turn " + turnNumber);
        currentPlayerLabel.setText(player.getName() + "\nTURN");
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