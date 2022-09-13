package net.oliverbravery.coda.utilities;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.ItemEntity;
import net.minecraft.screen.slot.SlotActionType;

public class InventoryManipulator {
    public static void swapSlots(int sourceSlot, int destSlot){
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerInteractionManager interactionManager = mc.interactionManager;
        ClientPlayerEntity player = mc.player;
        //source slot starts hotbar index at 0
        //dest slot starts hotbar at 36
        interactionManager.clickSlot(player.playerScreenHandler.syncId, destSlot, sourceSlot, SlotActionType.SWAP, player);
    }

    public static void DropItem(int slotNumber) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerInteractionManager interactionManager = mc.interactionManager;
        ClientPlayerEntity player = mc.player;
        interactionManager.clickSlot(player.currentScreenHandler.syncId, slotNumber, 1, SlotActionType.THROW, player);
    }
}
