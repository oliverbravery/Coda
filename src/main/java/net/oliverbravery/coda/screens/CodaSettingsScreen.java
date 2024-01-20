package net.oliverbravery.coda.screens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.oliverbravery.coda.config.Config;
import net.oliverbravery.coda.features.SlotRandomiser;
import net.oliverbravery.coda.features.SortInventory;
import net.oliverbravery.coda.utilities.Utils;

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
            SlotRandomiser.RemoveRandomSlot(slotNumber);
            //Change to Disabled
        }
        else {
            SlotRandomiser.AddRandomSlot(slotNumber);
            //change to Enabled
        }
        this.client.setScreen(new CodaSettingsScreen(this, this.client.options));
    }

    private String GetSlotStatus(int slotNumber) {
        var x = SlotRandomiser.randomiseSlotKeyList;
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
        boolean autoFishStatus = Boolean.parseBoolean(Config.GetValue("AutoFishEnabled", "true"));
        if (autoFishStatus) {
            return "§aEnabled";
        } else {
            return "§cDisabled";
        }
    }

    private String GetAutoSaveToolStatus() {
        boolean autoSaveToolStatus = Boolean.parseBoolean(Config.GetValue("AutoSaveToolEnabled", "true"));
        if (autoSaveToolStatus) {
            return "§aEnabled";
        } else {
            return "§cDisabled";
        }
    }

    private String GetAutoToolSwapStatus() {
        boolean autoSwapToolsStatus = Boolean.parseBoolean(Config.GetValue("AutoSwapToolsEnabled", "true"));
        if (autoSwapToolsStatus) {
            return "§aEnabled";
        } else {
            return "§cDisabled";
        }
    }

    private String GetCodaButtonStatus() {
        boolean codaButtonStatus = Boolean.parseBoolean(Config.GetValue("CodaButtonEnabled","true"));
        if (codaButtonStatus) {
            return "§aEnabled";
        } else {
            return "§cDisabled";
        }
    }

    private String GetRandomizeSlotsStatus() {
        boolean randomizeSlotsStatus = SlotRandomiser.randomiseSlotsActive;
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
            this.addDrawableChild(ButtonWidget.builder(Text.literal(String.format("%s", numberForSlot)), (button) -> {
                ToggleHotbarSlot(finalI);
            }).size(25, 20).position(width / 2 - 112 + (25 * i), this.height / 4 + 20 + -16).build());
        }
        //Toggle Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal(String.format("Randomize Slots : %s", GetRandomizeSlotsStatus())), (button) -> {
            SlotRandomiser.randomiseSlotsActive = !SlotRandomiser.randomiseSlotsActive;
            this.client.setScreen(new CodaSettingsScreen(this, this.client.options));
        }).size(140, 20).position(width / 2 - 70, this.height / 4 + 50 + -16).build());
    }

    private void AddAutoFishButton() {
        this.addDrawableChild(ButtonWidget.builder(Text.literal(String.format("AutoFish : %s", GetAutoFishStatus())), (button) -> {
            Config.SetValue("AutoFishEnabled", String.valueOf(!Boolean.parseBoolean(Config.GetValue("AutoFishEnabled", "true"))));
            this.client.setScreen(new CodaSettingsScreen(this, this.client.options));
        }).size(110, 20).position(width / 2 - 115, this.height / 4 + 80 + -16).build());
    }

    private void AddAutoSaveToolButton() {
        var x = this.addDrawableChild(ButtonWidget.builder(Text.literal(String.format("SaveTool : %s", GetAutoSaveToolStatus())), (button) -> {
            Config.SetValue("AutoSaveToolEnabled", String.valueOf(!Boolean.parseBoolean(Config.GetValue("AutoSaveToolEnabled", "true"))));
            this.client.setScreen(new CodaSettingsScreen(this, this.client.options));
        }).size(110, 20).position(width / 2 + 5, this.height / 4 + 80 + -16).build());
        if(Utils.SWITCHEROO_INSTALLED) {x.active = false;}
    }

    private void AddSavedInventoryScreenButton() {
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Save Inventory"), (button) -> {
            SortInventory.AddInventory();
        }).size(110, 20).position(width / 2 - 115, this.height / 4 + 110 + -16).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Load Inventory"), (button) -> {
            SortInventory.LoadInventory();
        }).size(110, 20).position(width / 2 + 5, this.height / 4 + 110 + -16).build());
    }

    private void AddToolSwap() {
        var x = this.addDrawableChild(ButtonWidget.builder(Text.literal(String.format("ToolSwap : %s", GetAutoToolSwapStatus())), (button) -> {
            Config.SetValue("AutoSwapToolsEnabled", String.valueOf(!Boolean.parseBoolean(Config.GetValue("AutoSwapToolsEnabled", "true"))));
            this.client.setScreen(new CodaSettingsScreen(this, this.client.options));
        }).size(110, 20).position(width / 2 - 115, this.height / 4 + 140 + -16).build());
        if(Utils.SWITCHEROO_INSTALLED) {x.active = false;}
    }

    private void DisplayCodaButton() {
        this.addDrawableChild(ButtonWidget.builder(Text.literal(String.format("CodaButton : %s", GetCodaButtonStatus())), (button) -> {
            Config.SetValue("CodaButtonEnabled", String.valueOf(!Boolean.parseBoolean(Config.GetValue("CodaButtonEnabled", "true"))));
            this.client.setScreen(new CodaSettingsScreen(this, this.client.options));
        }).size(110, 20).position(width / 2 + 5, this.height / 4 + 140 + -16).build());
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fillGradient(0, 0, this.width, this.height, 1114112000, 1677704500);
        this.renderBackground(context, mouseX, mouseY, delta);
        MatrixStack matrices = context.getMatrices();
        matrices.scale(2.0F, 2.0F, 2.0F);
        if(this.height >= 240 && this.height <= 475) {
            context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2 / 2, 15, 16777215);
        }
        else {
            context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2 / 2, 45, 16777215);
        }
        matrices.scale(0.5F, 0.5F, 0.5F);
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Created by §2Oliver-Bravery"), (int) (this.width * 1.25) - 40, (int) (this.height * 1.25) - 1, 16777215);
        super.render(context, mouseX, mouseY, delta);
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
