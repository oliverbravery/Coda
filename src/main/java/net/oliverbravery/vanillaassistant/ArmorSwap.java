package net.oliverbravery.vanillaassistant;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class ArmorSwap {
    public static KeyBinding armorSwapKeybind;

    public ArmorSwap() {
        SetArmorSwapKeybind();
    }

    private int checkForElytra() {
        MinecraftClient mc = MinecraftClient.getInstance();
        PlayerInventory x = mc.player.getInventory();
        for (int i = 0; i < x.size(); i++) {
            if(x.getStack(i).toString().contains("elytra")) {
                return i;
            }
        }
        return -1;
    }

    private int checkForChestplate() {
        MinecraftClient mc = MinecraftClient.getInstance();
        PlayerInventory x = mc.player.getInventory();
        for (int i = 0; i < x.size(); i++) {
            String z = x.getStack(i).toString();
            if(z.contains("chestplate")) {
                return i;
            }
        }
        return -1;
    }

    public void SwapPieces() {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerInteractionManager interactionManager = mc.interactionManager;
        ClientPlayerEntity player = mc.player;
        int elytraSlot = checkForElytra();
        int chestplateSlot = checkForChestplate();
        if(elytraSlot != -1 && chestplateSlot != -1) {
            String chestItemName = player.getInventory().armor.get(2).toString();
            if (!chestItemName.contains("air")) {
                //if wearing a chest item
                if(chestItemName.toUpperCase().contains("ELYTRA")) {
                    //if wearing elytra
                    //move the chestplate on and elytra off
                    net.oliverbravery.vanillaassistant.InventoryManipulator.swapSlots(chestplateSlot, 6);
                    net.oliverbravery.vanillaassistant.VanillaAssistant.SendChatMessage("§6Now wearing your §2chestplate");
                }
                else {
                    //if wearing other
                    //move the elytra on and chestplate off
                    net.oliverbravery.vanillaassistant.InventoryManipulator.swapSlots(elytraSlot, 6);
                    net.oliverbravery.vanillaassistant.VanillaAssistant.SendChatMessage("§6Now wearing your §2elytra");
                }
            }
            else {
                net.oliverbravery.vanillaassistant.InventoryManipulator.swapSlots(elytraSlot, 6);
                net.oliverbravery.vanillaassistant.VanillaAssistant.SendChatMessage("§6Now wearing your §2elytra");
            }
        }
        else if(elytraSlot != -1) {
            net.oliverbravery.vanillaassistant.InventoryManipulator.swapSlots(elytraSlot, 6);
            net.oliverbravery.vanillaassistant.VanillaAssistant.SendChatMessage("§6Now wearing your §2elytra");
        }
        else if(chestplateSlot != -1) {
            net.oliverbravery.vanillaassistant.InventoryManipulator.swapSlots(chestplateSlot, 6);
            net.oliverbravery.vanillaassistant.VanillaAssistant.SendChatMessage("§6Now wearing your §2chestplate");
        }
        else {
            net.oliverbravery.vanillaassistant.VanillaAssistant.SendChatMessage("§4Could not find any chest armor to equip!");
        }
    }

    public static void SetArmorSwapKeybind() {
        armorSwapKeybind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.vanillaassistantmod.armorswap",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_V,
                        "Vanilla Assistant Mod"
                ));
    }

}
