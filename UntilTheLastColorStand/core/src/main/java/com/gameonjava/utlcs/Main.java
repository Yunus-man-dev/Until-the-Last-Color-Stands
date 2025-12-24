package com.gameonjava.utlcs;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gameonjava.utlcs.gui.Assets;
import com.gameonjava.utlcs.gui.EmpireSelectionScreen;
import com.gameonjava.utlcs.gui.GameScreen;
import com.gameonjava.utlcs.gui.MainMenuScreen;

import com.gameonjava.utlcs.gui.MapSelectionScreen;


public class Main extends Game {

    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;

    private com.gameonjava.utlcs.backend.Game backendGame;

    public enum ScreenType {
        MAIN_MENU,
        MAP_SELECTION,
        EMPIRE_SELECTION,
        GAME,
    }

    private MainMenuScreen mainMenuScreen;
    private MapSelectionScreen mapSelectionScreen;
    private EmpireSelectionScreen empireSelectionScreen;
    private GameScreen gameScreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        Assets.load();
        Assets.finishLoading();
        if (Assets.music != null) {
            Assets.music.setLooping(true);
            Assets.music.setVolume(0.5f);
            Assets.music.play();
        }

        backendGame = new com.gameonjava.utlcs.backend.Game();
        changeScreen(ScreenType.MAIN_MENU);
    }

    public void changeScreen(ScreenType type) {
        switch (type) {
            case MAIN_MENU:
                if (mainMenuScreen == null) mainMenuScreen = new MainMenuScreen(this);
                this.setScreen(mainMenuScreen);
                break;

            case MAP_SELECTION:
                // if (mapSelectionScreen == null) mapSelectionScreen = new MapSelectionScreen(this);
                this.setScreen(mapSelectionScreen);
                break;

            case EMPIRE_SELECTION:
                if (empireSelectionScreen == null) empireSelectionScreen = new EmpireSelectionScreen(this);
                this.setScreen(empireSelectionScreen);
                break;

            case GAME:
                if (gameScreen == null) {
                    gameScreen = new GameScreen(this, backendGame);
                }
                this.setScreen(gameScreen);
                break;
        }
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        Assets.dispose();

        if (mainMenuScreen != null) mainMenuScreen.dispose();
        if (mapSelectionScreen != null) mapSelectionScreen.dispose();
        if (empireSelectionScreen != null) empireSelectionScreen.dispose();
        if (gameScreen != null) gameScreen.dispose();
    }
    public void setBackendGame(com.gameonjava.utlcs.backend.Game game) {
        this.backendGame = game;

        if (gameScreen != null) {
            gameScreen.dispose();
            gameScreen = null;
        }
    }
}
