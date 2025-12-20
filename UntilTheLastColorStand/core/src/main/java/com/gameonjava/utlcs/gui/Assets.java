package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
    //Image goldIcon = new Image(Assets.gold);
    //Image foodIcon = new Image(Assets.food);
    //Image settingsBtn = new Image(Assets.settings);
    //BU ŞEKİLDE UI CLASSLARINDA KULLANILABILIR!!!

    public static final AssetManager manager = new AssetManager();

    public static final String SKIN_JSON = "skin/uiskin.json";

    public static final String BOOK = "ui/BookIcon.png";
    public static final String DASH = "ui/DashIcon.png";
    public static final String FARM = "ui/FarmIcon.png";
    public static final String GOLD = "ui/GoldIcon.png";
    public static final String FOOD = "ui/FoodIcon.png";
    public static final String LIBRARY = "ui/LibraryIcon.png";
    public static final String PORT = "ui/PortIcon.png";
    public static final String SETTINGS = "ui/SettingsIcon.png";
    public static final String SOLDIER = "ui/SoldierIcon.png";
    public static final String TECH = "ui/TechIcon.png";
    public static final String WAR = "ui/WarIcon.png";
    public static final String MINE = "ui/MineIcon.png";
    public static final String DICE1 = "ui/Dice1Icon.png";
    public static final String DICE2 = "ui/Dice2Icon.png";
    public static final String DICE3 = "ui/Dice3Icon.png";
    public static final String DICE4 = "ui/Dice4Icon.png";
    public static final String DICE5 = "ui/Dice5Icon.png";
    public static final String DICE6 = "ui/Dice6Icon.png";
    public static final String MUSIC = "ui/music.mp3";

    public static Skin skin;

    public static Texture book;
    public static Texture dash;
    public static Texture farm;
    public static Texture gold;
    public static Texture food;
    public static Texture library;
    public static Texture port;
    public static Texture settings;
    public static Texture soldier;
    public static Texture tech;
    public static Texture war;
    public static Texture mine;
    public static Texture dice1, dice2, dice3, dice4, dice5, dice6;
    public static Music music;


    public static void load() {
        manager.load(SKIN_JSON, Skin.class);

        manager.load(BOOK, Texture.class);
        manager.load(DASH, Texture.class);
        manager.load(FARM, Texture.class);
        manager.load(GOLD, Texture.class);
        manager.load(FOOD, Texture.class);
        manager.load(LIBRARY, Texture.class);
        manager.load(PORT, Texture.class);
        manager.load(SETTINGS, Texture.class);
        manager.load(SOLDIER, Texture.class);
        manager.load(TECH, Texture.class);
        manager.load(WAR, Texture.class);
        manager.load(MINE, Texture.class);
        manager.load(DICE1, Texture.class);
        manager.load(DICE2, Texture.class);
        manager.load(DICE3, Texture.class);
        manager.load(DICE4, Texture.class);
        manager.load(DICE5, Texture.class);
        manager.load(DICE6, Texture.class);
        manager.load(MUSIC, Music.class);
    }


    public static void finishLoading() {
        manager.finishLoading();

        skin = manager.get(SKIN_JSON, Skin.class);

        BitmapFont myFont = skin.getFont("default");

        myFont.getData().setScale(0.2f);

        BitmapFont myFont2 = skin.getFont("title");

        myFont2.getData().setScale(0.3f);

        book = manager.get(BOOK, Texture.class);
        dash = manager.get(DASH, Texture.class);
        farm = manager.get(FARM, Texture.class);
        gold = manager.get(GOLD, Texture.class);
        food = manager.get(FOOD, Texture.class);
        library = manager.get(LIBRARY, Texture.class);
        port = manager.get(PORT, Texture.class);
        settings = manager.get(SETTINGS, Texture.class);
        soldier = manager.get(SOLDIER, Texture.class);
        tech = manager.get(TECH, Texture.class);
        war = manager.get(WAR, Texture.class);
        mine = manager.get(MINE, Texture.class);
        dice1 = manager.get(DICE1, Texture.class);
        dice2 = manager.get(DICE2, Texture.class);
        dice3 = manager.get(DICE3, Texture.class);
        dice4 = manager.get(DICE4, Texture.class);
        dice5 = manager.get(DICE5, Texture.class);
        dice6 = manager.get(DICE6, Texture.class);
        music = manager.get(MUSIC, Music.class);
    }


    public static void dispose() {
        manager.dispose();
    }
}

