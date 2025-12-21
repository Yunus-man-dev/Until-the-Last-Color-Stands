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
        rootTable.pad(30);
        stage.addActor(rootTable);

        // Başlık
        Label title = new Label("SELECT WORLD MAP", skin, "default"); // "subtitle" da kullanabilirsin
        title.setFontScale(0.2f);
        title.setColor(Color.GOLD);
        rootTable.add(title).padBottom(40).row();

        // 3. Harita Kartlarını Yan Yana Diz (Container)
        Table mapsContainer = new Table();

        // --- MAP 1 ---
        Table map1 = createMapCard(
            "PANGEA", 
            "Standard balanced map. Best for learning.", 
            map1Preview, 
            1
        );

        // --- MAP 2 ---
        Table map2 = createMapCard(
            "THE DIVIDE", 
            "A world split by mountains. Strategic chokepoints.", 
            map2Preview, 
            2
        );

        // --- MAP 3 ---
        Table map3 = createMapCard(
            "ARCHIPELAGO", 
            "Scattered islands. Naval power is crucial.", 
            map3Preview, 
            3
        );

        // Kartları ekle
        mapsContainer.add(map1).width(300).height(400).padRight(30);
        mapsContainer.add(map2).width(300).height(400).padRight(30);
        mapsContainer.add(map3).width(300).height(400);

        rootTable.add(mapsContainer).expandY().top().row();

        // 4. Geri Butonu
        TextButton backBtn = new TextButton("BACK", skin);
        // backBtn.add("ui/Map_btn.png");
        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new EmpireSelectionScreen(game));
            }
        });
        rootTable.add(backBtn).left().bottom().width(150).height(50);
    }

    // Harita Kartı Oluşturan Yardımcı Metot
    private Table createMapCard(String title, String desc, Texture imgTex, final int mapID) {
        Table card = new Table();
        
        // Arka plan: panel_brown.png (9-patch)
        NinePatch patch = new NinePatch(panelTexture, 12, 12, 12, 12);
        card.setBackground(new NinePatchDrawable(patch));
        card.pad(15);

        // Resim
        Image mapImg = new Image(imgTex);
        
        // Başlık
        Label titleLbl = new Label(title, skin, "subtitle");
        titleLbl.setColor(Color.GOLD);
        titleLbl.setAlignment(Align.center);

        // Açıklama
        Label descLbl = new Label(desc, skin, "default");
        descLbl.setWrap(true);
        descLbl.setAlignment(Align.center);
        descLbl.setFontScale(0.1f);

        // Seç Butonu
        TextButton selectBtn = new TextButton("START GAME", skin);
        // selectBtn.add("ui/Map_btn.png");
        selectBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                startGame(mapID);
            }
        });

        // Dizilim
        card.add(titleLbl).padBottom(10).row();
        card.add(mapImg).size(250, 150).padBottom(15).row(); // Resim boyutu
        card.add(descLbl).width(260).expandY().top().padBottom(15).row();
        card.add(selectBtn).width(180).height(45).bottom();

        return card;
    }

    // --- OYUNU BAŞLATAN KRİTİK METOT ---
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
        // NOT: Henüz GameScreen classını atmadın, varsayılan olarak şöyle olmalı:
        game.setScreen(new GameScreen(game, backendGame));
    }

    private void loadTextures() {
        try {
            bgTexture = new Texture(Gdx.files.internal("ui/Map_bg.png"));
            panelTexture = new Texture(Gdx.files.internal("ui/MapPanel.png"));
            
            // Eğer bu resimler yoksa hata vermemesi için placeholder kullanın
            // assets/ui/map_placeholder_1.png vb. oluşturun.
            try { map1Preview = new Texture(Gdx.files.internal("ui/Map_1.png")); } 
            catch(Exception e) { map1Preview = panelTexture; } // Fallback
            
            try { map2Preview = new Texture(Gdx.files.internal("ui/Map_2.png")); } 
            catch(Exception e) { map2Preview = panelTexture; }
            
            try { map3Preview = new Texture(Gdx.files.internal("ui/Map_3.png")); } 
            catch(Exception e) { map3Preview = panelTexture; }

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
        if(bgTexture!=null) bgTexture.dispose();
        if(panelTexture!=null) panelTexture.dispose();
        if(map1Preview!=null && map1Preview!=panelTexture) map1Preview.dispose();
        // ... diğer textureları dispose et
    }
}