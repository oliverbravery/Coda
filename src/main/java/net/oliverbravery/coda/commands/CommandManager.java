package net.oliverbravery.coda.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;

@Environment(EnvType.CLIENT)
public class CommandManager implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(CommandManager::registerCommands);
    }

    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher,
                                        CommandRegistryAccess registryAccess) {
        CodaMenuCommand.register(dispatcher);
        LibrarianBookTradeCommand.register(dispatcher,registryAccess);
        AutoFishCommand.register(dispatcher);
        AutoSaveToolsCommand.register(dispatcher);
        ArmorSwapCommand.register(dispatcher);
        AutoSwapToolsCommand.register(dispatcher,registryAccess);
        FastPlaceCommand.register(dispatcher);
        SortInventoryCommand.register(dispatcher);
        SlotRandomiserCommand.register(dispatcher);
        ShulkerBoxUnloaderCommand.register(dispatcher);
        CodaMenuButtonCommand.register(dispatcher);
        WhitelistItemPickupCommand.register(dispatcher,registryAccess);
        SlotRandomiserCommand.register(dispatcher);
    }
}
