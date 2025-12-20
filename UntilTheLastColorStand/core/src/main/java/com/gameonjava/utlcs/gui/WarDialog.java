package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gameonjava.utlcs.backend.Army;
import com.gameonjava.utlcs.backend.Tile;

public class WarDialog extends Dialog {

    public WarDialog(String title, Skin skin, Tile attackerTile, Tile defenderTile, boolean attackerWon, int casualities) {
        super(title, skin);

        initializeResult(attackerTile, defenderTile, attackerWon, casualities);
        button("Close");
    }

    private void initializeResult(Tile att, Tile def, boolean attackerWon, int casualities) {
        Table content = getContentTable();
        
        Label titleLbl = new Label("BATTLE REPORT", getSkin());
        titleLbl.setFontScale(1.5f);
        content.add(titleLbl).padBottom(20).colspan(2).row();

        // Taraflar
        content.add(new Label("Attacker: " + att.getOwner().getName(), getSkin()));
        content.add(new Label("Defender: " + (def.getOwner() != null ? def.getOwner().getName() : "Neutral"), getSkin())).row();

        // Görsel (Kılıçlar vs)
        Image warIcon = new Image(Assets.war);
        content.add(warIcon).size(64).colspan(2).pad(15).row();

        // Sonuç Metni
        String resultText = attackerWon ? "VICTORY!" : "DEFEAT!";
        Label resultLbl = new Label(resultText, getSkin());
        if (attackerWon) {
            resultLbl.setColor(0, 1, 0, 1); // Yeşil
        } else {
            resultLbl.setColor(1, 0, 0, 1); // Kırmızı
        }
        content.add(resultLbl).colspan(2).padBottom(10).row();

        // Kayıplar
        content.add(new Label("Casualties: " + casualities + " soldiers lost.", getSkin())).colspan(2);
    }
}