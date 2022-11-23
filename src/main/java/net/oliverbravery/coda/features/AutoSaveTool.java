package net.oliverbravery.coda.features;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.oliverbravery.coda.config.Config;
import net.oliverbravery.coda.utilities.InventoryManipulator;
import net.oliverbravery.coda.utilities.Utils;
import org.lwjgl.glfw.GLFW;

public class AutoSaveTool {
    public static KeyBinding autoSaveToolKeybind;

    private static int FindFreeSlot(String itemName) {
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

    private static int FindSlotWithoutTool() {
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

    public static void tick(MinecraftClient client) {
        if(Boolean.parseBoolean(Config.GetValue("AutoSaveToolEnabled","true"))) {
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
                                InventoryManipulator.swapSlots(slotNumber, freeSlot);
                                cpe.sendMessage(Text.literal("§6To prevent your tool from breaking it was moved from your hotbar."), false);
                            }
                            else {
                                InventoryManipulator.swapSlots(slotNumber, freeSlot);
                                cpe.sendMessage(Text.literal("§6To prevent your tool from breaking it was moved from your hotbar."), false);
                            }
                        }
                        else {
                            freeSlot = FindSlotWithoutTool();
                            if(freeSlot != -1) {
                                if(freeSlot >= 0 && freeSlot <= 8) {
                                    freeSlot = 36 + freeSlot;
                                    InventoryManipulator.swapSlots(slotNumber, freeSlot);
                                    cpe.sendMessage(Text.literal("§6To prevent your tool from breaking it was moved from your hotbar."), false);
                                }
                                else {
                                    InventoryManipulator.swapSlots(slotNumber, freeSlot);
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
                        "key.coda.toggleautosavetool",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_K,
                        "Coda"
                ));
    }

    public static int Toggle() {
        if(!Utils.SWITCHEROO_INSTALLED) {
            Config.SetValue("AutoSaveToolEnabled", String.valueOf(!Boolean.parseBoolean(Config.GetValue("AutoSaveToolEnabled", "true"))));
            Utils.SendChatMessage(String.format("§6Auto Save Tool has been toggled to §6%s", Config.GetValue("AutoSaveToolEnabled", "true")));
        }
        return 1;
    }

    public static void KeybindCheck() {
        if(AutoSaveTool.autoSaveToolKeybind.wasPressed()) {
            AutoSaveTool.Toggle();
        }
    }
}
