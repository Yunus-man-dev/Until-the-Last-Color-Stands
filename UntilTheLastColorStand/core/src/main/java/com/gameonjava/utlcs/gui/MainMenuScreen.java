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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gameonjava.utlcs.Main;
import com.gameonjava.utlcs.backend.Game;
import com.gameonjava.utlcs.backend.SaveLoad;

public class MainMenuScreen extends ScreenAdapter {

    private final Main game;
    private Stage stage;

    // Bellek yönetimi için texture referanslarını tutuyoruz
    private Texture backgroundTexture;
    private Texture buttonTexture;

    public MainMenuScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        Assets.music.setLooping(true);

        // 2. Ses seviyesini ayarla (0.0 ile 1.0 arası)
        // Rapordaki "Settings" kısmını buraya bağlayacağız.
        Assets.music.setVolume(0.5f); // %50 ses

        // 3. Çalmaya başla (Eğer zaten çalmıyorsa)
        if (!Assets.music.isPlaying()) {
            Assets.music.play();
        }
        // 1. SAHNE KURULUMU
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // =================================================================
        // 2. ARKA PLAN (menu_bg.png)
        // =================================================================
            backgroundTexture = new Texture(Gdx.files.internal("ui/bg.png"));
            Image bgImage = new Image(backgroundTexture);
            bgImage.setFillParent(true); // Resmi ekrana yay
            stage.addActor(bgImage);     // En arkaya ekle


        // =================================================================
        // 3. ÖZEL BUTON STİLİ OLUŞTURMA (button_yellow.png)
        // =================================================================
        // JSON dosyasıyla uğraşmadan, elimizdeki PNG ile stili kodda yaratıyoruz.

        TextButton.TextButtonStyle customButtonStyle = new TextButton.TextButtonStyle();

            buttonTexture = new Texture(Gdx.files.internal("ui/BrownGameButton.png"));

            // NinePatch: Resmin köşelerini (örn: 10px) koru, sadece ortasını esnet.
            // Bu sayede buton büyüse de kenarlar bozulmaz.
            // (10, 10, 10, 10 değerleri resmin çerçeve kalınlığına göre değiştirilebilir)
            NinePatch patch = new NinePatch(buttonTexture, 12, 12, 12, 12);
            NinePatchDrawable buttonDrawable = new NinePatchDrawable(patch);

            customButtonStyle.up = buttonDrawable; // Normal hali
            customButtonStyle.down = buttonDrawable.tint(Color.GRAY); // Basılınca koyulaşsın
            customButtonStyle.over = buttonDrawable.tint(Color.LIGHT_GRAY); // Üzerine gelince parlasın


        // Fontu Skin'den al (Brookshire fontu)
        customButtonStyle.font = Assets.skin.getFont("default");
        customButtonStyle.fontColor = Color.WHITE; // Sarı buton üstüne siyah yazı daha iyi okunur
        customButtonStyle.downFontColor = Color.WHITE;

        // =================================================================
        // 4. ARAYÜZ DÜZENİ (TABLE)
        // =================================================================
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        rootTable.pad(40);
        rootTable.padTop(100);
        // --- Başlık ---
        //Label titleLabel = new Label("Until the Last Color Stands", Assets.skin, "default");
        //titleLabel.setFontScale(1.5f); // Başlığı büyüt

        TextButton btnNewGame = new TextButton("New Game", customButtonStyle);
        TextButton btnLoadGame = new TextButton("Load Game", customButtonStyle);
        TextButton btnSettings = new TextButton("Settings", customButtonStyle);
        TextButton btnTutorial = new TextButton("Tutorial", customButtonStyle);

        // --- Buton İşlevleri (Listeners) ---
        btnNewGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new EmpireSelectionScreen(game));
            }
        });

        btnSettings.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                new SettingsDialog2(Assets.skin).show(stage);
            }
        });

        // btnLoadGame.addListener(new ChangeListener() {
        //     @Override
        //     public void changed(ChangeEvent event, Actor actor) {
        //         SaveLoad sl = new SaveLoad();
        //         // CHANGE THIS: Match your actual file name
        //         Game loadedGame = sl.load("savefile.json");

        //         if (loadedGame != null) {
        //             // game.gameHUD = new GameHUD(game.batch, loadedGame);
        //             game.changeScreen(Main.ScreenType.GAME);
        //             System.out.println("Load successful!");
        //         } else {
        //             // This is the error you are currently seeing
        //             System.err.println("Load failed: savefile.json not found or corrupted.");
        //         }
        //     }
        // });
        btnLoadGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SaveLoad sl = new SaveLoad();
                // Dosya adının doğru olduğundan emin ol (savefile.json vs.)
                Game loadedGame = sl.load("savefile.json");

                if (loadedGame != null) {
                    // DÜZELTME BURADA:
                    // Yüklenen oyunu Main sınıfına "set" ediyoruz.
                    game.setBackendGame(loadedGame);

                    // Sonra ekrana geçiyoruz
                    game.changeScreen(Main.ScreenType.GAME);
                    System.out.println("Save Loaded Successfully!");
                } else {
                    System.err.println("Load failed: File not found or corrupted.");
                }
            }
        });

        btnTutorial.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.net.openURI("https://drive.google.com/file/d/1M6P292S_tqcnMlREHtBu2P3BDVfUtAMP/view?usp=sharing");
            }
        });

        // --- Tabloya Ekleme ve Hizalama ---
        // Butonların hepsini aynı genişlikte (300px) yapıyoruz.
        float btnWidth = 220f;
        float btnHeight = 100f;
        float gap = 10f; // Butonlar arası boşluk

        //rootTable.add(titleLabel).padBottom(60).row();
        rootTable.add(btnNewGame).width(btnWidth).height(btnHeight).padBottom(gap).row();
        rootTable.add(btnLoadGame).width(btnWidth).height(btnHeight).padBottom(gap).row();
        rootTable.add(btnSettings).width(btnWidth).height(btnHeight).padBottom(gap).row();
        rootTable.add(btnTutorial).width(btnWidth).height(btnHeight).padBottom(gap).row();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.12f, 0.2f, 1);
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
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (buttonTexture != null) buttonTexture.dispose();
    }
}
