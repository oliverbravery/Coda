package net.oliverbravery.coda.utilities;

import net.minecraft.client.MinecraftClient;
import net.oliverbravery.coda.features.*;

public class KeybindEvents {
    public static void tick(MinecraftClient client) {
        //Checks for keybindings pressed
        AutoSaveTool.KeybindCheck();
        AutoFish.KeybindCheck();
        SlotRandomiser.KeybindCheck();
        ArmorSwap.KeybindCheck();
        SortInventory.KeybindCheck();
        FastPlace.KeybindCheck();
        AutoSwapTools.KeybindCheck();
    }

    public static void InitializeKeybinds() {
        AutoFish.SetAutoFishKeybind();
        ArmorSwap.SetArmorSwapKeybind();
        AutoSaveTool.SetAutoSaveToolKeybind();
        SortInventory.SetupKeybinds();
        FastPlace.SetKeybind();
        AutoSwapTools.SetKeybind();
    }
}
