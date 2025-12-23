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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gameonjava.utlcs.Main;
// import com.gameonjava.utlcs.backend.Assets;
import com.gameonjava.utlcs.backend.Game; // Backend Game sınıfı
import com.gameonjava.utlcs.backend.Player; // Backend Player sınıfı

import java.util.ArrayList;

public class MapSelectionScreen extends ScreenAdapter {

    private final Main game;
    private final ArrayList<Player> readyPlayers; // Önceki ekrandan gelen oyuncular
    private Stage stage;
    private Skin skin;

    // Görseller
    private Texture bgTexture;
    private Texture panelTexture; // panel_brown.png
    private Texture map1Preview, map2Preview, map3Preview; // map_placeholder_X.png

    public MapSelectionScreen(Main game, ArrayList<Player> players) {
        this.game = game;
        this.readyPlayers = players;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = Assets.skin;
        loadTextures();

        // 1. Arka Plan
        Image bg = new Image(bgTexture);
        bg.setFillParent(true);
        stage.addActor(bg);

        // 2. Ana Tablo
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        // rootTable.setDebug(true); // Hizalamayı görmek için çizgileri açabilirsin
        rootTable.pad(30);
        stage.addActor(rootTable);

        // Başlık
        Label title = new Label("Select World Map", skin, "default");
        title.setColor(Color.BLACK);
        title.setAlignment(Align.center);
        rootTable.add(title).padBottom(50).row();

        // 3. Harita Kartlarını Alt Alta Diz (Container)
        Table mapsContainer = new Table();

        // --- HİZALAMA AYARI (BURASI ÖNEMLİ) ---
        // Container içindeki her eleman için varsayılan ayar yapıyoruz:
        // Hepsi ortalansın (Align.center), genişlikleri aynı olsun.
        mapsContainer.defaults()
                .width(800) // Panellerin genişliği (İçerik sığsın diye biraz arttırdım)
                .height(190) // Panellerin yüksekliği
                .padBottom(20) // Aralarındaki boşluk
                .align(Align.center); // Hepsi ortalı dursun

        // --- KARTLARI OLUŞTUR ---
        Table map1 = createMapCard("Map 1", "Standard balanced map.", map1Preview, 1);
        Table map2 = createMapCard("Map 2", "Scattered islands.", map2Preview, 2);
        Table map3 = createMapCard("Map 3", "Strategic mountains.", map3Preview, 3);

        // --- KARTLARI EKLE ---
        // Artık .width ve .height dememize gerek yok, defaults'tan alacak.
        mapsContainer.add(map1).row();
        mapsContainer.add(map2).row();
        mapsContainer.add(map3).row();

        // Container'ı ana tabloya ekle
        // expandY().top() diyerek listeyi ekranın üstüne doğru itiyoruz ki ortada
        // sıkışmasın.
        rootTable.add(mapsContainer).expandY().top().padTop(20).row();

        // 4. Geri Butonu
        TextButton.TextButtonStyle backStyle = new TextButton.TextButtonStyle();
        backStyle.font = skin.getFont("default"); // Varsayılan fontu al
        backStyle.fontColor = Color.BLACK; // Yazı rengi

        // 2. Asset'ten Resmi Çek (Assets.brownGameButton veya Assets.empireSelectionBtn
        // kullanabilirsin)
        // Burada genel kahverengi butonu kullanıyoruz:
        Texture backBtnTexture = Assets.empireSelectionBtn;

        if (backBtnTexture != null) {
            // Görsel bozulmasın diye 9-Patch yapıyoruz
            NinePatch patch = new NinePatch(backBtnTexture, 10, 10, 10, 10);
            NinePatchDrawable drawable = new NinePatchDrawable(patch);

            backStyle.up = drawable; // Normal hali
            backStyle.down = drawable.tint(Color.GRAY); // Basılınca koyulaşsın
        }
        TextButton backBtn = new TextButton("Back", backStyle);
        backBtn.getLabel().setFontScale(0.18f);

        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new EmpireSelectionScreen(game));
            }
        });

        // Butonu en alta sola koy
        rootTable.add(backBtn).left().bottom().width(150).height(50);
    }

    private Table createMapCard(String title, String desc, Texture imgTex, final int mapID) {
        Table card = new Table();

        // Arka Plan
        NinePatch patch = new NinePatch(panelTexture, 12, 12, 12, 12);
        card.setBackground(new NinePatchDrawable(patch));
        // Pad değerlerini ayarladık
        card.pad(10, 10, 10, 10);

        // --- SOL TARAFI (RESİM) ---
        Image mapImg = new Image(imgTex);
        // Resmi biraz küçülttük ki karta sığsın
        card.add(mapImg).size(300, 180).padRight(15);

        // --- SAĞ TARAFI (YAZILAR VE BUTON) ---
        Table infoTable = new Table();

        Label titleLbl = new Label(title, skin, "default");
        titleLbl.setColor(Color.BLACK);
        titleLbl.setAlignment(Align.left);
        titleLbl.setFontScale(0.25f); // Başlık biraz büyük olsun

        Label descLbl = new Label(desc, skin, "default");
        descLbl.setWrap(true);
        descLbl.setAlignment(Align.left);
        descLbl.setFontScale(0.17f); // Yazı biraz küçük olsun sığması için

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();

        // 2. Font ayarlarını mevcut skin'den alın
        style.font = skin.getFont("default");
        style.fontColor = Color.BLACK; // Veya Color.WHITE

        // 3. Asset'i Kullanın (Örnek olarak Assets.brownGameButton kullanıldı)
        // Assets sınıfınızda tanımlı olan texture'ı alıyoruz
        Texture buttonTexture = Assets.brownGameButton;

        // Eğer texture null ise (henüz yüklenmediyse) Assets'ten çekmeyi deneyelim:
        if (buttonTexture == null) {
            // Assets.BROWN_GAME_BUTTON string referansınız varsa:
            buttonTexture = Assets.manager.get(Assets.BROWN_GAME_BUTTON, Texture.class);
        }

        if (buttonTexture != null) {
            // Görselin boyutlanırken bozulmaması için NinePatch kullanıyoruz (Kenar
            // payları: 10px)
            patch = new NinePatch(buttonTexture, 1, 1, 1, 1);
            NinePatchDrawable drawable = new NinePatchDrawable(patch);

            // 4. Stilin 'up' (basılmamış) ve 'down' (basılmış) durumlarını ayarlayın
            style.up = drawable;
            style.down = drawable.tint(Color.GRAY); // Basılınca biraz koyulaşsın
        }

        // 5. Butonu OLUŞTURURKEN bu yeni stili kullanın
        TextButton selectBtn = new TextButton("Start", style);

        // 6. Font ölçeğini ayarlayın
        selectBtn.getLabel().setFontScale(0.15f);
        selectBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                startGame(mapID);
            }
        });

        // Bilgileri sağ tabloya diz
        infoTable.add(titleLbl).left().row();
        infoTable.add(descLbl).width(140).left().padBottom(5).row(); // Genişlik sınırı önemli
        infoTable.add(selectBtn).width(100).height(35).left();

        // Sağ tabloyu karta ekle
        card.add(infoTable).expand().fill();

        return card;
    }

    private void startGame(int mapID) {
        Gdx.app.log("MapSelection", "Starting Map ID: " + mapID + " with " + readyPlayers.size() + " players.");

        // 1. Backend Game objesini oluştur
        Game backendGame = new Game();

        // 2. EmpireSelection'dan gelen oyuncuları ekle
        for (Player p : readyPlayers) {
            backendGame.addPlayer(p);
        }

        // 3. Haritayı başlat (Game.java içindeki initializeMap çağrılır)
        backendGame.startGame(mapID);

        // 4. GameScreen'e geç (Oyunun oynandığı asıl ekran)
        game.setScreen(new GameScreen(game, backendGame));
    }

    private void loadTextures() {
        try {
            bgTexture = new Texture(Gdx.files.internal("ui/Map_bg.png"));
            panelTexture = new Texture(Gdx.files.internal("ui/MapPanel.png"));

            // assets/ui/map_placeholder_1.png vb. oluşturun.
            try {
                map1Preview = new Texture(Gdx.files.internal("ui/Map_1.png"));
            } catch (Exception e) {
                map1Preview = panelTexture;
            } // Fallback

            try {
                map2Preview = new Texture(Gdx.files.internal("ui/Map_2.png"));
            } catch (Exception e) {
                map2Preview = panelTexture;
            }

            try {
                map3Preview = new Texture(Gdx.files.internal("ui/Map_3.png"));
            } catch (Exception e) {
                map3Preview = panelTexture;
            }

        } catch (Exception e) {
            Gdx.app.error("MapSelection", "Texture yüklenemedi", e);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        if (bgTexture != null)
            bgTexture.dispose();
        if (panelTexture != null)
            panelTexture.dispose();
        if (map1Preview != null && map1Preview != panelTexture)
            map1Preview.dispose();
        // ... diğer textureları dispose et
    }
}
