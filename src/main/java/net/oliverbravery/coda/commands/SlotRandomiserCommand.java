package net.oliverbravery.coda.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.oliverbravery.coda.features.SlotRandomiser;
import net.oliverbravery.coda.screens.SlotRandomiserScreen;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class SlotRandomiserCommand {
    public static LiteralCommandNode register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        return dispatcher.register(
                literal("coda")
                        .then(literal("slotrandomiser")
                                .executes(SlotRandomiserCommand::Run)
                                .then(literal("configure")
                                        .executes(SlotRandomiserCommand::Configure))));
    }

    public static void ShowMenu() {
        MinecraftClient client = MinecraftClient.getInstance();
        client.send(() -> client.setScreen(new SlotRandomiserScreen(client.currentScreen,client.options)));
    }
    private static int Run(CommandContext<FabricClientCommandSource> context){
        return SlotRandomiser.Toggle();
    }
    private static int Configure(CommandContext<FabricClientCommandSource> context) {
        ShowMenu();
        return 1;
    }
}
