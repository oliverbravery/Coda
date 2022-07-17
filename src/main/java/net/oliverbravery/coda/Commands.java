package net.oliverbravery.coda;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class Commands {
    public static int OpenCodaMenu(final CommandContext<FabricClientCommandSource> command) {
        MinecraftClient client = command.getSource().getClient();
        client.send(() -> client.setScreen(new CodaSettingsScreen(client.currentScreen,client.options)));
        return 1;
    }
}
