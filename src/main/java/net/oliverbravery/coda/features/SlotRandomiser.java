package net.oliverbravery.coda.features;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.oliverbravery.coda.config.Config;
import net.oliverbravery.coda.utilities.Utils;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class SlotRandomiser {
    public static List<Integer> randomiseSlotKeyList = new ArrayList<Integer>();
    public static KeyBinding randomiseSlotKeybind;
    public static boolean randomiseSlotsActive = false;

    public static void Initialize(){
        SetRandomizeSlotKeybind();
        LoadSlotsFromConfig();
    }

    //FOR RANDOMIZE HOTBAR
    public static void AddRandomSlot(int slotID) {
        randomiseSlotKeyList.add(slotID);
        SaveSlotConfig();
    }
    public static void RemoveRandomSlot(int slotID) {
        for (int i = 0; i < randomiseSlotKeyList.size(); i++) {
            if(randomiseSlotKeyList.get(i) == slotID) {
                randomiseSlotKeyList.remove(i);
            }
        }
        SaveSlotConfig();
    }
    private static void SaveSlotConfig() {
        String activeSlots = "";
        for (int i = 0; i < randomiseSlotKeyList.size(); i++) {
            if(i+1 == randomiseSlotKeyList.size()) {
                activeSlots += String.format("%s", randomiseSlotKeyList.get(i));
            }
            else {
                activeSlots += String.format("%s,", randomiseSlotKeyList.get(i));
            }
        }
        Config.SetValue("SlotRandomiserSlots", activeSlots);
    }
    public static void LoadSlotsFromConfig() {
        String slotsString = Config.GetValue("SlotRandomiserSlots", "");
        String[] slots = slotsString.split(",");
        for (String slot:
             slots) {
            try {
                AddRandomSlot(Integer.parseInt(slot));
            }catch(Exception e){}
        }
    }
    public static void SetRandomizeSlotKeybind() {
        randomiseSlotKeybind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                "key.coda.togglekeyrandomiser",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "Coda"
        ));
    }

    public static int Toggle() {
        SlotRandomiser.randomiseSlotsActive = !SlotRandomiser.randomiseSlotsActive;
        Utils.SendChatMessage(String.format("§6Slot Randomizer has been toggled to §6%s",  SlotRandomiser.randomiseSlotsActive));
        return 1;
    }

    public static void KeybindCheck() {
        if(SlotRandomiser.randomiseSlotKeybind.wasPressed()) {
            SlotRandomiser.Toggle();
        }
    }
}
