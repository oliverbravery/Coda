package net.oliverbravery.coda.screens;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.oliverbravery.coda.Coda;
import net.oliverbravery.coda.config.Config;

public class CodaSettingsScreen extends Screen {
    private final Screen parent;
    private final GameOptions settings;
    private ElementListWidget elList;

    public CodaSettingsScreen(Screen parent, GameOptions gameOptions) {
        super(Text.literal("Coda"));
        this.parent = parent;
        this.settings = gameOptions;
    }
    private void ToggleHotbarSlot(int slotNumber) {
        if(GetSlotStatus(slotNumber) == "Enabled") {
            Coda.slotRandomiser.RemoveRandomSlot(slotNumber);
            //Change to Disabled
        }
        else {
            Coda.slotRandomiser.AddRandomSlot(slotNumber);
            //change to Enabled
        }
        this.client.setScreen(new CodaSettingsScreen(this, this.client.options));
    }

    private String GetSlotStatus(int slotNumber) {
        var x = Coda.slotRandomiser.randomiseSlotKeyList;
        Boolean slotEnabled = false;
        for (var i: x) {
            if (i == slotNumber) {
                slotEnabled = true;
            }
        }
        if(slotEnabled) {
            return "Enabled";
        }
        else {
            return "Disabled";
        }
    }

    private String GetAutoFishStatus() {
        boolean autoFishStatus = Coda.autoFish.autoFishEnabled;
        if (autoFishStatus) {
            return "§aEnabled";
        } else {
            return "§cDisabled";
        }
    }

    private String GetAutoSaveToolStatus() {
        boolean autoFishStatus = Coda.autoSaveTool.isEnabled;
        if (autoFishStatus) {
            return "§aEnabled";
        } else {
            return "§cDisabled";
        }
    }

    private String GetAutoToolSwapStatus() {
        boolean autoSwapToolsStatus = Coda.autoSwapTools.isEnabled;
        if (autoSwapToolsStatus) {
            return "§aEnabled";
        } else {
            return "§cDisabled";
        }
    }

    private String GetCodaButtonStatus() {
        boolean codaButtonStatus = Coda.codaButtonEnabled;
        if (codaButtonStatus) {
            return "§aEnabled";
        } else {
            return "§cDisabled";
        }
    }

    private String GetRandomizeSlotsStatus() {
        boolean randomizeSlotsStatus = Coda.slotRandomiser.randomiseSlotsActive;
        if (randomizeSlotsStatus) {
            return "§aEnabled";
        } else {
            return "§cDisabled";
        }
    }

    private void AddHotbarRandomizerButtons() {
        //Slot Buttons
        for(int i = 0; i<9; i++) {
            String numberForSlot = String.format("%s", i+1);
            if(GetSlotStatus(i) == "Enabled") {
                numberForSlot = String.format("§a%s", i + 1);
            }
            else {
                numberForSlot = String.format("§c%s", i + 1);
            }
            int finalI = i;
            this.addDrawableChild(new ButtonWidget(width / 2 - 112 + (25 * i), this.height / 4 + 20 + -16,
                    25, 20, Text.literal(String.format("%s", numberForSlot)),
                    button -> {
                        ToggleHotbarSlot(finalI);
                    }));
        }
        //Toggle Button
        this.addDrawableChild(new ButtonWidget(width / 2 - 70, this.height / 4 + 50 + -16,
                140, 20, Text.literal(String.format("Randomize Slots : %s", GetRandomizeSlotsStatus())),
                button -> {
                    Coda.slotRandomiser.randomiseSlotsActive = !Coda.slotRandomiser.randomiseSlotsActive;
                    this.client.setScreen(new CodaSettingsScreen(this, this.client.options));
                }));
    }

    private void AddAutoFishButton() {
        this.addDrawableChild(new ButtonWidget(width / 2 - 115, this.height / 4 + 80 + -16,
                110, 20, Text.literal(String.format("AutoFish : %s", GetAutoFishStatus())),
                button -> {
                    Coda.autoFish.autoFishEnabled = !Coda.autoFish.autoFishEnabled;
                    Config.SetValue("AutoFishEnabled", Boolean.toString(Coda.autoFish.autoFishEnabled));
                    this.client.setScreen(new CodaSettingsScreen(this, this.client.options));
                }));
    }

    private void AddAutoSaveToolButton() {
        var x = this.addDrawableChild(new ButtonWidget(width / 2 + 5, this.height / 4 + 80 + -16,
                110, 20, Text.literal(String.format("SaveTool : %s", GetAutoSaveToolStatus())),
                button -> {
                    Coda.autoSaveTool.isEnabled = !Coda.autoSaveTool.isEnabled;
                    Config.SetValue("AutoSaveToolEnabled", Boolean.toString(Coda.autoSaveTool.isEnabled));
                    this.client.setScreen(new CodaSettingsScreen(this, this.client.options));
                }));
        if(Coda.utils.SWITCHEROO_INSTALLED) {x.active = false;}
    }

    private void AddSavedInventoryScreenButton() {
        this.addDrawableChild(new ButtonWidget(width / 2 - 115, this.height / 4 + 110 + -16,
                110, 20, Text.literal("Save Inventory"),
                button -> {
                    Coda.sortInventory.AddInventory();
                }));
        this.addDrawableChild(new ButtonWidget(width / 2 + 5, this.height / 4 + 110 + -16,
                110, 20, Text.literal("Load Inventory"),
                button -> {
                    Coda.sortInventory.LoadInventory();
                }));
    }

    private void AddToolSwap() {
        var x = this.addDrawableChild(new ButtonWidget(width / 2 - 115, this.height / 4 + 140 + -16,
                110, 20, Text.literal(String.format("ToolSwap : %s", GetAutoToolSwapStatus())),
                button -> {
                    Coda.autoSwapTools.isEnabled = !Coda.autoSwapTools.isEnabled;
                    Config.SetValue("AutoSwapToolsEnabled", Boolean.toString(Coda.autoSwapTools.isEnabled));
                    this.client.setScreen(new CodaSettingsScreen(this, this.client.options));
                }));
        if(Coda.utils.SWITCHEROO_INSTALLED) {x.active = false;}
    }

    private void DisplayCodaButton() {
        this.addDrawableChild(new ButtonWidget(width / 2 + 5, this.height / 4 + 140 + -16,
                110, 20, Text.literal(String.format("CodaButton : %s", GetCodaButtonStatus())),
                button -> {
                    Coda.codaButtonEnabled = !Coda.codaButtonEnabled;
                    Config.SetValue("CodaButtonEnabled", Boolean.toString(Coda.codaButtonEnabled));
                    this.client.setScreen(new CodaSettingsScreen(this, this.client.options));
                }));
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.fillGradient(matrices, 0, 0, this.width, this.height, 1114112000, 1677704500);
        matrices.push();
        matrices.scale(2.0F, 2.0F, 2.0F);
        if(this.height >= 240 && this.height <= 475) {
            drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2 / 2, 15, 16777215);
        }
        else {
            drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2 / 2, 45, 16777215);
        }
        matrices.pop();

        matrices.push();
        matrices.scale(0.75F, 0.75F, 0.75F);
        drawCenteredText(matrices, this.textRenderer, Text.literal("Created by §2Oliver-Bravery"), (int) (this.width * 1.25) - 40, (int) (this.height * 1.25) - 1, 16777215);
        matrices.pop();

        super.render(matrices, mouseX, mouseY, delta);
    }


    protected void init() {
        AddHotbarRandomizerButtons();
        AddAutoFishButton();
        AddAutoSaveToolButton();
        AddSavedInventoryScreenButton();
        AddToolSwap();
        DisplayCodaButton();
    }
}
