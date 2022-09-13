package net.oliverbravery.coda.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import net.oliverbravery.coda.utilities.InventoryManipulator;

public class ShulkerBoxUnloader {
    public static void UnloadShulkerBox() {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        ShulkerBoxScreenHandler shulkerBoxScreenHandler = (ShulkerBoxScreenHandler) player.currentScreenHandler;
        DefaultedList<Slot> slots = shulkerBoxScreenHandler.slots;
        for (var i =0; i<27; i++) {
            if(!slots.get(i).getStack().getItem().getTranslationKey().toString().contains("air")) {
                InventoryManipulator.DropItem(i);
            }
        }
    }
}
