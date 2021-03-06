package net.oliverbravery.coda;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class Coda implements ClientModInitializer {
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
	public static AutoSwapTools autoSwapTools = new AutoSwapTools();
	public static String configPath = "CodaSettings.txt";

	public static boolean codaButtonEnabled = true;

	@Override
	public void onInitializeClient() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ClientTickEvents.END_CLIENT_TICK.register(autoFish::tick);
		ClientTickEvents.END_CLIENT_TICK.register(autoSaveTool::tick);
		ClientTickEvents.END_CLIENT_TICK.register(fastPlace::tick);
		ClientTickEvents.END_CLIENT_TICK.register(autoSwapTools::tick);
		ClientTickEvents.END_CLIENT_TICK.register(this::tick);
		LoadSettings();

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(
					literal("coda").executes(Commands::OpenCodaMenu)
						.then(literal("button")
							.then(literal("toggle").executes(ctx -> {codaButtonEnabled = !codaButtonEnabled; return 1;}))));
		});
	}

	public void LoadSettings() {
		String content = "";
		try {
			File file = new File(configPath);
			Scanner sc = new Scanner(file);
			while(sc.hasNextLine()) {
				content = sc.nextLine();
			}
			sc.close();
		}
		catch(Exception e) {}
		if(content != "") {
			String[] config = content.split(";");
			if(!config[0].equals(">")) {
				String[] activeSlots = config[0].split(",");
				for (var slot:activeSlots) {slotRandomiser.AddRandomSlot(Integer.parseInt(slot));}
			}
			if(config[1].equals("T")){autoFish.autoFishEnabled = true;}else{autoFish.autoFishEnabled = false;}
			if(config[2].equals("T")){autoSaveTool.isEnabled  = true;}else{autoSaveTool.isEnabled  = false;}
			if(config[3].equals("T")){autoSwapTools.isEnabled  = true;}else{autoSwapTools.isEnabled  = false;}
			if(config[4].equals("T")){codaButtonEnabled  = true;}else{codaButtonEnabled  = false;}
		}
		if(Utils.SWITCHEROO_INSTALLED) {
			Coda.autoSwapTools.isEnabled = false;
			Coda.autoSaveTool.isEnabled = false;
		}
	}

	public static void SaveSettings() {
		String content = "";
		if(slotRandomiser.randomiseSlotKeyList.size() == 0) {
			content += ">;";
		}
		else {
			for (int i = 0; i < slotRandomiser.randomiseSlotKeyList.size(); i++) {
				if(i + 1 == slotRandomiser.randomiseSlotKeyList.size()) {
					content += String.format("%s;", slotRandomiser.randomiseSlotKeyList.get(i));
				}
				else {
					content += String.format("%s,", slotRandomiser.randomiseSlotKeyList.get(i));
				}
			}
		}
		content	+= String.format("%s;", autoFish.autoFishEnabled ? "T" : "F");
		content	+= String.format("%s;", autoSaveTool.isEnabled ? "T" : "F");
		content	+= String.format("%s;", autoSwapTools.isEnabled ? "T" : "F");
		content	+= String.format("%s", codaButtonEnabled ? "T" : "F");
		try {
			FileWriter myWriter = new FileWriter(configPath);
			myWriter.write(content);
			myWriter.close();
		}
		catch(Exception e) {}
	}

	public void tick(MinecraftClient client) {
		//Checks for keybinds pressed
		if(autoSaveTool.autoSaveToolKeybind.wasPressed()) {
			if(!Utils.SWITCHEROO_INSTALLED) {
				SaveSettings();
				autoSaveTool.isEnabled = !autoSaveTool.isEnabled;
				SendChatMessage(String.format("??6Auto Save Tool has been toggled to ??6%s",  autoSaveTool.isEnabled));
			}
		}
		if(autoFish.autoFishKeybind.wasPressed()) {
			SaveSettings();
			autoFish.autoFishEnabled = !autoFish.autoFishEnabled;
			SendChatMessage(String.format("??6Auto Fish has been toggled to ??6%s",  autoFish.autoFishEnabled));
		}
		if(slotRandomiser.randomiseSlotKeybind.wasPressed()) {
			slotRandomiser.randomiseSlotsActive = !slotRandomiser.randomiseSlotsActive;
			SendChatMessage(String.format("??6Slot Randomizer has been toggled to ??6%s",  slotRandomiser.randomiseSlotsActive));
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
			if(!Utils.SWITCHEROO_INSTALLED) {SaveSettings();autoSwapTools.isEnabled = !autoSwapTools.isEnabled;}
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
