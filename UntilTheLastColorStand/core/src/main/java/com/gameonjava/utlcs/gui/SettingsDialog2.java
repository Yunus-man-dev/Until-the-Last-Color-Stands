package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;

public class SettingsDialog2 extends Dialog {

    public SettingsDialog2(Skin skin) {
        super("Settings", skin);


        NinePatchDrawable backgroundDrawable;

        if (PauseDialog.brownPanelDrawable != null) {
            backgroundDrawable = PauseDialog.brownPanelDrawable;
        } else {
            try {
                Texture panelTex = new Texture(Gdx.files.internal("ui/panel_brown.png"));
                backgroundDrawable = new NinePatchDrawable(new NinePatch(panelTex, 12, 12, 12, 12));
            } catch (Exception e) {
                backgroundDrawable = (NinePatchDrawable) skin.getDrawable("dialog");
            }
        }

        setBackground(backgroundDrawable);

        setModal(true);
        setMovable(false);
        setResizable(false);

        getTitleLabel().setAlignment(Align.center);
        getTitleLabel().setColor(Color.BLACK);

        pad(60, 40, 40, 40);

        Table content = getContentTable();

        Label volumeLabel = new Label("Music Volume", skin);
        volumeLabel.setAlignment(Align.center);
        volumeLabel.setColor(Color.BLACK);

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

        content.add(volumeLabel).expandX().fillX().padBottom(10).row();
        content.add(volumeSlider).width(200).padBottom(20).row();

        Label separator = new Label("- - - - - -", skin);
        separator.setColor(Color.BLACK);
        content.add(separator).padBottom(20).row();


        TextButton.TextButtonStyle closeStyle;

        if (PauseDialog.yellowButtonStyle != null) {
            closeStyle = PauseDialog.yellowButtonStyle;
        } else {
            closeStyle = new TextButton.TextButtonStyle();
            closeStyle.font = skin.getFont("default");
            closeStyle.fontColor = Color.BLACK;
            try {
                Texture btnTex = new Texture(Gdx.files.internal("ui/button_yellow.png"));
                NinePatch patch = new NinePatch(btnTex, 10, 10, 10, 10);
                closeStyle.up = new NinePatchDrawable(patch);
                closeStyle.down = new NinePatchDrawable(patch).tint(Color.GRAY);
            } catch (Exception e) {
                closeStyle = skin.get(TextButton.TextButtonStyle.class);
            }
        }

        TextButton closeButton = new TextButton("Close", closeStyle);

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });

        getButtonTable().add(closeButton).width(150).height(50).padTop(10);
    }
}
