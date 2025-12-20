package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Align;

public class SettingsDialog extends Dialog {

    private Runnable onBackAction; // Geri dönünce yapılacak işlem

    public SettingsDialog(String title, Skin skin, Runnable onBackAction) {
        super(title, skin);
        this.onBackAction = onBackAction;

        // 1. Pencere Ayarları
        setModal(true);      // Arkaya tıklanmasını engelle
        setMovable(false);   // ELLE HAREKET ETTİRMEYİ KAPAT
        setResizable(false); // Boyutlandırmayı kapat
        
        // 2. Başlığı Ortala
        getTitleLabel().setAlignment(Align.center);
        
        // İçerik dolgusu
        pad(60, 40, 40, 40); 

        initializeControls();
        
        // 3. Back Butonu (Kapanınca onBackAction'ı çalıştırır)
        TextButton closeBtn = new TextButton("Back", skin);
        closeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide(); // Settings'i kapat
                if (onBackAction != null) {
                    onBackAction.run(); // Pause menüsünü geri aç
                }
            }
        });
        
        getButtonTable().add(closeBtn).width(150).height(50).padTop(10);
    }

    private void initializeControls() {
        Table content = getContentTable();
        Skin skin = getSkin();

        // Müzik Sesi
        Label volumeLabel = new Label("Music Volume", skin);
        volumeLabel.setAlignment(Align.center); // Yazıyı ortala
        
        final Slider volumeSlider = new Slider(0f, 1f, 0.1f, false, skin);
        if (Assets.music != null) {
            volumeSlider.setValue(Assets.music.getVolume());
        } else {
            volumeSlider.setValue(0.5f);
        }

        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (Assets.music != null) {
                    Assets.music.setVolume(volumeSlider.getValue());
                }
            }
        });

        // Tutorial Butonu
        TextButton tutorialBtn = new TextButton("Play Tutorial", skin);
        tutorialBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Tutorial başlatılıyor...");
            }
        });

        // Tabloya Ekleme
        content.add(volumeLabel).expandX().fillX().padBottom(10).row();
        content.add(volumeSlider).width(200).padBottom(20).row();
        content.add(new Label("- - - - - -", skin)).padBottom(20).row();
        content.add(tutorialBtn).width(200).height(50).row();
    }
}