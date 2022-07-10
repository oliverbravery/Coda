package net.oliverbravery.vanillaassistant;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class SlotRandomiser {
    public static List<Integer> randomiseSlotKeyList = new ArrayList<Integer>();
    public static KeyBinding randomiseSlotKeybind;
    public static boolean randomiseSlotsActive = false;

    public SlotRandomiser() {
        SetRandomizeSlotKeybind();
    }

    //FOR RANDOMIZE HOTBAR
    public static void AddRandomSlot(int slotID) {
        randomiseSlotKeyList.add(slotID);
    }
    public static void RemoveRandomSlot(int slotID) {
        for (int i = 0; i < randomiseSlotKeyList.size(); i++) {
            if(randomiseSlotKeyList.get(i) == slotID) {
                randomiseSlotKeyList.remove(i);
            }
        }
    }
    public static void SetRandomizeSlotKeybind() {
        randomiseSlotKeybind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                "key.vanillaassistantmod.togglekeyrandomiser",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "Vanilla Assistant Mod"
        ));
    }
}
