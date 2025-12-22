package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
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


    OrthographicCamera camera ;
    MapManager harita ;
    ShapeRenderer rend;
    final float r = 15f;
    float textureYOffset = 0f;
    MapInputProcessor mapInput;





    public GameScreen(Main game, com.gameonjava.utlcs.backend.Game backendGame) {
        this.game = game;

        this.hud = new GameHUD(game.batch, backendGame);

        harita = new MapManager(r);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        // Kamerayı haritanın ortasına odakla
        float centerX = (harita.mapLeft + harita.mapRight) / 2f;
        float centerY = (harita.mapTop + harita.mapBottom) / 2f;
        camera.position.set(centerX, centerY, 0);
        camera.update();

        rend = new ShapeRenderer();


        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(hud.stage);
        mapInput = new MapInputProcessor(harita, camera,r);
        multiplexer.addProcessor(mapInput);

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
        constrainCamera();
        camera.update();

        // --- 2. TEMİZLE ---
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1); // Rengi biraz koyu gri yaptım, harita parlasın
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // --- 3. HARİTA ÇİZİMİ (ARKADA) ---
        // Kamerayı batch'e tanıt
        game.batch.setProjectionMatrix(camera.combined);
        
        game.batch.begin();
        // Haritayı çiz (varsa offset değeri gönder)
        harita.render(game.batch, 0); 
        game.batch.end();

        // --- 4. DEBUG ÇİZİMİ (OPSİYONEL) ---
        rend.setProjectionMatrix(camera.combined);
        rend.begin(ShapeRenderer.ShapeType.Line);
        rend.setColor(Color.BLUE);
        rend.rect(harita.mapLeft, harita.mapBottom, 
                           harita.mapRight - harita.mapLeft, 
                           harita.mapTop - harita.mapBottom);
        rend.end();

        // --- 5. HUD ÇİZİMİ (EN ÖNDE) ---
        // HUD kendi içinde zaten stage.draw() çağırdığı için en üste çizer.
        hud.render();
        // game.batch.begin();
        // ...
        // game.batch.end();

        hud.render();
    }


    public void resize(int width, int height) {
        hud.resize(width, height);
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    public GameHUD getHud() {
        return hud;
    }

    public Main getMainGame() {
        return game;
    }

    public void dispose() {
        // game.batch.dispose(); // DİKKAT: Batch Main'den geliyor, burada dispose ETME!
        if(hud != null) hud.dispose(); // HUD içinde dispose varsa
        if(harita != null) harita.dispose();
        if(harita != null) harita.dispose();
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

    public GameHUD getGameHud(){
        return hud;
    }

      private void constrainCamera() {
        float halfW = (camera.viewportWidth * camera.zoom) / 2f;
        float halfH = (camera.viewportHeight * camera.zoom) / 2f;

        // X EKSENİ: Sığıyorsa ortala, sığmıyorsa sınırla
        if ((harita.mapRight - harita.mapLeft) < (halfW * 2)) {
            camera.position.x = (harita.mapLeft + harita.mapRight) / 2f;
        } else {
            camera.position.x = MathUtils.clamp(camera.position.x, harita.mapLeft + halfW, harita.mapRight - halfW);
        }

        // Y EKSENİ: Sığıyorsa ortala, sığmıyorsa sınırla
        if ((harita.mapTop - harita.mapBottom) < (halfH * 2)) {
            camera.position.y = (harita.mapBottom + harita.mapTop) / 2f;
        } else {
            camera.position.y = MathUtils.clamp(camera.position.y, harita.mapBottom + halfH, harita.mapTop - halfH);
        }
    }

}
