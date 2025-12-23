package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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

        buttonTable.center();

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
        if (hasArmy) {
            int count = t.getArmy().getSoldiers();
            Label soldierLabel = new Label("Soldier: " + count, skin);
            soldierLabel.setColor(Color.BLACK);
            soldierLabel.setAlignment(Align.center);
            buttonTable.add(soldierLabel).padRight(20);
        }

        boolean canMove = false;
        if (hasArmy) {
            int allowedMoves = 1;
            if (me.getCivilization() instanceof com.gameonjava.utlcs.backend.civilization.Red) {
                allowedMoves = 2;
            }
            if (t.getArmy().getMovesMadeThisTurn() < allowedMoves) {
                canMove = true;
            }
        }

        if ((!hasBuilding || !isMaxLevel) && hasArmy) {
            setSlotImage(Assets.ibSlot3);

            if (!hasBuilding) {
                if (canConstruct)
                    addTextButton("Construct", () -> openBuildingDialog(t));
                else
                    addEmptySlot();
            } else {
                addTextButton("Develop", () -> developBuilding(t));
            }

            addTextButton("Recruit", () -> openRecruitDialog(t));


            if (canMove) {
                addTextButton("Move", () -> enableMoveMode(t));
            } else {
                addEmptySlot();
            }
            // -----------------------------------------

            addCancelButton();
        }

        else if (isMaxLevel && hasArmy) {
            setSlotImage(Assets.ibSlot3);
            addTextButton("Recruit", () -> openRecruitDialog(t));

            if (canMove) {
                addTextButton("Move", () -> enableMoveMode(t));
            } else {
                addEmptySlot();
            }
            // ----------------------------

            addCancelButton();
        }

        else if (!hasBuilding && !hasArmy) {
            if (canConstruct) {
                setSlotImage(Assets.ibSlot3);
                addTextButton("Construct", () -> openBuildingDialog(t));
                addTextButton("Recruit", () -> openRecruitDialog(t));
                addCancelButton();
            } else {
                setSlotImage(Assets.ibSlot2);
                addTextButton("Recruit", () -> openRecruitDialog(t));
                addCancelButton();
            }
        }

        else if (hasBuilding && isMaxLevel && !hasArmy) {
            setSlotImage(Assets.ibSlot2);
            addTextButton("Recruit", () -> openRecruitDialog(t));
            addCancelButton();
        }

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
            slotBackground.setDrawable(new TextureRegionDrawable(new TextureRegion(tex)));
        }
    }

    private void addTextButton(String text, Runnable action) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = skin.getFont("default");
        style.fontColor = Color.BLACK;
        style.up = Assets.btnGenericDr;

        if (Assets.btnGenericDr != null)
            style.down = Assets.btnGenericDr.tint(Color.LIGHT_GRAY);

        TextButton btn = new TextButton(text, style);

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                action.run();
            }
        });

        buttonTable.add(btn).width(120).height(50).pad(10);
    }

    private void addCancelButton() {
        addTextButton("Cancel", () -> setVisible(false));
    }

    private void addEmptySlot() {
        buttonTable.add().width(120).height(50).pad(10);
    }

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

            if (player.getGold().checkForResource(cost)) {
                player.getGold().reduceResource(cost);
                b.upgrade();
                System.out.println("Building upgraded to Level " + b.getLevel());
                hud.updateStats(player, Game.getCurrentTurn());
                updateContent(t);
            } else {
                showError("Not enough Gold! Need: " + cost);
            }
        }
    }

    private void openRecruitDialog(Tile t) {
        enableRecruitMode(t);
    }

    private void enableRecruitMode(final Tile t) {
        buttonTable.clear();
        setSlotImage(Assets.ibSlot3);

        final Player player = gameBackend.getCurrentPlayer();
        String civName = player.getCivilization().getClass().getSimpleName();
        final int TURN_LIMIT = civName.contains("Red") ? 10 : 5;
        final int GOLD_COST_PER_UNIT = 50;
        final int MP_COST_PER_UNIT = 3;
        final int recruitedAlready = t.getRecruitedThisTurn();
        final int remainingLimit = TURN_LIMIT - recruitedAlready;
        final int[] recruitAmount = { 0 };

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = skin.getFont("default");
        style.fontColor = Color.BLACK;
        style.up = Assets.btnGenericDr;
        if (Assets.btnGenericDr != null)
            style.down = Assets.btnGenericDr.tint(Color.GRAY);

        TextButton btnMinus = new TextButton("-", style);
        final Label countLabel = new Label("0", skin);
        countLabel.setColor(Color.BLACK);
        countLabel.setAlignment(Align.center);
        TextButton btnPlus = new TextButton("+", style);
        TextButton btnConfirm = new TextButton("Recruit", style);
        TextButton btnCancel = new TextButton("Cancel", style);

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

                    if (player.getGold().checkForResource(totalGoldCost)
                        && player.getMp().checkForResource(totalMpCost)) {
                        player.getGold().reduceResource(totalGoldCost);
                        player.getMp().reduceResource(totalMpCost);

                        if (!t.hasArmy())
                            t.setArmy(new Army(recruitAmount[0], player, t));
                        else
                            t.getArmy().addSoldiers(recruitAmount[0]);

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

        buttonTable.add(btnMinus).width(40).height(40).padRight(5);
        buttonTable.add(countLabel).width(40).height(40).padRight(5);
        buttonTable.add(btnPlus).width(40).height(40).padRight(15);
        buttonTable.add(btnConfirm).width(100).height(40).padRight(5);
        buttonTable.add(btnCancel).width(80).height(40);
    }

    private void enableMoveMode(final Tile t) {
        buttonTable.clear();
        setSlotImage(Assets.ibSlot3);

        if (!t.hasArmy()) {
            updateContent(t);
            return;
        }

        final int maxSoldiers = t.getArmy().getSoldiers();
        final int[] moveAmount = { maxSoldiers };

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = skin.getFont("default");
        style.fontColor = Color.BLACK;
        style.up = Assets.btnGenericDr;
        if (Assets.btnGenericDr != null)
            style.down = Assets.btnGenericDr.tint(Color.GRAY);

        TextButton btnMinus = new TextButton("-", style);
        final Label countLabel = new Label(String.valueOf(moveAmount[0]), skin);
        countLabel.setColor(Color.BLACK);
        countLabel.setAlignment(Align.center);
        TextButton btnPlus = new TextButton("+", style);
        TextButton btnMove = new TextButton("Move", style);
        TextButton btnCancel = new TextButton("Cancel", style);

        btnMinus.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (moveAmount[0] > 1) {
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
                if (hud.getInputProcessor() != null) {
                    hud.getInputProcessor().startMoveMode(t, moveAmount[0]);
                    setVisible(false);
                }
            }
        });

        btnCancel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                updateContent(t);
            }
        });

        buttonTable.add(btnMinus).width(40).height(40).padRight(5);
        buttonTable.add(countLabel).width(40).height(40).padRight(5);
        buttonTable.add(btnPlus).width(40).height(40).padRight(15);
        buttonTable.add(btnMove).width(100).height(40).padRight(5);
        buttonTable.add(btnCancel).width(80).height(40);
    }

    private void showError(String message) {
        Dialog errorDialog = new Dialog("Warning", skin) {
            public void result(Object obj) {
            }
        };

        if (Assets.infoBgBrown != null) {
            errorDialog.setBackground(new TextureRegionDrawable(new TextureRegion(Assets.infoBgBrown)));
        }

        Label l = new Label(message, skin);
        l.setColor(Color.WHITE);
        l.setAlignment(Align.center);
        errorDialog.getContentTable().add(l).pad(20);

        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font = skin.getFont("default");
        btnStyle.fontColor = Color.WHITE;
        if (Assets.brownGameButton != null) {
            btnStyle.up = new TextureRegionDrawable(new TextureRegion(Assets.brownGameButton));
        }

        TextButton okBtn = new TextButton("OK", btnStyle);
        okBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                errorDialog.hide();
            }
        });

        errorDialog.getButtonTable().add(okBtn).width(80).height(40).padBottom(10);

        if (hud.stage != null) {
            errorDialog.show(hud.stage);
        }
    }
}
