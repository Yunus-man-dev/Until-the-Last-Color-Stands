package com.gameonjava.utlcs.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.gameonjava.utlcs.backend.*;
import com.gameonjava.utlcs.backend.Enum.TerrainType;
import com.gameonjava.utlcs.backend.building.Building;

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

        // 1. GÜVENLİK KONTROLÜ
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

        // 1. (Bina Yok/Yükseltilebilir) VE (Asker Var) -> 3 Aksiyon + Cancel = 4 Buton
        if ( (!hasBuilding || !isMaxLevel) && hasArmy ) {
            setSlotImage(Assets.ibSlot3); // 4 buton sığdırmak için en geniş barı kullanıyoruz

            if (!hasBuilding) {
                if (canConstruct) addTextButton("Construct", () -> openBuildingDialog(t));
                else addEmptySlot();
            } else {
                addTextButton("Develop", () -> developBuilding(t));
            }

            addTextButton("Recruit", () -> openRecruitDialog(t));
            addTextButton("Move", () -> enableMoveMode(t));

            addCancelButton(); // EN SAĞA EKLENDİ
        }

        // 2. (Bina Tam Seviye) VE (Asker Var) -> 2 Aksiyon + Cancel = 3 Buton
        else if (isMaxLevel && hasArmy) {
            setSlotImage(Assets.ibSlot3); // 3 buton için Slot3

            addTextButton("Recruit", () -> openRecruitDialog(t));
            addTextButton("Move", () -> enableMoveMode(t));

            addCancelButton(); // EN SAĞA EKLENDİ
        }

        // 3. (Bina Yok) VE (Asker Yok) -> 2 Aksiyon + Cancel = 3 Buton
        else if (!hasBuilding && !hasArmy) {

            if (canConstruct) {
                setSlotImage(Assets.ibSlot3); // 3 buton için Slot3
                addTextButton("Construct", () -> openBuildingDialog(t));
                addTextButton("Move", () -> System.out.println("No soldiers!"));

                addCancelButton(); // EN SAĞA EKLENDİ
            } else {
                setVisible(false);
            }
        }

        // 4. (Bina Var/Tam) VE (Asker Yok) -> 1 Aksiyon + Cancel = 2 Buton
        else if (hasBuilding && isMaxLevel && !hasArmy) {
            setSlotImage(Assets.ibSlot2); // 2 buton için Slot2 yeterli
            addTextButton("Recruit", () -> openRecruitDialog(t));

            addCancelButton(); // EN SAĞA EKLENDİ
        }

        // 5. Bina var (Max değil) ve Asker Yok -> 2 Aksiyon + Cancel = 3 Buton
        else if (hasBuilding && !isMaxLevel && !hasArmy) {
            setSlotImage(Assets.ibSlot3); // 3 buton için Slot3
            addTextButton("Develop", () -> developBuilding(t));
            addTextButton("Recruit", () -> openRecruitDialog(t));

            addCancelButton(); // EN SAĞA EKLENDİ
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

    // --- YENİ EKLENEN METOD: CANCEL BUTONU ---
    private void addCancelButton() {
        addTextButton("Cancel", new Runnable() {
            @Override
            public void run() {
                // Barı gizle
                setVisible(false);
                // Eğer harita üzerindeki seçimi de kaldırmak isterseniz
                // GameScreen veya TileSelector'a erişim gerekir.
                // Şimdilik sadece menüyü kapatıyoruz.
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

            if (player.getGold().checkForResource(cost)) {
                player.getGold().reduceResource(cost);
                b.upgrade();
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

        TextButton btnMinus = new TextButton("-", GameHUD.beigeStyle);
        final Label countLabel = new Label("0", skin);
        countLabel.setColor(Color.BLACK);
        countLabel.setAlignment(Align.center);
        TextButton btnPlus = new TextButton("+", GameHUD.beigeStyle);
        TextButton btnConfirm = new TextButton("Confirm", GameHUD.beigeStyle);
        TextButton btnCancel = new TextButton("Cancel", GameHUD.beigeStyle);


        // AZALTMA (-)
        btnMinus.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (recruitAmount[0] > 0) {
                    recruitAmount[0]--;
                    countLabel.setText(String.valueOf(recruitAmount[0]));
                } else {
                    showError("Amount cannot be negative!");
                }
            }
        });

        // ARTIRMA (+)
        btnPlus.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (recruitAmount[0] + 1 > remainingLimit) {
                    if (remainingLimit <= 0) {
                        showError("Turn limit reached! (" + TURN_LIMIT + " per turn)");
                    } else {
                        showError("You can only recruit " + remainingLimit + " more this turn!");
                    }
                    return;
                }

                double totalGoldCost = (recruitAmount[0] + 1) * GOLD_COST_PER_UNIT;
                if (!player.getGold().checkForResource(totalGoldCost)) {
                    showError("Not enough Gold! Cost: " + (int)totalGoldCost);
                    return;
                }

                double totalMpCost = (recruitAmount[0] + 1) * MP_COST_PER_UNIT;
                if (!player.getMp().checkForResource(totalMpCost)) {
                    showError("Not enough Movement Points! Need: " + (int)totalMpCost);
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

                    boolean hasGold = player.getGold().checkForResource(totalGoldCost);
                    boolean hasMp = player.getMp().checkForResource(totalMpCost);

                    if (hasGold && hasMp) {
                        player.getGold().reduceResource(totalGoldCost);
                        player.getMp().reduceResource(totalMpCost);

                        if (!t.hasArmy()) {
                            t.setArmy(new Army(recruitAmount[0], player, t));
                        } else {
                            t.getArmy().addSoldiers(recruitAmount[0]);
                        }

                        t.setRecruitedThisTurn(t.getRecruitedThisTurn() + recruitAmount[0]);

                        System.out.println("Recruited: " + recruitAmount[0] + ". Gold: -" + totalGoldCost + ", MP: -" + totalMpCost);

                        hud.updateStats(player, Game.getCurrentTurn());
                        updateContent(t);
                    } else {
                        if(!hasGold) showError("Insufficient Gold!");
                        else showError("Insufficient Movement Points!");
                    }
                } else {
                    showError("Select at least 1 soldier.");
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

    private void showError(String message) {
        Dialog errorDialog = new Dialog("Warning", skin) {
            public void result(Object obj) {
            }
        };

        errorDialog.text(message);
        errorDialog.button("OK", true);

        if (hud.stage != null) {
            errorDialog.show(hud.stage);
        }
    }

    private void enableMoveMode(Tile t) {
        System.out.println("Move Mode Active.");
    }
}
