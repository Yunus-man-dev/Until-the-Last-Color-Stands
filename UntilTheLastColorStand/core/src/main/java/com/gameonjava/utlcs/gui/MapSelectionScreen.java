package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.ScreenAdapter;
import com.gameonjava.utlcs.Main;

public class MapSelectionScreen extends ScreenAdapter {
    private Main game;

    public MapSelectionScreen(Main game) {
        this.game = game;
    }

    //for moving to game screen
    public void goToNextScreen() {
        game.changeScreen(Main.ScreenType.GAME);
    }
}
