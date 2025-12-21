package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gameonjava.utlcs.Main;
// import com.gameonjava.utlcs.backend.Assets;
// BACKEND IMPORTLARI
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.civilization.*;

import java.util.ArrayList;
import java.util.HashMap;

public class EmpireSelectionScreen extends ScreenAdapter {

    private final Main game;
    private Stage stage;
    private Skin skin;

    // --- State Yönetimi ---
    private int currentPlayerIndex = 1;
    private final int MAX_PLAYERS = 4;

    // ARTIK DOĞRUDAN PLAYER LISTESİ TUTUYORUZ
    private ArrayList<Player> readyPlayers = new ArrayList<>();

    private final HashMap<String, TextButton> empireButtons = new HashMap<>();
    private String selectedEmpireName = null;

    // --- UI Değişkenleri ---
    private Texture menuBgTexture, empireListBgTexture, colorBgTexture, uniqueFeatureBgTexture, winConditionBgTexture, btnTexture;
    private Image colorPreviewImage;
    private Label winConditionLabel, featureLabel, playerTitleLabel;
    private TextField nameField;
    private TextButton actionButton;

    public EmpireSelectionScreen(Main game) {
        this.game = game;
    }

    public ArrayList<Player> getReadyPlayers() {
        return readyPlayers;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = Assets.skin;
        loadTextures();

        // ... (ARKA PLAN VE TABLO KURULUMLARI AYNI - ÖNCEKİ KODLA AYNI) ...
        // ... (Kod tekrarı olmaması için UI kurulum kısımlarını özet geçiyorum) ...

        // 1. Arka Planı Ekle
        Image bg = new Image(menuBgTexture);
        bg.setFillParent(true);
        stage.addActor(bg);

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.pad(20);
        stage.addActor(rootTable);

        // --- SOL, ORTA, SAĞ SÜTUNLARI OLUŞTUR (Önceki kodun aynısı) ---
        // Sadece handleNextButton mantığı değişecek, arayüz aynı.

        // ... (Burada createLeftColumn, createCenterColumn, createRightColumn işlemleri var varsayıyoruz) ...
        // ... Hızlıca kurulumu yapalım:

        setupUI(rootTable); // UI kodlarını aşağıda metod içine aldım temiz olsun diye

        // İlk açılış ayarı
        updatePreview("Blue");
    }

    // --- UI KURULUMU (Önceki kodun aynısı, sadece düzenli dursun diye metoda aldım) ---
    private void setupUI(Table rootTable) {
         // --- SOL SÜTUN ---
        Table leftColumn = new Table();
        Table colorPanel = new Table();
        colorPanel.setBackground(new NinePatchDrawable(new NinePatch(colorBgTexture, 10, 10, 10, 10)));
        colorPanel.add(new Label("Color", skin, "default")).padBottom(5).row();
        colorPreviewImage = new Image(skin.newDrawable("white", Color.WHITE));
        colorPanel.add(colorPreviewImage).size(50, 50);

        Table winPanel = new Table();
        winPanel.setBackground(new NinePatchDrawable(new NinePatch(winConditionBgTexture, 12, 12, 12, 12)));
        winPanel.add(new Label("Victory", skin, "default")).padBottom(10).row();
        winConditionLabel = new Label("", skin);
        winConditionLabel.setWrap(true);
        winConditionLabel.setAlignment(Align.center);
        winPanel.add(winConditionLabel).width(200).row();

        TextButton backBtn = createNavButton("Back");
        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        leftColumn.add(colorPanel).width(250).height(120).padBottom(20).row();
        leftColumn.add(winPanel).width(250).growY().padBottom(20).row();
        leftColumn.add(backBtn).width(180).height(50).left();

        // --- ORTA SÜTUN ---
        Table centerColumn = new Table();
        playerTitleLabel = new Label("Player 1 Selection", skin, "default");
        playerTitleLabel.setColor(Color.GOLD);

        nameField = new TextField("", skin);
        nameField.setMessageText("Enter Name...");
        nameField.setAlignment(Align.center);

        Table listPanel = new Table();
        listPanel.setBackground(new NinePatchDrawable(new NinePatch(empireListBgTexture, 14, 14, 14, 14)));
        TextButton.TextButtonStyle listBtnStyle = new TextButton.TextButtonStyle();
        listBtnStyle.font = skin.getFont("default");
        listBtnStyle.fontColor = Color.WHITE;
        listBtnStyle.overFontColor = Color.GOLD;
        listBtnStyle.downFontColor = Color.GRAY;
        listBtnStyle.disabledFontColor = new Color(0.4f, 0.4f, 0.4f, 0.5f);

        String[] empires = {"Blue", "Cyan", "Red", "Dark Red", "Gold", "Orange", "Brown", "Black"};
        for (String emp : empires) {
            TextButton btn = new TextButton(emp, listBtnStyle);
            btn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (!btn.isDisabled()) updatePreview(emp);
                }
            });
            listPanel.add(btn).padBottom(8).row();
            empireButtons.put(emp, btn);
        }
        centerColumn.add(playerTitleLabel).padBottom(15).row();
        centerColumn.add(new Label("Empire Name", skin, "default")).padBottom(5).row();
        centerColumn.add(nameField).width(250).height(40).padBottom(20).row();
        centerColumn.add(listPanel).width(300).growY();

        // --- SAĞ SÜTUN ---
        Table rightColumn = new Table();
        Table featurePanel = new Table();
        featurePanel.setBackground(new NinePatchDrawable(new NinePatch(uniqueFeatureBgTexture, 12, 12, 12, 12)));
        featurePanel.add(new Label("Unique Features", skin, "default")).padBottom(10).row();
        featureLabel = new Label("", skin);
        featureLabel.setWrap(true);
        featureLabel.setAlignment(Align.center);
        featurePanel.add(featureLabel).width(200).row();

        actionButton = createNavButton("Next Player");
        actionButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handleNextButton();
            }
        });
        rightColumn.add(featurePanel).width(250).growY().padBottom(20).row();
        rightColumn.add(actionButton).width(180).height(50).right();

        rootTable.add(leftColumn).width(260).expandY().fillY().padRight(20);
        rootTable.add(centerColumn).width(320).expandY().fillY().padRight(20);
        rootTable.add(rightColumn).width(260).expandY().fillY();
    }

    // --- KRİTİK DEĞİŞİKLİK BURADA ---
    private void handleNextButton() {
        if (selectedEmpireName == null) return;

        String playerName = nameField.getText().trim();
        if (playerName.isEmpty()) playerName = "Player " + currentPlayerIndex;

        // 1. Factory Metodu ile Civilization Objesini Oluştur
        Civilization selectedCiv = getCivilizationByName(selectedEmpireName);

        // 2. DOĞRUDAN BACKEND PLAYER OBJESİNİ OLUŞTUR
        // Player constructor'ı çalıştığında, Civilization'a göre kaynaklar (Gold, Food vs.)
        // otomatik olarak Player'ın içinde oluşturulacak. Harika!
        Player newPlayer = new Player(playerName, selectedCiv);

        // 3. Listeye Ekle
        readyPlayers.add(newPlayer);

        System.out.println("Oyuncu Oluşturuldu: " + newPlayer.getName() + " - " + selectedCiv.getCivilizationName());

        // Butonu Kilitle
        TextButton usedBtn = empireButtons.get(selectedEmpireName);
        if (usedBtn != null) usedBtn.setDisabled(true);

        if (currentPlayerIndex < MAX_PLAYERS) {
            currentPlayerIndex++;
            prepareForNextPlayer();
        } else {
            // HEPSİ BİTTİ -> Harita Seçimine GERÇEK OYUNCU LISTESINI gönder
            game.setScreen(new MapSelectionScreen(game, readyPlayers));
        }
    }

    private void prepareForNextPlayer() {
        nameField.setText("");
        playerTitleLabel.setText("Player " + currentPlayerIndex + " Selection");
        if (currentPlayerIndex == MAX_PLAYERS) actionButton.setText("START GAME");

        selectedEmpireName = null;
        for (String key : empireButtons.keySet()) {
            if (!empireButtons.get(key).isDisabled()) {
                updatePreview(key);
                break;
            }
        }
    }

    private void updatePreview(String empireName) {
        this.selectedEmpireName = empireName;
        // ... (Renk ve yazı güncellemeleri önceki kodla aynı) ...
        if (empireName.contains("Blue") || empireName.contains("Cyan")) {
            colorPreviewImage.setColor(Color.CYAN);
            winConditionLabel.setText("SCIENTIFIC:\nCollect Books.");
            featureLabel.setText("Tech Bonus");
        } else if (empireName.contains("Red") || empireName.contains("Dark Red")) {
            colorPreviewImage.setColor(Color.RED);
            winConditionLabel.setText("DOMINATION:\nConquer Map.");
            featureLabel.setText("Attack Damage");
        } else if (empireName.contains("Gold") || empireName.contains("Orange")) {
            colorPreviewImage.setColor(Color.GOLD);
            winConditionLabel.setText("ECONOMIC:\nAmass Gold.");
            featureLabel.setText("Double Gold");
        } else {
            colorPreviewImage.setColor(Color.BROWN);
            winConditionLabel.setText("SURVIVAL:\nSurvive.");
            featureLabel.setText("Defense Bonus");
        }
    }

    // İSİMDEN NESNEYE ÇEVİRİCİ
    public static Civilization getCivilizationByName(String name) {
        if (name == null) return new Blue();

        if (name.equals("Blue")) return new Blue();
        if (name.equals("Cyan")) return new Cyan();
        if (name.equals("Brown")) return new Brown();
        if (name.equals("Black")) return new Black();

        // Dosyaları gelince bunları aç:
        if (name.equals("Red")) return new Red();
        if (name.equals("Dark Red")) return new DarkRed();
        if (name.equals("Gold")) return new GoldCivilization();
        if (name.equals("Orange")) return new Orange();

        return new Blue();
    }

    // Texture yükleme ve dispose kodları...
    private TextButton createNavButton(String text) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = skin.getFont("default");
        style.fontColor = Color.BLACK;
        NinePatch patch = new NinePatch(btnTexture, 10, 10, 10, 10);
        style.up = new NinePatchDrawable(patch);
        style.down = new NinePatchDrawable(patch).tint(Color.GRAY);
        return new TextButton(text, style);
    }
    private void loadTextures() {
        try {
            menuBgTexture = new Texture(Gdx.files.internal("ui/EmpireSelection_bg.png"));
            empireListBgTexture = new Texture(Gdx.files.internal("ui/EmpireList_bg.png"));
            colorBgTexture = new Texture(Gdx.files.internal("ui/Color_bg.png"));
            uniqueFeatureBgTexture = new Texture(Gdx.files.internal("ui/UniqueFeature_bg.png"));
            winConditionBgTexture = new Texture(Gdx.files.internal("ui/WinCondition_bg.png"));
            btnTexture = new Texture(Gdx.files.internal("ui/EmpireSelection_btn.png"));
        } catch (Exception e) {}
    }
    @Override public void render(float d) {
        Gdx.gl.glClearColor(0,0,0,1); Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); stage.act(d); stage.draw();
    }
    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override public void dispose() { stage.dispose(); if(menuBgTexture!=null) menuBgTexture.dispose(); }
}
