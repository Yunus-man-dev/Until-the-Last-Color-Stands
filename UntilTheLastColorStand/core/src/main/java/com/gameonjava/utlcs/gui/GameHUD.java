package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameHUD implements Disposable {

    public Stage stage;
    private Viewport viewport;

    private Label goldLabel;
    private Label foodLabel;
    private Label techLabel;
    private Label turnLabel;

    public GameHUD(SpriteBatch batch) {
        //1280x720 fixed res
        viewport = new FitViewport(1280, 720, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.top(); //start from top
        stage.addActor(rootTable);

       //resource panel
        Image settingsIcon = new Image(Assets.settings);
        Image goldIcon = new Image(Assets.gold);
        Image foodIcon = new Image(Assets.food);
        Image techIcon = new Image(Assets.tech);
        Image bookIcon = new Image(Assets.book);

        // texts
        goldLabel = new Label("???", Assets.skin);
        foodLabel = new Label("???", Assets.skin);
        techLabel = new Label("???", Assets.skin);
        turnLabel = new Label("Turn: ?", Assets.skin);

        // top panel
        Table topPanel = new Table();
        topPanel.setBackground(Assets.skin.newDrawable("white", 0.2f, 0.2f, 0.2f, 0.8f)); // Koyu gri şeffaf arka plan

        topPanel.add(settingsIcon).size(40).padRight(20);

        topPanel.add(goldIcon).size(32).padRight(5);
        topPanel.add(goldLabel).padRight(20);

        topPanel.add(foodIcon).size(32).padRight(5);
        topPanel.add(foodLabel).padRight(20);

        topPanel.add(techIcon).size(32).padRight(5);
        topPanel.add(techLabel).padRight(20);

        topPanel.add(turnLabel).size(32).padRight(5);

        // add top panel to main panel
        rootTable.add(topPanel).growX().height(60).top();
        rootTable.row(); // moveonto next row

        // map will come here
        rootTable.add().expand().fill();
        rootTable.row();

        //bottom bar
        Table bottomPanel = new Table();
        bottomPanel.bottom();


        TextButton endTurnBtn = new TextButton("END TURN", Assets.skin);

        bottomPanel.add(endTurnBtn).width(150).height(50);

        rootTable.add(bottomPanel).growX().height(80).bottom();
    }

    public void render() {
        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
