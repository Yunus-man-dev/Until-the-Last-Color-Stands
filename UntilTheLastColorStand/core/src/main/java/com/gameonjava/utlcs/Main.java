package com.gameonjava.utlcs;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gameonjava.utlcs.backend.SaveLoad;
import com.gameonjava.utlcs.gui.Assets;
import com.gameonjava.utlcs.gui.EmpireSelectionScreen;
import com.gameonjava.utlcs.gui.GameHUD;
import com.gameonjava.utlcs.gui.GameScreen;
import com.gameonjava.utlcs.gui.MainMenuScreen;
import com.gameonjava.utlcs.gui.MapSelectionScreen;


// main manager and screen transition
public class Main extends Game {

    public SpriteBatch batch;           // for draw
    public ShapeRenderer shapeRenderer; // for geo shapes

    public GameHUD gameHUD;

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

        if (Assets.music != null) {
            Assets.music.setLooping(true);
            Assets.music.setVolume(0.5f);
            Assets.music.play();
        }
        changeScreen(ScreenType.MAIN_MENU);
    }

    //CHANGE SCREEN METHOD. OTHER CLASSES MUST USE game.changeScreen(DesiredScreen) to change screens
    public void changeScreen(ScreenType type) {
        switch (type) {
            case MAIN_MENU:
                if (mainMenuScreen == null) mainMenuScreen = new MainMenuScreen(this);
                this.setScreen(mainMenuScreen);
                break;

            case MAP_SELECTION:
                if (mapSelectionScreen == null) mapSelectionScreen = new MapSelectionScreen(this, empireSelectionScreen.getReadyPlayers());
                this.setScreen(mapSelectionScreen);
                break;

            case EMPIRE_SELECTION:
                if (empireSelectionScreen == null) empireSelectionScreen = new EmpireSelectionScreen(this);
                this.setScreen(empireSelectionScreen);
                break;

            case GAME:
                if (gameScreen == null) {
                    gameScreen = new GameScreen(this, gameHUD.getBackendGame());
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
        // Change "autosave.json" to "savegame.json" so the Load button finds it
        if (gameHUD != null && gameHUD.getBackendGame() != null) {
            new SaveLoad().save(gameHUD.getBackendGame(), "savegame.json");
        }
        super.dispose();
    }
}
