package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer; // Zamanlayıcı için gerekli
import com.gameonjava.utlcs.Main;
import com.gameonjava.utlcs.backend.SaveLoad;
import com.gameonjava.utlcs.backend.Game;

public class PauseDialog extends Dialog {

    private Main gameMain;
    private Game backendGame;

    public PauseDialog(String title, Skin skin, Main gameMain, Game backendGame) {
        super(title, skin);
        this.gameMain = gameMain;
        this.backendGame = backendGame;
        
        // 1. Pencere Ayarları
        setModal(true);
        setMovable(false);   // Sabit durur
        setResizable(false);
        
        // 2. Başlığı Ortala
        getTitleLabel().setAlignment(Align.center);
        
        pad(60, 40, 40, 40);
        
        initializeControls();
    }

    private void initializeControls() {
        Table contentTable = getContentTable();
        Skin skin = getSkin();
        
        float btnWidth = 220f;
        float btnHeight = 55f;
        float padding = 15f;

        // Resume (Devam Et) - Bu menüyü kapatır
        TextButton resumeBtn = new TextButton("Resume Game", skin);
        resumeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });

        // Save (Kaydet) - DÜZELTİLDİ: Menüyü KAPATMAZ
        final TextButton saveBtn = new TextButton("Save Game", skin);
        saveBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Kayıt işlemi
                SaveLoad saver = new SaveLoad();
                saver.save(backendGame, "savefile.json");
                System.out.println("Oyun kaydedildi!");
                
                // hide() komutunu kaldırdık, menü kapanmayacak.
                
                // Kullanıcıya geri bildirim verelim:
                saveBtn.setText("Saved!"); // Yazıyı değiştir
                
                // 1 saniye sonra yazıyı tekrar "Save Game" yap
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        saveBtn.setText("Save Game");
                    }
                }, 1f);
            }
        });

        // Settings (Ayarlar)
        TextButton settingsBtn = new TextButton("Settings", skin);
        settingsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                final Stage stage = getStage();
                hide(); // Settings'e geçerken Pause kapanmalı
                
                SettingsDialog settingsDialog = new SettingsDialog("Settings", getSkin(), new Runnable() {
                    @Override
                    public void run() {
                        if (stage != null) {
                            show(stage); // Geri dönünce Pause tekrar açılır
                        }
                    }
                });
                settingsDialog.show(stage);
            }
        });

        // Quit (Çıkış)
        TextButton quitBtn = new TextButton("Quit to Menu", skin);
        quitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameMain.changeScreen(Main.ScreenType.MAIN_MENU);
            }
        });

        contentTable.add(resumeBtn).width(btnWidth).height(btnHeight).padBottom(padding).row();
        contentTable.add(saveBtn).width(btnWidth).height(btnHeight).padBottom(padding).row();
        contentTable.add(settingsBtn).width(btnWidth).height(btnHeight).padBottom(padding).row();
        contentTable.add(quitBtn).width(btnWidth).height(btnHeight).row();
    }
}