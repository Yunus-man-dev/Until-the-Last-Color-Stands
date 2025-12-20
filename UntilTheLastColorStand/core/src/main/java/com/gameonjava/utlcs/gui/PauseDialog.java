package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.gameonjava.utlcs.Main;
import com.gameonjava.utlcs.backend.SaveLoad;
import com.gameonjava.utlcs.backend.Game;

public class PauseDialog extends Dialog {

    private Main gameMain;
    private Game backendGame;

    public static TextButton.TextButtonStyle yellowButtonStyle;
    public static NinePatchDrawable brownPanelDrawable;

    public PauseDialog(String title, Skin skin, Main gameMain, Game backendGame) {
        super(title, skin);
        this.gameMain = gameMain;
        this.backendGame = backendGame;

        if (yellowButtonStyle == null) {
            loadCustomStyles(skin);
        }

        // 2. Arka Planı Ayarla
        setBackground(brownPanelDrawable);

        // 3. Pencere Ayarları
        setModal(true);
        setMovable(false);
        setResizable(false);

        // --- DEĞİŞİKLİK 1: BAŞLIK YAZISINI BÜYÜTME ---
        getTitleLabel().setAlignment(Align.center);
        getTitleLabel().setColor(Color.BLACK);

        // --- DEĞİŞİKLİK 2: ARKA PLANI KISALTME (PADDING AYARI) ---
        // Yeni değerler: Üstten ve alttan kıstık.
        pad(40, 40, 20, 40);

        initializeControls();

        pack();
    }

    private void loadCustomStyles(Skin skin) {
        // A) KAHVERENGİ PANEL (Arka Plan)
        Texture panelTexture = new Texture(Gdx.files.internal("ui/panel_brown.png"));
        NinePatch panelPatch = new NinePatch(panelTexture, 12, 12, 12, 12);
        brownPanelDrawable = new NinePatchDrawable(panelPatch);

        // B) SARI BUTON
        Texture buttonTexture = new Texture(Gdx.files.internal("ui/button_yellow.png"));
        NinePatch buttonPatch = new NinePatch(buttonTexture, 12, 12, 12, 12);

        // Normal hali
        NinePatchDrawable buttonDrawable = new NinePatchDrawable(buttonPatch);

        yellowButtonStyle = new TextButton.TextButtonStyle();
        yellowButtonStyle.up = buttonDrawable;
        yellowButtonStyle.down = buttonDrawable.tint(Color.LIGHT_GRAY); // Basınca kararır

        // Font ayarı
        yellowButtonStyle.font = skin.getFont("default");
        yellowButtonStyle.fontColor = Color.BLACK;
    }

    private void initializeControls() {
        Table contentTable = getContentTable();

        float btnWidth = 200f;
        float btnHeight = 50f;
        float padding = 10f; // Butonlar arası boşluğu biraz azalttım

        // --- Resume Butonu ---
        TextButton resumeBtn = new TextButton("Resume Game", yellowButtonStyle);
        resumeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });

        // --- Save Butonu ---
        final TextButton saveBtn = new TextButton("Save Game", yellowButtonStyle);
        saveBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SaveLoad saver = new SaveLoad();
                saver.save(backendGame, "savefile.json");

                saveBtn.setText("Saved!");
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        saveBtn.setText("Save Game");
                    }
                }, 1f);
            }
        });

        // --- Settings Butonu ---
        TextButton settingsBtn = new TextButton("Settings", yellowButtonStyle);
        settingsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                final Stage stage = getStage();
                hide(); // Mevcut menüyü gizle

                // Settings Dialog'u aç
                SettingsDialog settingsDialog = new SettingsDialog("Settings", getSkin(), new Runnable() {
                    @Override
                    public void run() {
                        if (stage != null) {
                            show(stage); // Geri dönünce Pause menüsünü tekrar aç
                        }
                    }
                });
                settingsDialog.show(stage);
            }
        });

        // --- Quit Butonu ---
        TextButton quitBtn = new TextButton("Quit to Menu", yellowButtonStyle);
        quitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameMain.changeScreen(Main.ScreenType.MAIN_MENU);
            }
        });

        // Tabloya ekleme
        contentTable.add(resumeBtn).width(btnWidth).height(btnHeight).padBottom(padding).row();
        contentTable.add(saveBtn).width(btnWidth).height(btnHeight).padBottom(padding).row();
        contentTable.add(settingsBtn).width(btnWidth).height(btnHeight).padBottom(padding).row();
        contentTable.add(quitBtn).width(btnWidth).height(btnHeight).row();
    }
}
