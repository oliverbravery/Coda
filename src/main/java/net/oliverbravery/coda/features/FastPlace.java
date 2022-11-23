package net.oliverbravery.coda.features;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.oliverbravery.coda.utilities.Utils;
import org.lwjgl.glfw.GLFW;

public class FastPlace {
    public static boolean FastPlaceEnabled = false;
    public static KeyBinding keybind;
    public static int tickWaitTime = 1;
    private static int delay = tickWaitTime;

    public void SetTickWaitTime(int value) {tickWaitTime = value;}

    public static void tick(MinecraftClient client) {
        if(client.player != null && FastPlaceEnabled) {
            if(delay == 0) {
                delay = tickWaitTime;
                //do
                PlaceBlockIfAvaliable(client);
            }
            else {
                delay--;
            }
        }
    }

    public static void PlaceBlockIfAvaliable(MinecraftClient mc) {
        HitResult rayTrace = mc.crosshairTarget;
        if (rayTrace instanceof BlockHitResult && mc.interactionManager != null) {
            Direction side = ((BlockHitResult) rayTrace).getSide();
            BlockPos blockPos = new BlockPos(((BlockHitResult) rayTrace).getBlockPos());
            BlockState blockState = mc.world.getBlockState(blockPos);
            String blockName = blockState.getBlock().getTranslationKey().toString();
            if(!blockName.contains("air")) {
                mc.interactionManager.interactBlock(
                        mc.player, Hand.MAIN_HAND, new BlockHitResult(
                                new Vec3d(blockPos.getX(),blockPos.getY(), blockPos.getZ()),
                                side,
                                blockPos,
                                false
                        )
                );
            }
        }

    }

    public static void SetKeybind() {
        keybind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.coda.togglefastplace",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_BACKSLASH,
                        "Coda"
                ));
    }

    public static int Toggle() {
        FastPlace.FastPlaceEnabled = !FastPlace.FastPlaceEnabled;
        Utils.SendChatMessage(String.format("§6Fast Place has been toggled to §6%s", FastPlace.FastPlaceEnabled));
        return 1;
    }

    public static void KeybindCheck() {
        if(FastPlace.keybind.wasPressed()) {
            FastPlace.Toggle();
        }
    }
}
