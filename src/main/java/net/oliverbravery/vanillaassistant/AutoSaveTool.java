package net.oliverbravery.vanillaassistant;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class AutoSaveTool {
    boolean isEnabled = true;
    public static KeyBinding autoSaveToolKeybind;

    public AutoSaveTool() {
        SetAutoSaveToolKeybind();
    }

    private int FindFreeSlot(String itemName) {
        //NOT hotbar
        MinecraftClient mc = MinecraftClient.getInstance();
        PlayerInventory x = mc.player.getInventory();
        for (int i = 9; i < 36; i++) {
            String z = x.getStack(i).toString();
            if(z.contains("air") || z == "") {
                if(!z.contains(itemName)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int FindSlotWithoutTool() {
        //NOT HOTBAR
        MinecraftClient mc = MinecraftClient.getInstance();
        PlayerInventory x = mc.player.getInventory();
        for (int i = 9; i < x.size(); i++) {
            ItemStack z = x.getStack(i);
            if(!z.isDamageable() && i > 8) {
                return i;
            }
        }
        return -1;
    }

    public void tick(MinecraftClient client) {
        if(isEnabled) {
            MinecraftClient mc = MinecraftClient.getInstance();
            PlayerEntity player = mc.player;
            if(player != null) {
                PlayerInventory inventory = player.getInventory();
                ClientPlayerEntity cpe = mc.player;
                ItemStack currentItem = inventory.getMainHandStack();
                if (currentItem.isDamageable()) {
                    int slotNumber = inventory.selectedSlot;
                    int remaining = currentItem.getMaxDamage() - currentItem.getDamage();
                    if(remaining <= 5) {
                        //move and say
                        int freeSlot = FindFreeSlot(currentItem.toString());
                        if(freeSlot != -1) {
                            if(freeSlot >= 0 && freeSlot <= 8) {
                                freeSlot = 36 + freeSlot;
                                net.oliverbravery.vanillaassistant.InventoryManipulator.swapSlots(slotNumber, freeSlot);
                                cpe.sendMessage(Text.literal("§6To prevent your tool from breaking it was moved from your hotbar."), false);
                            }
                            else {
                                net.oliverbravery.vanillaassistant.InventoryManipulator.swapSlots(slotNumber, freeSlot);
                                cpe.sendMessage(Text.literal("§6To prevent your tool from breaking it was moved from your hotbar."), false);
                            }
                        }
                        else {
                            freeSlot = FindSlotWithoutTool();
                            if(freeSlot != -1) {
                                if(freeSlot >= 0 && freeSlot <= 8) {
                                    freeSlot = 36 + freeSlot;
                                    net.oliverbravery.vanillaassistant.InventoryManipulator.swapSlots(slotNumber, freeSlot);
                                    cpe.sendMessage(Text.literal("§6To prevent your tool from breaking it was moved from your hotbar."), false);
                                }
                                else {
                                    net.oliverbravery.vanillaassistant.InventoryManipulator.swapSlots(slotNumber, freeSlot);
                                    cpe.sendMessage(Text.literal("§6To prevent your tool from breaking it was moved from your hotbar."), false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void SetAutoSaveToolKeybind() {
        autoSaveToolKeybind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.vanillaassistantmod.toggleautosavetool",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_K,
                        "Vanilla Assistant Mod"
                ));
    }
}
