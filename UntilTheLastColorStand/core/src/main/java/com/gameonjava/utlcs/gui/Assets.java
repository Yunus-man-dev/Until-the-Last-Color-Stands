package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Assets {
    // Image goldIcon = new Image(Assets.gold);
    // Image foodIcon = new Image(Assets.food);
    // Image settingsBtn = new Image(Assets.settings);
    // BU ŞEKİLDE UI CLASSLARINDA KULLANILABILIR!!!

    public static final AssetManager manager = new AssetManager();

    public static final String SKIN_JSON = "skin/uiskin.json";

    public static final Color COL_RED       = Color.valueOf("de0f3f");
    public static final Color COL_DARK_RED  = Color.valueOf("960829");

    public static final Color COL_BLUE      = Color.valueOf("38b6ff");
    public static final Color COL_CYAN      = Color.valueOf("36eee0");

    public static final Color COL_BROWN     = Color.valueOf("824703");
    public static final Color COL_GRAY      = Color.valueOf("a6a6a6");

    public static final Color COL_GOLD      = Color.valueOf("ffd230");
    public static final Color COL_ORANGE    = Color.valueOf("ff914d");

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
    //War
    public static final String WAR_BG_BROWN = "ui/WarBgBrown.png";
    public static final String WAR_BG_YELLOW = "ui/WarBgYellow.png";
    public static final String WAR_BTN_DONE = "ui/WarBtnDone.png";
    //Alt Panel
    public static final String IB_SLOT_1 = "ui/SlotBg1.png";
    public static final String IB_SLOT_2 = "ui/SlotBg2.png";
    public static final String IB_SLOT_3 = "ui/SlotBg3.png";
    public static final String BTN_GENERIC = "ui/BtnGeneric.png";


    public static Skin skin;
    public static final String goldBg = "ui/goldBg.png";
    public static final String blueBg = "ui/blueBg.png";
    public static final String brownBg = "ui/brownBg.png";
    public static final String redBg = "ui/redBg.png";
    public static final String ORANGEBG = "ui/orangeBg.png";
    public static final String CYANBG = "ui/cyanBg.png";
    public static final String DARKREDBG = "ui/darkredBg.png";
    public static final String GRAYBG = "ui/grayBg.png";

    public static final String INFOBG = "ui/infobg.png";
    public static final String TOPBARBG = "ui/topbarbg.png";
    public static final String RBARBG = "ui/rbarbg.png";
    public static final String TFBG = "ui/tfbg.png";
    public static final String MAIN_MENU_BACKGROUND = "ui/MainMenuArkaPlan.png";
    public static final String BROWN_GAME_BUTTON = "ui/BrownGameButton.png";
    public static final String COLOR_BG = "ui/Color_bg.png";
    public static final String EMPIRE_LIST_BG = "ui/EmpireList_bg.png";
    public static final String EMPIRE_SELECTION_BG = "ui/EmpireSelection_bg.png";
    public static final String EMPIRE_SELECTION_BTN = "ui/EmpireSelection_btn.png";
    public static final String UNIQUE_FEATURE = "ui/UniqueFeature_bg.png";
    public static final String WIN_CONDITION = "ui/WinCondition_bg.png";
    public static final String MAP_1 = "ui/Map_1.png";
    public static final String MAP_2 = "ui/Map_2.png";
    public static final String MAP_3 = "ui/Map_3.png";
    public static final String MAP_BG = "ui/Map_bg.png";
    public static final String MAP_BTN = "ui/Map_btn.png";
    public static final String MAP_PANEL = "ui/MapPanel.png";
    public static final String FILTERBARBG = "ui/filterbarbg.png";



    public static TextureRegionDrawable bgRed, bgBlue, bgBrown, bgGold, bgDarkred,bgCyan,bgGray, bgOrange,infobg, topbarbg, rbarbg, tfbg,filterbarbg;
    public static Texture infoBgBrown;
    public static Texture infoBgYellow;
    public static Texture infoBtnTrade;
    public static Texture tradeBgBrown;
    public static Texture tradeBgYellow;
    public static Texture tradeBtnApprove;
    public static Texture mainMenuBackground;
    public static Texture brownGameButton;
    public static Texture colorBg;
    public static Texture empireListBg;
    public static Texture empireSelectionBg;
    public static Texture empireSelectionBtn;
    public static Texture uniqueFeature;
    public static Texture winCondition;
    public static Texture map1;
    public static Texture map2;
    public static Texture map3;
    public static Texture mapBg;
    public static Texture mapBtn;
    public static Texture mapPanel;
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
    public static Texture warBgBrown, warBgYellow, warBtnDone;
    public static Drawable warBgBrownDr, warBgYellowDr, warBtnDoneDr;
    public static Texture ibSlot1, ibSlot2, ibSlot3, btnGeneric;
    public static TextureRegionDrawable btnGenericDr;


    public static void load() {
        manager.load(SKIN_JSON, Skin.class);
        manager.load(goldBg, Texture.class);
        manager.load(redBg, Texture.class);
        manager.load(blueBg, Texture.class);
        manager.load(brownBg, Texture.class);
        manager.load(CYANBG, Texture.class);
        manager.load(ORANGEBG, Texture.class);
        manager.load(GRAYBG, Texture.class);
        manager.load(DARKREDBG, Texture.class);

        manager.load(INFOBG, Texture.class);
        manager.load(TOPBARBG, Texture.class);
        manager.load(RBARBG, Texture.class);
        manager.load(TFBG, Texture.class);
        manager.load(MAIN_MENU_BACKGROUND, Texture.class);
        manager.load(BROWN_GAME_BUTTON, Texture.class);
        manager.load(COLOR_BG, Texture.class);
        manager.load(EMPIRE_LIST_BG, Texture.class);
        manager.load(EMPIRE_SELECTION_BG, Texture.class);
        manager.load(EMPIRE_SELECTION_BTN, Texture.class);
        manager.load(UNIQUE_FEATURE, Texture.class);
        manager.load(WIN_CONDITION, Texture.class);
        manager.load(MAP_1, Texture.class);
        manager.load(MAP_2, Texture.class);
        manager.load(MAP_3, Texture.class);
        manager.load(MAP_BG, Texture.class);
        manager.load(MAP_BTN, Texture.class);
        manager.load(MAP_PANEL, Texture.class);
        manager.load(FILTERBARBG, Texture.class);

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
        manager.load(WAR_BG_BROWN, Texture.class);
        manager.load(WAR_BG_YELLOW, Texture.class);
        manager.load(WAR_BTN_DONE, Texture.class);
        manager.load(IB_SLOT_1, Texture.class);
        manager.load(IB_SLOT_2, Texture.class);
        manager.load(IB_SLOT_3, Texture.class);
        manager.load(BTN_GENERIC, Texture.class);
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
        mainMenuBackground = manager.get(MAIN_MENU_BACKGROUND, Texture.class);
        brownGameButton = manager.get(BROWN_GAME_BUTTON, Texture.class);
        colorBg = manager.get(COLOR_BG, Texture.class);
        empireListBg = manager.get(EMPIRE_LIST_BG, Texture.class);
        empireSelectionBg = manager.get(EMPIRE_SELECTION_BG, Texture.class);
        empireSelectionBtn = manager.get(EMPIRE_SELECTION_BTN, Texture.class);
        uniqueFeature = manager.get(UNIQUE_FEATURE, Texture.class);
        winCondition = manager.get(WIN_CONDITION, Texture.class);
        map1 = manager.get(MAP_1, Texture.class);
        map2 = manager.get(MAP_2, Texture.class);
        map3 = manager.get(MAP_3, Texture.class);
        mapBg = manager.get(MAP_BG, Texture.class);
        mapBtn = manager.get(MAP_BTN, Texture.class);
        mapPanel = manager.get(MAP_PANEL, Texture.class);
        warBgBrown = manager.get(WAR_BG_BROWN, Texture.class);
        warBgYellow = manager.get(WAR_BG_YELLOW, Texture.class);
        warBtnDone = manager.get(WAR_BTN_DONE, Texture.class);
        ibSlot1 = manager.get(IB_SLOT_1, Texture.class);
        ibSlot2 = manager.get(IB_SLOT_2, Texture.class);
        ibSlot3 = manager.get(IB_SLOT_3, Texture.class);
        btnGeneric = manager.get(BTN_GENERIC, Texture.class);

        btnGenericDr = new TextureRegionDrawable(new TextureRegion(btnGeneric));

        warBgBrownDr = new TextureRegionDrawable(new TextureRegion(warBgBrown));
        warBgYellowDr = new TextureRegionDrawable(new TextureRegion(warBgYellow));
        warBtnDoneDr = new TextureRegionDrawable(new TextureRegion(warBtnDone));
        bgRed = new TextureRegionDrawable(new TextureRegion(manager.get(redBg, Texture.class)));
        bgBlue = new TextureRegionDrawable(new TextureRegion(manager.get(blueBg, Texture.class)));
        bgBrown = new TextureRegionDrawable(new TextureRegion(manager.get(brownBg, Texture.class)));
        bgGold = new TextureRegionDrawable(new TextureRegion(manager.get(goldBg, Texture.class)));
        bgCyan = new TextureRegionDrawable(new TextureRegion(manager.get(CYANBG, Texture.class)));
        bgOrange = new TextureRegionDrawable(new TextureRegion(manager.get(ORANGEBG, Texture.class)));
        bgGray = new TextureRegionDrawable(new TextureRegion(manager.get(GRAYBG, Texture.class)));
        bgDarkred = new TextureRegionDrawable(new TextureRegion(manager.get(DARKREDBG, Texture.class)));

        infobg = new TextureRegionDrawable(new TextureRegion(manager.get(INFOBG, Texture.class)));
        topbarbg = new TextureRegionDrawable(new TextureRegion(manager.get(TOPBARBG, Texture.class)));
        rbarbg = new TextureRegionDrawable(new TextureRegion(manager.get(RBARBG, Texture.class)));
        tfbg = new TextureRegionDrawable(new TextureRegion(manager.get(TFBG, Texture.class)));
        filterbarbg = new TextureRegionDrawable(new TextureRegion(manager.get(FILTERBARBG, Texture.class)));
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
