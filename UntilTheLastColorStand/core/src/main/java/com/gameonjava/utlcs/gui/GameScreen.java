package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.gameonjava.utlcs.Main;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Game;
import com.gameonjava.utlcs.backend.civilization.Civilization;

public class GameScreen extends ScreenAdapter {
    private Main game;
    private GameHUD hud;
    private InputController inputController;
    private boolean showBuildings = true;
    private boolean showSoldiers = true;

    public GameScreen(Main game, com.gameonjava.utlcs.backend.Game backendGame) {
        this.game = game;

        this.hud = new GameHUD(game.batch, backendGame);

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
                Civilization currentCiv = siradakiOyuncu.getCivilization();
                int yeniTurSayisi = Game.getCurrentTurn();

                hud.updateStats(siradakiOyuncu, yeniTurSayisi);
                TextureRegionDrawable newBg = getCivBg(currentCiv);
                String winText = getWinConditionText(currentCiv);
                hud.updateTurnInfo(winText, newBg);
            }
        });
        hud.getSettingsBtn().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PauseDialog pause = new PauseDialog("Game Paused", Assets.skin, game,
                    hud.backendGame);
                pause.show(hud.stage);
            }
        });
        hud.createFilterMenu(this);

        hud.getFilterBtn().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hud.toggleFilterMenu();
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

    public GameHUD getHud() {
        return hud;
    }

    public Main getMainGame() {
        return game;
    }
    private TextureRegionDrawable getCivBg(Civilization civ) {
        String name = civ.getCivilizationColor();

        if (name.contains("Red")) return Assets.bgRed;
        if (name.contains("Blue")) return Assets.bgBlue;
        if (name.contains("Gold")) return Assets.bgGold;
        if (name.contains("Brown")) return Assets.bgBrown;

        return null;
    }

    private String getWinConditionText(Civilization civ) {
        String name = civ.getCivilizationColor();

        if (name.contains("Red")) return "CONQUER 20 TILES\nAND HAVE 500 GOLD";
        if (name.contains("Blue")) return "HAVE 200 TECH\nAND 10 LIBRARIES";
        if (name.contains("Gold")) return "HAVE 100 GOLD";
        if (name.contains("Brown")) return "HAVE 100 GOLD";
        if (name.contains("Black")) return "HAVE 100 GOLD";
        if (name.contains("Dark Red")) return "HAVE 100 GOLD";
        if (name.contains("Cyan")) return "HAVE 100 GOLD";
        return null;
    }
    public boolean isShowBuildings() {
        return showBuildings;
    }

    public void setShowBuildings(boolean show) {
        this.showBuildings = show;
    }

    public boolean isShowSoldiers() {
        return showSoldiers;
    }

    public void setShowSoldiers(boolean show) {
        this.showSoldiers = show;
    }

}
