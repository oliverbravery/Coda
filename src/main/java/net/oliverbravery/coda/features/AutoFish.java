package net.oliverbravery.coda.features;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

public class AutoFish {
    private int count = 0;
    private int waitTime = 10;
    private boolean shouldRecast = false;
    public boolean autoFishEnabled = true;
    public static KeyBinding autoFishKeybind;

    public AutoFish() {
        SetAutoFishKeybind();
    }

    public void tick(MinecraftClient client)
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
    public void RecastRod() {
        shouldRecast = true;
    }

    public static void SetAutoFishKeybind() {
        autoFishKeybind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.coda.toggleautofish",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_G,
                        "Coda"
                ));
    }

}
