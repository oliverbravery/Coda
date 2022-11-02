package net.oliverbravery.coda;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.oliverbravery.coda.commands.Commands;
import net.oliverbravery.coda.config.Config;
import net.oliverbravery.coda.features.*;
import net.oliverbravery.coda.utilities.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
* The Coda class is the main 'entry' class for the mod.
* This class is used to instantiate classes such as features that store
* variables that could be used globally.
*
* The class inherits from ClientModInitializer instead of the standard ModInitializer as
* Coda is a client side mod.
* */
public class Coda implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");
	public static SlotRandomiser slotRandomiser = new SlotRandomiser();
	public static AutoFish autoFish = new AutoFish();
	public static ArmorSwap armorSwap = new ArmorSwap();
	public static AutoSaveTool autoSaveTool = new AutoSaveTool();
	public static SortInventory sortInventory = new SortInventory();
	public static FastPlace fastPlace = new FastPlace();
	public static Commands commands = new Commands();
	public static Utils utils = new Utils();
	public static AutoSwapTools autoSwapTools = new AutoSwapTools();

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(autoFish::tick);
		ClientTickEvents.END_CLIENT_TICK.register(autoSaveTool::tick);
		ClientTickEvents.END_CLIENT_TICK.register(fastPlace::tick);
		ClientTickEvents.END_CLIENT_TICK.register(autoSwapTools::tick);
		ClientTickEvents.END_CLIENT_TICK.register(this::tick);
		Config config = new Config();
		commands.InitialiseCommands();
	}

	public void tick(MinecraftClient client) {
		//Checks for keybinds pressed
		if(autoSaveTool.autoSaveToolKeybind.wasPressed()) {
			if(!Utils.SWITCHEROO_INSTALLED) {
				Config.SetValue("AutoSaveToolEnabled", String.valueOf(!Boolean.parseBoolean(Config.GetValue("AutoSaveToolEnabled", "true"))));
				Utils.SendChatMessage(String.format("§6Auto Save Tool has been toggled to §6%s", Config.GetValue("AutoSaveToolEnabled", "true")));
			}
		}
		if(autoFish.autoFishKeybind.wasPressed()) {
			Config.SetValue("AutoFishEnabled", String.valueOf(!Boolean.parseBoolean(Config.GetValue("AutoFishEnabled", "true"))));
			Utils.SendChatMessage(String.format("§6Auto Fish has been toggled to §6%s", Config.GetValue("AutoFishEnabled", "true")));
		}
		if(slotRandomiser.randomiseSlotKeybind.wasPressed()) {
			slotRandomiser.randomiseSlotsActive = !slotRandomiser.randomiseSlotsActive;
			Utils.SendChatMessage(String.format("§6Slot Randomizer has been toggled to §6%s",  slotRandomiser.randomiseSlotsActive));
		}
		if(armorSwap.armorSwapKeybind.wasPressed()) {
			armorSwap.SwapPieces();
		}
		if(sortInventory.sortInventoryKeybind.wasPressed()) {
			sortInventory.LoadInventory();
		}
		if(sortInventory.saveInventoryKeybind.wasPressed()) {
			sortInventory.AddInventory();
		}
		if(fastPlace.keybind.wasPressed()) {
			fastPlace.FastPlaceEnabled = !fastPlace.FastPlaceEnabled;
		}
		if(autoSwapTools.keybind.wasPressed()) {
			if(!Utils.SWITCHEROO_INSTALLED) {
				Config.SetValue("AutoSwapToolsEnabled", String.valueOf(!Boolean.parseBoolean(Config.GetValue("AutoSwapToolsEnabled", "true"))));
			}
		}
	}
}
