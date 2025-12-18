package com.gameonjava.utlcs;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gameonjava.utlcs.gui.Assets;
import com.gameonjava.utlcs.gui.EmpireSelectionScreen;
import com.gameonjava.utlcs.gui.GameScreen;
import com.gameonjava.utlcs.gui.MainMenuScreen;
import com.gameonjava.utlcs.gui.MapSelectionScreen;


// main manager and screen transition
public class Main extends Game {

    public SpriteBatch batch;           // for draw
    public ShapeRenderer shapeRenderer; // for geo shapes

    //enum
    public enum ScreenType {
        MAIN_MENU,
        MAP_SELECTION,
        EMPIRE_SELECTION,
        GAME,
    }

 //references for screen
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
        changeScreen(ScreenType.GAME);
    }

    //CHANGE SCREEN METHOD. OTHER CLASSES MUST USE game.changeScreen(DesiredScreen) to change screens
    public void changeScreen(ScreenType type) {
        switch (type) {
            case MAIN_MENU:
                if (mainMenuScreen == null) mainMenuScreen = new MainMenuScreen(this);
                this.setScreen(mainMenuScreen);
                break;

            case MAP_SELECTION:
                if (mapSelectionScreen == null) mapSelectionScreen = new MapSelectionScreen(this);
                this.setScreen(mapSelectionScreen);
                break;

            case EMPIRE_SELECTION:
                if (empireSelectionScreen == null) empireSelectionScreen = new EmpireSelectionScreen(this);
                this.setScreen(empireSelectionScreen);
                break;

            case GAME:
                if (gameScreen == null) {
                    gameScreen = new GameScreen(this);
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
}
