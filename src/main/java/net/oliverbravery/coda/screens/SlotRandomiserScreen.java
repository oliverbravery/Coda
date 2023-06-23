package net.oliverbravery.coda.screens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.oliverbravery.coda.features.SlotRandomiser;

public class SlotRandomiserScreen extends Screen {

    private final Screen parent;
    private final GameOptions settings;

    public SlotRandomiserScreen(Screen parent, GameOptions gameOptions) {
        super(Text.literal("Slot Randomiser"));
        this.parent = parent;
        this.settings = gameOptions;
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

    private String GetRandomizeSlotsStatus() {
        boolean randomizeSlotsStatus = SlotRandomiser.randomiseSlotsActive;
        if (randomizeSlotsStatus) {
            return "§aEnabled";
        } else {
            return "§cDisabled";
        }
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
        this.client.setScreen(new SlotRandomiserScreen(this, this.client.options));
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
            this.client.setScreen(new SlotRandomiserScreen(this, this.client.options));
        }).size(140, 20).position(width / 2 - 70, this.height / 4 + 50 + -16).build());
    }


    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Created by §2Oliver-Bravery"), (int) (this.width * 1.25) - 40, (int) (this.height * 1.25) - 1, 16777215);
        super.render(context, mouseX, mouseY, delta);
    }

    protected void init() {
        AddHotbarRandomizerButtons();
    }



}
