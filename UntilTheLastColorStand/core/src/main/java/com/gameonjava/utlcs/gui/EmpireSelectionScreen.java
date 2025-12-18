package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.ScreenAdapter;
import com.gameonjava.utlcs.Main;

public class EmpireSelectionScreen extends ScreenAdapter {
    private Main game;

    public EmpireSelectionScreen(Main game) {
        this.game = game;
    }

    //for changing to game screen
    public void goToNextScreen() {
        game.changeScreen(Main.ScreenType.MAP_SELECTION);
    }
}
