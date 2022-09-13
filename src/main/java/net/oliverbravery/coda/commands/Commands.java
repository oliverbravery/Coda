package net.oliverbravery.coda.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.oliverbravery.coda.Coda;
import net.oliverbravery.coda.config.Config;
import net.oliverbravery.coda.screens.CodaSettingsScreen;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

@Environment(EnvType.CLIENT)
public class Commands {

    public void InitialiseCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                    literal("coda").executes(Commands::OpenCodaMenu)
                            .then(literal("button")
                                    .then(literal("toggle").executes(ctx -> {
                                        Coda.codaButtonEnabled = !Coda.codaButtonEnabled;
                                        Config.SetValue("CodaButtonEnabled", String.valueOf(Coda.codaButtonEnabled)); return 1;})))
                            .then(literal("ShulkerBox")
                                    .then(literal("Unloader")
                                            .then(literal("toggle").executes(ctx -> {Coda.shulkerBoxUnloadEnabled = !Coda.shulkerBoxUnloadEnabled;
                                                Config.SetValue("ShulkerBoxUnloadEnabled", String.valueOf(Coda.shulkerBoxUnloadEnabled)); return 1;}))))
                    );
        });
    }
    public static int OpenCodaMenu(final CommandContext<FabricClientCommandSource> command) {
        MinecraftClient client = command.getSource().getClient();
        client.send(() -> client.setScreen(new CodaSettingsScreen(client.currentScreen,client.options)));
        return 1;
    }
}
