package net.oliverbravery.vanillaassistant;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Language;

public class VanillaAssistantScreen extends Screen {
    private final Screen parent;
    private final GameOptions settings;
    private ElementListWidget elList;

    public VanillaAssistantScreen(Screen parent, GameOptions gameOptions) {
        super(Text.literal("Vanilla Assistant Mod"));
        this.parent = parent;
        this.settings = gameOptions;
    }
    private void ToggleHotbarSlot(int slotNumber) {
        if(GetSlotStatus(slotNumber) == "Enabled") {
            net.oliverbravery.vanillaassistant.VanillaAssistant.slotRandomiser.RemoveRandomSlot(slotNumber);
            //Change to Disabled
        }
        else {
            net.oliverbravery.vanillaassistant.VanillaAssistant.slotRandomiser.AddRandomSlot(slotNumber);
            //change to Enabled
        }
        this.client.setScreen(new VanillaAssistantScreen(this, this.client.options));
    }

    private String GetSlotStatus(int slotNumber) {
        var x = net.oliverbravery.vanillaassistant.VanillaAssistant.slotRandomiser.randomiseSlotKeyList;
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
        boolean autoFishStatus = net.oliverbravery.vanillaassistant.VanillaAssistant.autoFish.autoFishEnabled;
        if (autoFishStatus) {
            return "§aEnabled";
        } else {
            return "§cDisabled";
        }
    }

    private String GetAutoSaveToolStatus() {
        boolean autoFishStatus = net.oliverbravery.vanillaassistant.VanillaAssistant.autoSaveTool.isEnabled;
        if (autoFishStatus) {
            return "§aEnabled";
        } else {
            return "§cDisabled";
        }
    }

    private String GetRandomizeSlotsStatus() {
        boolean randomizeSlotsStatus = net.oliverbravery.vanillaassistant.VanillaAssistant.slotRandomiser.randomiseSlotsActive;
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
            this.addDrawableChild(new ButtonWidget(width / 2 - 112 + (25 * i), this.height / 4 + 40 + -16,
                    25, 20, Text.literal(String.format("%s", numberForSlot)),
                    button -> {
                        ToggleHotbarSlot(finalI);
                    }));
        }
        //Toggle Button
        this.addDrawableChild(new ButtonWidget(width / 2 - 70, this.height / 4 + 70 + -16,
                140, 20, Text.literal(String.format("Randomize Slots : %s", GetRandomizeSlotsStatus())),
                button -> {
                    net.oliverbravery.vanillaassistant.VanillaAssistant.slotRandomiser.randomiseSlotsActive = !net.oliverbravery.vanillaassistant.VanillaAssistant.slotRandomiser.randomiseSlotsActive;
                    this.client.setScreen(new VanillaAssistantScreen(this, this.client.options));
                }));
    }

    private void AddAutoFishButton() {
        this.addDrawableChild(new ButtonWidget(width / 2 - 115, this.height / 4 + 100 + -16,
                110, 20, Text.literal(String.format("AutoFish : %s", GetAutoFishStatus())),
                button -> {
                    net.oliverbravery.vanillaassistant.VanillaAssistant.autoFish.autoFishEnabled = !net.oliverbravery.vanillaassistant.VanillaAssistant.autoFish.autoFishEnabled;
                    this.client.setScreen(new VanillaAssistantScreen(this, this.client.options));
                }));
    }

    private void AddArmorSwapButton() {

    }

    private void AddAutoSaveToolButton() {
        this.addDrawableChild(new ButtonWidget(width / 2 + 5, this.height / 4 + 100 + -16,
                110, 20, Text.literal(String.format("SaveTool : %s", GetAutoSaveToolStatus())),
                button -> {
                    net.oliverbravery.vanillaassistant.VanillaAssistant.autoSaveTool.isEnabled = !net.oliverbravery.vanillaassistant.VanillaAssistant.autoSaveTool.isEnabled;
                    this.client.setScreen(new VanillaAssistantScreen(this, this.client.options));
                }));
    }

    private void AddSavedInventoryScreenButton() {
        this.addDrawableChild(new ButtonWidget(width / 2 - 115, this.height / 4 + 130 + -16,
                110, 20, Text.literal("Save Inventory"),
                button -> {
                    VanillaAssistant.sortInventory.AddInventory();
                }));
        this.addDrawableChild(new ButtonWidget(width / 2 + 5, this.height / 4 + 130 + -16,
                110, 20, Text.literal("Load Inventory"),
                button -> {
                    VanillaAssistant.sortInventory.LoadInventory();
                }));
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.fillGradient(matrices, 0, 0, this.width, this.height, 1114112000, 1677704500);
        matrices.push();
        matrices.scale(2.0F, 2.0F, 2.0F);
        if(this.height >= 240 && this.height <= 475) {
            drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2 / 2, 25, 16777215);
        }
        else {
            drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2 / 2, 55, 16777215);
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
        AddArmorSwapButton();
        AddAutoSaveToolButton();
        AddSavedInventoryScreenButton();
    }
}
