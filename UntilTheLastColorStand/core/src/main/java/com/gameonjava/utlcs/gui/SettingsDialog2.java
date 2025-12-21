package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
// import com.gameonjava.utlcs.backend.Assets;

public class SettingsDialog2 extends Dialog {

    public SettingsDialog2(Skin skin) {
        super("Settings", skin, "dialog"); // Uses the 'dialog' style from uiskin.json
        
        // 1. Setup the Layout
        // The 'getContentTable()' is the main area of the dialog
        Table content = getContentTable();
        content.pad(20);

        // 2. Music Volume Controls
        Label volumeLabel = new Label("Music Volume", skin);
        
        // Create Slider: min=0, max=1, step=0.1, vertical=false
        final Slider volumeSlider = new Slider(0f, 1f, 0.1f, false, skin);
        
        // Set current value based on actual music volume
        if (Assets.music != null) {
            volumeSlider.setValue(Assets.music.getVolume());
        }

        // Add Listener: Changes volume immediately as you drag
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (Assets.music != null) {
                    Assets.music.setVolume(volumeSlider.getValue());
                }
            }
        });

        // 3. Add Widgets to Layout
        content.add(volumeLabel).padRight(10);
        content.add(volumeSlider).width(200).row();

        // 4. The Close / Quit Button
        // The 'getButtonTable()' is the bottom area for action buttons
        TextButton closeButton = new TextButton("Close", skin);
        
        // Adding it to the button table automatically handles closing if we use the button() method,
        // but adding a listener manually gives us more control if needed.
        button(closeButton, true); // The second argument is the result object sent to result()
    }

    @Override
    protected void result(Object object) {
        // This is called when a button in the buttonTable is clicked.
        // Returning true (or any object) automatically hides() the dialog.
        System.out.println("Settings closed. Volume saved.");
    }

    @Override
    public Dialog show(Stage stage) {
        // Center the dialog and fade it in
        return super.show(stage);
    }
}
