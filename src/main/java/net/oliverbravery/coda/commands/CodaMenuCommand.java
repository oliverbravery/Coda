package net.oliverbravery.coda.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.oliverbravery.coda.screens.CodaSettingsScreen;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CodaMenuCommand {
    public static void ShowMenu() {
        MinecraftClient client = MinecraftClient.getInstance();
        client.send(() -> client.setScreen(new CodaSettingsScreen(client.currentScreen,client.options)));
    }

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("coda")
                    .executes(ctx -> {
                        ShowMenu();
                        return 1;}));
    }
}
