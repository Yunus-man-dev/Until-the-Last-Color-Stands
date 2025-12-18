package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.ScreenAdapter;
import com.gameonjava.utlcs.Main;

public class MainMenuScreen extends ScreenAdapter {
    private Main game;

    public MainMenuScreen(Main game) {
        this.game = game;
    }
    //for changing to game screen
    public void goToNextScreen() {
        game.changeScreen(Main.ScreenType.EMPIRE_SELECTION);
    }
}
