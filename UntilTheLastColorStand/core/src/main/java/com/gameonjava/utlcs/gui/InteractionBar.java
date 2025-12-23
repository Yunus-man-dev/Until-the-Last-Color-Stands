package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog; // Dialog eklendi
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label; // Label eklendi
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.gameonjava.utlcs.backend.Game;
import com.gameonjava.utlcs.backend.Map;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Tile;
import com.gameonjava.utlcs.backend.Enum.TerrainType;
import com.gameonjava.utlcs.backend.building.Building;
import com.gameonjava.utlcs.backend.Army;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
public class InteractionBar extends Table {

    private Skin skin;
    private Game gameBackend;
    private GameHUD hud;
    private Map map;

    // UI Bileşenleri
    private Image slotBackground;
    private Table buttonTable;

    public InteractionBar(Skin skin, Game gameBackend, GameHUD hud, Map map) {
        this.skin = skin;
        this.gameBackend = gameBackend;
        this.hud = hud;
        this.map = map;

        this.bottom();
        this.padBottom(20);

        Stack stack = new Stack();

        slotBackground = new Image();
        slotBackground.setScaling(Scaling.none);
        slotBackground.setAlign(Align.center);

        buttonTable = new Table();
        buttonTable.setFillParent(true);

        stack.add(slotBackground);
        stack.add(buttonTable);

        this.add(stack).height(60).expandX().center();
    }

    public void updateContent(final Tile t) {
        buttonTable.clear();

        if (t == null) {
            setVisible(false);
            return;
        }
        Player me = gameBackend.getCurrentPlayer();

        if (t.getOwner() == null || !t.getOwner().equals(me)) {
            setVisible(false);
            return;
        }

        setVisible(true);

        boolean hasBuilding = t.hasBuilding();
        boolean isMaxLevel = hasBuilding && t.getBuilding().getLevel() >= 3;
        boolean hasArmy = t.hasArmy() && t.getArmy().getSoldiers() > 0;

        boolean canConstruct = (t.getTerrainType() == TerrainType.PLAIN ||
                                t.getTerrainType() == TerrainType.FOREST);

        // --- SENARYOLAR ---

        if ( (!hasBuilding || !isMaxLevel) && hasArmy ) {
            setSlotImage(Assets.ibSlot3);

            if (!hasBuilding) {
                if (canConstruct) addTextButton("Construct", () -> openBuildingDialog(t));
                else addEmptySlot();
            } else {
                addTextButton("Develop", () -> developBuilding(t));
            }

            addTextButton("Recruit", () -> openRecruitDialog(t));
            addTextButton("Move", () -> enableMoveMode(t));

            addCancelButton();
        }

        else if (isMaxLevel && hasArmy) {
            setSlotImage(Assets.ibSlot3);

            addTextButton("Recruit", () -> openRecruitDialog(t));
            addTextButton("Move", () -> enableMoveMode(t));

            addCancelButton();
        }

        // 3. (Bina Yok) VE (Asker Yok) -> 2 Aksiyon + Cancel
        else if (!hasBuilding && !hasArmy) {
            if (canConstruct) {
                setSlotImage(Assets.ibSlot3);
                addTextButton("Construct", () -> openBuildingDialog(t));
                addTextButton("Move", () -> System.out.println("No soldiers!"));
                addCancelButton();
            } else {
                setVisible(false);
            }
        }

        // 4. (Bina Var/Tam) VE (Asker Yok) -> 1 Aksiyon + Cancel
        else if (hasBuilding && isMaxLevel && !hasArmy) {
            setSlotImage(Assets.ibSlot2);
            addTextButton("Recruit", () -> openRecruitDialog(t));
            addCancelButton();
        }

        // 5. Bina var (Max değil) ve Asker Yok -> 2 Aksiyon + Cancel
        else if (hasBuilding && !isMaxLevel && !hasArmy) {
            setSlotImage(Assets.ibSlot3);
            addTextButton("Develop", () -> developBuilding(t));
            addTextButton("Recruit", () -> openRecruitDialog(t));
            addCancelButton();
        }

        else {
            setVisible(false);
        }
    }

    private void setSlotImage(Texture tex) {
        if (tex != null) {
            slotBackground.setDrawable(new TextureRegionDrawable(tex));
        }
    }

    private void addTextButton(String text, Runnable action) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = skin.getFont("default");
        style.fontColor = Color.BLACK;
        style.up = Assets.btnGenericDr;

        if(Assets.btnGenericDr != null)
             style.down = Assets.btnGenericDr.tint(Color.LIGHT_GRAY);

        TextButton btn = new TextButton(text, style);

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                action.run();
            }
        });

        buttonTable.add(btn).width(120).height(50).expandX().center();
    }

    private void addCancelButton() {
        addTextButton("Cancel", new Runnable() {
            @Override
            public void run() {
                setVisible(false);
            }
        });
    }

    private void addEmptySlot() {
        buttonTable.add().expandX();
    }

    // --- AKSİYONLAR ---

    private void openBuildingDialog(Tile t) {
        if (t.getOwner() != null) {
            BuildingSelectionDialog build = new BuildingSelectionDialog(Assets.skin, t, t.getOwner(), hud, map);
            build.show(hud.stage);
        }
    }

    private void developBuilding(Tile t) {
        Player player = gameBackend.getCurrentPlayer();

        if (t.hasBuilding()) {
            Building b = t.getBuilding();
            double cost = player.getGold().DEVELOP;

            if (player.getGold().checkForResource(cost) && player.getMp().checkForResource(player.getCivilization().M_UPGRADE)) {
                player.getGold().reduceResource(cost);
                player.developBuilding(t);
                System.out.println("Building upgraded to Level " + b.getLevel());
                hud.updateStats(player, Game.getCurrentTurn());
                updateContent(t);
            } else {
                System.out.println("Yetersiz Altın! Gerekli: " + cost);
            }
        }
    }

    private void openRecruitDialog(Tile t) {
        enableRecruitMode(t);
    }

    private void enableRecruitMode(final Tile t) {
        // [Recruit Kodu Buradaydı, kısalttım, aynı kalacak]
        // Daha önce yazdığım kodun aynısı buraya gelecek.
        // Yer kazanmak için tekrar kopyalamıyorum ama proje dosyasında önceki hali durmalı.
        // (Eğer silindiyse bir önceki mesajdaki kodu kullan)

        // *Önceki recruit kodunu buraya yapıştırdığını varsayıyorum*
        // Hızlıca yeniden ekliyorum tam olması için:

        buttonTable.clear();
        setSlotImage(Assets.ibSlot3);

        final Player player = gameBackend.getCurrentPlayer();
        String civName = player.getCivilization().getClass().getSimpleName();
        final int TURN_LIMIT = civName.contains("Red") ? 10 : 5;
        final int GOLD_COST_PER_UNIT = 50;
        final int MP_COST_PER_UNIT = 3;
        final int recruitedAlready = t.getRecruitedThisTurn();
        final int remainingLimit = TURN_LIMIT - recruitedAlready;
        final int[] recruitAmount = {0};

        TextButton btnMinus = new TextButton("-", skin); // Skin'den default alalım stil yoksa
        if(GameHUD.beigeStyle != null) btnMinus.setStyle(GameHUD.beigeStyle); // Varsa set et

        final Label countLabel = new Label("0", skin);
        countLabel.setColor(Color.BLACK);
        countLabel.setAlignment(Align.center);

        TextButton btnPlus = new TextButton("+", skin);
        if(GameHUD.beigeStyle != null) btnPlus.setStyle(GameHUD.beigeStyle);

        TextButton btnConfirm = new TextButton("Confirm", skin);
        if(GameHUD.beigeStyle != null) btnConfirm.setStyle(GameHUD.beigeStyle);

        TextButton btnCancel = new TextButton("Cancel", skin);
        if(GameHUD.beigeStyle != null) btnCancel.setStyle(GameHUD.beigeStyle);

        btnMinus.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (recruitAmount[0] > 0) {
                    recruitAmount[0]--;
                    countLabel.setText(String.valueOf(recruitAmount[0]));
                }
            }
        });

        btnPlus.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (recruitAmount[0] + 1 > remainingLimit) {
                    showError("Limit reached! (" + TURN_LIMIT + ")");
                    return;
                }
                recruitAmount[0]++;
                countLabel.setText(String.valueOf(recruitAmount[0]));
            }
        });

        btnConfirm.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (recruitAmount[0] > 0) {
                    double totalGoldCost = recruitAmount[0] * GOLD_COST_PER_UNIT;
                    double totalMpCost = recruitAmount[0] * MP_COST_PER_UNIT;
                    if (player.getGold().checkForResource(totalGoldCost) && player.getMp().checkForResource(totalMpCost)) {
                        player.getGold().reduceResource(totalGoldCost);
                        player.getMp().reduceResource(totalMpCost);
                        if (!t.hasArmy()) t.setArmy(new Army(recruitAmount[0], player, t));
                        else t.getArmy().addSoldiers(recruitAmount[0]);
                        t.setRecruitedThisTurn(t.getRecruitedThisTurn() + recruitAmount[0]);
                        hud.updateStats(player, Game.getCurrentTurn());
                        updateContent(t);
                    } else {
                        showError("Not enough resources!");
                    }
                }
            }
        });

        btnCancel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                updateContent(t);
            }
        });

        buttonTable.add(btnMinus).width(40).height(50).padRight(5);
        buttonTable.add(countLabel).width(40).height(50).padRight(5);
        buttonTable.add(btnPlus).width(40).height(50).padRight(15);
        buttonTable.add(btnConfirm).width(100).height(50).padRight(5);
        buttonTable.add(btnCancel).width(80).height(50);
    }

    private void enableMoveMode(final Tile t) {
        buttonTable.clear();
        setSlotImage(Assets.ibSlot3);

        final Player player = gameBackend.getCurrentPlayer();

        if (!t.hasArmy()) {
            System.out.println("Error: No army to move.");
            updateContent(t);
            return;
        }

        final int maxSoldiers = t.getArmy().getSoldiers();
        final int[] moveAmount = {maxSoldiers};

        TextButton btnMinus = new TextButton("-", skin);
        if(GameHUD.beigeStyle != null) btnMinus.setStyle(GameHUD.beigeStyle);

        final Label countLabel = new Label(String.valueOf(moveAmount[0]), skin);
        countLabel.setColor(Color.BLACK);
        countLabel.setAlignment(Align.center);

        TextButton btnPlus = new TextButton("+", skin);
        if(GameHUD.beigeStyle != null) btnPlus.setStyle(GameHUD.beigeStyle);

        TextButton btnMove = new TextButton("Move", skin); //
        if(GameHUD.beigeStyle != null) btnMove.setStyle(GameHUD.beigeStyle);

        TextButton btnCancel = new TextButton("Cancel", skin);
        if(GameHUD.beigeStyle != null) btnCancel.setStyle(GameHUD.beigeStyle);

        btnMinus.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (moveAmount[0] > 1) { // En az 1 asker taşınmalı
                    moveAmount[0]--;
                    countLabel.setText(String.valueOf(moveAmount[0]));
                }
            }
        });

        btnPlus.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (moveAmount[0] < maxSoldiers) {
                    moveAmount[0]++;
                    countLabel.setText(String.valueOf(moveAmount[0]));
                }
            }
        });

        btnMove.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Move Initiated: " + moveAmount[0]);

                setVisible(false);

                if (hud.getInputProcessor() != null) {
                    // moveAmount[0] dizisindeki değeri gönderiyoruz
                    hud.getInputProcessor().startMoveMode(t, moveAmount[0]);
                }
            }
        });

        btnCancel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                updateContent(t);
            }
        });

        buttonTable.add(btnMinus).width(40).height(50).padRight(5);
        buttonTable.add(countLabel).width(40).height(50).padRight(5);
        buttonTable.add(btnPlus).width(40).height(50).padRight(15);
        buttonTable.add(btnMove).width(100).height(50).padRight(5);
        buttonTable.add(btnCancel).width(80).height(50);
    }


    private void showError(String message) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);

        pixmap.setColor(new Color(0.9f, 0.8f, 0.2f, 1f));
        pixmap.fill();

        TextureRegionDrawable yellowBackground = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();

        com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle winStyle = new com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle();
        winStyle.background = yellowBackground;
        winStyle.titleFont = skin.getFont("default");
        winStyle.titleFontColor = Color.BLACK;

        Dialog errorDialog = new Dialog("", winStyle);

        Label messageLabel = new Label(message, skin);
        messageLabel.setColor(Color.BLACK);
        messageLabel.setAlignment(Align.center);
        messageLabel.setWrap(true);


        errorDialog.getContentTable().add(messageLabel).width(300).pad(60);

        TextButton okBtn = new TextButton("OK", GameHUD.beigeStyle);

        errorDialog.button(okBtn, true);

        errorDialog.getButtonTable().getCell(okBtn).width(150).height(50);

        errorDialog.getButtonTable().padBottom(30);

        errorDialog.pack();

        errorDialog.setPosition(
            Math.round((hud.stage.getWidth() - errorDialog.getWidth()) / 2),
            Math.round((hud.stage.getHeight() - errorDialog.getHeight()) / 2)
        );

        if (hud.stage != null) {
            errorDialog.show(hud.stage);
        }
    }
}
