package net.oliverbravery.coda;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.oliverbravery.coda.commands.CommandManager;
import net.oliverbravery.coda.config.Config;
import net.oliverbravery.coda.features.*;
import net.oliverbravery.coda.utilities.KeybindEvents;
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
	public static CommandManager commandManager = new CommandManager();

	@Override
	public void onInitializeClient() {
		Coda.InitializeFeatures();
		RegisterTickEvents();
		KeybindEvents.InitializeKeybinds();
		commandManager.onInitializeClient();
	}

	public void RegisterTickEvents() {
		ClientTickEvents.END_CLIENT_TICK.register(AutoFish::tick);
		ClientTickEvents.END_CLIENT_TICK.register(AutoSaveTool::tick);
		ClientTickEvents.END_CLIENT_TICK.register(FastPlace::tick);
		ClientTickEvents.END_CLIENT_TICK.register(AutoSwapTools::tick);
		ClientTickEvents.END_CLIENT_TICK.register(LibrarianBookTrade::tick);
		ClientTickEvents.END_CLIENT_TICK.register(KeybindEvents::tick);
	}

	public static void InitializeFeatures() {
		SortInventory.Initialize();
		SlotRandomiser.Initialize();
		Utils.Initialize();
		Config.Initialize();
		AutoSwapTools.Initialize();
	}
}
