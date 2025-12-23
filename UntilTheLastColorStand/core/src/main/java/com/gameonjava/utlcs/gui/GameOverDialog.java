package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.gameonjava.utlcs.Main;
import com.gameonjava.utlcs.backend.Player;

public class GameOverDialog extends Dialog {

    private Main game;

    public GameOverDialog(String title, Skin skin, Main game, Player winner) {
        super("", createStyle(skin));
        this.game = game;

        Label titleLabel = new Label("Game Over", skin);
        titleLabel.setColor(Color.BLACK);
        titleLabel.setFontScale(1.5f);
        titleLabel.setAlignment(Align.center);

        String winText = winner.getName() + " Has Won!";
        Label winnerLabel = new Label(winText, skin);
        winnerLabel.setColor(Color.BLACK);
        winnerLabel.setAlignment(Align.center);
        winnerLabel.setWrap(true);

        getContentTable().defaults().width(1000).pad(10);

        getContentTable().add(titleLabel).padTop(20).row();
        getContentTable().add(winnerLabel).padBottom(30).row();

        TextButton menuBtn = new TextButton("Return to Main Menu", GameHUD.beigeStyle);
        button(menuBtn, true);

        getButtonTable().getCell(menuBtn).width(250).height(60).padBottom(30);
    }

    private static WindowStyle createStyle(Skin skin) {
        WindowStyle style = new WindowStyle();
        style.titleFont = skin.getFont("default");
        style.titleFontColor = Color.BLACK;

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GOLDENROD);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        style.background = new TextureRegionDrawable(new TextureRegion(texture));
        pixmap.dispose();

        return style;
    }

    @Override
    protected void result(Object object) {
        game.changeScreen(Main.ScreenType.MAIN_MENU);
    }
}
