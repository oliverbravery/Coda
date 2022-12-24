package net.oliverbravery.coda.features;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Hand;
import net.oliverbravery.coda.config.Config;
import net.oliverbravery.coda.utilities.Utils;
import org.lwjgl.glfw.GLFW;

public class AutoFish {
    private static int count = 0;
    private static int waitTime = 10;
    private static boolean shouldRecast = false;
    public static KeyBinding autoFishKeybind;

    public static void tick(MinecraftClient client)
    {
        if(shouldRecast) {
            if(count == waitTime) {
                //Recast Rod
                client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
                count = 0;
                shouldRecast = false;
            }
            else {
                count++;
            }
        }
    }
    public static void RecastRod() {
        shouldRecast = true;
    }

    public static void SetAutoFishKeybind() {
        autoFishKeybind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.coda.toggleautofish",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_UNKNOWN,
                        "Coda"
                ));
    }

    public static int Toggle() {
        Config.SetValue("AutoFishEnabled", String.valueOf(!Boolean.parseBoolean(Config.GetValue("AutoFishEnabled", "true"))));
        Utils.SendChatMessage(String.format("§6Auto Fish has been toggled to §6%s", Config.GetValue("AutoFishEnabled", "true")));
        return 1;
    }

    public static void KeybindCheck() {
        if(AutoFish.autoFishKeybind.wasPressed()) {
            AutoFish.Toggle();
        }
    }

}
