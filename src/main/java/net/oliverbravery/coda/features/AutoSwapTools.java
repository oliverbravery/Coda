package net.oliverbravery.coda.features;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.oliverbravery.coda.config.Config;
import net.oliverbravery.coda.utilities.InventoryManipulator;
import net.oliverbravery.coda.utilities.Utils;
import org.lwjgl.glfw.GLFW;

public class AutoSwapTools {
    public static KeyBinding keybind;
    public static int slotToSwitchBack = -1;
    public static boolean swapInProgress = false;

    public static void FindBestTool(BlockState blockState, MinecraftClient mc) {
        if(mc.player != null) {
            double bestSpeed = mc.player.getInventory().getMainHandStack().getMiningSpeedMultiplier(blockState);
            int bestSlot = mc.player.getInventory().selectedSlot;
            Inventory inv = mc.player.getInventory();
            for (int i = 0; i < inv.size(); i++) {
                if(inv.getStack(i).getMiningSpeedMultiplier(blockState) > bestSpeed) {
                    if(Boolean.parseBoolean(Config.GetValue("AutoSaveToolEnabled", "true")) && inv.getStack(i).isDamageable()) {
                        int remaining = inv.getStack(i).getMaxDamage() - inv.getStack(i).getDamage();
                        if(remaining > 5) {
                            bestSlot = i;
                            bestSpeed = inv.getStack(i).getMiningSpeedMultiplier(blockState);
                        }
                    }
                    else {
                        bestSlot = i;
                        bestSpeed = inv.getStack(i).getMiningSpeedMultiplier(blockState);
                    }
                }
            }
            int destSlot = mc.player.getInventory().selectedSlot;
            if(bestSlot >= 0 && bestSlot <= 8) {
                mc.player.getInventory().selectedSlot = bestSlot;
            }
            else {
                //Not in hotbar
                if(swapInProgress && bestSlot != mc.player.getInventory().selectedSlot) {
                    if(destSlot < 9) {destSlot += 36;}
                    InventoryManipulator.swapSlots(slotToSwitchBack, destSlot);
                    swapInProgress = false;
                }
                slotToSwitchBack = bestSlot;
                swapInProgress = true;
                if(destSlot < 9) {destSlot += 36;}
                InventoryManipulator.swapSlots(bestSlot, destSlot);
            }
        }
    }

    public static void tick(MinecraftClient client) {
        HitResult rayTrace = client.crosshairTarget;
        if (rayTrace instanceof BlockHitResult && client.interactionManager != null &&
                client.interactionManager.isBreakingBlock() && Boolean.parseBoolean(Config.GetValue("AutoSwapToolsEnabled","true"))) {
            BlockPos blockPos = new BlockPos(((BlockHitResult) rayTrace).getBlockPos());
            BlockState blockState = client.world.getBlockState(blockPos);
            FindBestTool(blockState, client);
        }
        else {
            if(client.player != null && !client.interactionManager.isBreakingBlock() && swapInProgress) {
                int destSlot = client.player.getInventory().selectedSlot;
                if(destSlot < 9) {destSlot += 36;}
                InventoryManipulator.swapSlots(slotToSwitchBack, destSlot);
                swapInProgress = false;
            }
        }
    }

    public static void SetKeybind() {
        keybind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.coda.toolswap",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_UNKNOWN,
                        "Coda"
                ));
    }

    public static int Toggle() {
        if(!Utils.SWITCHEROO_INSTALLED) {
            Config.SetValue("AutoSwapToolsEnabled", String.valueOf(!Boolean.parseBoolean(Config.GetValue("AutoSwapToolsEnabled", "true"))));
            Utils.SendChatMessage(String.format("§6Auto Swap Tool has been toggled to §6%s", Config.GetValue("AutoSwapToolsEnabled", "true")));
        }
        return 1;
    }

    public static void KeybindCheck(){
        if(AutoSwapTools.keybind.wasPressed()) {
            AutoSwapTools.Toggle();
        }
    }
}
