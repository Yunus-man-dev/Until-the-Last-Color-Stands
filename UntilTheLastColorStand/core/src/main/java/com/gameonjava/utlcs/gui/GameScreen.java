package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.gameonjava.utlcs.Main;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Tile;
import com.gameonjava.utlcs.backend.Game;
import com.gameonjava.utlcs.backend.civilization.Civilization;
public class GameScreen extends ScreenAdapter {

    private Main game;
    private GameHUD hud;

    private MapManager mapManager;
    private OrthographicCamera camera;
    private ShapeRenderer debugRenderer;
    private MapInputProcessor mapInput;
    private InputController uiInput;        // Eski adı: inputController

    private boolean showBuildings = true;
    private boolean showSoldiers = true;

    final float r = 15f; // Hex yarıçapı

    public GameScreen(Main game, com.gameonjava.utlcs.backend.Game backendGame) {
        this.game = game;

        // 1. HUD ve Harita Yöneticisi
        this.hud = new GameHUD(game.batch, backendGame);
        this.mapManager = new MapManager(backendGame.getMap(),r);

        // 2. Kamera Ayarları
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Kamerayı haritanın soluna + ekranın yarısına odakla (Sola yaslı başlasın)
        float startX = mapManager.mapLeft + (Gdx.graphics.getWidth() / 2f);
        float startY = (mapManager.mapTop + mapManager.mapBottom) / 2f;
        camera.position.set(startX, startY, 0);
        camera.update();

        // 3. Debug Çizici
        debugRenderer = new ShapeRenderer();

        // 4. Input Multiplexer (ÖNEMLİ: Sıralama)
        InputMultiplexer multiplexer = new InputMultiplexer();

        // A. Önce HUD (Butonlar)
        multiplexer.addProcessor(hud.stage);

        // B. Sonra Harita (Tıklama/Sürükleme) - HUD parametresi eklendi!
        mapInput = new MapInputProcessor(mapManager, camera, r, hud);
        multiplexer.addProcessor(mapInput);

        hud.setInputProcessor(mapInput);
        // C. En Son Klavye Kısayolları (ESC, P vs.)
        uiInput = new InputController(this);
        multiplexer.addProcessor(uiInput);

        Gdx.input.setInputProcessor(multiplexer);

        // --- HUD LISTENERLARI ---
        setupHudListeners();
    }

    private void setupHudListeners() {
        hud.updateStats(hud.backendGame.getCurrentPlayer(), Game.getCurrentTurn());

        hud.getEndTurnBtn().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hud.backendGame.nextTurn();

                if (hud.backendGame.getWinner() != null) {
                    hud.showGameOver(hud.backendGame.getWinner(), game);
                    return;
                }
                Player current = hud.backendGame.getCurrentPlayer();
                hud.updateStats(current, Game.getCurrentTurn());

                Civilization currentCiv = current.getCivilization();
                TextureRegionDrawable newBg = getCivBg(currentCiv);
                String winText = currentCiv.getWinCondText();
                hud.updateTurnInfo(winText, newBg);
            }
        });

        hud.getSettingsBtn().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PauseDialog pause = new PauseDialog("Game Paused", Assets.skin, game, hud.backendGame);
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

    @Override
    public void render(float delta) {
        // 1. Logic ve Kamera
        constrainCamera();
        camera.update();

        // 2. Temizle
        Gdx.gl.glClearColor(0.51f, 0.28f, 0.01f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 3. HARİTA ÇİZİMİ
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        // Filtreleri göndererek çizim yap
        Tile moveSource = null;
        if (mapInput != null && mapInput.isMoveMode()) {
            moveSource = mapInput.getMoveSourceTile();
        }
        mapManager.render(game.batch, 0, showSoldiers, showBuildings,moveSource);
        game.batch.end();

        // 4. DEBUG ÇİZİMİ (Mavi Kutu - Harita Sınırları)
        debugRenderer.setProjectionMatrix(camera.combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(Color.BLUE);
        debugRenderer.rect(mapManager.mapLeft, mapManager.mapBottom,
                           mapManager.mapRight - mapManager.mapLeft,
                           mapManager.mapTop - mapManager.mapBottom);
        debugRenderer.end();

        // 5. HUD ÇİZİMİ
        hud.render();
    }

    private void constrainCamera() {
        float halfW = (camera.viewportWidth * camera.zoom) / 2f;
        float halfH = (camera.viewportHeight * camera.zoom) / 2f;

        float leftMargin = 100f;
        // Harita Sınırları
        float mapLeft = mapManager.mapLeft;
        float mapRight = mapManager.mapRight;
        float mapBottom = mapManager.mapBottom;
        float mapTop = mapManager.mapTop;

        float minX = mapLeft + halfW;
        float maxX = mapRight - halfW;
        float minY = mapBottom + halfH;
        float maxY = mapTop - halfH;

        if (minX < maxX) {
            camera.position.x = MathUtils.clamp(camera.position.x, minX, maxX);
        } else {
            camera.position.x = mapLeft + halfW- leftMargin;
        }

        if (minY < maxY) {
            camera.position.y = MathUtils.clamp(camera.position.y, minY, maxY);
        } else {
            camera.position.y = (mapBottom + mapTop) / 2f;
        }
    }

    @Override
    public void resize(int width, int height) {
        hud.resize(width, height);
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void dispose() {
        if (hud != null) hud.dispose();
        if (mapManager != null) mapManager.dispose();
        if (debugRenderer != null) debugRenderer.dispose();
    }

    public GameHUD getGameHud() { return hud; }
    public GameHUD getHud() { return hud; }
    public Main getMainGame() { return game; }

    public boolean isShowBuildings() { return showBuildings; }
    public void setShowBuildings(boolean show) { this.showBuildings = show; }

    public boolean isShowSoldiers() { return showSoldiers; }
    public void setShowSoldiers(boolean show) { this.showSoldiers = show; }

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
}
