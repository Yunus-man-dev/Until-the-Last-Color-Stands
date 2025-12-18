package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.gameonjava.utlcs.Main;

public class GameScreen extends ScreenAdapter {
    private Main game;
    private GameHUD hud;
    private InputController inputController;

    public GameScreen(Main game) {
        this.game = game;

        this.hud = new GameHUD(game.batch);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(hud.stage);
        multiplexer.addProcessor(new InputController(this));
        Gdx.input.setInputProcessor(multiplexer);
    }
}
