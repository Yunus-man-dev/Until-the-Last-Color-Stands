package com.gameonjava.utlcs.gui;

import java.util.ArrayList;
import java.util.HashMap;

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
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.civilization.Blue;
import com.gameonjava.utlcs.backend.civilization.Brown;
import com.gameonjava.utlcs.backend.civilization.Civilization;
import com.gameonjava.utlcs.backend.civilization.Cyan;
import com.gameonjava.utlcs.backend.civilization.DarkRed;
import com.gameonjava.utlcs.backend.civilization.GoldCivilization;
import com.gameonjava.utlcs.backend.civilization.Gray;
import com.gameonjava.utlcs.backend.civilization.Orange;
import com.gameonjava.utlcs.backend.civilization.Red;

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
        // [Resim Dosyalarıyla Uyumlu Köşe Değeri]
        // PNG'nizin köşeleri ne kadar yumuşaksa bu sayıyı ona göre ayarlayın (6 veya 8 iyidir)
        int corner = 6; 
        int borderSize = 4; // Çerçeve kalınlığı

        // --- SOL SÜTUN ---
        Table leftColumn = new Table();

        // 1. COLOR PANEL (Özel Durum: İçinde resim var)
        // Yardımcı metodu kullanarak çerçeveli yapıyı kuruyoruz
        Table colorContainer = createBorderedTable(colorBgTexture, 4, borderSize);
        // Container'ın içindeki asıl tabloya erişmek için:
        Table colorInner = (Table) colorContainer.getChildren().get(0);
        
        colorPreviewImage = new Image(skin.newDrawable("white", Color.WHITE));
        colorPreviewImage.setScaling(com.badlogic.gdx.utils.Scaling.stretch);
        colorInner.add(colorPreviewImage).grow(); // İç tabloyu doldur

        // 2. WIN CONDITION PANEL
        Table winContainer = createBorderedTable(winConditionBgTexture, corner, borderSize);
        Table winInner = (Table) winContainer.getChildren().get(0);

        winInner.add(new Label("Win Condition", skin, "default")).padBottom(10).row();
        winConditionLabel = new Label("", skin);
        winConditionLabel.setColor(Color.BLACK);
        winConditionLabel.setWrap(true);
        winConditionLabel.setAlignment(Align.center);
        winInner.add(winConditionLabel).width(200).row();

        // Back Butonu
        TextButton backBtn = createNavButton("Back");
        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        // Sol Sütuna Ekleme
        leftColumn.add(colorContainer).width(250).height(500).padBottom(20).row();
        leftColumn.add(winContainer).width(250).growY().padBottom(20).row();
        leftColumn.add(backBtn).width(180).height(50).left();


        // --- ORTA SÜTUN ---
        Table centerColumn = new Table();
        playerTitleLabel = new Label("Player 1 Selection", skin, "default");
        playerTitleLabel.setColor(Color.BLACK);
        playerTitleLabel.setFontScale(0.22f); // Yazı boyutu düzeltildi

        nameField = new TextField("", skin);
        nameField.setMessageText("Enter Name...");
        nameField.setAlignment(Align.center);

        // 3. LIST PANEL (Empire Listesi)
        Table listContainer = createBorderedTable(empireListBgTexture, corner, borderSize);
        Table listInner = (Table) listContainer.getChildren().get(0);

        TextButton.TextButtonStyle listBtnStyle = new TextButton.TextButtonStyle();
        listBtnStyle.font = skin.getFont("default");
        listBtnStyle.fontColor = Color.BLACK;
        listBtnStyle.overFontColor = Color.GOLD;
        listBtnStyle.downFontColor = Color.GRAY;
        listBtnStyle.disabledFontColor = new Color(0.4f, 0.4f, 0.4f, 0.5f);

        String[] empires = {"Blue", "Cyan", "Red", "Dark Red", "Gold", "Orange", "Brown", "Gray"};
        for (String emp : empires) {
            TextButton btn = new TextButton(emp, listBtnStyle);
            btn.getLabel().setFontScale(0.3f); // Yazı boyutu ayarı
            btn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (!btn.isDisabled()) updatePreview(emp);
                }
            });
            listInner.add(btn).width(250).height(80).padBottom(5).row(); // Yükseklik ve boşluk ayarı
            empireButtons.put(emp, btn);
        }

        centerColumn.add(playerTitleLabel).padBottom(15).row();
        centerColumn.add(new Label("Empire name", skin, "default")).padBottom(5).row();
        centerColumn.add(nameField).width(250).height(40).padBottom(20).row();
        centerColumn.add(listContainer).width(300).growY(); // Container'ı ekliyoruz


        // --- SAĞ SÜTUN ---
        Table rightColumn = new Table();
        
        // 4. FEATURE PANEL
        Table featureContainer = createBorderedTable(uniqueFeatureBgTexture, corner, borderSize);
        Table featureInner = (Table) featureContainer.getChildren().get(0);

        featureInner.add(new Label("Unique Features:", skin, "default")).padBottom(10).row();
        featureLabel = new Label("", skin);
        featureLabel.setColor(Color.BLACK);
        featureLabel.setWrap(true);
        featureLabel.setAlignment(Align.center);
        featureInner.add(featureLabel).width(200).row();

        actionButton = createNavButton("Next Player");
        actionButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handleNextButton();
            }
        });

        rightColumn.add(featureContainer).width(250).growY().padBottom(20).row();
        rightColumn.add(actionButton).width(180).height(50).right();

        // --- ROOT'A EKLEME ---
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
        if (currentPlayerIndex == MAX_PLAYERS) actionButton.setText("Start Game");

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
            Civilization selectedCiv = getCivilizationByName(empireName);

            winConditionLabel.setText(selectedCiv.getWinCondText());
            featureLabel.setText(selectedCiv.getFeaturesText());
            if( empireName.contains("Blue")) {
                colorPreviewImage.setColor(Assets.COL_BLUE);
            }
            else if( empireName.equals("Cyan")) {
                colorPreviewImage.setColor(Assets.COL_CYAN);
            }
            else if( empireName.equals("Red")) {
                colorPreviewImage.setColor(Assets.COL_RED);
            }
            else if( empireName.equals("Dark Red")) {
                colorPreviewImage.setColor(Assets.COL_DARK_RED);
            }
            else if( empireName.equals("Gold")) {
                colorPreviewImage.setColor(Assets.COL_GOLD);
            }
            else if( empireName.equals("Orange")) {
                colorPreviewImage.setColor(Assets.COL_ORANGE);
            }
            if( empireName.equals("Brown")) {
                colorPreviewImage.setColor(Assets.COL_BROWN);
            }
            else if( empireName.equals("Gray")) {
                colorPreviewImage.setColor(Assets.COL_GRAY);
            }
    }

    // İSİMDEN NESNEYE ÇEVİRİCİ
    public static Civilization getCivilizationByName(String name) {
        if (name == null) return new Blue("Blue");

        if (name.equals("Blue")) return new Blue("Blue");
        if (name.equals("Cyan")) return new Cyan("Cyan Civilization");
        if (name.equals("Brown")) return new Brown("Brown");
        if (name.equals("Gray")) return new Gray("Gray Civilization");

        // Dosyaları gelince bunları aç:
        if (name.equals("Red")) return new Red("Red");
        if (name.equals("Dark Red")) return new DarkRed("Dark Red Civilization");
        if (name.equals("Gold")) return new GoldCivilization("Gold");
        if (name.equals("Orange")) return new Orange("Orange Civilization");

        return new Blue("Blue");
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
    // --- YARDIMCI METOT: Çerçeveli ve Yuvarlak Köşeli Panel Oluşturucu ---
private Table createBorderedTable(Texture texture, int cornerRadius, int borderThickness) {
    // 1. Doku'dan 9-Patch oluştur
    NinePatch patch = new NinePatch(texture, cornerRadius, cornerRadius, cornerRadius, cornerRadius);
    
    // 2. DIŞ KATMAN (Çerçeve Rengi): Aynı dokuyu SİYAH'a boyuyoruz
    NinePatchDrawable borderDrawable = new NinePatchDrawable(patch).tint(Color.BLACK);
    
    // 3. İÇ KATMAN (Asıl Panel): Dokunun orijinal hali
    NinePatchDrawable backgroundDrawable = new NinePatchDrawable(patch);

    // 4. Tabloları oluştur
    Table container = new Table();
    container.setBackground(borderDrawable); // Dışa siyahı koy
    
    Table innerTable = new Table();
    innerTable.setBackground(backgroundDrawable); // İçe normali koy
    
    // 5. İç tabloyu dışa ekle ve kenarlardan (borderThickness kadar) boşluk bırak
    container.add(innerTable).grow().pad(borderThickness);
    
    return container; // Artık bu container'ı kullanacağız, innerTable'a da eleman ekleyeceğiz.
}
    @Override public void render(float d) {
        Gdx.gl.glClearColor(0,0,0,1); Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); stage.act(d); stage.draw();
    }
    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override public void dispose() { stage.dispose(); if(menuBgTexture!=null) menuBgTexture.dispose(); }
}
