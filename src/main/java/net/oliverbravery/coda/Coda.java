package net.oliverbravery.coda;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Coda implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");
	public static net.oliverbravery.coda.SlotRandomiser slotRandomiser = new net.oliverbravery.coda.SlotRandomiser();
	public static net.oliverbravery.coda.AutoFish autoFish = new net.oliverbravery.coda.AutoFish();
	public static net.oliverbravery.coda.ArmorSwap armorSwap = new net.oliverbravery.coda.ArmorSwap();
	public static net.oliverbravery.coda.AutoSaveTool autoSaveTool = new net.oliverbravery.coda.AutoSaveTool();
	public static net.oliverbravery.coda.SortInventory sortInventory = new SortInventory();
	public static net.oliverbravery.coda.FastPlace fastPlace = new FastPlace();
	public static MinecraftClient mc = MinecraftClient.getInstance();
	public static Utils utils = new Utils();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Hello Fabric world!");
		ClientTickEvents.END_CLIENT_TICK.register(autoFish::tick);
		ClientTickEvents.END_CLIENT_TICK.register(autoSaveTool::tick);
		ClientTickEvents.END_CLIENT_TICK.register(fastPlace::tick);
		ClientTickEvents.END_CLIENT_TICK.register(this::tick);
	}

	public void tick(MinecraftClient client) {
		//Checks for keybinds pressed
		if(autoSaveTool.autoSaveToolKeybind.wasPressed()) {
			autoSaveTool.isEnabled = !autoSaveTool.isEnabled;
			SendChatMessage(String.format("§6Auto Save Tool has been toggled to §6%s",  autoSaveTool.isEnabled));
		}
		if(autoFish.autoFishKeybind.wasPressed()) {
			autoFish.autoFishEnabled = !autoFish.autoFishEnabled;
			SendChatMessage(String.format("§6Auto Fish has been toggled to §6%s",  autoFish.autoFishEnabled));
		}
		if(slotRandomiser.randomiseSlotKeybind.wasPressed()) {
			slotRandomiser.randomiseSlotsActive = !slotRandomiser.randomiseSlotsActive;
			SendChatMessage(String.format("§6Slot Randomizer has been toggled to §6%s",  slotRandomiser.randomiseSlotsActive));
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
	}

	public static void SendChatMessage(String message) {
		ClientPlayerEntity cpe;
		if(mc.player != null) {
			cpe = mc.player;
			cpe.sendMessage(Text.literal(message), false);
		}
	}
}
