package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Assets {
    // Image goldIcon = new Image(Assets.gold);
    // Image foodIcon = new Image(Assets.food);
    // Image settingsBtn = new Image(Assets.settings);
    // BU ŞEKİLDE UI CLASSLARINDA KULLANILABILIR!!!

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
    //Info Bar
    public static final String INFO_BG_BROWN = "ui/InfoBarArkaPlan.png";
    public static final String INFO_BG_YELLOW = "ui/InfoBarBilgi.png";
    public static final String INFO_BTN_TRADE = "ui/InfoBarTradeButon.png";
    //Trade Menu
    public static final String TRADE_BG_BROWN = "ui/TradeBarArkaPlan.png";
    public static final String TRADE_BG_YELLOW = "ui/TradeBarInfoBar.png";
    public static final String TRADE_BTN_APPROVE = "ui/TradeBarButon.png";
    //Incoming Trade
    public static final String MAIL = "ui/MailIcon.png";
    public static final String INCOMING_BG = "ui/IncomingBg.png"; 
    public static final String INCOMING_HEADER = "ui/IncomingHeader.png"; 
    public static final String INCOMING_CIRCLE = "ui/IncomingCircle.png"; 
    public static final String BTN_ACCEPT = "ui/BtnAccept.png"; 
    public static final String BTN_REFUSE = "ui/BtnRefuse.png";

    public static Skin skin;
    public static final String goldBg = "ui/goldBg.png";
    public static final String blueBg = "ui/blueBg.png";
    public static final String brownBg = "ui/brownBg.png";
    public static final String redBg = "ui/redBg.png";
    public static final String INFOBG = "ui/infobg.png";
    public static final String TOPBARBG = "ui/topbarbg.png";
    public static final String RBARBG = "ui/rbarbg.png";
    public static final String TFBG = "ui/tfbg.png";

    public static TextureRegionDrawable bgRed, bgBlue, bgBrown, bgGold, infobg, topbarbg, rbarbg, tfbg;
    public static Texture infoBgBrown;
    public static Texture infoBgYellow;
    public static Texture infoBtnTrade;
    public static Texture tradeBgBrown;
    public static Texture tradeBgYellow;
    public static Texture tradeBtnApprove;
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
    public static Texture mail;
    public static Texture incomingBg;
    public static Texture incomingHeader;
    public static Texture incomingCircle;
    public static Texture btnAccept;
    public static Texture btnRefuse;

    public static void load() {
        manager.load(SKIN_JSON, Skin.class);
        manager.load(goldBg, Texture.class);
        manager.load(redBg, Texture.class);
        manager.load(blueBg, Texture.class);
        manager.load(brownBg, Texture.class);
        manager.load(INFOBG, Texture.class);
        manager.load(TOPBARBG, Texture.class);
        manager.load(RBARBG, Texture.class);
        manager.load(TFBG, Texture.class);

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
        manager.load(INFO_BG_BROWN, Texture.class);
        manager.load(INFO_BG_YELLOW, Texture.class);
        manager.load(INFO_BTN_TRADE, Texture.class);
        manager.load(TRADE_BG_BROWN, Texture.class);
        manager.load(TRADE_BG_YELLOW, Texture.class);
        manager.load(TRADE_BTN_APPROVE, Texture.class);
        manager.load(MAIL, Texture.class);
        manager.load(INCOMING_BG, Texture.class);
        manager.load(INCOMING_HEADER, Texture.class);
        manager.load(INCOMING_CIRCLE, Texture.class);
        manager.load(BTN_ACCEPT, Texture.class);
        manager.load(BTN_REFUSE, Texture.class);
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
        infoBgBrown = manager.get(INFO_BG_BROWN, Texture.class);
        infoBgYellow = manager.get(INFO_BG_YELLOW, Texture.class);
        infoBtnTrade = manager.get(INFO_BTN_TRADE, Texture.class);
        tradeBgBrown = manager.get(TRADE_BG_BROWN, Texture.class);
        tradeBgYellow = manager.get(TRADE_BG_YELLOW, Texture.class);
        tradeBtnApprove = manager.get(TRADE_BTN_APPROVE, Texture.class);
        bgRed = new TextureRegionDrawable(new TextureRegion(manager.get(redBg, Texture.class)));
        bgBlue = new TextureRegionDrawable(new TextureRegion(manager.get(blueBg, Texture.class)));
        bgBrown = new TextureRegionDrawable(new TextureRegion(manager.get(brownBg, Texture.class)));
        bgGold = new TextureRegionDrawable(new TextureRegion(manager.get(goldBg, Texture.class)));
        infobg = new TextureRegionDrawable(new TextureRegion(manager.get(INFOBG, Texture.class)));
        topbarbg = new TextureRegionDrawable(new TextureRegion(manager.get(TOPBARBG, Texture.class)));
        rbarbg = new TextureRegionDrawable(new TextureRegion(manager.get(RBARBG, Texture.class)));
        tfbg = new TextureRegionDrawable(new TextureRegion(manager.get(TFBG, Texture.class)));
        mail = manager.get(MAIL, Texture.class);
        incomingBg = manager.get(INCOMING_BG, Texture.class);
        incomingHeader = manager.get(INCOMING_HEADER, Texture.class);
        incomingCircle = manager.get(INCOMING_CIRCLE, Texture.class);
        btnAccept = manager.get(BTN_ACCEPT, Texture.class);
        btnRefuse = manager.get(BTN_REFUSE, Texture.class);

    }

    public static void dispose() {
        manager.dispose();
    }
}
