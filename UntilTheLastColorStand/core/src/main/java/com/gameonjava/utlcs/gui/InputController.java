package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.InputAdapter;

public class InputController extends InputAdapter {
    private GameScreen screen;

    public InputController(GameScreen screen) {
        this.screen = screen;
    }
}

