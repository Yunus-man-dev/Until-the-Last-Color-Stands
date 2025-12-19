package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gameonjava.utlcs.Main;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Game;

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
        hud.updateStats(hud.backendGame.getCurrentPlayer(), Game.getCurrentTurn());

        hud.getEndTurnBtn().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                hud.backendGame.nextTurn();

                Player siradakiOyuncu = hud.backendGame.getCurrentPlayer();
                int yeniTurSayisi = Game.getCurrentTurn();

                hud.updateStats(siradakiOyuncu, yeniTurSayisi);
            }
        });
    }
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.5f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // game.batch.begin();
        // ...
        // game.batch.end();

        hud.render();
    }
    public void resize(int width, int height) {
        hud.resize(width, height); 
    }
}
