package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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

    private Runnable onBackAction;

    public SettingsDialog(String title, Skin skin, Runnable onBackAction) {
        super(title, skin);
        this.onBackAction = onBackAction;

        if (PauseDialog.brownPanelDrawable != null) {
            setBackground(PauseDialog.brownPanelDrawable);
        }

        setModal(true);
        setMovable(false);
        setResizable(false);

        getTitleLabel().setAlignment(Align.center);
        getTitleLabel().setColor(Color.BLACK);

        pad(60, 40, 40, 40);

        initializeControls();

        TextButton closeBtn;
        if (PauseDialog.yellowButtonStyle != null) {
            closeBtn = new TextButton("Back", PauseDialog.yellowButtonStyle);
        } else {
            closeBtn = new TextButton("Back", skin); // Yedek stil
        }

        closeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
                if (onBackAction != null) {
                    onBackAction.run();
                }
            }
        });

        getButtonTable().add(closeBtn).width(150).height(50).padTop(10);
    }

    private void initializeControls() {
        Table content = getContentTable();
        Skin skin = getSkin();

        Label volumeLabel = new Label("Music Volume", skin);
        volumeLabel.setAlignment(Align.center);
        volumeLabel.setColor(Color.BLACK); // Kahverengi üstünde siyah yazı

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

        TextButton tutorialBtn;
        if (PauseDialog.yellowButtonStyle != null) {
            tutorialBtn = new TextButton("Tutorial & Lore", PauseDialog.yellowButtonStyle);
        } else {
            tutorialBtn = new TextButton("Tutorial & Lore", skin);
        }

        tutorialBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://drive.google.com/file/d/1vFYNv4yKXk--r8xFhPQ2YLAbYYHBq-KH/view?usp=sharing");
            }
        });

        content.add(volumeLabel).expandX().fillX().padBottom(10).row();
        content.add(volumeSlider).width(200).padBottom(20).row();
        content.add(new Label("- - - - - -", skin)).padBottom(20).row();
        content.add(tutorialBtn).width(200).height(50).row();
    }
}
