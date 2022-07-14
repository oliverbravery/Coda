package net.oliverbravery.coda;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class AutoSwapTools {
    public boolean isEnabled = false;
    public static KeyBinding keybind;
    public AutoSwapTools() { SetKeybind(); }

    public void FindBestTool(BlockState blockState, MinecraftClient mc) {
        if(mc.player != null) {
            double bestSpeed = mc.player.getInventory().getMainHandStack().getMiningSpeedMultiplier(blockState);
            int bestSlot = mc.player.getInventory().selectedSlot;
            Inventory inv = mc.player.getInventory();
            for (int i = 0; i < inv.size(); i++) {
                if(inv.getStack(i).getMiningSpeedMultiplier(blockState) > bestSpeed) {
                    bestSlot = i;
                    bestSpeed = inv.getStack(i).getMiningSpeedMultiplier(blockState);
                }
            }
            int destSlot = mc.player.getInventory().selectedSlot;
            if(destSlot < 9) {destSlot += 36;}
            InventoryManipulator.swapSlots(bestSlot, destSlot);
        }
    }

    public void tick(MinecraftClient client) {
        HitResult rayTrace = client.crosshairTarget;
        if (rayTrace instanceof BlockHitResult && client.interactionManager != null &&
                client.interactionManager.isBreakingBlock() && isEnabled) {
            BlockPos blockPos = new BlockPos(((BlockHitResult) rayTrace).getBlockPos());
            BlockState blockState = client.world.getBlockState(blockPos);
            FindBestTool(blockState, client);
        }
    }

    public static void SetKeybind() {
        keybind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.coda.toolswap",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_O,
                        "Coda"
                ));
    }
}
